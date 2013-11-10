package magic.ui.widget;

import magic.data.IconImages;
import magic.model.MagicMessage;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;

public class MessagePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public MessagePanel(final MagicMessage message,final int maxWidth) {

        setBorder(FontsAndBorders.EMPTY_BORDER);
        setLayout(new BorderLayout(4,0));
        setOpaque(true);

        add(getPlayerPanel(message), BorderLayout.WEST);

        final TextLabel textLabel=new TextLabel(message.getText(),maxWidth+26,false);
        textLabel.setColors(Color.BLACK,Color.BLUE);
        add(textLabel,BorderLayout.CENTER);

        final JPanel gamePanel=new JPanel(new BorderLayout());
        gamePanel.setOpaque(false);
        add(gamePanel,BorderLayout.EAST);

        final JLabel turnLabel=new JLabel("Turn "+message.getTurn());
        turnLabel.setFont(FontsAndBorders.FONT1);
        turnLabel.setHorizontalAlignment(JLabel.RIGHT);
        gamePanel.add(turnLabel,BorderLayout.NORTH);

        final JLabel phaseLabel=new JLabel(message.getPhaseType().getName());
        phaseLabel.setFont(FontsAndBorders.FONT0);
        phaseLabel.setHorizontalAlignment(JLabel.RIGHT);
        gamePanel.add(phaseLabel,BorderLayout.SOUTH);
    }

    /**
     *  Displays the player avatar & health.
     */
    private JPanel getPlayerPanel(final MagicMessage message) {
        final JPanel playerPanel = new JPanel(new MigLayout("insets 0", "[]2[]"));
        playerPanel.setOpaque(false);
        playerPanel.add(getPlayerAvatar(message));
        playerPanel.add(getPlayerLifePanel(message));
        return playerPanel;
    }

    private JPanel getPlayerLifePanel(final MagicMessage message) {
        final JPanel lifePanel = new JPanel(new MigLayout("insets 0, gap 0, flowy"));
        lifePanel.setOpaque(false);
        final int life=message.getLife();
        final JLabel lifeLabel=new JLabel(String.valueOf(Math.abs(life)));
        lifeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        if (life<=0) {
            lifeLabel.setForeground(Color.RED);
        }
        lifePanel.add(new JLabel(IconImages.REGENERATED), "center");
        lifePanel.add(lifeLabel, "w 100%");
        return lifePanel;
    }

    private JLabel getPlayerAvatar(final MagicMessage message) {
        final Theme theme = ThemeFactory.getInstance().getCurrentTheme();
        final int face = message.getPlayer().getPlayerDefinition().getFace();
        return new JLabel(theme.getAvatarIcon(face,1));
    }
}
