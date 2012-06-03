package magic.card;


import java.util.Iterator;

import magic.data.TokenCardDefinitions;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicTiming;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Ib_Halfheart__Goblin_Tactician {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
	@Override
	public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
	    final MagicPlayer player = permanent.getController();
            if (permanent != otherPermanent &&
		otherPermanent.getController() == player &&
		otherPermanent.isCreature() &&
		otherPermanent.hasSubType(MagicSubType.Goblin, game)) {
            	return new MagicEvent(
            			permanent,
            			player,
            			new Object[] { otherPermanent, otherPermanent.getBlockingCreatures() },
            			this,
            			player + " sacrifices " + otherPermanent + ". If " + player + " does, " + otherPermanent + " deals 4 damage to each creature blocking it.");
            }
            return MagicEvent.NONE;
	}
	
	@Override
	public void executeEvent(
        final MagicGame game,
        final MagicEvent event,
        final Object data[],
        final Object[] choiceResults) {
	    	MagicPermanent goblin = (MagicPermanent)data[0];
	    	MagicPermanentList blockingCreatures = (MagicPermanentList)data[1];
		Iterator<MagicPermanent> iterator = blockingCreatures.iterator();
		while (iterator.hasNext()) {
		    MagicPermanent blockingCreature = iterator.next();
		    MagicDamage damage = new MagicDamage(goblin, blockingCreature, 4, false);
		    game.doAction(new MagicDealDamageAction(damage));
		}
		game.doAction( new MagicSacrificeAction( goblin ) );
	}
    };
    
    public static final MagicPermanentActivation A1 = new MagicPermanentActivation(
	new MagicCondition[] { MagicCondition.TWO_MOUNTAINS_CONDITION },
	new MagicActivationHints(MagicTiming.Token, false), "Token") 
    {

    	@Override
    	public MagicEvent[] getCostEvent(final MagicSource source) {
    	    return new MagicEvent[] {
    		    new MagicSacrificePermanentEvent(source, source.getController(), MagicTargetChoice.SACRIFICE_MOUNTAIN),
    		    new MagicSacrificePermanentEvent(source, source.getController(), MagicTargetChoice.SACRIFICE_MOUNTAIN)
    	    };
    	}
    
    	@Override
    	public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
    	    MagicPlayer player = source.getController();
    	    return new MagicEvent(
                        source,
                        player,
                        new Object[]{player},
                        this,
                        player + " puts two 1/1 red Goblin creature tokens into play.");
    	}
    
    	@Override
    	public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
    		final MagicPlayer player = (MagicPlayer)data[0];
    		game.doAction(new MagicPlayTokenAction(player, TokenCardDefinitions.get("Goblin1")));
    		game.doAction(new MagicPlayTokenAction(player, TokenCardDefinitions.get("Goblin1")));
    	}
    };
}
