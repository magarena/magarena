package magic.model.event;

import java.util.List;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCardDefinitionInit;
import magic.model.MagicSubType;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.action.PlayTokenAction;
import magic.model.condition.MagicCondition;

public class MagicEternalizeActivation extends MagicCardAbilityActivation {

    private static final MagicCondition[] COND = new MagicCondition[]{ MagicCondition.SORCERY_CONDITION };
    private static final MagicActivationHints HINT = new MagicActivationHints(MagicTiming.Token);
    private static final MagicCardDefinitionInit ETERNALIZED = (MagicCardDefinition it) -> {
        it.setPowerToughness(4,4);
        it.setColors("b");
        it.setCost(MagicManaCost.NONE);
        it.addSubType(MagicSubType.Zombie);
    };

    private final List<MagicMatchedCostEvent> costs;

    public MagicEternalizeActivation(final List<MagicMatchedCostEvent> aCosts) {
        super(
            COND,
            HINT,
            "Eternalize"
        );
        costs = aCosts;
    }

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addGraveyardAct(this);
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return MagicMatchedCostEvent.getCostEvent(costs, source);
    }

    @Override
    public MagicEvent getCardEvent(final MagicCard source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "PN creates a token that's a copy of SN, except it's a 4/4 black creature with no mana cost, and it's a Zombie in addition to its other types."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new PlayTokenAction(
            event.getPlayer(),
            MagicCardDefinition.token(
                event.getCard(),
                ETERNALIZED
            )
        ));
    }
}
