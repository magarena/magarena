package magic.model.event;

import java.util.LinkedList;
import java.util.List;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.AttachAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicArtificialCondition;
import magic.model.condition.MagicCondition;
import magic.model.target.MagicEquipTargetPicker;
import magic.model.target.MagicOtherPermanentTargetFilter;
import magic.model.target.MagicTargetFilterFactory;
import magic.model.target.MagicTargetHint;
import magic.model.target.MagicTargetFilter;

public class MagicEquipActivation extends MagicPermanentActivation {

    private final List<MagicMatchedCostEvent> costs;
    private final MagicTargetFilter<MagicPermanent> filter;
    private static final MagicMatchedCostEvent AI_MAX_TWICE = new MagicArtificialCondition(MagicCondition.ABILITY_TWICE_CONDITION);

    public MagicEquipActivation(final List<MagicMatchedCostEvent> aCosts) {
        this(aCosts, "Equip", MagicTargetFilterFactory.CREATURE_YOU_CONTROL);
    }

    public MagicEquipActivation(final List<MagicMatchedCostEvent> aCosts, final MagicTargetFilter<MagicPermanent> filter) {
        this(aCosts, "Equip", filter);
    }

    public MagicEquipActivation(final List<MagicMatchedCostEvent> aCosts, final String description, final MagicTargetFilter<MagicPermanent> aFilter) {
        super(
            new MagicCondition[]{
                MagicCondition.SORCERY_CONDITION,
                MagicCondition.NOT_CREATURE_CONDITION,
            },
            new MagicActivationHints(MagicTiming.Equipment),
            description
        );
        costs = aCosts;
        filter = aFilter;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
        final List<MagicEvent> costEvents = new LinkedList<>();
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
                filter,
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
        event.processTargetPermanent(game, (final MagicPermanent creature) ->
            game.doAction(new AttachAction(event.getPermanent(),creature))
        );
    }
}
