package magic.ui.widget.duel.sidebar;

import java.awt.Color;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import magic.data.MagicIcon;
import magic.model.MagicMessage;
import magic.ui.MagicImages;
import magic.ui.screen.duel.game.SwingGameController;
import magic.ui.widget.message.TextLabel;
import magic.ui.widget.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class MessagePanel extends JPanel {

    private static final CompoundBorder SEPARATOR_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
        FontsAndBorders.EMPTY_BORDER
    );

    private static int lastTurn = 0;
    private static int textLabelWidth = 0;

    private final MagicMessage message;

    MessagePanel(final MagicMessage message0, final JComponent container, SwingGameController aController) {

        message = message0;

        setOpaque(false);
        setBorder(SEPARATOR_BORDER);

        setMessagePanelLayout(container, aController);
    }

    MagicMessage getMessage() {
        return message;
    }

    private void setMessagePanelLayout(final JComponent container, SwingGameController aController) {

        final int GAP = 8; // pixels
        setLayout(new MigLayout("insets 0, gap " + GAP, "[][][grow,right]", "[top]"));

        JPanel playerPanel = getPlayerPanel();
        JPanel turnPanel = getTurnPanel();

        final Insets insets1 = container.getInsets();
        final Insets insets2 = SEPARATOR_BORDER.getBorderInsets(this);
        final int totalInsets = insets1.left + insets1.right + insets2.left + insets2.right;

        if (textLabelWidth == 0) {
            textLabelWidth =
                    container.getWidth() -
                    playerPanel.getPreferredSize().width -
                    turnPanel.getPreferredSize().width -
                    totalInsets -
                    (GAP * 2);
        }

        final TextLabel textLabel = new TextLabel(
            message.getText(),
            LogStackViewer.MESSAGE_FONT,
            textLabelWidth,
            false,
            LogStackViewer.CHOICE_COLOR,
            aController
        );

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
        lifePanel.add(new JLabel(MagicImages.getIcon(MagicIcon.REGENERATED)), "center");
        lifePanel.add(lifeLabel, "w 100%");
        return lifePanel;
    }

    private JLabel getPlayerAvatar() {
        return new JLabel(MagicImages.getIconSize1(message.getPlayer().getPlayerDefinition()));
    }
}
