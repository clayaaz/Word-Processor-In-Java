import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class SimpleWordProcessor extends JFrame {

    private JTextArea textArea;
    private String currentFilePath;

    public SimpleWordProcessor() {
        super("Simple Word Processor");

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

        add(scrollPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newDocument();
            }
        });
        fileMenu.add(newMenuItem);

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDocument();
            }
        });
        fileMenu.add(openMenuItem);

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDocument();
            }
        });
        fileMenu.add(saveMenuItem);

        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDocumentAs();
            }
        });
        fileMenu.add(saveAsMenuItem);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);

        JMenu formatMenu = new JMenu("Format");
        menuBar.add(formatMenu);

        JMenuItem fontMenuItem = new JMenuItem("Font");
        fontMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFont();
            }
        });
        formatMenu.add(fontMenuItem);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void newDocument() {
        textArea.setText("");
        currentFilePath = null;
    }

    private void openDocument() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            currentFilePath = file.getAbsolutePath();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                textArea.setText(content.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveDocument() {
        if (currentFilePath == null) {
            saveDocumentAs();
        } else {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFilePath))) {
                writer.write(textArea.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveDocumentAs() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            currentFilePath = file.getAbsolutePath();
            saveDocument();
        }
    }

    private void chooseFont() {
        // Show font selection dialog
        Font selectedFont = JFontChooser.showDialog(this, "Choose Font", textArea.getFont());
        if (selectedFont != null) {
            textArea.setFont(selectedFont);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SimpleWordProcessor();
            }
        });
    }
}
class JFontChooser extends JDialog {
    private Font selectedFont;

    private JFontChooser(Frame owner, String title, Font initialFont) {
        super(owner, title, true);

        selectedFont = initialFont;

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel fontLabel = new JLabel("Select Font:");
        panel.add(fontLabel, BorderLayout.NORTH);

        JList<String> fontList = new JList<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        JScrollPane fontScrollPane = new JScrollPane(fontList);
        panel.add(fontScrollPane, BorderLayout.CENTER);

        JLabel sizeLabel = new JLabel("Font Size:");
        panel.add(sizeLabel, BorderLayout.WEST);

        SpinnerNumberModel sizeModel = new SpinnerNumberModel(initialFont.getSize(), 1, 100, 1);
        JSpinner sizeSpinner = new JSpinner(sizeModel);
        panel.add(sizeSpinner, BorderLayout.EAST);

        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedFontName = fontList.getSelectedValue();
                int selectedFontSize = (int) sizeSpinner.getValue();
                
                if (selectedFontName != null) {
                    selectedFont = new Font(selectedFontName, Font.PLAIN, selectedFontSize);
                }
                dispose();
            }
        });
        panel.add(selectButton, BorderLayout.SOUTH);

        add(panel);
        pack();
        setLocationRelativeTo(owner);
    }

    public static Font showDialog(Frame owner, String title, Font initialFont) {
        JFontChooser fontChooser = new JFontChooser(owner, title, initialFont);
        fontChooser.setVisible(true);
        return fontChooser.selectedFont;
    }
}
