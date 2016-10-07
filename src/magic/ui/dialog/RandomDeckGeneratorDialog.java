package magic.ui.dialog;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import magic.data.DeckGenerator;
import magic.ui.MagicFrame;
import magic.ui.ScreenController;
import magic.translate.UiString;
import magic.ui.dialog.button.CancelButton;
import magic.ui.dialog.button.SaveButton;
import magic.ui.FontsAndBorders;
import magic.ui.widget.SliderPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class RandomDeckGeneratorDialog extends MagicDialog implements ChangeListener {

    // translatable strings
    private static final String _S1 = "Create Deck";
    private static final String _S2 = "Percentage of deck size allocated to non-land cards.";
    private static final String _S3 = "Maximum percentage of spells allocated to creature cards.";
    private static final String _S4 = "Maximum number of colors to use in deck.";
    private static final String _S5 = "Random Deck Generator";
    private static final String _S6 = "Card Pool:";
    private static final String _S7 = "Deck size:";
    private static final String _S8 = "Spells (%):";
    private static final String _S9 = "Max. Creatures (%):";
    private static final String _S10 = "Max. Colors:";
    private static final String _S11 = "Spells:";
    private static final String _S12 = "Max Creatures:";
    private static final String _S13 = "Lands:";

    private boolean isCancelled = false;
    private final JButton saveButton = new SaveButton(UiString.get(_S1));
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
    public RandomDeckGeneratorDialog(final MagicFrame frame, final int cardPoolSize, final int defaultDeckSize) {

        super(ScreenController.getMainFrame(), UiString.get(_S5), new Dimension(460, 340));

        this.cardPoolSize = cardPoolSize;

        deckSizeSlider = new SliderPanel("", 40, 100, 10, defaultDeckSize, false);
        deckSizeSlider.addChangeListener(this);

        spellsSlider = new SliderPanel("", 0, 100, 1, 60, false);
        spellsSlider.setToolTipText(UiString.get(_S2));
        spellsSlider.addChangeListener(this);

        creaturesSlider = new SliderPanel("", 0, 100, 1, 66, false);
        creaturesSlider.setToolTipText(UiString.get(_S3));
        creaturesSlider.addChangeListener(this);

        maxColorsSlider = new SliderPanel("", 1, 3, 1, 2, true);
        maxColorsSlider.setToolTipText(UiString.get(_S4));
        maxColorsSlider.addChangeListener(this);

        refreshLayout();
        setListeners();

        recalculate();
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
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

        final JPanel panel = getDialogContentPanel();
        panel.setLayout(new MigLayout("flowx, wrap 2"));

        panel.add(getFilterCaptionLabel(UiString.get(_S6)), "alignx right");
        panel.add(new JLabel(Integer.toString(cardPoolSize)), "w 100%");

        panel.add(getFilterCaptionLabel(UiString.get(_S7)), "alignx right");
        panel.add(deckSizeSlider, "w 100%");

        panel.add(getFilterCaptionLabel(UiString.get(_S8)), "alignx right");
        panel.add(spellsSlider, "w 100%");

        panel.add(getFilterCaptionLabel(UiString.get(_S9)), "alignx right");
        panel.add(creaturesSlider, "w 100%");

        panel.add(getFilterCaptionLabel(UiString.get(_S10)), "alignx right");
        panel.add(maxColorsSlider, "w 100%");

        panel.add(getFilterCaptionLabel(UiString.get(_S11)), "alignx right, gaptop 10");
        panel.add(spellsLabel, "w 100%");
        panel.add(getFilterCaptionLabel(UiString.get(_S12)), "alignx right");
        panel.add(creaturesLabel, "w 100%");
        panel.add(getFilterCaptionLabel(UiString.get(_S13)), "alignx right");
        panel.add(landsLabel, "w 100%");

        panel.add(getButtonPanel(), "w 100%, h 30!, pushy, aligny bottom, spanx");

    }

    private JLabel getFilterCaptionLabel(final String text) {
        final JLabel lbl = new JLabel(text);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl.setFont(FontsAndBorders.FONT1);
        return lbl;
    }

    private JPanel getButtonPanel() {
        final JPanel buttonPanel = new JPanel(new MigLayout("insets 0, alignx right, aligny bottom"));
        buttonPanel.add(getCancelButton(), "w 120");
        buttonPanel.add(saveButton, "w 120");
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
    public void stateChanged(ChangeEvent e) {
        recalculate();
    }

    public DeckGenerator getDeckGenerator() {
        return deckGenerator;
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

}
