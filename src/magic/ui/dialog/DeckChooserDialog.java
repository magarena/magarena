package magic.ui.dialog;

import magic.data.DeckType;
import magic.ui.MagicFrame;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.deck.CustomDecksComboxBox;
import magic.ui.widget.deck.PrebuiltDecksComboxBox;
import magic.ui.widget.deck.RandomDecksComboBox;
import net.miginfocom.swing.MigLayout;

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

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import magic.ui.widget.deck.FiremindDecksComboxBox;
import magic.firemind.FiremindJsonReader;

@SuppressWarnings("serial")
public class DeckChooserDialog extends JDialog {

    private final JComboBox<DeckType> deckTypeCombo;
    private DeckType selectedDeckType = DeckType.Random;
    private JComboBox<String> deckValueCombo;
    private final DecksPanel decksPanel;
    private boolean isCancelled = false;
    private final JButton saveButton = new JButton("Save");

    // CTR : edit an existing profile.
    public DeckChooserDialog(final MagicFrame frame) {

        super(frame, true);
        this.setTitle("Select Deck");
        this.setSize(300, 180);
        this.setLocationRelativeTo(frame);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        decksPanel = new DecksPanel();

        deckTypeCombo = new JComboBox<>();
        deckTypeCombo.setModel(new DefaultComboBoxModel<>(DeckType.values()));
        deckTypeCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            selectedDeckType = (DeckType)e.getItem();
                            decksPanel.setDeckType(selectedDeckType);
                        }
                    });
                }
            }
        });
        deckTypeCombo.setLightWeightPopupEnabled(false);
        deckTypeCombo.setFocusable(false);
        deckTypeCombo.setFont(FontsAndBorders.FONT2);
        deckTypeCombo.setSelectedIndex(0);

        decksPanel.setDeckType((DeckType)deckTypeCombo.getSelectedItem());

        saveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getDeckValue().trim().length() > 0) {
                    dispose();
                }
            }
        });

        getContentPane().setLayout(new MigLayout("flowy"));
        getContentPane().add(deckTypeCombo);
        getContentPane().add(decksPanel, "w 100%");
        getContentPane().add(getButtonPanel(), "w 100%, h 40!, pushy, aligny bottom");

        setEscapeKeyAction();

        setVisible(true);
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

        public DecksPanel() {
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
                deckValueCombo = new RandomDecksComboBox("");
                break;
            case Preconstructed:
                deckValueCombo = new PrebuiltDecksComboxBox();
                break;
            case Custom:
                deckValueCombo = new CustomDecksComboxBox();
                break;
            case Firemind:
                FiremindJsonReader.refreshTopDecks();
                deckValueCombo = new FiremindDecksComboxBox();
                break;
            }
            saveButton.setEnabled(deckValueCombo.getItemCount() > 0);
            return deckValueCombo;
        }

    }

    public DeckType getDeckType() {
        return selectedDeckType;
    }

    public String getDeckValue() {
        return (String)deckValueCombo.getSelectedItem();
    }

}
