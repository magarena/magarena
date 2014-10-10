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
import magic.model.MagicPlayerDefinition;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.TitleBar;
import net.miginfocom.swing.MigLayout;

public class DeckDescriptionViewer extends JPanel implements FocusListener {

    private static final long serialVersionUID = 1L;

    private static final Dimension PREFERRED_SIZE = new Dimension(270, 110);
    private final JTextArea textArea;
    private MagicPlayerDefinition player;
    private final JScrollPane scrollPane = new JScrollPane();

    public DeckDescriptionViewer() {
        setPreferredSize(PREFERRED_SIZE);
        setBorder(FontsAndBorders.UP_BORDER);
        setLayout(new BorderLayout());

        final TitleBar titleBar = new TitleBar("Deck Description");
        add(titleBar, BorderLayout.NORTH);

        final TexturedPanel mainPanel = new TexturedPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(FontsAndBorders.BLACK_BORDER_2);
        add(mainPanel,BorderLayout.CENTER);

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setForeground(ThemeFactory.getInstance().getCurrentTheme().getTextColor());
        textArea.addFocusListener(this);

        scrollPane.getVerticalScrollBar().setUnitIncrement(8);
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
        //
        scrollPane.setViewportView(textArea);
        scrollPane.getVerticalScrollBar().setUnitIncrement(8);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBackground(Color.RED);
        //
        removeAll();
        setLayout(new MigLayout("insets 0"));
        add(scrollPane, "w 100%, h 100%");
        revalidate();
    }

    public void setPlayer(final MagicPlayerDefinition playerDef) {
        this.player = playerDef;
        textArea.setText(playerDef.getDeck().getDescription());
        textArea.setCaretPosition(0);

    }

    @Override
    public void focusGained(final FocusEvent event) {}

    @Override
    public void focusLost(final FocusEvent event) {
        if (player != null) {
            player.getDeck().setDescription(textArea.getText());
        }
    }

    public void setDeckDescription(String text) {
        textArea.setText(text == null || text.isEmpty() ? "" : text.replaceAll("\\\\n", "\n"));
        textArea.setCaretPosition(0);
    }

    public void setDeckDescription(final MagicDeck deck) {
        if (deck != null) {
            final String text = deck.getDescription();
            textArea.setText(text == null || text.isEmpty() ? "" : text.replaceAll("\\\\n", "\n"));
            textArea.setCaretPosition(0);
            textArea.setForeground(deck.isValid() ? Color.BLACK : Color.RED.darker());
        } else {
            textArea.setText("");
        }
    }

}
