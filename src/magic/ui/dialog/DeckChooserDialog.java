package magic.ui.dialog;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import magic.data.DeckType;
import magic.firemind.FiremindJsonReader;
import magic.translate.MText;
import magic.ui.FontsAndBorders;
import magic.ui.dialog.button.CancelButton;
import magic.ui.dialog.button.SaveButton;
import magic.ui.widget.deck.CustomDecksComboxBox;
import magic.ui.widget.deck.FiremindDecksComboxBox;
import magic.ui.widget.deck.PrebuiltDecksComboxBox;
import magic.ui.widget.deck.RandomDecksComboBox;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckChooserDialog extends MagicDialog {

    // translatable strings
    private static final String _S2 = "Select Deck";

    private final JComboBox<DeckType> deckTypeCombo;
    private JComboBox<String> deckNameCombo;
    private final DecksPanel decksPanel;
    private boolean isCancelled = false;
    private final JButton saveButton = new SaveButton();

    public DeckChooserDialog(final DeckType aDeckType, final String aDeckName) {

        super(MText.get(_S2), new Dimension(300, 180));

        deckTypeCombo = getDeckTypeComboBox();
        deckTypeCombo.setSelectedItem(aDeckType);
        addDeckTypeComboBoxListener();

        decksPanel = new DecksPanel(aDeckName);
        decksPanel.setDeckType(aDeckType);

        setSaveButtonAction();

        refreshLayout();

        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        setVisible(true);

    }

    private void refreshLayout() {
        final JPanel panel = getDialogContentPanel();
        panel.setLayout(new MigLayout("flowy, gap 0"));
        panel.add(deckTypeCombo, "w 100%");
        panel.add(decksPanel, "w 100%");
        panel.add(getButtonPanel(), "w 100%, h 30!, pushy, aligny bottom");
    }

    private JComboBox<DeckType> getDeckTypeComboBox() {
        final JComboBox<DeckType> cbo = new JComboBox<>();
        cbo.setModel(new DefaultComboBoxModel<>(DeckType.getDuelDeckTypes()));
        cbo.setLightWeightPopupEnabled(false);
        cbo.setFocusable(false);
        cbo.setFont(FontsAndBorders.FONT2);
        ((JLabel)cbo.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        return cbo;
    }

    private void addDeckTypeComboBoxListener() {
        deckTypeCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                SwingUtilities.invokeLater(() -> {
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    decksPanel.setDeckType((DeckType) e.getItem());
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                });
            }
        });
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
        final JPanel buttonPanel = new JPanel(new MigLayout("insets 0, alignx right"));
        buttonPanel.add(getCancelButton());
        buttonPanel.add(saveButton);
        return buttonPanel;
    }

    private JButton getCancelButton() {
        final JButton btn = new CancelButton();
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
            // NB! "w 100%" works more like "w 100%!" when applied to
            // a JComboBox hence the need for the min value so that it
            // fits to the container width instead of exceeding it.
            add(getDecksCombo(deckType), "w 10:100%");
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
                default:
                    throw new RuntimeException("Not a duel deck type: " + deckType);
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
