package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.trigger.MagicWhenAttacksUnblockedTrigger;

public class Farrel_s_Zealot {
    public static final MagicWhenAttacksUnblockedTrigger T = new MagicWhenAttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature == permanent) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(MagicTargetChoice.NEG_TARGET_CREATURE),
                    new MagicDamageTargetPicker(3),
                    this,
                    "PN may$ have SN deal 3 damage to target creature$."
                ) :
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        final MagicPermanent permanent = event.getPermanent();
                        final MagicDamage damage = new MagicDamage(
                            permanent,
                            creature,
                            3
                        );
                        game.doAction(new MagicDealDamageAction(damage));
                        game.doAction(new MagicChangeStateAction(
                                permanent,
                                MagicPermanentState.NoCombatDamage,true));
                    }
                });
            }
        }
    };
}
