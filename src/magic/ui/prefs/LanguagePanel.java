package magic.ui.prefs;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.ui.UiString;
import magic.exception.DesktopNotSupportedException;
import magic.ui.ScreenController;
import magic.ui.utility.DesktopUtils;
import magic.utility.MagicFileSystem;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.io.FilenameUtils;

@SuppressWarnings("serial")
class LanguagePanel extends JPanel {

    // translatable strings.
    public static final String _S1 = "Edit";
    public static final String _S2 = "<html>Delete the <b>%s</b> translation file?</html>";
    public static final String _S3 = "Delete translation file";
    public static final String _S4 = "Del";
    public static final String _S5 = "eg. français, español, Deutsch, etc.";
    public static final String _S6 = "New Translation File";
    public static final String _S7 = "A translation file with this name already exists.";
    public static final String _S8 = "Invalid filename!";
    public static final String _S9 = "Failed to create translation file.\n%s";
    public static final String _S10 = "Could not open translation file.\n%s";
    public static final String _S11 = "Could not locate JAR.\n%s";
    public static final String _S12 = "New";
    public static final String _S13 = "Could not open file in default 'txt' editor.\n%s";

    private final JComboBox<String> languageCombo = new JComboBox<>();
    private final JButton newButton = new JButton();
    private final JButton editButton = new JButton();
    private final JButton deleteButton = new JButton();

    LanguagePanel() {

        setupNewButton();
        setupEditButton();
        setupDeleteButton();
        setupComboBox();

        setLayout(new MigLayout("insets 0"));
        add(languageCombo, "w 50:100%");
        add(newButton);
        add(editButton);
        add(deleteButton);

    }

    private void setupComboBox() {
        refreshLanguageCombo();
        languageCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                setStateOfActionButtons();
            }
        });
        languageCombo.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    try {
                        doOpenTranslationsFolder();
                    } catch (IOException ex) {
                        ScreenController.showWarningMessage(ex.getMessage());
                    }
                } else {
                    super.mousePressed(e);
                }
            }
        });        
        languageCombo.setFocusable(false);
    }

    private void setStateOfActionButtons() {
        final boolean isEnglish = languageCombo.getSelectedIndex() == 0;
        final boolean isActive = getSelectedLanguage().equals(GeneralConfig.getInstance().getTranslation());
        editButton.setEnabled(!isEnglish);
        deleteButton.setEnabled(!isEnglish && !isActive);
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

    private void setupEditButton() {
        editButton.setText(UiString.get(_S1));
        editButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editButton.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try {
                    doEditTranslationFile();
                } catch (URISyntaxException | IOException ex) {
                    ScreenController.showWarningMessage(ex.getMessage());
                } finally {
                    editButton.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
        editButton.setEnabled(false);
    }

    private void doDeleteTranslationFile() {
        final String lang = (String) languageCombo.getSelectedItem();
        final File langFile = MagicFileSystem.getDataPath(MagicFileSystem.DataPath.TRANSLATIONS).resolve(lang + ".txt").toFile();
        if (langFile.exists()) {
            if (JOptionPane.showOptionDialog(
                    ScreenController.getMainFrame(),
                    UiString.get(_S2, lang),
                    UiString.get(_S3),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[] {"Yes", "No"}, "No"
            ) == JOptionPane.YES_OPTION) {
                langFile.delete();
                refreshLanguageCombo();
            }
        }
    }

    private void setupDeleteButton() {
        deleteButton.setText(UiString.get(_S4));
        deleteButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doDeleteTranslationFile();
            }
        });
        deleteButton.setEnabled(false);
    }

    private void doAddNewLangFile() {
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
                    UiString.createLangFile(langFile);
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

    private void setupNewButton() {
        newButton.setText(UiString.get(_S12));
        newButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newButton.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                doAddNewLangFile();
                newButton.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    private List<String> getLangFilenames() {
        final List<String> filenames = new ArrayList<>();
        final Path langPath = MagicFileSystem.getDataPath(MagicFileSystem.DataPath.TRANSLATIONS);
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(langPath, "*.txt")) {
            for (Path p : ds) {
                filenames.add(FilenameUtils.getBaseName(p.getFileName().toString()));
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return filenames;
    }

    private void refreshLanguageCombo() {
        final List<String> languages = getLangFilenames();
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
        return languageCombo.getSelectedIndex() == 0 ? "" : (String)languageCombo.getSelectedItem();
    }

}
