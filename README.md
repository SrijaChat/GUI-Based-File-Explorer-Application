# GUI-Based-File-Explorer-Application
A desktop-based File Explorer built using Java Swing. This application allows users to navigate directories, view file properties, and perform common file operations like open, copy, delete, rename, and navigation through file history.
- Directory Navigation using JTree
  - Users can easily explore the directory structure using a collapsible tree on the left panel.
    
- File Viewing Panel (JList)
  - It Displays the contents of the selected directory in a list format with interactive selection.
    
- File Operations
  - Open: Launches the selected file with its default application.
  - Copy: Creates a duplicate of the selected file in the same directory.
  - Delete: Deletes the selected file with confirmation.
 
- File Properties Display
  - Shows detailed file attributes like size, creation time, and last modified time at the top.

- Back and Forward Navigation
  - Enables users to go back and forth between previously visited directories, mimicking browser-style navigation.

- Dynamic Tree Model Generation
  - The directory tree dynamically loads subdirectories and reflects changes.

- Responsive UI with JSplitPane
  - Clean layout using JSplitPane for adjustable view between tree and file list.

- Cross-Platform
  - Runs on any system with Java installed — Windows, macOS, Linux.

- Uses Java NIO for File Attributes
  - Retrieves file metadata using java.nio.file for modern, robust IO operations.

- Minimal Dependencies
  - Built purely using Java SE (Standard Edition) libraries — no external dependencies required.

