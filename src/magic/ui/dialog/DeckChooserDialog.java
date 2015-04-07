package magic.ui.dialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
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
public class DeckChooserDialog extends MagicDialog {

    private final JComboBox<DeckType> deckTypeCombo;
    private JComboBox<String> deckNameCombo;
    private final DecksPanel decksPanel;
    private boolean isCancelled = false;
    private final JButton saveButton = new JButton("Save");

    public DeckChooserDialog(final DeckType aDeckType, final String aDeckName) {

        super(ScreenController.getMainFrame(), "Select Deck", new Dimension(300, 180));
        
        deckTypeCombo = getDeckTypeComboBox();
        deckTypeCombo.setSelectedItem(aDeckType);

        decksPanel = new DecksPanel(aDeckName);
        decksPanel.setDeckType(aDeckType);

        setSaveButtonAction();

        refreshLayout();

    }

    private void refreshLayout() {
        final JPanel panel = getDialogContentPanel();
        panel.setLayout(new MigLayout("flowy, gap 0"));
        panel.add(deckTypeCombo);
        panel.add(decksPanel, "w 100%");
        panel.add(getButtonPanel(), "w 100%, h 40!, pushy, aligny bottom");
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

    private JPanel getButtonPanel() {
        final JPanel buttonPanel = new JPanel(new MigLayout("alignx right"));
        buttonPanel.add(saveButton, "w 80!");
        buttonPanel.add(getCancelButton(), "w 80!");
        return buttonPanel;
    }

    private JButton getCancelButton() {
        final JButton btn = new JButton("Cancel");
        btn.addActionListener(getCancelAction());
        return btn;
    }

    public boolean isCancelled() {
        return isCancelled;
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
