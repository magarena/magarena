package magic.ui.duel.viewerinfo;

import javax.swing.ImageIcon;
import magic.model.MagicMessage;
import magic.ui.MagicImages;

public class MessageViewerInfo {

    private final String text;
    private final String phase;
    private final int turn;
    private final int life;
    private final ImageIcon avatar;

    public MessageViewerInfo(final MagicMessage msg) {
        text = msg.getText();
        phase = msg.getPhaseType().getAbbreviation();
        turn = new Integer(msg.getTurn());
        life = new Integer(msg.getLife());
        avatar = MagicImages.getIconSize1(msg.getPlayer().getConfig());
    }

    public String getText() {
        return text;
    }

    public String getPhase() {
        return phase;
    }

    public int getTurn() {
        return turn;
    }

    public int getLife() {
        return life;
    }

    public ImageIcon getPlayerAvatar() {
        return avatar;
    }
}
