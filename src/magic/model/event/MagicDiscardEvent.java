package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicCopyMap;
import magic.model.action.DiscardCardAction;
import magic.model.choice.MagicCardChoice;
import magic.model.choice.MagicRandomCardChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicDiscardEvent extends MagicEvent {

    private final MagicCondition cond;
    private final boolean rand;

    public MagicDiscardEvent(final MagicSource source,final MagicPlayer player, final int amount) {
        this(source, player, amount, false);
    }

    public MagicDiscardEvent(final MagicSource source,final MagicPlayer player) {
        this(source, player, 1);
    }

    public MagicDiscardEvent(final MagicSource source,final int amount) {
        this(source, source.getController(), amount);
    }

    public MagicDiscardEvent(final MagicSource source) {
        this(source, source.getController(), 1);
    }

    public static MagicDiscardEvent Random(final MagicSource source, final MagicPlayer player, final int amount) {
        return new MagicDiscardEvent(source, player, amount, true);
    }

    public static MagicDiscardEvent Random(final MagicSource source, final int amount) {
        return Random(source, source.getController(), amount);
    }

    public static MagicDiscardEvent Random(final MagicSource source, final MagicPlayer player) {
        return Random(source, player, 1);
    }

    public static MagicDiscardEvent Random(final MagicSource source) {
        return Random(source, source.getController(), 1);
    }

    private MagicDiscardEvent(final MagicSource source,final MagicPlayer player,final int amount,final boolean random) {
        super(
            source,
            player,
            random ? new MagicRandomCardChoice(amount) : new MagicCardChoice(amount),
            amount,
            EVENT_ACTION,
            "PN " + genDescription(player,amount)
        );
        //check if source is in player's hand
        final int minHandSize = player.getHand().contains(source) ? amount + 1 : amount;
        cond = MagicConditionFactory.HandAtLeast(minHandSize);
        rand = random;
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) ->
        event.processChosenCards(game, (final MagicCard card) ->
            game.doAction(new DiscardCardAction(event.getPlayer(), card))
        );

    @Override
    public boolean isSatisfied() {
        return cond.accept(getSource()) && super.isSatisfied();
    }

    @Override
    public MagicEvent copy(final MagicCopyMap copyMap) {
        return new MagicDiscardEvent(
            copyMap.copy(getSource()),
            copyMap.copy(getPlayer()),
            getRefInt(),
            rand
        );
    }

    private static String genDescription(final MagicPlayer player,final int amount) {
        final int actualAmount = Math.min(amount,player.getHandSize());
        switch (actualAmount) {
            case 0: return "has no cards to discard.";
            case 1: return "discards a card$.";
            default : return "discards RN cards$.";
        }
    }
}
