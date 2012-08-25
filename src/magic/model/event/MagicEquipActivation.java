package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicAttachEquipmentAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.target.MagicEquipTargetPicker;

public class MagicEquipActivation extends MagicPermanentActivation {

    private final MagicManaCost equipCost;
    
    public MagicEquipActivation(final MagicManaCost equipCost) {
        super(
            new MagicCondition[]{
                MagicCondition.SORCERY_CONDITION,
                MagicCondition.NOT_CREATURE_CONDITION,
                equipCost.getCondition()
            },
            new MagicActivationHints(MagicTiming.Equipment,false,2),
            "Equip"
            );
        this.equipCost=equipCost;
    }

    @Override
    protected MagicEvent[] getCostEvent(final MagicSource source) {
        return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),equipCost)};
    }

    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
                source,
                source.getController(),
                MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,
                new MagicEquipTargetPicker(source),
                new Object[]{source},
                this,
                "Attach " + source + " to target creature$ you control.");
    }

    @Override
    public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
        event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
            public void doAction(final MagicPermanent creature) {
                game.doAction(new MagicAttachEquipmentAction((MagicPermanent)data[0],creature));
            }
        });
    }
}
