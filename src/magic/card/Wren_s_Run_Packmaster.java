package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicExileUntilThisLeavesPlayAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicExileTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Wren_s_Run_Packmaster {
	public static final MagicStatic S = new MagicStatic(
			MagicLayer.Ability, 
			MagicTargetFilter.TARGET_WOLF_YOU_CONTROL) {
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return flags | MagicAbility.Deathtouch.getMask();
		}
	};
	    
	public static final MagicWhenComesIntoPlayTrigger T1 = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
			final MagicTargetFilter targetFilter = 
					new MagicTargetFilter.MagicOtherPermanentTargetFilter(
	                MagicTargetFilter.TARGET_ELF_YOU_CONTROL,permanent);
			final MagicTargetChoice targetChoice = 
					new MagicTargetChoice(
	                targetFilter,false,MagicTargetHint.None,"another Elf to exile");
	        final MagicChoice championChoice = 
	        		new MagicMayChoice(
	        		"You may exile another Elf you control. " +
	        		"If you don't, sacrifice " + permanent + ".",
	        		targetChoice);
			return new MagicEvent(
                    permanent,
                    player,
                    championChoice,
                    MagicExileTargetPicker.getInstance(),
                    new Object[]{permanent},
                    this,
                    "You may$ exile another Elf you control$. " +
	        		"If you don't, sacrifice " + permanent + ".");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPermanent permanent = (MagicPermanent)data[0];
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
					public void doAction(final MagicPermanent creature) {
						game.doAction(new MagicExileUntilThisLeavesPlayAction(permanent,creature));
					}
				});
			} else {
				game.doAction(new MagicSacrificeAction(permanent));
			}
		}
    };
    
    public static final MagicWhenLeavesPlayTrigger T2 = new MagicWhenLeavesPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent data) {
			if (permanent == data &&
				!permanent.getExiledCards().isEmpty()) {
				final MagicCard exiledCard = permanent.getExiledCards().get(0);;
				return new MagicEvent(
						permanent,
						permanent.getController(),
						new Object[]{exiledCard,permanent.getController()},
						this,
						"Return " + exiledCard + " to the battlefield");
			}
            return MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicCard exiledCard = (MagicCard)data[0];
			game.doAction(new MagicRemoveCardAction(exiledCard,MagicLocationType.Exile));
			game.doAction(new MagicPlayCardAction(exiledCard,exiledCard.getOwner(),MagicPlayCardAction.NONE));
		}
    };
    
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicManaCost.TWO_GREEN.getCondition()},
            new MagicActivationHints(MagicTiming.Token,true),
            "Token"
            ) {
		
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_GREEN)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player = source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{player},
                    this,
                    player + " puts a 2/2 green Wolf creature token onto the battlefield.");
        }
		
        @Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
        	game.doAction(new MagicPlayTokenAction(
        			(MagicPlayer)data[0],
        			TokenCardDefinitions.getInstance().getTokenDefinition("Wolf")));
        }
	};
}
