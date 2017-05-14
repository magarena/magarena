package magic.ui.duel.viewerinfo;

import javax.swing.ImageIcon;
import magic.model.MagicMessage;

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
        avatar = msg.getPlayer().getProfile().getAvatar().getIcon(1);
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
