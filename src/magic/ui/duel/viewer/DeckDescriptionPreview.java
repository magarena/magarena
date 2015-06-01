package magic.ui.duel.viewer;


import magic.utility.FileIO;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Collections;


public class DeckDescriptionPreview extends JComponent implements PropertyChangeListener {

    private static final long serialVersionUID = 1L;

    File file;
    String description;
    private final JTextArea textArea = new JTextArea();
    private final JScrollPane scrollPane = new JScrollPane(textArea);

    public DeckDescriptionPreview(final JFileChooser fc) {
        setPreferredSize(new Dimension(200, 50));
        setLayout(new BorderLayout());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        add(scrollPane, BorderLayout.CENTER);
        fc.addPropertyChangeListener(this);
    }

    public void loadDescription() {
        List<String> content = Collections.emptyList();
        description = "";
        try { //load deck description
            if (file != null) {
                content = FileIO.toStrList(file);
            }
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to load " + file.getName());
            ex.printStackTrace();
            return;
        }

        for (final String nextLine: content) {
            final String line = nextLine.trim();
            if (line.startsWith(">")) {
                description = line.substring(1);
                break;
            }
        }

        showDescription();
    }
    public void showDescription() {
        textArea.setText(description);
        textArea.setCaretPosition(0);
    }

    public void propertyChange(final PropertyChangeEvent e) {
        final String prop = e.getPropertyName();
        if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
            file = (File) e.getNewValue();
            if (isShowing()) {
                loadDescription();
            }
        }
    }
}
