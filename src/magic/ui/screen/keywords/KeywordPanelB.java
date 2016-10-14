package magic.ui.screen.keywords;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.awt.MagicFont;
import magic.model.MagicCardDefinition;
import magic.ui.widget.cards.canvas.CardsCanvas;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class KeywordPanelB extends JPanel {

    private static final Font KEYWORD_FONT =
            MagicFont.MPlantinBold.get().deriveFont(24f);

    private static final Font TEXT_FONT =
            MagicFont.MPlantin.get().deriveFont(21f);

    private final JLabel keywordLabel = new JLabel();
    private final JLabel textLabel = new JLabel();
    private final CardsCanvas cardsCanvas = new CardsCanvas();

    KeywordPanelB() {

        cardsCanvas.setAnimationEnabled(false);
        cardsCanvas.setLayoutMode(CardsCanvas.LayoutMode.SCALE_TO_FIT);

        keywordLabel.setForeground(Color.WHITE);
        keywordLabel.setFont(KEYWORD_FONT);

        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(TEXT_FONT);
        textLabel.setVerticalAlignment(SwingConstants.TOP);

        setLayout(new MigLayout("flowy, insets n 30 n n",
                "[fill, grow]",
                "[40!][growprio 200][bottom, grow, fill]")
        );
        refreshLayout();

        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.GRAY));
        setOpaque(false);
    }

    private void drawCardImages(Keyword keyword) {
        showExampleCard(keyword);
    }

    private void refreshLayout() {
        removeAll();
        add(keywordLabel);
        add(textLabel);
        add(cardsCanvas, "h ::560");
        revalidate();
    }

    private void showExampleCard(Keyword keyword) {
        final List<MagicCardDefinition> cards = keyword.getExampleCards();
        if (!cards.isEmpty()) {
//            cardsCanvas.setCard(cards.get(MagicRandom.nextRNGInt(cards.size())));
        } else {
//            cardsCanvas.clear();
        }      
    }

    void setKeyword(Keyword keyword) {
        keywordLabel.setText(keyword.getName());
        textLabel.setText(keyword.getDescriptionAsHtml());
        drawCardImages(keyword);
        refreshLayout();
    }

}
