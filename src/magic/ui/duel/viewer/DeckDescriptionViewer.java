package magic.ui.duel.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import magic.model.MagicDeck;
import magic.model.DuelPlayerConfig;
import magic.ui.UiString;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.TitleBar;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckDescriptionViewer extends JPanel implements FocusListener {

    // translatable strings
    private static final String _S1 = "Deck Description";

    private static final Dimension PREFERRED_SIZE = new Dimension(270, 110);
    private final JTextArea textArea;
    private DuelPlayerConfig player;
    private final JScrollPane scrollPane = new JScrollPane();

    public DeckDescriptionViewer() {

        setPreferredSize(PREFERRED_SIZE);
        setBorder(FontsAndBorders.UP_BORDER);
        setLayout(new BorderLayout());

        final TitleBar titleBar = new TitleBar(UiString.get(_S1));
        add(titleBar, BorderLayout.NORTH);

        final TexturedPanel mainPanel = new TexturedPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(FontsAndBorders.BLACK_BORDER_2);
        add(mainPanel,BorderLayout.CENTER);

        textArea = new JTextArea();
        textArea.setBackground(new Color(0, 0, 0, 0));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setForeground(ThemeFactory.getInstance().getCurrentTheme().getTextColor());
        textArea.addFocusListener(this);
        textArea.setBorder(null);

        scrollPane.setViewportView(textArea);
        scrollPane.getVerticalScrollBar().setUnitIncrement(8);
        scrollPane.setBorder(null);

        mainPanel.add(scrollPane,BorderLayout.CENTER);

    }

    public void setDeckChooserLayout() {
        setBorder(null);
        setOpaque(false);
        //
        textArea.setForeground(Color.BLACK);
        textArea.setBackground(new Color(0, 0, 0, 1));
        textArea.setFont(new Font("dialog", Font.ITALIC, 12));
        textArea.setOpaque(false);
        textArea.setFocusable(false);
        //
        scrollPane.setViewportView(textArea);
        scrollPane.getVerticalScrollBar().setUnitIncrement(8);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBackground(Color.RED);
        scrollPane.setBorder(null);
        //
        removeAll();
        setLayout(new MigLayout("insets 3"));
        add(scrollPane, "w 100%, h 100%");
        revalidate();
    }

    public void setPlayer(final DuelPlayerConfig playerDef) {
        this.player = playerDef;
        setDeckDescription(playerDef.getDeck().getDescription());
    }

    @Override
    public void focusGained(final FocusEvent event) {}

    @Override
    public void focusLost(final FocusEvent event) {
        if (player != null) {
            player.getDeck().setDescription(textArea.getText());
        }
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
