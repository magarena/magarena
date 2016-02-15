package magic.ui.deck.widget;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import magic.model.DuelPlayerConfig;
import magic.model.MagicDeck;
import magic.translate.UiString;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TitleBar;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckDescriptionViewer extends JPanel {

    // translatable strings
    private static final String _S1 = "Deck Description";

    private final JTextArea textArea;
    private final JScrollPane scrollPane;

    public DeckDescriptionViewer() {

        setOpaque(false);

        final TitleBar titleBar = new TitleBar(UiString.get(_S1));

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(FontsAndBorders.TEXTAREA_TRANSPARENT_COLOR_HACK);

        scrollPane = new JScrollPane(textArea);
        scrollPane.getVerticalScrollBar().setUnitIncrement(8);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setMinimumSize(new Dimension(0, 0));
        scrollPane.setPreferredSize(new Dimension(getWidth(), 0));

        setMinimumSize(new Dimension(0, titleBar.getMinimumSize().height));

        final MigLayout mig = new MigLayout();
        mig.setLayoutConstraints("flowy, insets 0, gap 0");
        mig.setColumnConstraints("[fill, grow]");
        mig.setRowConstraints("[][fill, grow]");
        setLayout(mig);
        add(titleBar);
        add(scrollPane);

    }

    public void setPlayer(final DuelPlayerConfig playerDef) {
        setDeckDescription(playerDef.getDeck().getDescription());
    }

    public void setDeckDescription(final String text) {
        textArea.setText(text == null || text.isEmpty() ? "" : text.replaceAll("\\\\n", "\n").trim());
        textArea.setCaretPosition(0);
    }

    public void setDeckDescription(final MagicDeck deck) {
        if (deck != null) {
            setDeckDescription(deck.getDescription());
            textArea.setForeground(deck.isValid() ? Color.BLACK : Color.RED.darker());
        } else {
            setDeckDescription("");
        }
    }

}
