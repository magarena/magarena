package magic.ui.screen.duel.decks;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.model.MagicDuel;
import magic.model.player.PlayerProfile;
import magic.ui.MagicImages;
import magic.ui.widget.player.PlayerDetailsPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class PlayerPanel extends JPanel {

    private boolean isSelected = true;

    PlayerPanel(PlayerProfile profile, MagicDuel duel) {
        setLayout(new MigLayout("insets 0"));
        setOpaque(false);
        add(new JLabel(MagicImages.getPlayerAvatar(profile).getIcon(4)));
        add(new PlayerDetailsPanel(profile, Color.BLACK), "w 100%");
        add(getScoreLabel(getScore(profile, duel)), "w 100%");
        setPreferredSize(new Dimension(280, 54));
    }

    private int getScore(PlayerProfile profile, MagicDuel duel) {
        return profile.isHuman()
            ? duel.getGamesWon()
            : duel.getGamesPlayed() - duel.getGamesWon();
    }

    private JLabel getScoreLabel(final int score) {
        final JLabel lbl = new JLabel(Integer.toString(score));
        lbl.setFont(new Font("Dialog", Font.PLAIN, 24));
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        return lbl;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!isSelected) {
            final Graphics2D g2d = (Graphics2D) g;
            final Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
            g2d.setComposite(composite);
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    void setSelected(boolean b) {
        isSelected = b;
        repaint();
    }

}
