package magic.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;
import magic.model.MagicPowerToughness;
import magic.model.MagicSource;
import magic.model.MagicType;
import magic.model.action.MagicBecomesCreatureAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicChangeExtraTurnsAction;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicChangePlayerStateAction;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicCounterItemOnStackAction;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicDrawAction;
import magic.model.action.MagicGainControlAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.action.MagicPreventDamageAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.action.MagicReanimateAction;
import magic.model.action.MagicRegenerateAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.action.MagicShuffleIntoLibraryAction;
import magic.model.action.MagicTapAction;
import magic.model.action.MagicUntapAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicCounterUnlessEvent;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicTriggerOnStack;
import magic.model.target.MagicBounceTargetPicker;
import magic.model.target.MagicCopyTargetPicker;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.target.MagicExileTargetPicker;
import magic.model.target.MagicFirstStrikeTargetPicker;
import magic.model.target.MagicFlyingTargetPicker;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.target.MagicHasteTargetPicker;
import magic.model.target.MagicIndestructibleTargetPicker;
import magic.model.target.MagicNoCombatTargetPicker;
import magic.model.target.MagicPowerTargetPicker;
import magic.model.target.MagicPreventTargetPicker;
import magic.model.target.MagicPumpTargetPicker;
import magic.model.target.MagicTapTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTrampleTargetPicker;
import magic.model.target.MagicUnblockableTargetPicker;
import magic.model.target.MagicWeakenTargetPicker;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;

public class CardEventDefinitions {

	// ***** INSTANTS *****
	
