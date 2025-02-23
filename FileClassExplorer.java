package OOP;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class FileClassExplorer extends JFrame {
    private JTree directoryTree;
    private JList<String> fileList;
    private DefaultListModel<String> listModel;
    private File currentDirectory;
    private Stack<File> backStack = new Stack<>();
    private Stack<File> forwardStack = new Stack<>();
    private JLabel propertiesLabel;

    public FileClassExplorer() {
        setTitle("File Explorer");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        directoryTree = new JTree(createTreeModel(new File(System.getProperty("user.home"))));
        directoryTree.addTreeSelectionListener(e -> {
            File selectedDir = (File) ((DefaultMutableTreeNode) e.getPath().getLastPathComponent()).getUserObject();
            if (selectedDir != null) {
                navigateTo(selectedDir);
            }
        });
        listModel = new DefaultListModel<>();
        fileList = new JList<>(listModel);
        fileList.addListSelectionListener(e -> showProperties());
        JPanel buttonPanel = new JPanel();
        JButton openButton = new JButton("Open");
        JButton copyButton = new JButton("Copy");
        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back");
        JButton forwardButton = new JButton("Forward");
        openButton.addActionListener(e -> openFile());
        copyButton.addActionListener(e -> copyFile());
        deleteButton.addActionListener(e -> deleteFile());
        backButton.addActionListener(e -> goBack());
        forwardButton.addActionListener(e -> goForward());
        buttonPanel.add(openButton);
        buttonPanel.add(copyButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        buttonPanel.add(forwardButton);
        propertiesLabel = new JLabel("File Properties: ");
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(directoryTree), new JScrollPane(fileList));
        splitPane.setDividerLocation(300);
        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(propertiesLabel, BorderLayout.NORTH);
    }
    private DefaultTreeModel createTreeModel(File rootFile) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootFile);
        populateTree(rootNode, rootFile);
        return new DefaultTreeModel(rootNode);
    }
    private void populateTree(DefaultMutableTreeNode node, File file) {
        File[] directories = file.listFiles(File::isDirectory);
        if (directories != null) {
            for (File dir : directories) {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(dir);
                node.add(childNode);
                populateTree(childNode, dir);
            }
        }
    }
    private void navigateTo(File directory) {
        if (currentDirectory != null) {
            backStack.push(currentDirectory);
        }
        currentDirectory = directory;
        forwardStack.clear();
        displayFiles(directory);
    }

    private void displayFiles(File directory) {
        listModel.clear();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                listModel.addElement(file.getName());
            }
        }
        showProperties();
    }

    private void showProperties() {
        String selectedFile = fileList.getSelectedValue();
        if (selectedFile != null) {
            File file = new File(currentDirectory, selectedFile);
            try {
                BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                propertiesLabel.setText(String.format("File: %s | Size: %d bytes | Created: %s | Modified: %s",
                        file.getName(),
                        attrs.size(),
                        attrs.creationTime(),
                        attrs.lastModifiedTime()));
            } catch (IOException e) {
                propertiesLabel.setText("Could not retrieve properties.");
            }
        } else {
            propertiesLabel.setText("File Properties: ");
        }
    }

    private void openFile() {
        String selectedFile = fileList.getSelectedValue();
        if (selectedFile != null) {
            try {
                Desktop.getDesktop().open(new File(currentDirectory, selectedFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void copyFile() {
        String selectedFile = fileList.getSelectedValue();
        if (selectedFile != null) {
            File source = new File(currentDirectory, selectedFile);
            File destination = new File(currentDirectory, "Copy_of_" + selectedFile);
            try {
                Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                listModel.addElement(destination.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteFile() {
        String selectedFile = fileList.getSelectedValue();
        if (selectedFile != null) {
            File fileToDelete = new File(currentDirectory, selectedFile);
            if (fileToDelete.delete()) {
                listModel.removeElement(selectedFile);
            } else {
                JOptionPane.showMessageDialog(this, "Delete operation failed");
            }
        }
    }

    private void goBack() {
        if (!backStack.isEmpty()) {
            forwardStack.push(currentDirectory);
            currentDirectory = backStack.pop();
            displayFiles(currentDirectory);
        }
    }

    private void goForward() {
        if (!forwardStack.isEmpty()) {
            backStack.push(currentDirectory);
            currentDirectory = forwardStack.pop();
            displayFiles(currentDirectory);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FileClassExplorer().setVisible(true));
    }
}

