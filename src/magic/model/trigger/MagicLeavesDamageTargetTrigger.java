package magic.model.trigger;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class MagicLeavesDamageTargetTrigger extends MagicWhenLeavesPlayTrigger {

    private final int n;
    private final MagicTargetChoice targetChoice;

    public MagicLeavesDamageTargetTrigger(final MagicTargetChoice targetChoice, final int n) {
        this.n = n;
        this.targetChoice = targetChoice;
    }
    
    private final String genDescription(final MagicTargetChoice targetChoice) {
        String description = "creature or player$.";
        if (targetChoice == MagicTargetChoice.TARGET_CREATURE) {
            description = "creature$.";
        }
        else if (targetChoice == MagicTargetChoice.NEG_TARGET_PLAYER) {
            description = "player$.";
        }
        return description;
    }
    
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent data) {
        final MagicPlayer player = permanent.getController();
        return (permanent == data) ?
            new MagicEvent(
                permanent,
                player,
                targetChoice,
                new MagicDamageTargetPicker(n),
                MagicEvent.NO_DATA,
                this,
                permanent + " deals " + n + " damage to target " +
                        genDescription(targetChoice)) :
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] data,
            final Object[] choiceResults) {
        event.processTarget(game,choiceResults,0,new MagicTargetAction() {
            public void doAction(final MagicTarget target) {
                final MagicDamage damage = new MagicDamage(event.getSource(),target,n,false);
                game.doAction(new MagicDealDamageAction(damage));
            }
        });
    }
}
