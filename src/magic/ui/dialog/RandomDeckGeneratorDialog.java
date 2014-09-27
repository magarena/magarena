package magic.ui.dialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import magic.data.DeckGenerator;
import magic.ui.MagicFrame;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.SliderPanel;
import magic.ui.widget.deck.DeckFilter;
import magic.ui.widget.deck.DeckFilter.NumericFilter;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class RandomDeckGeneratorDialog extends JDialog implements ChangeListener {

    private final Theme THEME = ThemeFactory.getInstance().getCurrentTheme();

    private static final List<DeckFilter> filterHistory = new ArrayList<>();
    private static int historyIndex = 0;

    private final MigLayout migLayout = new MigLayout();
    private boolean isCancelled = false;
    private DeckFilter deckFilter = null;
    private final DeckSizeFilterPanel deckSizeFilterPanel;
    private final JTextField cardNameFilterText = new JTextField();
    private final JTextField deckNameFilterText = new JTextField();
    private final JTextField deckDescFilterText = new JTextField();
    private final JButton saveButton = new JButton("Create Deck");

    private final int cardPoolSize;
    private DeckGenerator deckGenerator = new DeckGenerator();

    // deck size.
    private final SliderPanel deckSizeSlider;
    // percentage of deck size allocated to non-land cards.
    private final SliderPanel spellsSlider;
    private final JLabel spellsLabel = new JLabel();
    // maximum percentage of spells allocated to creature cards.
    private final SliderPanel creaturesSlider;
    private final JLabel creaturesLabel = new JLabel();
    private final JLabel landsLabel = new JLabel();
    // max colors
    private final SliderPanel maxColorsSlider;

    // CTR
    public RandomDeckGeneratorDialog(final MagicFrame frame, final int cardPoolSize) {
        super(frame, true);

        this.cardPoolSize = cardPoolSize;

        if (filterHistory.size() > 0) {
            deckFilter = filterHistory.get(historyIndex-1);
        }
        
        deckSizeFilterPanel = new DeckSizeFilterPanel(deckFilter);
        deckNameFilterText.setText(deckFilter != null ? deckFilter.getDeckNameFilterText() : "");
        deckDescFilterText.setText(deckFilter != null ? deckFilter.getDeckDescFilterText() : "");
        cardNameFilterText.setText(deckFilter != null ? deckFilter.getCardNameFilterText() : "");

        deckSizeSlider = new SliderPanel("", null, 40, 100, 10, 60, false);
        deckSizeSlider.setPaintTicks(false);
        deckSizeSlider.addChangeListener(this);

        spellsSlider = new SliderPanel("", null, 0, 100, 1, 60, false);
        spellsSlider.setToolTipText("Percentage of deck size allocated to non-land cards.");
        spellsSlider.setPaintTicks(false);
        spellsSlider.addChangeListener(this);

        creaturesSlider = new SliderPanel("", null, 0, 100, 1, 66, false);
        creaturesSlider.setToolTipText("Maximum percentage of spells allocated to creature cards.");
        creaturesSlider.setPaintTicks(false);
        creaturesSlider.addChangeListener(this);

        maxColorsSlider = new SliderPanel("", null, 1, 3, 1, 2, true);
        maxColorsSlider.setToolTipText("Maximum number of colors to use in deck.");
        maxColorsSlider.setPaintTicks(false);
        maxColorsSlider.addChangeListener(this);

        setLookAndFeel();
        refreshLayout();
        setEscapeKeyAction();
        setListeners();
        refreshContent();

        recalculate();
    }

    private void recalculate() {
        deckGenerator = new DeckGenerator();
        deckGenerator.deckSize = deckSizeSlider.getValue();
        deckGenerator.spellsPercent = spellsSlider.getValue();
        deckGenerator.maxCreaturesPercent = creaturesSlider.getValue();
        deckGenerator.maxColors = maxColorsSlider.getValue();
        spellsLabel.setText(Integer.toString(deckGenerator.getSpellsCount()));
        creaturesLabel.setText(Integer.toString(deckGenerator.getMaxCreaturesCount()));
        landsLabel.setText(Integer.toString(deckGenerator.getLandsCount()));
    }

    private void refreshContent() {
        if (deckFilter != null) {
            deckSizeFilterPanel.refreshContent(deckFilter);
            deckNameFilterText.setText(deckFilter.getDeckNameFilterText());
            deckDescFilterText.setText(deckFilter.getDeckDescFilterText());
            cardNameFilterText.setText(deckFilter.getCardNameFilterText());
        }
    }

    private void setLookAndFeel() {
        migLayout.setLayoutConstraints("flowy, insets 0");
        final JComponent content = (JComponent)getContentPane();
        content.setLayout(migLayout);
        //
        this.setTitle("Random Deck Generator");
        this.setSize(500, 400);
        this.setLocationRelativeTo(getOwner());
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        content.setBorder(BorderFactory.createMatteBorder(0, 8, 8, 8, THEME.getColor(Theme.COLOR_TITLE_BACKGROUND)));
    }
    
    private void setListeners() {
        // save button
        saveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deckGenerator = new DeckGenerator();
                deckGenerator.deckSize = deckSizeSlider.getValue();
                deckGenerator.spellsPercent = spellsSlider.getValue();
                deckGenerator.maxCreaturesPercent = creaturesSlider.getValue();
                deckGenerator.maxColors = maxColorsSlider.getValue();
                dispose();
            }
        });
    }

    private void refreshLayout() {
        final JComponent content = (JComponent)getContentPane();
        content.removeAll();
        content.add(getDialogCaptionLabel(), "w 100%, h 26!");
        content.add(getContentPanel(), "w 100%, h 100%");
    }
    
    private JPanel getContentPanel() {
        final JPanel panel = new JPanel(new MigLayout("flowx, wrap 2"));
        //
        panel.add(getFilterCaptionLabel("Card Pool:"), "alignx right");
        panel.add(new JLabel(Integer.toString(cardPoolSize)), "w 100%");
        panel.add(getFilterCaptionLabel("Deck size:"), "alignx right");
        panel.add(deckSizeSlider, "w 100%");
        panel.add(getFilterCaptionLabel("Spells (%):"), "alignx right");
        panel.add(spellsSlider, "w 100%");
        panel.add(getFilterCaptionLabel("Max. Creatures (%):"), "alignx right");
        panel.add(creaturesSlider, "w 100%");
        panel.add(getFilterCaptionLabel("Max. Colors:"), "alignx right");
        panel.add(maxColorsSlider, "w 100%");
        //
        panel.add(getFilterCaptionLabel("Spells:"), "alignx right, gaptop 10");
        panel.add(spellsLabel, "w 100%");
        panel.add(getFilterCaptionLabel("Max Creatures:"), "alignx right");
        panel.add(creaturesLabel, "w 100%");
        panel.add(getFilterCaptionLabel("Lands:"), "alignx right");
        panel.add(landsLabel, "w 100%");
        //
        panel.add(getButtonPanel(), "w 100%, h 40!, pushy, aligny bottom, spanx");
        return panel;
    }

    private JLabel getDialogCaptionLabel() {
        final JLabel lbl = new JLabel(getTitle());
        lbl.setOpaque(true);
        lbl.setBackground(THEME.getColor(Theme.COLOR_TITLE_BACKGROUND));
        lbl.setForeground(THEME.getColor(Theme.COLOR_TITLE_FOREGROUND));
        lbl.setFont(FontsAndBorders.FONT1.deriveFont(14f));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    private JLabel getFilterCaptionLabel(final String text) {
        final JLabel lbl = new JLabel(text);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl.setFont(FontsAndBorders.FONT1);
        return lbl;
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
        final JPanel buttonPanel = new JPanel(new MigLayout(""));
        buttonPanel.add(saveButton, "w 100!, alignx right, pushx");
        buttonPanel.add(getCancelButton(), "w 100!, alignx right");
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

    @Override
    public void stateChanged(ChangeEvent e) {
        recalculate();
    }

    private class DeckSizeFilterPanel extends JPanel {

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
            add(numericFilterCombo, "w 150!");
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
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                final NumericFilter filter = (NumericFilter) e.getItem();
                                sizeSpinner1.setVisible(filter.getSpinnersRequired() >= 1);
                                sizeSpinner2.setVisible(filter.getSpinnersRequired() >= 2);
                                refreshLayout();
                            }
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

    public DeckGenerator getDeckGenerator() {
        return deckGenerator;
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
