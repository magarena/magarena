package magic.model.event;

import magic.model.MagicManaCost;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicCard;
import magic.model.choice.MagicPayManaCostChoice;

public class MagicPayManaCostEvent extends MagicEvent {

    public static final MagicPayManaCostEvent Cast(final MagicCard card, final String cost) {
        return Cast(card, MagicManaCost.create(cost));
    }

    public static final MagicPayManaCostEvent Cast(final MagicCard card, final MagicManaCost cost) {
        return new MagicPayManaCostEvent(card, card.getGame().modCost(card, cost));
    }

    public MagicPayManaCostEvent(final MagicSource source, final String cost) {
        this(source, source.getController(), MagicManaCost.create(cost));
    }

    public MagicPayManaCostEvent(final MagicSource source, final MagicManaCost cost) {
        this(source, source.getController(), cost);
    }

    public MagicPayManaCostEvent(final MagicSource source, final MagicPlayer player, final String cost) {
        this(source, player, MagicManaCost.create(cost));
    }

    private MagicPayManaCostEvent(final MagicSource source,final MagicPlayer player,final MagicManaCost cost) {
        super(
            source,
            player,
            new MagicPayManaCostChoice(cost),
            MagicEventAction.NONE,
            cost == MagicManaCost.NONE ? "" : "Pay " + cost.getText() + "$."
        );
    }
}