	private static final MagicSpellCardEvent ABSORB=new MagicSpellCardEvent("Absorb") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_SPELL,new Object[]{cardOnStack,player},this,
				"Counter target spell$. You gain 3 life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicCardOnStack targetSpell=event.getTarget(game,choiceResults,0);
			if (targetSpell!=null) {
				game.doAction(new MagicCounterItemOnStackAction(targetSpell));
			}
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[1],3));
		}
	};

	private static final MagicSpellCardEvent BACKLASH=new MagicSpellCardEvent("Backlash") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),
				MagicTargetChoice.NEG_TARGET_UNTAPPED_CREATURE,new MagicTapTargetPicker(true,false),new Object[]{cardOnStack},this,
				"Tap target untapped creature$. That creature deals damage equal to its power to its controller.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicTapAction(creature,true));
				final MagicDamage damage=new MagicDamage(creature,creature.getController(),creature.getPower(game),false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
	private static final MagicSpellCardEvent BEACON_OF_DESTRUCTION=new MagicSpellCardEvent("Beacon of Destruction") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			final MagicCard card=cardOnStack.getCard();
			return new MagicEvent(card,player,MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(5),new Object[]{card},this,
				"Beacon of Destruction deals 5 damage to target creature or player$. Shuffle Beacon of Destruction into its owner's library.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCard card=(MagicCard)data[0];
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage(card,target,5,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			game.doAction(new MagicShuffleIntoLibraryAction(card));
		}
	};
	
	private static final MagicSpellCardEvent BURST_LIGHTNING=new MagicSpellCardEvent("Burst Lightning") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,
				new MagicKickerChoice(MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,MagicManaCost.FOUR,false),
				new MagicDamageTargetPicker(2),new Object[]{cardOnStack},this,
				"Burst Lightning deals 2 damage to target creature or player$. "+
				"If Burst Lightning was kicked$, it deals 4 damage to that creature or player instead.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final int amount=((Integer)choiceResults[1])>0?4:2;
				final MagicDamage damage=new MagicDamage(cardOnStack.getCard(),target,amount,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};

	private static final MagicSpellCardEvent CHAR=new MagicSpellCardEvent("Char") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(4),
				new Object[]{cardOnStack,player},this,"Char deals 4 damage to target creature or player$ and 2 damage to you.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicSource source=cardOnStack.getCard();
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage1=new MagicDamage(source,target,4,false);
				game.doAction(new MagicDealDamageAction(damage1));
			}
			final MagicDamage damage2=new MagicDamage(source,(MagicPlayer)data[1],2,false);
			game.doAction(new MagicDealDamageAction(damage2));
		}
	};
	
	private static final MagicSpellCardEvent CHASTISE=new MagicSpellCardEvent("Chastise") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_ATTACKING_CREATURE,new MagicDestroyTargetPicker(false),
				new Object[]{cardOnStack,player},this,"Destroy target attacking creature$. You gain life equal to its power.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				final int power=creature.getPower(game);
				game.doAction(new MagicDestroyAction(creature));
				game.doAction(new MagicChangeLifeAction((MagicPlayer)data[1],power));
			}
		}
	};

	private static final MagicSpellCardEvent COLOSSAL_MIGHT=new MagicSpellCardEvent("Colossal Might") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance(),
				new Object[]{cardOnStack},this,"Target creature$ gets +4/+2 and gains trample until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,4,2));
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Trample));
			}
		}
	};

	private static final MagicSpellCardEvent COUNTERSPELL=new MagicSpellCardEvent("Counterspell") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_SPELL,new Object[]{cardOnStack},this,"Counter target spell$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicCardOnStack targetSpell=event.getTarget(game,choiceResults,0);
			if (targetSpell!=null) {
				game.doAction(new MagicCounterItemOnStackAction(targetSpell));
			}
		}
	};

	private static final MagicSpellCardEvent COUNTERSQUALL=new MagicSpellCardEvent("Countersquall") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_NONCREATURE_SPELL,
				new Object[]{cardOnStack},this,"Counter target noncreature spell$. Its controller loses 2 life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicCardOnStack targetSpell=event.getTarget(game,choiceResults,0);
			if (targetSpell!=null) {
				game.doAction(new MagicCounterItemOnStackAction(targetSpell));
				game.doAction(new MagicChangeLifeAction(targetSpell.getController(),-2));
			}
		}
	};

	private static final MagicSpellCardEvent DELUGE=new MagicSpellCardEvent("Deluge") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),new Object[]{cardOnStack},this,"Tap all creatures without flying.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final Collection<MagicTarget> targets=game.filterTargets(cardOnStack.getController(),MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING);
			for (final MagicTarget target : targets) {
				
				game.doAction(new MagicTapAction((MagicPermanent)target,true));
			}
		}
	};

	private static final MagicLocalVariable DIMINISH_VARIABLE=new MagicDummyLocalVariable() {

		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {

			pt.power=1;
			pt.toughness=1;
		}
	};
	
	private static final MagicSpellCardEvent DIMINISH=new MagicSpellCardEvent("Diminish") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.TARGET_CREATURE,new MagicDestroyTargetPicker(true),
				new Object[]{cardOnStack},this,"Target creature$ becomes 1/1 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicBecomesCreatureAction(creature,DIMINISH_VARIABLE));
			}
		}
	};
	
	private static final MagicSpellCardEvent DISFIGURE=new MagicSpellCardEvent("Disfigure") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_CREATURE,new MagicWeakenTargetPicker(2,2),
				new Object[]{cardOnStack},this,"Target creature$ gets -2/-2 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,-2,-2));
			}
		}
	};
	
	private static final MagicSpellCardEvent DISMAL_FAILURE=new MagicSpellCardEvent("Dismal Failure") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_SPELL,
				new Object[]{cardOnStack},this,"Counter target spell$. Its controller discards a card.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicCardOnStack counteredCard=event.getTarget(game,choiceResults,0);
			if (counteredCard!=null) {
				game.doAction(new MagicCounterItemOnStackAction(counteredCard));
				game.addEvent(new MagicDiscardEvent(cardOnStack.getCard(),counteredCard.getController(),1,false));
			}
		}
	};
	
	private static final MagicSpellCardEvent DISPERSE=new MagicSpellCardEvent("Disperse") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_NONLAND_PERMANENT,MagicBounceTargetPicker.getInstance(),
				new Object[]{cardOnStack},this,"Return target nonland permanent$ to its owner's hand.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.OwnersHand));
			}
		}
	};
	
	private static final MagicSpellCardEvent DOOM_BLADE=new MagicSpellCardEvent("Doom Blade") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.NEG_TARGET_NONBLACK_CREATURE,
				new MagicDestroyTargetPicker(false),new Object[]{cardOnStack},this,"Destroy target nonblack creature$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicDestroyAction(creature));
			}
		}
	};

	private static final MagicSpellCardEvent DOUBLE_CLEAVE=new MagicSpellCardEvent("Double Cleave") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.POS_TARGET_CREATURE,MagicFirstStrikeTargetPicker.getInstance(),
				new Object[]{cardOnStack},this,"Target creature$ gains double strike until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.DoubleStrike));
			}
		}
	};

	private static final MagicSpellCardEvent DOUSE_IN_GLOOM=new MagicSpellCardEvent("Douse in Gloom") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_CREATURE,new MagicDamageTargetPicker(2),
				new Object[]{cardOnStack,player},this,"Douse in Gloom deals 2 damage to target creature$ and you gain 2 life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));			
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage(cardOnStack.getCard(),target,2,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[1],2));
		}
	};
	
	private static final MagicSpellCardEvent ESSENCE_SCATTER=new MagicSpellCardEvent("Essence Scatter") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_CREATURE_SPELL,
				new Object[]{cardOnStack},this,"Counter target creature spell$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicCardOnStack targetSpell=event.getTarget(game,choiceResults,0);
			if (targetSpell!=null) {
				game.doAction(new MagicCounterItemOnStackAction(targetSpell));
			}
		}
	};
	
	private static final MagicSpellCardEvent EVACUATION=new MagicSpellCardEvent("Evacuation") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),new Object[]{cardOnStack},this,
				"Return all creatures to their owner's hand.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));			
			final Collection<MagicTarget> targets=game.filterTargets(cardOnStack.getController(),MagicTargetFilter.TARGET_CREATURE);
			for (final MagicTarget target : targets) {
				
				game.doAction(new MagicRemoveFromPlayAction((MagicPermanent)target,MagicLocationType.OwnersHand));
			}
		}
	};

	private static final MagicSpellCardEvent FISTS_OF_THE_ANVIL=new MagicSpellCardEvent("Fists of the Anvil") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance(),
				new Object[]{cardOnStack},this,"Target creature$ gets +4/+0 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,4,0));
			}
		}
	};
	
	private static final MagicSpellCardEvent GIANT_GROWTH=new MagicSpellCardEvent("Giant Growth") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance(),
				new Object[]{cardOnStack},this,"Target creature$ gets +3/+3 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {			
				game.doAction(new MagicChangeTurnPTAction(creature,3,3));
			}
		}
	};

	private static final MagicSpellCardEvent GLORIOUS_CHARGE=new MagicSpellCardEvent("Glorious Charge") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,player},this,"Creatures you control get +1/+1 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));			
			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[1],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				
				game.doAction(new MagicChangeTurnPTAction((MagicPermanent)target,1,1));
			}
		}
	};

	private static final MagicSpellCardEvent GRASP_OF_DARKNESS=new MagicSpellCardEvent("Grasp of Darkness") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.NEG_TARGET_CREATURE,new MagicWeakenTargetPicker(-4,-4),
				new Object[]{cardOnStack},this,"Target creature$ gets -4/-4 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,-4,-4));
			}
		}
	};
	
	private static final MagicSpellCardEvent HEROES_REUNION=new MagicSpellCardEvent("Heroes' Reunion") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.POS_TARGET_PLAYER,
				new Object[]{cardOnStack},this,"Target player$ gains 7 life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				game.doAction(new MagicChangeLifeAction(player,7));
			}
		}
	};
	
	private static final MagicSpellCardEvent HIDEOUS_END=new MagicSpellCardEvent("Hideous End") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_NONBLACK_CREATURE,new MagicDestroyTargetPicker(false),
				new Object[]{cardOnStack},this,"Destroy target nonblack creature$. Its controller loses 2 life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				final MagicPlayer controller=creature.getController();
				game.doAction(new MagicDestroyAction(creature));
				game.doAction(new MagicChangeLifeAction(controller,-2));
			}
		}
	};

	private static final MagicSpellCardEvent INCINERATE=new MagicSpellCardEvent("Incinerate") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(3,true),
				new Object[]{cardOnStack},this,
				"Incinerate deals 3 damage to target creature or player$. A creature dealt damage this way can't be regenerated this turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage(cardOnStack.getCard(),target,3,false);
				damage.setNoRegeneration();
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
		
	private static final MagicSpellCardEvent INSPIRIT=new MagicSpellCardEvent("Inspirit") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance(),
				new Object[]{cardOnStack},this,"Untap target creature$. It gets +2/+4 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {			
				game.doAction(new MagicUntapAction(creature));
				game.doAction(new MagicChangeTurnPTAction(creature,2,4));
			}
		}
	};
	
	private static final MagicSpellCardEvent KINDLED_FURY=new MagicSpellCardEvent("Kindled Fury") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.POS_TARGET_CREATURE,MagicFirstStrikeTargetPicker.getInstance(),
				new Object[]{cardOnStack},this,"Target creature$ gets +1/+0 and gains first strike until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,1,0));
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.FirstStrike));
			}
		}
	};
	
	private static final MagicSpellCardEvent LIGHTNING_BOLT=new MagicSpellCardEvent("Lightning Bolt") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
				new MagicDamageTargetPicker(3),new Object[]{cardOnStack},this,"Lightning Bolt deals 3 damage to target creature or player$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage(cardOnStack.getCard(),target,3,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};

	private static final MagicSpellCardEvent LIGHTNING_HELIX=new MagicSpellCardEvent("Lightning Helix") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(3),
				new Object[]{cardOnStack,player},this,"Lightning Bolt deals 3 damage to target creature or player$ and you gain 3 life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage(cardOnStack.getCard(),target,3,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[1],3));
		}
	};
	
	private static final MagicSpellCardEvent MAELSTROM_PULSE=new MagicSpellCardEvent("Maelstrom Pulse") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.NEG_TARGET_NONLAND_PERMANENT,new MagicDestroyTargetPicker(false),
				new Object[]{cardOnStack},this,"Destroy target nonland permanent$ and all other permanents with the same name as that permanent.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			
			final MagicPermanent targetPermanent=event.getTarget(game,choiceResults,0);
			if (targetPermanent!=null) {
				final MagicTargetFilter targetFilter=new MagicTargetFilter.NameTargetFilter(targetPermanent.getName());
				final Collection<MagicTarget> targets=game.filterTargets(cardOnStack.getController(),targetFilter);
				for (final MagicTarget target : targets) {

					game.doAction(new MagicDestroyAction((MagicPermanent)target));
				}
			}
		}
	};
	
	private static final MagicSpellCardEvent MANA_LEAK=new MagicSpellCardEvent("Mana Leak") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.NEG_TARGET_SPELL,
				new Object[]{cardOnStack},this,"Counter target spell$ unless its controller pays {3}.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicCardOnStack targetSpell=event.getTarget(game,choiceResults,0);
			if (targetSpell!=null) {
				game.addEvent(new MagicCounterUnlessEvent(cardOnStack.getCard(),targetSpell,MagicManaCost.THREE));
			}
		}
	};
	
	private static final MagicSpellCardEvent MIGHT_OF_OAKS=new MagicSpellCardEvent("Might of Oaks") {
		
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance(),
				new Object[]{cardOnStack},this,"Target creature$ gets +7/+7 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,7,7));
			}
		}
	};

	private static final MagicSpellCardEvent MIGHTY_LEAP=new MagicSpellCardEvent("Mighty Leap") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.POS_TARGET_CREATURE,MagicFlyingTargetPicker.getInstance(),
				new Object[]{cardOnStack},this,"Target creature$ gets +2/+2 and gains flying until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,2,2));
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Flying));
			}
		}
	};
	
	private static final MagicSpellCardEvent MORTIFY=new MagicSpellCardEvent("Mortify") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_CREATURE_OR_ENCHANTMENT,
				new MagicDestroyTargetPicker(false),new Object[]{cardOnStack},this,"Destroy target creature or enchantment$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				game.doAction(new MagicDestroyAction(permanent));
			}
		}
	};
	
	private static final MagicSpellCardEvent NATURALIZE=new MagicSpellCardEvent("Naturalize") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.NEG_TARGET_ARTIFACT_OR_ENCHANTMENT,
				new MagicDestroyTargetPicker(false),new Object[]{cardOnStack},this,"Destroy target artifact or enchantment$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				game.doAction(new MagicDestroyAction(permanent));
			}
		}
	};
	
	private static final MagicSpellCardEvent OFFERING_TO_ASHA=new MagicSpellCardEvent("Offering to Asha") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_SPELL,
				new Object[]{cardOnStack,player},this,"Counter target spell$ unless its controller pays {4}. You gain 4 life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[1],4));
			final MagicCardOnStack targetSpell=event.getTarget(game,choiceResults,0);
			if (targetSpell!=null) {
				game.addEvent(new MagicCounterUnlessEvent(cardOnStack.getCard(),targetSpell,MagicManaCost.FOUR));
			}
		}
	};
	
	private static final MagicSpellCardEvent PLUMMET=new MagicSpellCardEvent("Plummet") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.NEG_TARGET_CREATURE_WITH_FLYING,
				new MagicDestroyTargetPicker(false),new Object[]{cardOnStack},this,"Destroy target creature$ with flying.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				game.doAction(new MagicDestroyAction((MagicPermanent)target));
			}
		}
	};

	private static final MagicSpellCardEvent PUNCTURE_BLAST=new MagicSpellCardEvent("Puncture Blast") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(3),
				new Object[]{cardOnStack},this,"Puncture Blast deals 3 damage to target creature or player$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage(cardOnStack.getCard(),target,3,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
	private static final MagicSpellCardEvent PUTREFY=new MagicSpellCardEvent("Putrefy") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_CREATURE_OR_ARTIFACT,new MagicDestroyTargetPicker(true),
				new Object[]{cardOnStack},this,"Destroy target artifact or creature$. It can't be regenerated.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				game.doAction(new MagicChangeStateAction(permanent,MagicPermanentState.CannotBeRegenerated,true));
				game.doAction(new MagicDestroyAction(permanent));
			}
		}
	};

	private static final MagicSpellCardEvent REMAND=new MagicSpellCardEvent("Remand") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_SPELL,
				new Object[]{cardOnStack,player},this,"Counter target spell$. If you do, return that spell card to its owner's hand. Draw a card.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicCardOnStack targetSpell=event.getTarget(game,choiceResults,0);
			if (targetSpell!=null) {
				game.doAction(new MagicCounterItemOnStackAction(targetSpell,MagicLocationType.OwnersHand));
			}
			game.doAction(new MagicDrawAction((MagicPlayer)data[1],1));
		}
	};
		
	private static final MagicSpellCardEvent REPULSE=new MagicSpellCardEvent("Repulse") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.TARGET_CREATURE,MagicBounceTargetPicker.getInstance(),
				new Object[]{cardOnStack,player},this,"Return target creature$ to its owner's hand. Draw a card.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.OwnersHand));
			}
			game.doAction(new MagicDrawAction((MagicPlayer)data[1],1));
		}
	};

	private static final MagicSpellCardEvent SAFE_PASSAGE=new MagicSpellCardEvent("Safe Passage") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,player},this,
				"Prevent all damage that would be dealt to you and creatures you control this turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			game.doAction(new MagicChangePlayerStateAction((MagicPlayer)data[1],MagicPlayerState.PreventAllDamage,true));			
		}
	};

	private static final MagicSpellCardEvent SCAR=new MagicSpellCardEvent("Scar") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_CREATURE,new MagicWeakenTargetPicker(-1,-1),
				new Object[]{cardOnStack},this,"Put a -1/-1 counter on target creature$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.MinusOne,1,true));
			}
		}
	};
	
	private static final MagicSpellCardEvent SIGIL_BLESSING=new MagicSpellCardEvent("Sigil Blessing") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,MagicPumpTargetPicker.getInstance(),
				new Object[]{cardOnStack,player},this,"Until end of turn, target creature$ you control gets +3/+3 and other creatures you control get +1/+1.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[1],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				
				final MagicPermanent permanent=(MagicPermanent)target;
				if (permanent==creature) {
					game.doAction(new MagicChangeTurnPTAction(permanent,3,3));
				} else {
					game.doAction(new MagicChangeTurnPTAction(permanent,1,1));
				}
			}
		}
	};
	
	private static final MagicSpellCardEvent SMOTHER=new MagicSpellCardEvent("Smother") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),
				MagicTargetChoice.NEG_TARGET_CREATURE_CONVERTED_3_OR_LESS,new MagicDestroyTargetPicker(true),
				new Object[]{cardOnStack},this,"Destroy target creature$ with converted mana cost 3 or less. It can't be regenerated.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeStateAction(creature,MagicPermanentState.CannotBeRegenerated,true));
				game.doAction(new MagicDestroyAction(creature));
			}
		}
	};
	
	private static final MagicSpellCardEvent SWORDS_TO_PLOWSHARES=new MagicSpellCardEvent("Swords to Plowshares") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,
				MagicTargetChoice.NEG_TARGET_CREATURE,MagicExileTargetPicker.getInstance(),new Object[]{cardOnStack},this,
				"Exile target creature$. Its controller gains life equal to its power.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeLifeAction(creature.getController(),creature.getPower(game)));
				game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.Exiled));
			}
		}
	};
	
	private static final MagicSpellCardEvent TERMINATE=new MagicSpellCardEvent("Terminate") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_CREATURE,new MagicDestroyTargetPicker(true),
				new Object[]{cardOnStack},this,"Destroy target creature$. It can't be regenerated.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeStateAction(creature,MagicPermanentState.CannotBeRegenerated,true));
				game.doAction(new MagicDestroyAction(creature));
			}
		}
	};
	
	private static final MagicSpellCardEvent THUNDER_STRIKE=new MagicSpellCardEvent("Thunder Strike") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.POS_TARGET_CREATURE,MagicFirstStrikeTargetPicker.getInstance(),
				new Object[]{cardOnStack},this,"Target creature$ gets +2/+0 and gains first strike until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,2,0));
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.FirstStrike));
			}
		}
	};

	private static final MagicSpellCardEvent TO_ARMS=new MagicSpellCardEvent("To Arms!") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,player},this,"Untap all creatures you control. Draw a card.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPlayer player=(MagicPlayer)data[1];
			final Collection<MagicTarget> targets=game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				
				game.doAction(new MagicUntapAction((MagicPermanent)target));
			}
			game.doAction(new MagicDrawAction(player,1));
		}
	};

	private static final MagicSpellCardEvent TRUMPET_BLAST=new MagicSpellCardEvent("Trumpet Blast") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack},this,"Attacking creatures get +2/+0 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];			
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final Collection<MagicTarget> targets=game.filterTargets(cardOnStack.getController(),MagicTargetFilter.TARGET_ATTACKING_CREATURE);
			for (final MagicTarget target : targets) {
				
				game.doAction(new MagicChangeTurnPTAction((MagicPermanent)target,2,0));
			}
		}
	};
	
	private static final MagicSpellCardEvent UNDERMINE=new MagicSpellCardEvent("Undermine") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_SPELL,
				new Object[]{cardOnStack},this,"Counter target spell$. Its controller loses 3 life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicCardOnStack counteredCard=event.getTarget(game,choiceResults,0);
			if (counteredCard!=null) {
				game.doAction(new MagicCounterItemOnStackAction(counteredCard));
				game.doAction(new MagicChangeLifeAction(counteredCard.getController(),-3));
			}
		}
	};
	
	private static final MagicSpellCardEvent UNMAKE=new MagicSpellCardEvent("Unmake") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.NEG_TARGET_CREATURE,
				MagicExileTargetPicker.getInstance(),new Object[]{cardOnStack},this,"Exile target creature$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.Exiled));
			}
		}
	};
	
	private static final MagicSpellCardEvent UNSUMMON=new MagicSpellCardEvent("Unsummon") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.TARGET_CREATURE,MagicBounceTargetPicker.getInstance(),
				new Object[]{cardOnStack},this,"Return target creature$ to its owner's hand.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.OwnersHand));
			}
		}
	};

	private static final MagicSpellCardEvent VAULT_SKYWARD=new MagicSpellCardEvent("Vault Skyward") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.POS_TARGET_CREATURE,MagicFlyingTargetPicker.getInstance(),
				new Object[]{cardOnStack},this,"Target creature$ gains flying until end of turn. Untap it.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Flying));
				game.doAction(new MagicUntapAction(creature));
			}
		}
	};
	
	private static final MagicSpellCardEvent VETERANS_REFLEXES=new MagicSpellCardEvent("Veteran's Reflexes") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance(),
				new Object[]{cardOnStack},this,"Target creature$ gets +1/+1 until end of turn. Untap that creature.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,1,1));
				game.doAction(new MagicUntapAction(creature));
			}
		}
	};
	
	private static final MagicSpellCardEvent WILDSIZE=new MagicSpellCardEvent("Wildsize") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance(),
				new Object[]{cardOnStack,player},this,"Target creature$ gets +2/+2 and gains trample until end of turn. Draw a card.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,2,2));
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Trample));
			}
			game.doAction(new MagicDrawAction((MagicPlayer)data[1],1));
		}
	};

	private static final MagicSpellCardEvent WITHSTAND=new MagicSpellCardEvent("Withstand") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.POS_TARGET_CREATURE_OR_PLAYER,MagicPreventTargetPicker.getInstance(),
				new Object[]{cardOnStack,player},this,"Prevent the next 3 damage that would be dealt to target creature or player$ this turn. Draw a card.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				game.doAction(new MagicPreventDamageAction(target,3));
			}
			game.doAction(new MagicDrawAction((MagicPlayer)data[1],1));
		}
	};

	private static final MagicSpellCardEvent WITHSTAND_DEATH=new MagicSpellCardEvent("Withstand Death") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.POS_TARGET_CREATURE,MagicIndestructibleTargetPicker.getInstance(),
				new Object[]{cardOnStack},this,"Target creature$ is indestructible turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Indestructible));
			}
		}
	};
	
	private static final MagicSpellCardEvent WRAP_IN_VIGOR=new MagicSpellCardEvent("Wrap in Vigor") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,player},this,"Regenerate each creature you control.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));			
			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[1],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				
				final MagicPermanent creature=(MagicPermanent)target;
				if (creature.canRegenerate()) {
					game.doAction(new MagicRegenerateAction(creature));
				}
			}
		}
	};
	
	private static final MagicSpellCardEvent ZEALOUS_PERSECUTION=new MagicSpellCardEvent("Zealous Persecution") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,player},this,
				"Until end of turn, creatures you control get +1/+1 and creatures your opponent controls get -1/-1.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPlayer player=(MagicPlayer)data[1];
			final Collection<MagicTarget> targets=game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE);
			for (final MagicTarget target : targets) {
				
				final MagicPermanent creature=(MagicPermanent)target;
				if (creature.getController()==player) {
					game.doAction(new MagicChangeTurnPTAction(creature,1,1));
				} else {
					game.doAction(new MagicChangeTurnPTAction(creature,-1,-1));
				}
			}
		}
	};
	
	// ***** SORCERIES *****
	
	private static final MagicSpellCardEvent ASSASSINATE=new MagicSpellCardEvent("Assassinate") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_TAPPED_CREATURE,new MagicDestroyTargetPicker(false),
				new Object[]{cardOnStack},this,"Destroy target tapped creature$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicDestroyAction(creature));
			}
		}
	};

	private static final MagicSpellCardEvent BEACON_OF_UNREST=new MagicSpellCardEvent("Beacon of Unrest") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			final MagicCard card=cardOnStack.getCard();
			return new MagicEvent(card,player,MagicTargetChoice.TARGET_ARTIFACT_OR_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
				MagicGraveyardTargetPicker.getInstance(),new Object[]{card,player},this,
				"Return target artifact or creature card$ from a graveyard onto the battlefield under your control. "+
				"Shuffle Beacon of Unrest into its owner's library.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCard targetCard=event.getTarget(game,choiceResults,0);
			if (targetCard!=null) {
				game.doAction(new MagicReanimateAction((MagicPlayer)data[1],targetCard,MagicPlayCardAction.NONE));
			}
			game.doAction(new MagicShuffleIntoLibraryAction((MagicCard)data[0]));
		}
	};
	
	private static final MagicSpellCardEvent BESTIAL_MENACE=new MagicSpellCardEvent("Bestial Menace") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,player},this,
				"Put a 1/1 green Snake creature token, a 2/2 green Wolf creature token and a 3/3 green Elephant creature token onto the battlefield.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPlayer player=(MagicPlayer)data[1];
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.SNAKE_TOKEN_CARD));
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.WOLF_TOKEN_CARD));
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.ELEPHANT_TOKEN_CARD));
		}
	};
	
	private static final MagicSpellCardEvent BLAZE=new MagicSpellCardEvent("Blaze") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {

			final int amount=payedCost.getX();
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
				new MagicDamageTargetPicker(amount),new Object[]{cardOnStack,amount},this,"Blaze deals "+amount+" damage to target creature or player$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage(cardOnStack.getCard(),target,(Integer)data[1],false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};

	private static final MagicSpellCardEvent BLIGHTNING=new MagicSpellCardEvent("Blightning") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_PLAYER,
				new Object[]{cardOnStack},this,"Blightning deals 3 damage to target player$. That player discards two cards.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				final MagicDamage damage=new MagicDamage(cardOnStack.getCard(),player,3,false);
				game.doAction(new MagicDealDamageAction(damage));
				game.addEvent(new MagicDiscardEvent(cardOnStack.getCard(),player,2,false));
			}
		}
	};

	private static final MagicSpellCardEvent CHAIN_REACTION=new MagicSpellCardEvent("Chain Reaction") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack},this,
				"Chain Reaction deals X damage to each creature, where X is the number of creatures on the battlefield.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final int amount=game.getNrOfPermanents(MagicType.Creature);
			final MagicSource source=cardOnStack.getCard();
			final Collection<MagicTarget> targets=game.filterTargets(cardOnStack.getController(),MagicTargetFilter.TARGET_CREATURE);
			for (final MagicTarget target : targets) {

				final MagicDamage damage=new MagicDamage(source,target,amount,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};

	private static final MagicSpellCardEvent CORPSEHATCH=new MagicSpellCardEvent("Corpsehatch") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=(MagicPlayer)cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_NONBLACK_CREATURE,
				new MagicDestroyTargetPicker(false),new Object[]{cardOnStack,player},this,
				"Destroy target nonblack creature$. Put two 0/1 colorless Eldrazi Spawn creature tokens onto the battlefield. "+
				"They have \"Sacrifice this creature: Add {1} to your mana pool.\"");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicDestroyAction(creature));
			}
			final MagicPlayer player=(MagicPlayer)data[1];
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.ELDRAZI_SPAWN_TOKEN_CARD));
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.ELDRAZI_SPAWN_TOKEN_CARD));
		}
	};
	
	private static final MagicSpellCardEvent CRUEL_EDICT=new MagicSpellCardEvent("Cruel Edict") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.TARGET_OPPONENT,new Object[]{cardOnStack},this,
				"Target opponent$ sacrifices a creature.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicPlayer opponent=(MagicPlayer)event.getTarget(game,choiceResults,0);
			if (opponent!=null&&opponent.controlsPermanentWithType(MagicType.Creature)) {
				game.addEvent(new MagicSacrificePermanentEvent(cardOnStack.getCard(),opponent,MagicTargetChoice.SACRIFICE_CREATURE));
			}
		}
	};
	
	private static final MagicSpellCardEvent DAY_OF_JUDGMENT=new MagicSpellCardEvent("Day of Judgment") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),new Object[]{cardOnStack},this,"Destroy all creatures.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final Collection<MagicTarget> targets=game.filterTargets(cardOnStack.getController(),MagicTargetFilter.TARGET_CREATURE);			
			for (final MagicTarget target : targets) {
				
				game.doAction(new MagicDestroyAction((MagicPermanent)target));
			}
		}
	};
		
	private static final MagicSpellCardEvent DEATH_GRASP=new MagicSpellCardEvent("Death Grasp") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final int amount=payedCost.getX();
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(amount),
				new Object[]{cardOnStack,player,amount},this,"Death Grasp deals "+amount+" damage to target creature or player$. You gain "+amount+" life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final int amount=(Integer)data[2];
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage(cardOnStack.getCard(),target,amount,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[1],amount));
		}
	};
	
	private static final MagicSpellCardEvent DIVINATION=new MagicSpellCardEvent("Divination") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,player},this,"Draw two cards.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			game.doAction(new MagicDrawAction((MagicPlayer)data[1],2));
		}
	};

	private static final MagicSpellCardEvent EARTHQUAKE=new MagicSpellCardEvent("Earthquake") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final int amount=payedCost.getX();
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,amount},this,
				"Earthquake deals "+amount+" damage to each creature without flying and each player.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicSource source=cardOnStack.getCard();
			final int amount=(Integer)data[1];
			final Collection<MagicTarget> targets=game.filterTargets(cardOnStack.getController(),MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING);
			for (final MagicTarget target : targets) {
			
				final MagicDamage damage=new MagicDamage(source,target,amount,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			for (final MagicPlayer player : game.getPlayers()) {
				
				final MagicDamage damage=new MagicDamage(source,player,amount,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};

	private static final MagicSpellCardEvent EXHAUSTION=new MagicSpellCardEvent("Exhaustion") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.TARGET_OPPONENT,
				new Object[]{cardOnStack},this,"Creatures and lands target opponent$ controls don't untap during that opponent's next untap step.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				game.doAction(new MagicChangePlayerStateAction(player,MagicPlayerState.Exhausted,true));
			}
		}
	};
	
	private static final MagicSpellCardEvent FLAME_SLASH=new MagicSpellCardEvent("Flame Slash") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.NEG_TARGET_CREATURE,
				new MagicDamageTargetPicker(4),new Object[]{cardOnStack},this,"Flame Slash deals 4 damage to target creature$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage(cardOnStack.getCard(),target,4,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
	private static final MagicSpellCardEvent HARMONIZE=new MagicSpellCardEvent("Harmonize") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,player},this,"Draw three cards.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			game.doAction(new MagicDrawAction((MagicPlayer)data[1],3));
		}
	};
	
	private static final MagicSpellCardEvent HURRICANE=new MagicSpellCardEvent("Hurricane") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final int amount=payedCost.getX();
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,amount},this,
				"Hurricane deals "+amount+" damage to each creature with flying and each player.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicSource source=cardOnStack.getCard();
			final int amount=(Integer)data[1];
			final Collection<MagicTarget> targets=game.filterTargets(cardOnStack.getController(),MagicTargetFilter.TARGET_CREATURE_WITH_FLYING);
			for (final MagicTarget target : targets) {
			
				final MagicDamage damage=new MagicDamage(source,target,amount,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			for (final MagicPlayer player : game.getPlayers()) {
				
				final MagicDamage damage=new MagicDamage(source,player,amount,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};

	private static final MagicSpellCardEvent INFEST=new MagicSpellCardEvent("Infest") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack},this,"All creatures get -2/-2 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final Collection<MagicTarget> targets=game.filterTargets(cardOnStack.getController(),MagicTargetFilter.TARGET_CREATURE);
			for (final MagicTarget target : targets) {
				
				game.doAction(new MagicChangeTurnPTAction((MagicPermanent)target,-2,-2));
			}
		}
	};
	
	private static final MagicSpellCardEvent KISS_OF_THE_AMESHA=new MagicSpellCardEvent("Kiss of the Amesha") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.POS_TARGET_PLAYER,
				new Object[]{cardOnStack},this,"Target player$ gains 7 life and draws two cards.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				game.doAction(new MagicChangeLifeAction(player,7));
				game.doAction(new MagicDrawAction(player,2));
			}
		}
	};

	private static final MagicSpellCardEvent MARSH_CASUALTIES=new MagicSpellCardEvent("Marsh Casualties") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,
				new MagicKickerChoice(MagicTargetChoice.NEG_TARGET_PLAYER,MagicManaCost.THREE,false),
				new Object[]{cardOnStack},this,
				"Creatures target player$ controls get -1/-1 until end of turn. "+
				"If Marsh Casualties was kicked$, those creatures get -2/-2 until end of turn instead.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicPlayer player=(MagicPlayer)choiceResults[0];
			final int amount=(Integer)choiceResults[1]>0?-2:-1;
			final Collection<MagicTarget> targets=game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				
				game.doAction(new MagicChangeTurnPTAction((MagicPermanent)target,amount,amount));
			}
		}
	};
	
	private static final MagicSpellCardEvent MARTIAL_COUP=new MagicSpellCardEvent("Martial Coup") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final int x=payedCost.getX();
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,player,x},this,
				"Put "+x+" 1/1 white Soldier creature tokens onto the battlefield."+(x>=5?" Destroy all other creatures.":""));
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicPlayer player=(MagicPlayer)data[1];
			int x=(Integer)data[2];
			final Collection<MagicTarget> targets;
			if (x>=5) {
				targets=game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE);
			} else {
				targets=Collections.<MagicTarget>emptyList();
			}
			for (;x>0;x--) {
				
				game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.SOLDIER_TOKEN_CARD));
			}
			for (final MagicTarget target : targets) {
				
				game.doAction(new MagicDestroyAction((MagicPermanent)target));
			}
		}
	};

	private static final MagicSpellCardEvent MIND_SPRING=new MagicSpellCardEvent("Mind Spring") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final int amount=payedCost.getX();
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,player,amount},this,"Draw "+amount+" cards.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			game.doAction(new MagicDrawAction((MagicPlayer)data[1],(Integer)data[2]));
		}
	};

	private static final MagicSpellCardEvent OVERWHELMING_STAMPEDE=new MagicSpellCardEvent("Overwhelming Stampede") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,player},this,
				"Until end of turn, creatures you control gain trample and get +X/+X, where X is the greatest power among creatures you control.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[1],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			int power=0;
			for (final MagicTarget target : targets) {

				final MagicPermanent creature=(MagicPermanent)target;
				power=Math.max(power,creature.getPowerToughness(game).power);
			}
			for (final MagicTarget target : targets) {
				
				final MagicPermanent creature=(MagicPermanent)target;
				game.doAction(new MagicChangeTurnPTAction(creature,power,power));
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Trample));
			}
		}
	};
	
	private static final MagicSpellCardEvent REANIMATE=new MagicSpellCardEvent("Reanimate") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
				MagicGraveyardTargetPicker.getInstance(),new Object[]{cardOnStack,player},this,
				"Put target creature card$ from a graveyard onto the battlefield under your control. You lose life equal to its converted mana cost.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicCard targetCard=event.getTarget(game,choiceResults,0);
			if (targetCard!=null) {
				final MagicPlayer player=(MagicPlayer)data[1];
				game.doAction(new MagicReanimateAction(player,targetCard,MagicPlayCardAction.NONE));
				game.doAction(new MagicChangeLifeAction(player,-targetCard.getCardDefinition().getConvertedCost()));
			}
		}
	};
	
	private static final MagicSpellCardEvent RECOVER=new MagicSpellCardEvent("Recover") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,MagicGraveyardTargetPicker.getInstance(),
				new Object[]{cardOnStack,player},this,"Return target creature card$ from your graveyard to your hand. Draw a card.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[1];
			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicCard targetCard=event.getTarget(game,choiceResults,0);
			if (targetCard!=null) {
				game.doAction(new MagicRemoveCardAction(targetCard,MagicLocationType.Graveyard));
				game.doAction(new MagicMoveCardAction(targetCard,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
			}
			game.doAction(new MagicDrawAction(player,1));
		}
	};

	private static final MagicSpellCardEvent RITE_OF_REPLICATION=new MagicSpellCardEvent("Rite of Replication") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new MagicKickerChoice(MagicTargetChoice.TARGET_CREATURE,MagicManaCost.FIVE,false),
				MagicCopyTargetPicker.getInstance(),new Object[]{cardOnStack,player},this,
				"Put a token onto the battlefield that's a copy of target creature$. "+
				"If Rite of Replication was kicked$, put five of those tokens onto the battlefield instead.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				final MagicPlayer player=(MagicPlayer)data[1];
				final MagicCardDefinition cardDefinition=creature.getCardDefinition();
				int count=(Integer)choiceResults[1]>0?5:1;
				for (;count>0;count--) {

					game.doAction(new MagicPlayTokenAction(player,cardDefinition));
				}
			}
		}
	};
	
	private static final MagicSpellCardEvent SAVAGE_TWISTER=new MagicSpellCardEvent("Savage Twister") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {

			final int amount=payedCost.getX();
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,amount},this,
				"Savage Twister deals "+amount+" damage to each creature.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final int amount=(Integer)data[1];
			final MagicSource source=cardOnStack.getCard();
			final Collection<MagicTarget> targets=game.filterTargets(cardOnStack.getController(),MagicTargetFilter.TARGET_CREATURE);
			for (final MagicTarget target : targets) {
			
				final MagicDamage damage=new MagicDamage(source,target,amount,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};

	private static final MagicSpellCardEvent SIFT=new MagicSpellCardEvent("Sift") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,player},this,"Draw three cards, then discard a card.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicPlayer player=(MagicPlayer)data[1];
			game.doAction(new MagicDrawAction(player,3));
			game.addEvent(new MagicDiscardEvent(cardOnStack.getCard(),player,1,false));
		}
	};
	
	private static final MagicSpellCardEvent SIGN_IN_BLOOD=new MagicSpellCardEvent("Sign in Blood") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.TARGET_PLAYER,
				new Object[]{cardOnStack},this,"Target player$ draws two cards and loses 2 life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				game.doAction(new MagicDrawAction(player,2));
				game.doAction(new MagicChangeLifeAction(player,-2));
			}
		}
	};

	private static final MagicSpellCardEvent SLAVE_OF_BOLAS=new MagicSpellCardEvent("Slave of Bolas") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_CREATURE,MagicExileTargetPicker.getInstance(),
				new Object[]{cardOnStack,player},this,
				"Gain control of target creature$. Untap that creature. It gains haste until end of turn. Sacrifice it at end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicGainControlAction((MagicPlayer)data[1],creature));
				game.doAction(new MagicUntapAction(creature));
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Haste));
				game.doAction(new MagicChangeStateAction(creature,MagicPermanentState.SacrificeAtEndOfTurn,true));
			}
		}
	};

	private static final MagicSpellCardEvent SLEEP=new MagicSpellCardEvent("Sleep") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_PLAYER,new Object[]{cardOnStack},this,
				"Tap all creatures target player$ controls. Those creatures don't untap during their controller's next untap step.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				final Collection<MagicTarget> targets=game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
				for (final MagicTarget target : targets) {
					
					final MagicPermanent creature=(MagicPermanent)target;
					game.doAction(new MagicTapAction(creature,true));
					game.doAction(new MagicChangeStateAction(creature,MagicPermanentState.DoesNotUntap,true));
				}
			}
		}
	};

	private static final MagicSpellCardEvent SOLEMN_OFFERING=new MagicSpellCardEvent("Solemn Offering") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_ARTIFACT_OR_ENCHANTMENT,new MagicDestroyTargetPicker(false),
				new Object[]{cardOnStack,player},this,"Destroy target artifact or enchantment$. You gain 4 life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				game.doAction(new MagicDestroyAction(permanent));
			}
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[1],4));
		}
	};
	
	private static final MagicSpellCardEvent SOULS_MAJESTY=new MagicSpellCardEvent("Soul's Majesty") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,MagicPowerTargetPicker.getInstance(),
				new Object[]{cardOnStack,player},this,"Draw cards equal to the power of target creature$ you control.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicDrawAction((MagicPlayer)data[1],creature.getPower(game)));
			}
		}
	};

	private static final MagicSpellCardEvent SOULS_MIGHT=new MagicSpellCardEvent("Soul's Might") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.POS_TARGET_CREATURE,MagicPowerTargetPicker.getInstance(),
				new Object[]{cardOnStack},this,"Put X +1/+1 counters on target creature$, where X is that creature's power.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.PlusOne,creature.getPower(game),true));
			}
		}
	};

	private static final MagicSpellCardEvent SOUL_FEAST=new MagicSpellCardEvent("Soul Feast") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_PLAYER,
				new Object[]{cardOnStack,player},this,"Target player$ loses 4 life and you gain 4 life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				game.doAction(new MagicChangeLifeAction(player,-4));
			}
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[1],4));
		}
	};
	
	private static final MagicSpellCardEvent STUPOR=new MagicSpellCardEvent("Stupor") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.TARGET_OPPONENT,
				new Object[]{cardOnStack},this,"Target opponent$ discards a card at random, then discards a card.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				game.addEvent(new MagicDiscardEvent(cardOnStack.getCard(),player,1,true));
				game.addEvent(new MagicDiscardEvent(cardOnStack.getCard(),player,1,false));
			}
		}
	};

	private static final MagicSpellCardEvent TIME_EBB=new MagicSpellCardEvent("Time Ebb") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_CREATURE,MagicBounceTargetPicker.getInstance(),
				new Object[]{cardOnStack},this,"Put target creature$ on top of its owner's library.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {	
				game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.TopOfOwnersLibrary));
			}
		}
	};

	private static final MagicSpellCardEvent TIME_WARP=new MagicSpellCardEvent("Time Warp") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.POS_TARGET_PLAYER,					
				new Object[]{cardOnStack},this,"Target player$ takes an extra turn after this one.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				game.doAction(new MagicChangeExtraTurnsAction(player,1));
			}
		}
	};
	
	private static final MagicSpellCardEvent TITANIC_ULTIMATUM=new MagicSpellCardEvent("Titanic Ultimatum") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,player},this,
				"Until end of turn, creatures you control get +5/+5 and gain first strike, lifelink and trample.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[1],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				
				final MagicPermanent creature=(MagicPermanent)target;
				game.doAction(new MagicChangeTurnPTAction(creature,5,5));
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.TITANIC_ULTIMATUM_FLAGS));
			}
		}
	};

	private static final MagicSpellCardEvent ZOMBIFY=new MagicSpellCardEvent("Zombify") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,MagicGraveyardTargetPicker.getInstance(),
				new Object[]{cardOnStack,player},this,"Return target creature card$ from your graveyard to the battlefield.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicCard targetCard=event.getTarget(game,choiceResults,0);
			if (targetCard!=null) {
				game.doAction(new MagicReanimateAction((MagicPlayer)data[1],targetCard,MagicPlayCardAction.NONE));
			}
		}
	};

	// ***** KICKER CREATURES ******
			
	private static final MagicEventAction GOBLIN_BUSHWHACKER_ACTION=new MagicEventAction() {
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			
			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				
				final MagicPermanent creature=(MagicPermanent)target;
				game.doAction(new MagicChangeTurnPTAction(creature,1,0));
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Haste));
			}			
		}
	};
	
	private static final MagicSpellCardEvent GOBLIN_BUSHWHACKER=new MagicSpellCardEvent("Goblin Bushwhacker") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),
				new MagicKickerChoice(null,MagicManaCost.RED,false),new Object[]{cardOnStack},this,
				"$Play Goblin Bushwhacker. When Goblin Bushwhacker enters the battlefield, "+
				"if it was kicked$, creatures you control get +1/+0 and gain haste until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final int kickerCount=(Integer)choiceResults[1];
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			final MagicPlayCardFromStackAction action=new MagicPlayCardFromStackAction(cardOnStack,null);
			game.doAction(action);
			if (kickerCount>0) {
				final MagicPermanent permanent=action.getPermanent();
				final MagicPlayer player=permanent.getController();
				final MagicEvent triggerEvent=new MagicEvent(permanent,player,new Object[]{player},GOBLIN_BUSHWHACKER_ACTION,
					"Creatures you control get +1/+0 and gain haste until end of turn.");
				game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(permanent,triggerEvent)));
			}
		}
	};
	
	private static final MagicLocalVariable KAVU_TITAN_VARIABLE=new MagicDummyLocalVariable() {
				
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {

			return flags|MagicAbility.Trample.getMask();
		}
	};
	
	private static final MagicSpellCardEvent KAVU_TITAN=new MagicSpellCardEvent("Kavu Titan") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new MagicKickerChoice(null,MagicManaCost.TWO_GREEN,false),
				new Object[]{cardOnStack,player},this,
				"$Play Kavu Titan. If Kavu Titan was kicked$, it enters the battlefield with three +1/+1 counters on it and has trample.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final boolean kicked=((Integer)choiceResults[1])>0;
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			final MagicPlayCardFromStackAction action=new MagicPlayCardFromStackAction(cardOnStack,null);
			game.doAction(action);
			final MagicPermanent permanent=action.getPermanent();
			if (kicked) {
				permanent.changeCounters(MagicCounterType.PlusOne,3);
				permanent.addLocalVariable(KAVU_TITAN_VARIABLE);
			}
		}
	};
	
	private static final MagicEventAction LIGHTKEEPER_OF_EMERIA_ACTION=new MagicEventAction() {
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],(Integer)data[1]));
		}
	};
	
	private static final MagicSpellCardEvent LIGHTKEEPER_OF_EMERIA=new MagicSpellCardEvent("Lightkeeper of Emeria") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new MagicKickerChoice(null,MagicManaCost.WHITE,true),
				new Object[]{cardOnStack,player},this,
				"$Play Lightkeeper of Emeria. When Lightkeeper of Emeria enters the battlefield, you gain 2 life for each time it was kicked$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final int kickerCount=(Integer)choiceResults[1];
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			final MagicPlayCardFromStackAction action=new MagicPlayCardFromStackAction(cardOnStack,null);
			game.doAction(action);
			if (kickerCount>0) {
				final MagicPermanent permanent=action.getPermanent();
				final MagicPlayer player=permanent.getController();
				final int life=kickerCount*2;
				final MagicEvent triggerEvent=new MagicEvent(permanent,player,new Object[]{player,life},
						LIGHTKEEPER_OF_EMERIA_ACTION,"You gain "+life+" life.");
				game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(permanent,triggerEvent)));
			}
		}
	};
		
	private static final MagicSpellCardEvent SPHINX_OF_LOST_TRUTHS=new MagicSpellCardEvent("Sphinx of Lost Truths") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new MagicKickerChoice(null,MagicManaCost.ONE_BLUE,false),
				new Object[]{cardOnStack,player},this,
				"$Play Sphinx of Lost Truths. When Sphinx of Lost Truths enters the battlefield, "+
				"draw three cards. Then if it wasn't kicked$, discard three cards.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPlayCardFromStackAction action=new MagicPlayCardFromStackAction((MagicCardOnStack)data[0],null);
			action.setKicked(((Integer)choiceResults[1])>0);
			game.doAction(action);
		}
	};
	
	private static final MagicEventAction WOLFBRIAR_ELEMENTAL_ACTION=new MagicEventAction() {
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			int count=(Integer)data[1];
			for (;count>0;count--) {
				
				game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.WOLF_TOKEN_CARD));
			}
		}
	};
	
	private static final MagicSpellCardEvent WOLFBRIAR_ELEMENTAL=new MagicSpellCardEvent("Wolfbriar Elemental") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new MagicKickerChoice(null,MagicManaCost.GREEN,true),
				new Object[]{cardOnStack,player},this,
				"$Play Wolfbriar Elemental. When Wolfbriar Elemental enters the battlefield, "+
				"put a 2/2 green Wolf creature token onto the battlefield for each time it was kicked$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final int kickerCount=(Integer)choiceResults[1];
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			final MagicPlayCardFromStackAction action=new MagicPlayCardFromStackAction(cardOnStack,null);
			game.doAction(action);
			if (kickerCount>0) {
				final MagicPermanent permanent=action.getPermanent();
				final MagicPlayer player=permanent.getController();
				final MagicEvent triggerEvent=new MagicEvent(permanent,player,new Object[]{player,kickerCount},
						WOLFBRIAR_ELEMENTAL_ACTION,"Put "+kickerCount+" 2/2 green Wolf creature tokens onto the battlefield.");
				game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(permanent,triggerEvent)));
			}
		}
	};
	
	// ***** AURA PERMANENTS *****
	
	private static final MagicSpellCardEvent ARMADILLO_CLOAK=new MagicPlayAuraEvent("Armadillo Cloak",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance());
	private static final MagicSpellCardEvent BOAR_UMBRA=new MagicPlayAuraEvent("Boar Umbra",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance());
	private static final MagicSpellCardEvent CLINGING_DARKNESS=new MagicPlayAuraEvent("Clinging Darkness",
			MagicTargetChoice.NEG_TARGET_CREATURE,new MagicWeakenTargetPicker(-4,-1));
	private static final MagicSpellCardEvent CURSE_OF_CHAINS=new MagicPlayAuraEvent("Curse of Chains",
			MagicTargetChoice.NEG_TARGET_CREATURE,new MagicNoCombatTargetPicker(true,true,true));
	private static final MagicSpellCardEvent DRAKE_UMBRA=new MagicPlayAuraEvent("Drake Umbra",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance());
	private static final MagicSpellCardEvent DUST_CORONA=new MagicPlayAuraEvent("Dust Corona",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance());
	private static final MagicSpellCardEvent EEL_UMBRA=new MagicPlayAuraEvent("Eel Umbra",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance());
	private static final MagicSpellCardEvent ELEPHANT_GUIDE=new MagicPlayAuraEvent("Elephant Guide",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance());
	private static final MagicSpellCardEvent EPIC_PROPORTIONS=new MagicPlayAuraEvent("Epic Proportions",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance());
	private static final MagicSpellCardEvent FISTS_OF_IRONWOOD=new MagicPlayAuraEvent("Fists of Ironwood",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicTrampleTargetPicker.getInstance());
	private static final MagicSpellCardEvent FLIGHT_OF_FANCY=new MagicPlayAuraEvent("Flight of Fancy",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicFlyingTargetPicker.getInstance());
	private static final MagicSpellCardEvent GALVANIC_ARC=new MagicPlayAuraEvent("Galvanic Arc",
			MagicTargetChoice.TARGET_CREATURE,MagicFirstStrikeTargetPicker.getInstance());
	private static final MagicSpellCardEvent GOBLIN_WAR_PAINT=new MagicPlayAuraEvent("Goblin War Paint",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicHasteTargetPicker.getInstance());
	private static final MagicSpellCardEvent GRIFFIN_GUIDE=new MagicPlayAuraEvent("Griffin Guide",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance());
	private static final MagicSpellCardEvent HYENA_UMBRA=new MagicPlayAuraEvent("Hyena Umbra",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicFirstStrikeTargetPicker.getInstance());
	private static final MagicSpellCardEvent LIGHTNING_TALONS=new MagicPlayAuraEvent("Lightning Talons",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicFirstStrikeTargetPicker.getInstance());
	private static final MagicSpellCardEvent PACIFISM=new MagicPlayAuraEvent("Pacifism",
			MagicTargetChoice.NEG_TARGET_CREATURE,new MagicNoCombatTargetPicker(true,true,true));
	private static final MagicSpellCardEvent PILLORY_OF_THE_SLEEPLESS=new MagicPlayAuraEvent("Pillory of the Sleepless",
			MagicTargetChoice.NEG_TARGET_CREATURE,new MagicNoCombatTargetPicker(true,true,true));
	private static final MagicSpellCardEvent PROTECTIVE_BUBBLE=new MagicPlayAuraEvent("Protective Bubble",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicUnblockableTargetPicker.getInstance());
	private static final MagicSpellCardEvent RANCOR=new MagicPlayAuraEvent("Rancor",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicTrampleTargetPicker.getInstance());
	private static final MagicSpellCardEvent SERRAS_EMBRACE=new MagicPlayAuraEvent("Serra's Embrace",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance());
	private static final MagicSpellCardEvent TORPOR_DUST=new MagicPlayAuraEvent("Torpor Dust",
			MagicTargetChoice.NEG_TARGET_CREATURE,new MagicWeakenTargetPicker(3,0));
	private static final MagicSpellCardEvent VOLCANIC_STRENGTH=new MagicPlayAuraEvent("Volcanic Strength",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance());
	private static final MagicSpellCardEvent WEAKNESS=new MagicPlayAuraEvent("Weakness",
			MagicTargetChoice.NEG_TARGET_CREATURE,new MagicWeakenTargetPicker(2,1));
	
	private static final Collection<MagicSpellCardEvent> CARD_EVENTS=Arrays.asList(
		ABSORB,
		BACKLASH,
		BEACON_OF_DESTRUCTION,
		BURST_LIGHTNING,
		CHAR,
		CHASTISE,
		COLOSSAL_MIGHT,
		COUNTERSPELL,
		COUNTERSQUALL,
		DELUGE,
		DIMINISH,
		DISFIGURE,
		DISMAL_FAILURE,
		DISPERSE,
		DOOM_BLADE,
		DOUBLE_CLEAVE,
		DOUSE_IN_GLOOM,
		ESSENCE_SCATTER,
		EVACUATION,
		FISTS_OF_THE_ANVIL,
		GIANT_GROWTH,
		GLORIOUS_CHARGE,
		GRASP_OF_DARKNESS,
		HEROES_REUNION,
		HIDEOUS_END,
		INCINERATE,
		INSPIRIT,
		KINDLED_FURY,		
		LIGHTNING_BOLT,
		LIGHTNING_HELIX,
		MAELSTROM_PULSE,
		MANA_LEAK,
		MIGHT_OF_OAKS,
		MIGHTY_LEAP,
		MORTIFY,
		NATURALIZE,
		OFFERING_TO_ASHA,
		PLUMMET,
		PUNCTURE_BLAST,
		PUTREFY,
		REMAND,
		REPULSE,
		SAFE_PASSAGE,
		SCAR,
		SIGIL_BLESSING,
		SMOTHER,
		SWORDS_TO_PLOWSHARES,
		TERMINATE,
		THUNDER_STRIKE,
		TO_ARMS,
		TRUMPET_BLAST,
		UNDERMINE,
		UNMAKE,
		UNSUMMON,
		VAULT_SKYWARD,
		VETERANS_REFLEXES,
		WILDSIZE,
		WITHSTAND,
		WITHSTAND_DEATH,
		WRAP_IN_VIGOR,
		ZEALOUS_PERSECUTION,
		ASSASSINATE,
		BEACON_OF_UNREST,
		BESTIAL_MENACE,
		BLAZE,
		BLIGHTNING,
		CHAIN_REACTION,
		CRUEL_EDICT,
		CORPSEHATCH,
		DAY_OF_JUDGMENT,
		DEATH_GRASP,
		DIVINATION,
		EARTHQUAKE,
		EXHAUSTION,
		FLAME_SLASH,
		HARMONIZE,
		HURRICANE,
		INFEST,
		KISS_OF_THE_AMESHA,
		MARSH_CASUALTIES,
		MARTIAL_COUP,
		MIND_SPRING,
		OVERWHELMING_STAMPEDE,
		REANIMATE,
		RECOVER,
		RITE_OF_REPLICATION,
		SAVAGE_TWISTER,
		SOLEMN_OFFERING,
		SIFT,
		SIGN_IN_BLOOD,
		SLAVE_OF_BOLAS,
		SLEEP,
		SOULS_MAJESTY,
		SOULS_MIGHT,
		SOUL_FEAST,
		STUPOR,
		TIME_EBB,
		TIME_WARP,
		TITANIC_ULTIMATUM,
		ZOMBIFY,
		GOBLIN_BUSHWHACKER,
		KAVU_TITAN,
		LIGHTKEEPER_OF_EMERIA,		
		SPHINX_OF_LOST_TRUTHS,
		WOLFBRIAR_ELEMENTAL,
		ARMADILLO_CLOAK,
		BOAR_UMBRA,
		CLINGING_DARKNESS,
		CURSE_OF_CHAINS,
		DRAKE_UMBRA,
		DUST_CORONA,
		EEL_UMBRA,
		ELEPHANT_GUIDE,
		EPIC_PROPORTIONS,
		FISTS_OF_IRONWOOD,
		FLIGHT_OF_FANCY,
		GALVANIC_ARC,
		GOBLIN_WAR_PAINT,
		GRIFFIN_GUIDE,
		HYENA_UMBRA,
		LIGHTNING_TALONS,
		PACIFISM,
		PILLORY_OF_THE_SLEEPLESS,
		PROTECTIVE_BUBBLE,
		RANCOR,
		SERRAS_EMBRACE,
		TORPOR_DUST,
		VOLCANIC_STRENGTH,
		WEAKNESS
	);
	
	public static void setCardEvents() {

		System.out.println("Setting "+CARD_EVENTS.size()+" card events...");
		for (final MagicSpellCardEvent cardEvent : CARD_EVENTS) {

			final MagicCardDefinition card=cardEvent.getCardDefinition();
			card.setCardEvent(cardEvent);
		}
	}
}