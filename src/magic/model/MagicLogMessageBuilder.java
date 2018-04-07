package magic.model;

import java.util.List;
import java.util.ArrayList;

public class MagicLogMessageBuilder {

    private final MagicGame game;
    private final StringBuilder[] messageBuilders;
    private final List<MagicPlayer> order;

    MagicLogMessageBuilder(final MagicGame aGame) {
        game = aGame;
        messageBuilders=new StringBuilder[]{new StringBuilder(),new StringBuilder()};
        order = new ArrayList<>(2);
    }

    void appendMessage(final MagicPlayer player,final String message) {
        final StringBuilder messageBuilder=messageBuilders[player.getIndex()];
        if (messageBuilder.length()>0) {
            messageBuilder.append(' ');
        }
        messageBuilder.append(message);

        if (order.contains(player) == false) {
            order.add(player);
        }
    }

    void logMessages() {
        for (final MagicPlayer player : order) {
            final StringBuilder messageBuilder=messageBuilders[player.getIndex()];
            if (messageBuilder.length()>0) {
                game.getLogBook().add(new MagicMessage(game,player,messageBuilder.toString()));
                messageBuilder.setLength(0);
            }
        }
        order.clear();
    }

    void clearMessages() {
        messageBuilders[0].setLength(0);
        messageBuilders[1].setLength(0);
        order.clear();
    }
}
