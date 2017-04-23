package magic.model.event;

import java.util.Arrays;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCardDefinitionInit;
import magic.model.MagicColor;
import magic.model.MagicSubType;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.action.MagicPlayMod;
import magic.model.action.PlayTokenAction;
import magic.model.condition.MagicCondition;

public class MagicEmbalmActivation extends MagicCardAbilityActivation {

    private static final MagicCondition[] COND = new MagicCondition[]{ MagicCondition.SORCERY_CONDITION };
    private static final MagicActivationHints HINT = new MagicActivationHints(MagicTiming.Token);
    private static final MagicCardDefinitionInit EMBALMED = (MagicCardDefinition it) -> {
        it.setColors("w");
        it.setCost(MagicManaCost.NONE);
        it.addSubType(MagicSubType.Zombie);
    };

    final MagicManaCost cost;

    public MagicEmbalmActivation(final MagicManaCost aCost) {
        super(
            COND,
            HINT,
            "Embalm"
        );
        cost = aCost;
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
            this,
            "PN creates a token that's a copy of SN, except it's white, it has no mana cost, and it's a Zombie in addition to its other types."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new PlayTokenAction(
            event.getPlayer(),
            MagicCardDefinition.token(
                event.getCard(),
                EMBALMED
            )
        ));
    }
}
