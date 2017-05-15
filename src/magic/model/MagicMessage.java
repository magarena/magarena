package magic.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;
import magic.model.choice.MagicCardChoiceResult;
import magic.model.phase.MagicPhaseType;
import magic.model.stack.MagicCardOnStack;
import magic.model.player.PlayerProfile;

public class MagicMessage {

    public static final char CARD_ID_DELIMITER = '~';

    private final DuelPlayerConfig playerConfig;
    private final PlayerProfile playerProfile;
    private final int playerIndex;
    private final int life;
    private final int turn;
    private final MagicPhaseType phaseType;
    private final String text;

    MagicMessage(final MagicGame game,final MagicPlayer player,final String text) {
        this.playerIndex=player.getIndex();
        this.playerConfig=player.getConfig();
        this.playerProfile=player.getProfile();
        this.life=player.getLife();
        this.turn=game.getTurn();
        this.phaseType=game.getPhase().getType();
        this.text=text;
    }

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public DuelPlayerConfig getPlayerConfig() {
        return playerConfig;
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
            .replaceAll("SN", getCardToken(source))
            .replaceAll("RN", getCardToken(ref))
            .replaceAll("\\bX\\b" + ARG.EVENQUOTES, getXCost(sourceText, ref));
    }

    public static String replaceChoices(final String sourceText, final Object[] choices) {

        String result = sourceText;

        for (int idx = 0; result.indexOf('$') >= 0; idx++) {

            final String choice = (idx < choices.length && choices[idx] != null)
                ? getCardToken(choices[idx])
                : "";

            final String replacement = !choice.isEmpty() ? " (" + choice + ")" : "";
            result = result.replaceFirst("\\$", replacement);
        }
        return result;
    }

    private static final String CARD_TOKEN = "<%s" + CARD_ID_DELIMITER + "%d>";

    private static String getXCost(final String sourceText, final Object obj) {
        if (obj != null && obj instanceof MagicPayedCost && sourceText.contains("where X") == false) {
            return "X (" + ((MagicPayedCost)obj).getX() + ")";
        } else {
            return "X";
        }
    }

    public static String format(final String template, final Object... args) {
        final Object[] strings = Arrays.stream(args).map(o -> getCardToken(o)).toArray();
        return String.format(template, strings);
    }

    public static String getCardToken(final Object obj) {

        if (obj == null) {
            return "";
        }

        if (obj instanceof MagicCard) {
            final MagicCard card = (MagicCard) obj;
            return String.format(CARD_TOKEN, card.getName(), card.getId());
        }

        if (obj instanceof MagicPermanent) {
            final MagicPermanent card = (MagicPermanent) obj;
            return String.format(CARD_TOKEN, card.getName(), card.getCard().getId());
        }

        if (obj instanceof MagicCardOnStack) {
            final MagicCardOnStack card = (MagicCardOnStack) obj;
            return String.format(CARD_TOKEN, card.getName(), card.getCard().getId());
        }

        if (obj instanceof MagicCardChoiceResult) {
            final MagicCardChoiceResult cards = (MagicCardChoiceResult) obj;
            return getTokenizedCardNames(cards);
        }

        // Please do not remove, thanks ~ lodici.
        // System.err.printf("getCardToken() : %s (%s)\n", obj.toString(), obj.getClass());

        return obj.toString();

    }

    public static String getCardToken(final String name, final MagicCard card) {
        return String.format(CARD_TOKEN, name, card.getId());
    }

    public static String getTokenizedCardNames(final Collection<MagicCard> cards) {
        return cards.stream()
            .map(card -> MagicMessage.getCardToken(card))
            .sorted()
            .collect(Collectors.joining(", "));
    }

}
