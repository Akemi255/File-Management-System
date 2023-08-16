import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

 class FileOrganizerApp extends JFrame {

    private static final long serialVersionUID = 1L;

    private JLabel statusLabel;

    public FileOrganizerApp() {
        super("File Organizer App");

        // Create components
        JButton chooseFolderButton = new JButton("Choose Folder");
        chooseFolderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFileChooser();
            }
        });

        statusLabel = new JLabel("Select a folder to organize.");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel mainPanel = new JPanel();
        mainPanel.add(chooseFolderButton);

        // Add components to the frame
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(statusLabel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(600, 150)); // Set preferred size (width, height)
        pack();
        setLocationRelativeTo(null);
    }

    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = fileChooser.getSelectedFile();
            organizeFiles(selectedFolder);
        }
    }

    private void organizeFiles(File folder) {
        if (folder == null || !folder.isDirectory()) {
            statusLabel.setText("Invalid folder selected.");
            return;
        }

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            statusLabel.setText("Selected folder is empty.");
            return;
        }

        for (File file : files) {
            if (file.isFile()) {
                String extension = getFileExtension(file);
                if (extension != null) {
                    File destinationFolder = new File(folder, extension);
                    destinationFolder.mkdir();
                    try {
                        Files.move(file.toPath(), destinationFolder.toPath().resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        statusLabel.setText("ARCHIVOS AGRUPADOS CORRECTAMENTE");
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastDotIndex = name.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < name.length() - 1) {
            return name.substring(lastDotIndex + 1).toLowerCase();
        }
        return null;
    }

    public static void main(String[] args) {
        // Set the look and feel to the system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FileOrganizerApp().setVisible(true);
            }
        });
    }
}
