package magic.ui.widget;

import magic.ui.IconImages;
import magic.model.MagicMessage;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;
import magic.data.MagicIcon;

public class MessagePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static int lastTurn = 0;
    private static int textLabelWidth = 0;

    private final MagicMessage message;

    public MessagePanel(final MagicMessage message0, final int containerWidth) {
        message = message0;
        setMessagePanelLayout(containerWidth);
    }

    public MagicMessage getMessage() {
        return message;
    }

    private void setMessagePanelLayout(final int containerWidth) {

        int gap = 8; // pixels
        setLayout(new MigLayout("insets 0, gap " + gap, "[][][grow,right]", "[top]"));

        JPanel playerPanel = getPlayerPanel();
        JPanel turnPanel = getTurnPanel();

        if (textLabelWidth == 0) {
            textLabelWidth =
                    containerWidth -
                    playerPanel.getPreferredSize().width -
                    turnPanel.getPreferredSize().width -
                    (gap * 2);
        }

        final TextLabel textLabel=new TextLabel(message.getText(), textLabelWidth, false);
        textLabel.setColors(Color.BLACK,Color.BLUE);

        add(playerPanel);
        add(textLabel);
        add(turnPanel);
    }

    private JPanel getTurnPanel() {
        final JPanel turnPanel = new JPanel(new MigLayout("insets 0, gap 0, flowy"));
        turnPanel.setOpaque(false);
        turnPanel.add(getTurnLabel(), "w 100%");
        turnPanel.add(getPhaseLabel(), "w 100%");
        return turnPanel;
    }

    private JLabel getPhaseLabel() {
        final JLabel phaseLabel=new JLabel(message.getPhaseType().getAbbreviation());
        phaseLabel.setFont(FontsAndBorders.FONT0);
        phaseLabel.setHorizontalAlignment(JLabel.RIGHT);
        return phaseLabel;
    }

    private JLabel getTurnLabel() {
        final int messageTurn = message.getTurn();
        final JLabel turnLabel = new JLabel(String.format("%03d", messageTurn));
        turnLabel.setFont(lastTurn != messageTurn ? FontsAndBorders.FONT1 : FontsAndBorders.FONT0);
        turnLabel.setHorizontalAlignment(JLabel.RIGHT);
        lastTurn = messageTurn;
        return turnLabel;
    }

    /**
     *  Displays the player avatar & health.
     */
    private JPanel getPlayerPanel() {
        final JPanel playerPanel = new JPanel(new MigLayout("insets 0", "[]2[]"));
        playerPanel.setOpaque(false);
        playerPanel.add(getPlayerAvatar());
        playerPanel.add(getPlayerLifePanel());
        return playerPanel;
    }

    private JPanel getPlayerLifePanel() {
        final JPanel lifePanel = new JPanel(new MigLayout("insets 0, gap 0, flowy"));
        lifePanel.setOpaque(false);
        final int life=message.getLife();
        final JLabel lifeLabel=new JLabel(String.valueOf(Math.abs(life)));
        lifeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        if (life<=0) {
            lifeLabel.setForeground(Color.RED);
        }
        lifePanel.add(new JLabel(IconImages.getIcon(MagicIcon.REGENERATED)), "center");
        lifePanel.add(lifeLabel, "w 100%");
        return lifePanel;
    }

    private JLabel getPlayerAvatar() {
        return new JLabel(IconImages.getIconSize1(message.getPlayer().getPlayerDefinition()));
    }
}
