package magic.ui.dialog.prefs;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.translate.UiString;
import magic.exception.DesktopNotSupportedException;
import magic.ui.ScreenController;
import magic.ui.URLUtils;
import magic.ui.utility.DesktopUtils;
import magic.utility.MagicFileSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class TranslationPanel extends JPanel {

    // translatable strings.
    private static final String _S1 = "Edit";
    private static final String _S2 = "Delete the <b>%s</b> translation file?";
    private static final String _S3 = "Delete translation file";
    private static final String _S4 = "Delete";
    private static final String _S5 = "eg. français, español, Deutsch, etc.";
    private static final String _S6 = "New Translation File";
    private static final String _S7 = "A translation file with this name already exists.";
    private static final String _S8 = "Invalid filename!";
    private static final String _S9 = "Failed to create translation file.\n%s";
    private static final String _S10 = "Could not open translation file.\n%s";
    private static final String _S11 = "Could not locate JAR.\n%s";
    private static final String _S12 = "New translation";
    private static final String _S13 = "Could not open file in default 'txt' editor.\n%s";
    private static final String _S14 = "File Explorer";
    private static final String _S15 = "Yes";
    private static final String _S16 = "No";
    private static final String _S17 = "Help";

    private final JComboBox<String> languageCombo = new JComboBox<>();
    private final JButton menuButton = new JButton();
    private final JPopupMenu popupMenu = new JPopupMenu();
    private final JMenuItem newMenuItem = new JMenuItem();
    private final JMenuItem editMenuItem = new JMenuItem();
    private final JMenuItem deleteMenuItem = new JMenuItem();

    TranslationPanel() {

        setupMenuButton();
        setupNewMenuItem();
        setupEditMenuItem();
        setupDeleteMenuItem();
        popupMenu.addSeparator();
        setupExplorerMenuItem();
        popupMenu.addSeparator();
        setupHelpMenuItem();

        setupComboBox();

        setLayout(new MigLayout("insets 0, gap 0"));
        add(languageCombo, "w 200!");
        add(menuButton, "w 26!, h 26!");

    }

    private void setupHelpMenuItem() {
        final JMenuItem menu = new JMenuItem(new AbstractAction(UiString.get(UiString.get(_S17))) {
            @Override
            public void actionPerformed(ActionEvent e) {
                URLUtils.openURL(URLUtils.URL_WIKI + "Translating-Magarena");
            }
        });
        popupMenu.add(menu);
    }

    private void setupExplorerMenuItem() {
        final JMenuItem itemReload = new JMenuItem(new AbstractAction(UiString.get(_S14)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    doOpenTranslationsFolder();
                } catch (IOException ex) {
                    ScreenController.showWarningMessage(ex.getMessage());
                } finally {
                    getParent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
        popupMenu.add(itemReload);
    }

    private void showPopupMenu() {
        popupMenu.show(this, menuButton.getX(), menuButton.getY() + menuButton.getHeight());
    }

    private void setupMenuButton() {
        menuButton.setText("...");
        menuButton.setFont(menuButton.getFont().deriveFont(Font.BOLD));
        menuButton.setFocusable(false);
        menuButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                showPopupMenu();
            }
        });
    }

    private void setupComboBox() {
        refreshLanguageCombo();
        languageCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                setStateOfActionButtons();
            }
        });
        languageCombo.setFocusable(false);
    }

    private void setStateOfActionButtons() {
        final boolean isEnglish = languageCombo.getSelectedIndex() == 0;
        final boolean isActive = getSelectedLanguage().equals(GeneralConfig.getInstance().getTranslation());
        editMenuItem.setEnabled(!isEnglish);
        deleteMenuItem.setEnabled(!isEnglish && !isActive);
    }

    private void doEditTranslationFile() throws URISyntaxException, UnsupportedEncodingException, IOException {

        final String lang = (String) languageCombo.getSelectedItem();
        final File langFile = MagicFileSystem.getDataPath(
                MagicFileSystem.DataPath.TRANSLATIONS).resolve(lang + ".txt").toFile();

        if (langFile.exists() == false) {
            throw new FileNotFoundException(langFile.getAbsolutePath());
        }

        // 1. Build strings map from UI.
        final Map<Long, String> latestStrings = UiString.getUiStringsMap();

        // 2. load map of escaped strings from translation file.
        final Map<Long, String> langStrings = UiString.getEscapedStringsMap(langFile);

        // 3. remove entries from 2. missing in 1.
        final Set<Long> latestKeys = latestStrings.keySet();
        final Set<Long> langKeys = langStrings.keySet();
        langKeys.retainAll(latestKeys);

        // 4. append remaining entries in 2. to end of 1.
        latestKeys.removeAll(langKeys);
        latestStrings.putAll(langStrings);

        // 5. save 1. by overwriting existing translation file.
        UiString.createTranslationFile(langFile, latestStrings);

        // 6. open file in default txt editor.
        try {
            DesktopUtils.openFileInDefaultOsEditor(langFile);
        } catch (IOException | DesktopNotSupportedException ex) {
            ScreenController.showWarningMessage(UiString.get(_S13, ex));
        }

    }

    private void doOpenTranslationsFolder() throws IOException {
        final Path folder = MagicFileSystem.getDataPath(MagicFileSystem.DataPath.TRANSLATIONS);
        DesktopUtils.openDirectory(folder.toString());
    }

    private void setupEditMenuItem() {
        editMenuItem.setAction(new AbstractAction(UiString.get(_S1)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try {
                    doEditTranslationFile();
                } catch (URISyntaxException | IOException ex) {
                    ScreenController.showWarningMessage(ex.getMessage());
                } finally {
                    getParent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
        editMenuItem.setEnabled(false);
        popupMenu.add(editMenuItem);
    }

    private void doDeleteTranslationFile() {
        final String lang = (String) languageCombo.getSelectedItem();
        final File langFile = MagicFileSystem.getDataPath(MagicFileSystem.DataPath.TRANSLATIONS).resolve(lang + ".txt").toFile();
        if (langFile.exists()) {
            if (JOptionPane.showOptionDialog(
                    ScreenController.getMainFrame(),
                    String.format("<html>%s</html>", UiString.get(_S2, lang)),
                    UiString.get(_S3),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{UiString.get(_S15), UiString.get(_S16)},
                    UiString.get(_S16)
            ) == JOptionPane.YES_OPTION) {
                langFile.delete();
                refreshLanguageCombo();
            }
        }
    }

    private void setupDeleteMenuItem() {
        deleteMenuItem.setAction(new AbstractAction(UiString.get(_S4)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                doDeleteTranslationFile();
                getParent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
        deleteMenuItem.setEnabled(false);
        popupMenu.add(deleteMenuItem);
    }

    private void doAddNewTranslationFile() {
        try {
            final String text = JOptionPane.showInputDialog(null, UiString.get(_S5), UiString.get(_S6), JOptionPane.QUESTION_MESSAGE);
            if (text != null) {
                final String language = text.trim();
                if (language.isEmpty() == false && language.equalsIgnoreCase("English") == false) {
                    File langFile = MagicFileSystem.getDataPath(MagicFileSystem.DataPath.TRANSLATIONS).resolve(language + ".txt").toFile();
                    if (langFile.exists()) {
                        ScreenController.showWarningMessage(UiString.get(_S7));
                        return;
                    }
                    UiString.createTranslationFIle(langFile);
                    DesktopUtils.openFileInDefaultOsEditor(langFile);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            refreshLanguageCombo();
                            languageCombo.setSelectedItem(language);
                        }
                    });
                } else {
                    ScreenController.showWarningMessage(UiString.get(_S8));
                }
            }
        } catch (FileNotFoundException ex) {
            ScreenController.showWarningMessage(UiString.get(_S9, ex));
        } catch (IOException | DesktopNotSupportedException ex) {
            ScreenController.showWarningMessage(UiString.get(_S10, ex));
        } catch (URISyntaxException ex) {
            ScreenController.showWarningMessage(UiString.get(_S11, ex));
        }
    }

    private void setupNewMenuItem() {
        newMenuItem.setAction(new AbstractAction(UiString.get(_S12)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                doAddNewTranslationFile();
                getParent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
        popupMenu.add(newMenuItem);
    }

    public void refreshLanguageCombo() {
        final List<String> languages = MagicFileSystem.getTranslationFilenames();
        languages.add(0, "English");
        languageCombo.setModel(new DefaultComboBoxModel<>(languages.toArray(new String[0])));
        languageCombo.setSelectedItem(GeneralConfig.getInstance().getTranslation());
        setStateOfActionButtons();
    }

    @Override
    public void setToolTipText(String text) {
        super.setToolTipText(text);
        languageCombo.setToolTipText(text);
    }

    @Override
    public synchronized void addMouseListener(MouseListener aListener) {
        languageCombo.addMouseListener(aListener);
    }

    String getSelectedLanguage() {
        return languageCombo.getSelectedIndex() == 0 ? "" : (String) languageCombo.getSelectedItem();
    }

}
