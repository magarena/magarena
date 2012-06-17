package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.mstatic.MagicLayer;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.action.MagicChangePoisonAction;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicStatic;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenOtherSpellIsCastTrigger;
import magic.model.MagicAbility;

public class Hand_of_the_Praetors {
    public static final MagicStatic S = new MagicStatic(
        MagicLayer.ModPT, 
        MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,1);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target && target.hasAbility(MagicAbility.Infect);
        }
    };
    
    public static final MagicWhenOtherSpellIsCastTrigger T = new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack data) {
            final MagicPlayer player = permanent.getController();
            final MagicCard card = data.getCard();
            return (card.getOwner() == player &&
                    data.getCardDefinition().isCreature() &&
                    data.getCardDefinition().hasAbility(MagicAbility.Infect)) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{game.getOpponent(player)},
                        this,
                        game.getOpponent(player) + " gets a poison counter."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangePoisonAction((MagicPlayer)data[0],1));
        }        
    };
}
