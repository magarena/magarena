package magic.ui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import magic.data.DeckType;
import magic.firemind.FiremindJsonReader;
import magic.ui.ScreenController;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.deck.CustomDecksComboxBox;
import magic.ui.widget.deck.FiremindDecksComboxBox;
import magic.ui.widget.deck.PrebuiltDecksComboxBox;
import magic.ui.widget.deck.RandomDecksComboBox;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckChooserDialog extends JDialog {

    private final JComboBox<DeckType> deckTypeCombo;
    private JComboBox<String> deckNameCombo;
    private final DecksPanel decksPanel;
    private boolean isCancelled = false;
    private final JButton saveButton = new JButton("Save");

    public DeckChooserDialog(final DeckType aDeckType, final String aDeckName) {

        // modal dialog
        super(ScreenController.getMainFrame(), true);
        
        this.setTitle("Select Deck");
        this.setSize(300, 180);
        this.setLocationRelativeTo(ScreenController.getMainFrame());
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        deckTypeCombo = getDeckTypeComboBox();
        deckTypeCombo.setSelectedItem(aDeckType);

        decksPanel = new DecksPanel(aDeckName);
        decksPanel.setDeckType(aDeckType);

        getContentPane().setLayout(new MigLayout("flowy"));
        getContentPane().add(deckTypeCombo);
        getContentPane().add(decksPanel, "w 100%");
        getContentPane().add(getButtonPanel(), "w 100%, h 40!, pushy, aligny bottom");

        setEscapeKeyAction();
        setSaveButtonAction();

        setVisible(true);
    }

    private JComboBox<DeckType> getDeckTypeComboBox() {

        final JComboBox<DeckType> cbo = new JComboBox<>();
        cbo.setModel(new DefaultComboBoxModel<>(DeckType.values()));
        cbo.setLightWeightPopupEnabled(false);
        cbo.setFocusable(false);
        cbo.setFont(FontsAndBorders.FONT2);

        cbo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            decksPanel.setDeckType((DeckType)e.getItem());
                        }
                    });
                }
            }
        });

        return cbo;
    }

    private void setSaveButtonAction() {
        saveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getDeckName().trim().length() > 0) {
                    dispose();
                }
            }
        });
    }

    private void setEscapeKeyAction() {
        JRootPane root = getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "closeDialog");
        root.getActionMap().put("closeDialog", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        });
    }

    private JPanel getButtonPanel() {
        final JPanel buttonPanel = new JPanel(new MigLayout("alignx right"));
        buttonPanel.add(saveButton, "w 80!");
        buttonPanel.add(getCancelButton(), "w 80!");
        return buttonPanel;
    }

    private JButton getCancelButton() {
        final JButton btn = new JButton("Cancel");
        btn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isCancelled = true;
                dispose();
            }
        });
        return btn;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    private class DecksPanel extends JPanel {

        private String defaultDeckName;

        public DecksPanel(final String aDefaultDeckName) {
            this.defaultDeckName = aDefaultDeckName;
            setLayout(new MigLayout("insets 0"));
        }

        public void setDeckType(final DeckType deckType) {
            removeAll();
            add(getDecksCombo(deckType), deckType == DeckType.Random ? "w 170!" : "w 10:100%");
            revalidate();
            repaint();
        }

        private JComboBox<String> getDecksCombo(final DeckType deckType) {
            switch (deckType) {
            case Random:
                deckNameCombo = new RandomDecksComboBox("");
                break;
            case Preconstructed:
                deckNameCombo = new PrebuiltDecksComboxBox();
                break;
            case Custom:
                deckNameCombo = new CustomDecksComboxBox();
                break;
            case Firemind:
                FiremindJsonReader.refreshTopDecks();
                deckNameCombo = new FiremindDecksComboxBox();
                break;
            }
            deckNameCombo.setSelectedItem(defaultDeckName);
            defaultDeckName = "";
            saveButton.setEnabled(deckNameCombo.getItemCount() > 0);
            return deckNameCombo;
        }

    }

    public DeckType getDeckType() {
        return deckTypeCombo.getItemAt(deckTypeCombo.getSelectedIndex());
    }

    public String getDeckName() {
        return (String)deckNameCombo.getSelectedItem();
    }

}
