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
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class MagicEntersDamageTargetTrigger extends MagicWhenComesIntoPlayTrigger {

    private final int n;

    public MagicEntersDamageTargetTrigger(final int n) {
        this.n = n;
    }
    
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
        return new MagicEvent(
                permanent,
                player,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(1),
                MagicEvent.NO_DATA,
                this,
                permanent + " deals " + n + " damage to target creature or player$.");
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
