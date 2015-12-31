package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.AttachAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicArtificialCondition;
import magic.model.target.MagicEquipTargetPicker;
import magic.model.target.MagicOtherPermanentTargetFilter;
import magic.model.target.MagicTargetFilterFactory;
import magic.model.target.MagicTargetHint;

import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;

public class MagicEquipActivation extends MagicPermanentActivation {

    private final List<MagicMatchedCostEvent> costs;
    private static final MagicMatchedCostEvent AI_MAX_TWICE = new MagicArtificialCondition(MagicCondition.ABILITY_TWICE_CONDITION);

    public MagicEquipActivation(final List<MagicMatchedCostEvent> aCosts) {
        this(aCosts, "Equip");
    }

    public MagicEquipActivation(final List<MagicMatchedCostEvent> aCosts, final String description) {
        super(
            new MagicCondition[]{
                MagicCondition.SORCERY_CONDITION,
                MagicCondition.NOT_CREATURE_CONDITION,
            },
            new MagicActivationHints(MagicTiming.Equipment),
            description
        );
        costs = aCosts;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
        final List<MagicEvent> costEvents = new LinkedList<MagicEvent>();
        for (final MagicMatchedCostEvent matched : costs) {
            costEvents.add(matched.getEvent(source));
        }
        costEvents.add(AI_MAX_TWICE.getEvent(source));
        return costEvents;
    }

    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        final MagicTargetChoice TARGET_OTHER_CREATURE_YOU_CONTROL = new MagicTargetChoice(
            new MagicOtherPermanentTargetFilter(
                MagicTargetFilterFactory.CREATURE_YOU_CONTROL,
                source.getEquippedCreature()
            ),
            MagicTargetHint.None,
            "target creature you control"
        );
        return new MagicEvent(
            source,
            TARGET_OTHER_CREATURE_YOU_CONTROL,
            new MagicEquipTargetPicker(source),
            this,
            "Attach SN to target creature$ you control."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTargetPermanent(game,new MagicPermanentAction() {
            public void doAction(final MagicPermanent creature) {
                game.doAction(new AttachAction(event.getPermanent(),creature));
            }
        });
    }
}
