package magic.ui.dialog;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import magic.data.DeckGenerator;
import magic.ui.MagicFrame;
import magic.ui.theme.Theme;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.SliderPanel;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class RandomDeckGeneratorDialog extends JDialog implements ChangeListener {

    private final MigLayout migLayout = new MigLayout();
    private boolean isCancelled = false;
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

        recalculate();
    }

    private void setDeckGenerator() {
        deckGenerator = new DeckGenerator();
        deckGenerator.deckSize = deckSizeSlider.getValue();
        deckGenerator.spellsPercent = spellsSlider.getValue();
        deckGenerator.maxCreaturesPercent = creaturesSlider.getValue();
        deckGenerator.maxColors = maxColorsSlider.getValue();
    }

    private void recalculate() {
        setDeckGenerator();
        spellsLabel.setText(Integer.toString(deckGenerator.getSpellsCount()));
        creaturesLabel.setText(Integer.toString(deckGenerator.getMaxCreaturesCount()));
        landsLabel.setText(Integer.toString(deckGenerator.getLandsCount()));
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
        content.setBorder(BorderFactory.createMatteBorder(0, 8, 8, 8, MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND)));
    }
    
    private void setListeners() {
        // save button
        saveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setDeckGenerator();
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
        lbl.setBackground(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND));
        lbl.setForeground(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_FOREGROUND));
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
        buttonPanel.add(getCancelButton(), "w 100!, alignx right, pushx");
        buttonPanel.add(saveButton, "w 100!, alignx right");
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

    public DeckGenerator getDeckGenerator() {
        return deckGenerator;
    }

}
