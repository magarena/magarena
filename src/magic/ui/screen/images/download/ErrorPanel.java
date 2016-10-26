package magic.ui.screen.images.download;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import magic.translate.MText;
import magic.ui.ScreenController;
import magic.ui.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ErrorPanel extends JPanel {

    private static final String _S4 = "Copy to clipboard";
    private static final String _S5 = "Error details have been copied to the clipboard.";

    private final JTextArea textArea;

    ErrorPanel() {

        final JButton copyButton = new JButton(MText.get(_S4));
        copyButton.setEnabled(false);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setForeground(Color.red);
        textArea.setFont(FontsAndBorders.FONT_README.deriveFont(11.0f));
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                copyButton.setEnabled(!isEmpty());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                copyButton.setEnabled(!isEmpty());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                copyButton.setEnabled(!isEmpty());
            }

            private boolean isEmpty() {
                return textArea.getText().trim().isEmpty();
            }
        });

        final JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(FontsAndBorders.BLACK_BORDER);

        copyButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                final StringSelection textSelection = new StringSelection(textArea.getText());
                clip.setContents(textSelection, null);
                ScreenController.showInfoMessage(MText.get(_S5));
            }
        });

        setVisible(false);
        setLayout(new MigLayout("flowy, gap 0, insets 10 0 10 0"));
        add(scrollPane, "w 100%, h 100%");
        add(copyButton, "w 100%");
    }

    void setErrorText(String text) {
        textArea.append(text);
    }

}
