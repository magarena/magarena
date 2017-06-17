package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.action.MagicPlayMod;
import magic.model.action.ReturnCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;

import java.util.Arrays;

public class MagicNinjutsuActivation extends MagicCardAbilityActivation {

    final MagicManaCost cost;

    public MagicNinjutsuActivation(final MagicManaCost aCost) {
        super(
            new MagicCondition[]{
                MagicCondition.NINJUTSU_CONDITION,
            },
            new MagicActivationHints(MagicTiming.Block,true),
            "Ninjutsu"
        );
        cost = aCost;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(
                source,
                cost
            ),
            new MagicBounceChosenPermanentEvent(
                source,
                MagicTargetChoice.AN_UNBLOCKED_ATTACKING_CREATURE_YOU_CONTROL
            )
        );
    }

    @Override
    public MagicEvent getCardEvent(final MagicCard source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "Put SN onto the battlefield from your hand tapped and attacking."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new ReturnCardAction(
            MagicLocationType.OwnersHand,
            event.getCard(),
            event.getPlayer(),
            MagicPlayMod.TAPPED, MagicPlayMod.ATTACKING
        ));
    }
}
