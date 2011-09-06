package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Stuffy_Doll {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicEvent.NO_DATA,
                    new MagicEventAction() {
                        @Override
                        public void executeEvent(
                            final MagicGame game,
                            final MagicEvent event,
                            final Object[] data,
                            final Object[] choiceResults) {
                        final MagicPermanent permanent=source.map(game);
                        final MagicDamage damage=new MagicDamage(permanent,permanent,1,false);
                        game.doAction(new MagicDealDamageAction(damage));
                    }},
                    source + " deals 1 damage to itself.");
		}
	};
	
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player=permanent.getController();
            final int amount=damage.getDealtAmount();
            return (damage.getTarget()==permanent) ?
				new MagicEvent(
                    permanent,
                    player,
                    MagicEvent.NO_DATA,
                    new MagicEventAction() {
                    @Override
                    public void executeEvent(
                            final MagicGame game,
                            final MagicEvent event,
                            final Object data[],
                            final Object[] choiceResults) {
                        final MagicPermanent source = permanent.map(game);
                        final MagicPlayer target = game.getOpponent(player.map(game));
                        final MagicDamage damage=new MagicDamage(source, target, amount,false);
                        game.doAction(new MagicDealDamageAction(damage));
                    }},
                    permanent + " deals "+amount+" damage to your opponent.") :
                MagicEvent.NONE;
		}
    };
}
