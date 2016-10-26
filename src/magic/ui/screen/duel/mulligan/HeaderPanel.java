package magic.ui.screen.duel.mulligan;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.translate.MText;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class HeaderPanel extends JPanel {

    // translatable string
    private static final String _S6 = "You play %s";
    private static final String _S7 = "first.";
    private static final String _S8 = "second.";

    private final JLabel playingFirstLabel = new JLabel();

    HeaderPanel(final MagicGame game) {
        setLookAndFeel();
        setContent(game);
    }

    private void setLookAndFeel() {
        setOpaque(false);
        setLayout(new MigLayout(
                "insets 0, gapy 2, flowy, aligny center",
                "[fill, grow]")
        );
        // playing first label
        playingFirstLabel.setForeground(Color.WHITE);
        playingFirstLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        playingFirstLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void refreshLayout() {
        removeAll();
        add(playingFirstLabel, "w 100%");
    }

    private void setContent(final MagicGame game) {
        final MagicPlayer turnPlayer = game.getTurnPlayer();
        final MagicPlayer humanPlayer = game.getPlayer(0);
        playingFirstLabel.setText(MText.get(_S6,
                turnPlayer == humanPlayer
                        ? MText.get(_S7)
                        : MText.get(_S8)));
        refreshLayout();
    }

}
