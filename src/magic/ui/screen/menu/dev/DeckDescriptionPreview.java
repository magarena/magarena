package magic.ui.screen.menu.dev;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import magic.utility.FileIO;

@SuppressWarnings("serial")
class DeckDescriptionPreview extends JComponent implements PropertyChangeListener {

    File file = null;
    String description;
    private final JTextArea textArea = new JTextArea();
    private final JScrollPane scrollPane = new JScrollPane(textArea);

    DeckDescriptionPreview(JFileChooser fc) {
        setPreferredSize(new Dimension(200, 50));
        setLayout(new BorderLayout());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        add(scrollPane, BorderLayout.CENTER);
        fc.addPropertyChangeListener(this);
    }

    void loadDescription() {
        String content;
        description = "";
        try { //load deck description
            content = FileIO.toStr(file);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to load " + file.getName());
            return;
        }

        try (final Scanner sc = new Scanner(content)) {
            while (sc.hasNextLine()) {
                final String line = sc.nextLine().trim();
                if (line.startsWith("#")) {
                    description += line.substring(1).trim() + System.lineSeparator();
                } else {
                    break;
                }
            }
        }

        showDescription();
    }

    void showDescription() {
        textArea.setText(description);
        textArea.setCaretPosition(0);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
            description = "";
            showDescription();
            file = (File) e.getNewValue();
            if (isShowing() && file != null && !file.isDirectory()) {
                loadDescription();
            }
        }
    }
}
