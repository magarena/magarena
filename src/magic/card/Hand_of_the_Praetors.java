package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.action.MagicChangePoisonAction;
import magic.model.action.MagicPlayerAction;
import magic.model.event.MagicEvent;
import magic.model.choice.MagicTargetChoice;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenOtherSpellIsCastTrigger;

public class Hand_of_the_Praetors {
    public static final MagicStatic S = new MagicStatic(
        MagicLayer.ModPT, 
        MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,1);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target && target.hasAbility(MagicAbility.Infect);
        }
    };
    
    public static final MagicWhenOtherSpellIsCastTrigger T = new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return (permanent.isFriend(cardOnStack) &&
                    cardOnStack.getCardDefinition().isCreature() &&
                    cardOnStack.getCardDefinition().hasAbility(MagicAbility.Infect)) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    this,
                    "Target player$ gets a poison counter."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    game.doAction(new MagicChangePoisonAction(player,1));
                }
            });
        }        
    };
}
