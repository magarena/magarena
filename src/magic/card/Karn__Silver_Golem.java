package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicSource;
import magic.model.MagicType;
import magic.model.action.MagicBecomesCreatureAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetPicker;
import magic.model.trigger.MagicBecomesBlockedPumpTrigger;
import magic.model.trigger.MagicWhenBlocksPumpTrigger;

public class Karn__Silver_Golem {
	private static final MagicStatic PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int cmc = permanent.getCardDefinition().getConvertedCost();
			pt.set(cmc,cmc);
		}
    };
    private static final MagicStatic ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicType.Artifact.getMask()|MagicType.Creature.getMask();
		}
	};
    private static final MagicTargetPicker<MagicPermanent> TP = new MagicTargetPicker<MagicPermanent>() {
        @Override
    	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
            final MagicPowerToughness pt=permanent.getPowerToughness(game);
            final int power = permanent.getCardDefinition().getConvertedCost();
            final int toughness = permanent.getCardDefinition().getConvertedCost();
            int score = (pt.power()-power)*2+(pt.toughness()-toughness);
            return permanent.getController() == player ? -score:score;
        }
    };
    
	public static final MagicBecomesBlockedPumpTrigger T1 = new MagicBecomesBlockedPumpTrigger(-4,4,false);
	
	public static final MagicWhenBlocksPumpTrigger T2 = new MagicWhenBlocksPumpTrigger(-4,4);
	
	public static final MagicPermanentActivation A =new MagicPermanentActivation(
			new MagicCondition[]{MagicManaCost.ONE.getCondition()},
            new MagicActivationHints(MagicTiming.Animate),
            "Animate") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(
                    source,
                    player,
                    MagicTargetChoice.POS_TARGET_NONCREATURE_ARTIFACT,
                    TP,
				    MagicEvent.NO_DATA,
				    this,
				    "Target noncreature artifact$ becomes an artifact creature with " + 
                    "power and toughness each equal to its converted mana cost until end of turn");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
			        game.doAction(new MagicBecomesCreatureAction(creature,PT,ST));
                }
			});
		}
	};
}
