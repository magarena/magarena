package magic.ui.dialog;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import magic.data.MagicIcon;
import magic.model.MagicDeck;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.dialog.button.CancelButton;
import magic.ui.dialog.button.MagicDialogButton;
import magic.ui.dialog.button.SaveButton;
import magic.ui.helpers.MouseHelper;
import magic.ui.mwidgets.MPlainTextViewer;
import magic.utility.DeckParser;
import magic.utility.FileIO;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckImportDialog extends MagicDialog {

    private static final Logger LOGGER = Logger.getLogger(DeckImportDialog.class.getName());

    private static String text = "";

    private final MPlainTextViewer textViewer;
    private final JButton saveButton = new SaveButton("Import");

    private boolean isCancelled = false;
    private MagicDeck deck = new MagicDeck();

    public DeckImportDialog(String text) {
        super("Import deck", new Dimension(600, (int)(ScreenController.getFrame().getSize().height * 0.67D)));
        DeckImportDialog.text = text;
        textViewer = new MPlainTextViewer();
        textViewer.setEditable(true);
        textViewer.setLineWrap(true);
        textViewer.setText(text);
        textViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    doPasteClipboardContents();
                }
            }
        });
        setLayout();
        setListeners();
        MouseHelper.showDefaultCursor();
        setVisible(true);
    }

    public DeckImportDialog() {
        this("");
    }

    private void parseTextForDeck(String text) {
        deck = DeckParser.parseText(text);
    }

    private void setListeners() {
        saveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseTextForDeck(textViewer.getText());
                text = textViewer.getText();
                dispose();
            }
        });
    }

    private void doPasteClipboardContents() {
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = c.getContents(this);
        if (t != null) {
            try {
                textViewer.setText((String) t.getTransferData(DataFlavor.stringFlavor));
            } catch (UnsupportedFlavorException | IOException ex) {
                // ignore
            }
        }
    }

    private JButton getPasteButton() {
        final JButton btn = new MagicDialogButton("");
        btn.setIcon(MagicImages.getIcon(MagicIcon.PASTE));
        btn.setToolTipText("Paste [Right-click]");
        btn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doPasteClipboardContents();
            }
        });
        return btn;
    }

    private JButton getClearButton() {
        final JButton btn = new MagicDialogButton("X");
        btn.setToolTipText("Clear");
        btn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textViewer.setText(null);
            }
        });
        return btn;
    }

    public static final FileFilter DECK_FILEFILTER = new FileFilter() {
        @Override
        public boolean accept(final File file) {
            return file.isDirectory() || file.getName().endsWith("*.dec");
        }
        @Override
        public String getDescription() {
            return "Magarena deck";
        }
    };

    private File startDirectory;

    private void doLoadDeck() {
        final JFileChooser fileChooser = new JFileChooser(startDirectory);
        fileChooser.setDialogTitle("Load deck");
        fileChooser.setAcceptAllFileFilterUsed(true);
        final int action = fileChooser.showOpenDialog(this);
        if (action == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            File aFile = new File(filename);
            startDirectory = aFile.getParentFile();
            if (aFile.length() > 2048) {
                textViewer.setText("File too big to be a valid deck file (greater than 2 MB).");
                return;
            }
            try {
                textViewer.setText(FileIO.toStr(aFile));
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, null, ex);
                textViewer.setText(ex.getMessage());
            }
        }
    }

    private JButton getLoadButton() {
        final JButton btn = new MagicDialogButton("+");
        btn.setToolTipText("Load deck");
        btn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLoadDeck();
            }
        });
        return btn;
    }

    private JButton getCancelButton() {
        final JButton btn = new CancelButton();
        btn.addActionListener(getCancelAction());
        return btn;
    }

    private JPanel getButtonPanel() {
        final JPanel buttonPanel = new JPanel(new MigLayout("insets 0, alignx right, aligny bottom"));
        buttonPanel.add(getLoadButton(), "w 40!, alignx left");
        buttonPanel.add(getClearButton(), "w 40!, alignx left");
        buttonPanel.add(getPasteButton(), "w 40!, alignx left, push x");
        buttonPanel.add(getCancelButton(), "w 120");
        buttonPanel.add(saveButton, "w 120");
        return buttonPanel;
    }

    private void setLayout() {
        final JPanel panel = getDialogContentPanel();
        panel.setLayout(new MigLayout("flowy, gapy 6"));
        panel.add(textViewer.component(), "w 100%, h 100%");
        panel.add(getButtonPanel(), "w 100%, h 30!, pushy, aligny bottom");
    }

    @Override
    protected AbstractAction getCancelAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isCancelled = true;
                dispose();
            }
        };
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public MagicDeck getDeck() {
        return deck;
    }

    public String getText() {
        return text;
    }

}
