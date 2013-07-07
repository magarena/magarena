package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicAttachEquipmentAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.target.MagicEquipTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicOtherPermanentTargetFilter;
import magic.model.target.MagicTargetHint;

public class MagicEquipActivation extends MagicPermanentActivation {

    private final MagicManaCost equipCost;

    public MagicEquipActivation(final MagicManaCost equipCost) {
        super(
            new MagicCondition[]{
                MagicCondition.SORCERY_CONDITION,
                MagicCondition.NOT_CREATURE_CONDITION,
            },
            new MagicActivationHints(MagicTiming.Equipment,2),
            "Equip"
        );
        this.equipCost=equipCost;
    }

    @Override
    public MagicEvent[] getCostEvent(final MagicPermanent source) {
        return new MagicEvent[]{new MagicPayManaCostEvent(source,equipCost)};
    }

    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        final MagicTargetChoice TARGET_OTHER_CREATURE_YOU_CONTROL = new MagicTargetChoice(
            new MagicOtherPermanentTargetFilter(
                MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,
                source.getEquippedCreature()
            ),
            true,
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
                game.doAction(new MagicAttachEquipmentAction(event.getPermanent(),creature));
            }
        });
    }
}
