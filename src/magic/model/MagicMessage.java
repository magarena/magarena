package magic.model;

import magic.model.phase.MagicPhaseType;
import magic.model.event.MagicEvent;

import java.util.Collection;
import java.util.Iterator;

public class MagicMessage {

    public static final char CARD_ID_DELIMITER = '#';

    private final MagicPlayer player;
    private final int life;
    private final int turn;
    private final MagicPhaseType phaseType;
    private final String text;

    MagicMessage(final MagicGame game,final MagicPlayer player,final String text) {
        this.player=player;
        this.life=player.getLife();
        this.turn=game.getTurn();
        this.phaseType=game.getPhase().getType();
        this.text=text;
    }

    public MagicPlayer getPlayer() {
        return player;
    }

    public int getLife() {
        return life;
    }

    public int getTurn() {
        return turn;
    }

    public MagicPhaseType getPhaseType() {
        return phaseType;
    }

    public String getText() {
        return text;
    }

    static void addNames(final StringBuilder builder,final Collection<String> names) {
        if (!names.isEmpty()) {
            boolean first=true;
            boolean next;
            final Iterator<String> iterator=names.iterator();
            do {
                final String name=iterator.next();
                next=iterator.hasNext();
                if (first) {
                    first=false;
                } else if (next) {
                    builder.append(", ");
                } else {
                    builder.append(" and ");
                }
                builder.append(name);
            } while (next);
        }
    }

    public static String replaceName(final String sourceText,final Object source, final Object player, final Object ref) {
        return sourceText
            .replaceAll("PN", player.toString())
            .replaceAll("SN", source.toString())
            .replaceAll("RN", ref.toString());
    }

    public static String replaceChoices(final String sourceText,final Object[] choices) {
        String result = sourceText;
        for (int idx = 0; result.indexOf('$') >= 0; idx++) {
            final String choice = (idx < choices.length && choices[idx] != null) ? choices[idx].toString() : "";
            final String replacement = (!choice.isEmpty()) ? " (" + choice + ")" : "";
            result = result.replaceFirst("\\$", replacement);
        }
        return result;
    }
}
