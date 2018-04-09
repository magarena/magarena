package magic.model.event;

import java.util.Arrays;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.ChangeCountersAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.target.MagicPumpTargetPicker;

public class MagicScavengeActivation extends MagicCardAbilityActivation {

    private static final MagicCondition[] COND = new MagicCondition[]{ MagicCondition.SORCERY_CONDITION };
    private static final MagicActivationHints HINT = new MagicActivationHints(MagicTiming.Pump,true);
    final MagicManaCost cost;
    final int power;

    public MagicScavengeActivation(final MagicCardDefinition aCdef, final MagicManaCost aCost) {
        super(
            COND,
            HINT,
            "Scavenge"
        );
        cost = aCost;
        power = aCdef.getCardPower();
    }

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addGraveyardAct(this);
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(source, cost),
            new MagicExileSelfEvent(source, MagicLocationType.Graveyard)
        );
    }

    @Override
    public MagicEvent getCardEvent(final MagicCard source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            MagicTargetChoice.POS_TARGET_CREATURE,
            MagicPumpTargetPicker.create(),
            power,
            this,
            "Put RN +1/+1 counters on target creature$."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTargetPermanent(game, (final MagicPermanent perm) ->
            game.doAction(new ChangeCountersAction(
                event.getSource(),
                perm,
                MagicCounterType.PlusOne,
                event.getRefInt()
            ))
        );
    }
}
