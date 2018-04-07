package magic.ui.dialog;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.NumberFormatter;
import magic.translate.MText;
import magic.ui.FontsAndBorders;
import magic.ui.dialog.button.CancelButton;
import magic.ui.dialog.button.SaveButton;
import magic.ui.widget.deck.DeckFilter;
import magic.ui.widget.deck.DeckFilter.NumericFilter;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DecksFilterDialog extends MagicDialog {

    // translatable strings
    private static final String _S1 = "Apply";
    private static final String _S2 = "Reset";
    private static final String _S3 = "Filter Decks";
    private static final String _S4 = "Deck Size:";
    private static final String _S5 = "Deck Name:";
    private static final String _S6 = "Description:";
    private static final String _S7 = "Card Name:";

    private static final List<DeckFilter> filterHistory = new ArrayList<>();
    private static int historyIndex = 0;

    private boolean isCancelled = false;
    private DeckFilter deckFilter = null;
    private final DeckSizeFilterPanel deckSizeFilterPanel;
    private final JTextField cardNameFilterText = new JTextField();
    private final JTextField deckNameFilterText = new JTextField();
    private final JTextField deckDescFilterText = new JTextField();
    private final JButton saveButton = new SaveButton(MText.get(_S1));
    private final JButton resetButton = new JButton(MText.get(_S2));

    // CTR
    public DecksFilterDialog() {

        super(MText.get(_S3), new Dimension(460, 300));

        if (filterHistory.size() > 0) {
            deckFilter = filterHistory.get(historyIndex-1);
        }

        deckSizeFilterPanel = new DeckSizeFilterPanel(deckFilter);
        deckNameFilterText.setText(deckFilter != null ? deckFilter.getDeckNameFilterText() : "");
        deckDescFilterText.setText(deckFilter != null ? deckFilter.getDeckDescFilterText() : "");
        cardNameFilterText.setText(deckFilter != null ? deckFilter.getCardNameFilterText() : "");

        refreshLayout();
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        setListeners();
        refreshContent();
    }

    private void refreshContent() {
        if (deckFilter != null) {
            deckSizeFilterPanel.refreshContent(deckFilter);
            deckNameFilterText.setText(deckFilter.getDeckNameFilterText());
            deckDescFilterText.setText(deckFilter.getDeckDescFilterText());
            cardNameFilterText.setText(deckFilter.getCardNameFilterText());
        }
    }

    private void setListeners() {
        // save button
        saveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deckFilter = new DeckFilter();
                addCurrentFilterToHistory();
                dispose();
            }
        });
        // reset button
        resetButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deckFilter = null;
                filterHistory.clear();
                dispose();
            }
        });
    }

    private void addCurrentFilterToHistory() {
        deckFilter.setDeckSizeFilterValues(deckSizeFilterPanel.getFilter(), deckSizeFilterPanel.getFilterValue1(), deckSizeFilterPanel.getFilterValue2());
        deckFilter.setDeckNameFilterText(deckNameFilterText.getText());
        deckFilter.setDeckDescFilterText(deckDescFilterText.getText());
        deckFilter.setCardNameFilterText(cardNameFilterText.getText());
        filterHistory.clear();
        filterHistory.add(deckFilter);
        historyIndex = filterHistory.size();
    }

    private void refreshLayout() {
        final JPanel panel = getDialogContentPanel();
        panel.setLayout(new MigLayout("flowx, wrap 2"));
        panel.add(getFilterCaptionLabel(MText.get(_S4)), "alignx right");
        panel.add(deckSizeFilterPanel, "w 100%");
        panel.add(getFilterCaptionLabel(MText.get(_S5)), "alignx right");
        panel.add(deckNameFilterText, "w 100%");
        panel.add(getFilterCaptionLabel(MText.get(_S6)), "alignx right");
        panel.add(deckDescFilterText, "w 100%");
        panel.add(getFilterCaptionLabel(MText.get(_S7)), "alignx right");
        panel.add(cardNameFilterText, "w 100%");
        panel.add(getButtonPanel(), "w 100%, h 40!, pushy, aligny bottom, spanx");
    }

    private JLabel getFilterCaptionLabel(final String text) {
        final JLabel lbl = new JLabel(text);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl.setFont(FontsAndBorders.FONT1);
        return lbl;
    }

    private JPanel getButtonPanel() {
        final JPanel buttonPanel = new JPanel(new MigLayout("insets 0, aligny bottom"));
        buttonPanel.add(resetButton, "w 110!, alignx left");
        buttonPanel.add(getCancelButton(), "alignx right, pushx");
        buttonPanel.add(saveButton, "alignx right");
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

    private static class DeckSizeFilterPanel extends JPanel {

        // ui components
        private final MigLayout migLayout = new MigLayout();
        private final JComboBox<NumericFilter> numericFilterCombo = new JComboBox<>();
        private final JSpinner sizeSpinner1 = new JSpinner(new SpinnerNumberModel(40, 0, 100, 1));
        private final JSpinner sizeSpinner2 = new JSpinner(new SpinnerNumberModel(60, 0, 100, 1));

        public DeckSizeFilterPanel(final DeckFilter deckFilter) {
            setLookAndFeel();
            refreshLayout();
            setListeners();
            refreshContent(deckFilter);
        }

        public NumericFilter getFilter() {
            return (NumericFilter)numericFilterCombo.getSelectedItem();
        }

        public int getFilterValue1() {
            return (int)sizeSpinner1.getValue();
        }

        public int getFilterValue2() {
            return (int)sizeSpinner2.getValue();
        }

        private void setLookAndFeel() {
            setOpaque(false);
            setLayout(migLayout);
            setPreferredSize(new Dimension(0, 30));
            // filter combo
            numericFilterCombo.setModel(new DefaultComboBoxModel<>(NumericFilter.values()));
            // spinner1
            sizeSpinner1.setVisible(false);
            // allow only numeric characters to be recognised.
            sizeSpinner1.setEditor(new JSpinner.NumberEditor(sizeSpinner1,"#"));
            final JFormattedTextField txt1 = ((JSpinner.NumberEditor) sizeSpinner1.getEditor()).getTextField();
            ((NumberFormatter)txt1.getFormatter()).setAllowsInvalid(false);
            // spinner2
            sizeSpinner2.setVisible(false);
            // allow only numeric characters to be recognised.
            sizeSpinner2.setEditor(new JSpinner.NumberEditor(sizeSpinner2,"#"));
            final JFormattedTextField txt2 = ((JSpinner.NumberEditor) sizeSpinner2.getEditor()).getTextField();
            ((NumberFormatter)txt2.getFormatter()).setAllowsInvalid(false);
        }

        private void refreshLayout() {
            removeAll();
            migLayout.setLayoutConstraints("insets 0, aligny center");
            add(numericFilterCombo, "w 150");
            if (sizeSpinner1 != null) { add(sizeSpinner1, "w 60!"); }
            if (sizeSpinner2 != null) { add(sizeSpinner2, "w 60!"); }
            revalidate();
            repaint();
        }

        private void setListeners() {
            // combo
            numericFilterCombo.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(final ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        SwingUtilities.invokeLater(() -> {
                            final NumericFilter filter = (NumericFilter) e.getItem();
                            sizeSpinner1.setVisible(filter.getSpinnersRequired() >= 1);
                            sizeSpinner2.setVisible(filter.getSpinnersRequired() >= 2);
                            refreshLayout();
                        });
                    }
                }
            });
        }

        private void refreshContent(final DeckFilter deckFilter) {
            if (deckFilter != null) {
                numericFilterCombo.setSelectedItem(deckFilter.getDeckSizeFilterType());
                sizeSpinner1.setValue(deckFilter.getDeckSizeFilterValue1());
                sizeSpinner2.setValue(deckFilter.getDeckSizeFilterValue2());
            }
        }

    }

    public DeckFilter getDeckFilter() {
        if (isNoFilter()) {
            return null;
        } else {
            return deckFilter;
        }
    }

    private boolean isNoFilter() {
        return (deckSizeFilterPanel.getFilter() == NumericFilter.Any) &&
               (deckNameFilterText.getText().trim().isEmpty()) &&
               (deckDescFilterText.getText().trim().isEmpty()) &&
               (cardNameFilterText.getText().trim().isEmpty());
    }

    public static DeckFilter getLastSavedDeckFilter() {
        if (filterHistory.size() > 0) {
            return filterHistory.get(0);
        } else {
            return null;
        }
    }

    public static void resetFilterHistory() {
        filterHistory.clear();
    }

}
