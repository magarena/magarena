package magic.data;

import java.util.Arrays;
import java.util.Collection;
import java.lang.reflect.Field;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.action.MagicAddTurnTriggerAction;
import magic.model.action.MagicBecomesCreatureAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicCopyCardOnStackAction;
import magic.model.action.MagicCounterItemOnStackAction;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicDrawAction;
import magic.model.action.MagicExileUntilEndOfTurnAction;
import magic.model.action.MagicPlayAbilityAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.action.MagicPreventDamageAction;
import magic.model.action.MagicRegenerateAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.action.MagicSetTurnColorAction;
import magic.model.action.MagicTapAction;
import magic.model.action.MagicUntapAction;
import magic.model.choice.MagicColorChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicArtificialCondition;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicSingleActivationCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicCounterUnlessEvent;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicExileEvent;
import magic.model.event.MagicGainActivation;
import magic.model.event.MagicLevelUpActivation;
import magic.model.event.MagicPayLifeEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPayManaCostSacrificeEvent;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPlayAbilityEvent;
import magic.model.event.MagicPumpActivation;
import magic.model.event.MagicRegenerationActivation;
import magic.model.event.MagicRemoveCounterEvent;
import magic.model.event.MagicSacrificeEvent;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;
import magic.model.event.MagicUntapEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicBecomeTargetPicker;
import magic.model.target.MagicBounceTargetPicker;
import magic.model.target.MagicCopyTargetPicker;
import magic.model.target.MagicCountersTargetPicker;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.target.MagicExileTargetPicker;
import magic.model.target.MagicFirstStrikeTargetPicker;
import magic.model.target.MagicHasteTargetPicker;
import magic.model.target.MagicIndestructibleTargetPicker;
import magic.model.target.MagicMustAttackTargetPicker;
import magic.model.target.MagicNoCombatTargetPicker;
import magic.model.target.MagicPreventTargetPicker;
import magic.model.target.MagicPumpTargetPicker;
import magic.model.target.MagicRegenerateTargetPicker;
import magic.model.target.MagicTapTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.target.MagicUnblockableTargetPicker;
import magic.model.target.MagicWeakenTargetPicker;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;

public class PermanentActivationDefinitions {
	
	private static final MagicPermanentActivation AIR_SERVANT=new MagicPermanentActivation(
            "Air Servant",
			new MagicCondition[]{MagicManaCost.TWO_BLUE.getCondition()},
            new MagicActivationHints(MagicTiming.Tapping),
            "Tap"            
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {

			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_BLUE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE_WITH_FLYING,
                    new MagicTapTargetPicker(true,false),
                    MagicEvent.NO_DATA,
                    this,
                    "Tap target creature$ with flying.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null&&!creature.isTapped()) {
				game.doAction(new MagicTapAction(creature,true));
			}
		}
	};
	
	private static final MagicPermanentActivation ARCANIS_THE_OMNIPOTENT1 = new MagicPermanentActivation(
            "Arcanis the Omnipotent",
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Token),
            "Draw"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(source,player,new Object[]{player},this,"You draw three cards.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicDrawAction((MagicPlayer)data[0],3));
		}
	};
	
	private static final MagicPermanentActivation ARCANIS_THE_OMNIPOTENT2=new MagicPermanentActivation(
            "Arcanis the Omnipotent",
			new MagicCondition[]{MagicManaCost.TWO_BLUE_BLUE.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Return"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_BLUE_BLUE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Return Arcanis the Omnipotent to its owner's hand.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicRemoveFromPlayAction((MagicPermanent)data[0],MagicLocationType.OwnersHand));
		}
	};
	
	private static final MagicPermanentActivation BOROS_GUILDMAGE1=new MagicPermanentActivation(
			"Boros Guildmage",
            new MagicCondition[]{MagicManaCost.ONE_RED.getCondition()},
            new MagicActivationHints(MagicTiming.Pump,true),
            "Haste"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {

			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_RED)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {

			return new MagicEvent(source,source.getController(),MagicTargetChoice.POS_TARGET_CREATURE,MagicHasteTargetPicker.getInstance(),
				MagicEvent.NO_DATA,this,"Target creature$ gains haste until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Haste));
			}
		}
	};

	private static final MagicPermanentActivation BOROS_GUILDMAGE2=new MagicPermanentActivation(
			"Boros Guildmage",
            new MagicCondition[]{MagicManaCost.ONE_WHITE.getCondition()},
            new MagicActivationHints(MagicTiming.Block,true),
            "First strike"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {

			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_WHITE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {

			return new MagicEvent(source,source.getController(),MagicTargetChoice.POS_TARGET_CREATURE,MagicFirstStrikeTargetPicker.getInstance(),
				MagicEvent.NO_DATA,this,"Target creature$ gains first strike until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.FirstStrike));
			}
		}	
	};
	
	private static final MagicPermanentActivation BOTTLE_GNOMES=new MagicPermanentActivation(
			"Bottle Gnomes",
            null,
            new MagicActivationHints(MagicTiming.Removal),
            "Life+3"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(source,player,new Object[]{player},this,"You gain 3 life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],3));
		}
	};

	private static final MagicPermanentActivation BRIGID_HERO_OF_KINSBAILE=new MagicPermanentActivation(
			"Brigid, Hero of Kinsbaile",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Block),
            "Damage"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {

			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {

			return new MagicEvent(source,source.getController(),MagicTargetChoice.NEG_TARGET_PLAYER,
				new Object[]{source},this,"Brigid, Hero of Kinsbaile deals 2 damage to each attacking or blocking creature target player$ controls.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				final MagicSource source=(MagicSource)data[0];
				final Collection<MagicTarget> targets=game.filterTargets(player,MagicTargetFilter.TARGET_ATTACKING_OR_BLOCKING_CREATURE_YOU_CONTROL);
				for (final MagicTarget target : targets) {
					final MagicDamage damage=new MagicDamage(source,target,2,false);
					game.doAction(new MagicDealDamageAction(damage));
				}
			}
		}
	};
	
	private static final MagicPermanentActivation BRION_STOUTARM=new MagicPermanentActivation(
            "Brion Stoutarm",
			new MagicCondition[]{
                MagicCondition.CAN_TAP_CONDITION,MagicManaCost.RED.getCondition(),
                MagicCondition.TWO_CREATURES_CONDITION
            },
			new MagicActivationHints(MagicTiming.Removal),
            "Damage"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {

			final MagicTargetFilter targetFilter=new MagicTargetFilter.MagicOtherPermanentTargetFilter(
					MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,(MagicPermanent)source);
			final MagicTargetChoice targetChoice=new MagicTargetChoice(
					targetFilter,false,MagicTargetHint.None,"a creature other than Brion Stoutarm to sacrifice");
			return new MagicEvent[]{
				new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.RED),
				new MagicSacrificePermanentEvent(source,source.getController(),targetChoice)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {

			final MagicTarget sacrificed=payedCost.getTarget();
			return new MagicEvent(source,source.getController(),MagicTargetChoice.NEG_TARGET_PLAYER,
				new Object[]{source,sacrificed},this,"Brion Stoutarm deals damage equal to the power of "+sacrificed.getName()+" to target player$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				final MagicPermanent sacrificed=(MagicPermanent)data[1];
				final MagicDamage damage=new MagicDamage((MagicPermanent)data[0],player,sacrificed.getPower(game),false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
	private static final MagicPermanentActivation CARNIFEX_DEMON=new MagicPermanentActivation(
            "Carnifex Demon",
			new MagicCondition[]{
                MagicCondition.MINUS_COUNTER_CONDITION,
                MagicManaCost.BLACK.getCondition()
            },
            new MagicActivationHints(MagicTiming.Removal),
            "-1/-1"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {

			return new MagicEvent[]{
				new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.BLACK),
				new MagicRemoveCounterEvent((MagicPermanent)source,MagicCounterType.MinusOne,1)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {

			return new MagicEvent(source,source.getController(),new Object[]{source},this,"Put a -1/-1 counter on each other creature.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPermanent creature=(MagicPermanent)data[0];
			final Collection<MagicTarget> targets=game.filterTargets(creature.getController(),MagicTargetFilter.TARGET_CREATURE);
			for (final MagicTarget target : targets) {
			
				if (target!=creature) {
					game.doAction(new MagicChangeCountersAction((MagicPermanent)target,MagicCounterType.MinusOne,1,true));
				}
			}
		}
	};

	private static final MagicPermanentActivation CINDER_ELEMENTAL=new MagicPermanentActivation(
            "Cinder Elemental",
			new MagicCondition[]{
                MagicCondition.CAN_TAP_CONDITION,
                MagicManaCost.X_RED.getCondition()
            },
            new MagicActivationHints(MagicTiming.Removal),
            "Damage"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
				new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.X_RED),
				new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final int amount=payedCost.getX();
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(amount),
                    new Object[]{source,amount},
                    this,
                    "Cinder Elemental deals "+amount+" damage to target creature or player$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,(Integer)data[1],false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
	private static final MagicPermanentActivation CHAMELEON_COLOSSUS=new MagicPermanentActivation(
			"Chameleon Colossus",
            new MagicCondition[]{MagicManaCost.TWO_GREEN_GREEN.getCondition()},
            new MagicActivationHints(MagicTiming.Pump),
            "Pump"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {

			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_GREEN_GREEN)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {

			return new MagicEvent(source,source.getController(),new Object[]{source},this,
				"Chameleon Colossus gets +X/+X until end of turn, where X is its power.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			final int power=permanent.getPower(game);
			game.doAction(new MagicChangeTurnPTAction(permanent,power,power));
		}
	};

	private static final MagicPermanentActivation CHARGING_TROLL=
        new MagicRegenerationActivation("Charging Troll",MagicManaCost.GREEN);

	private static final MagicPermanentActivation CUDGEL_TROLL=
        new MagicRegenerationActivation("Cudgel Troll",MagicManaCost.GREEN);
	
	private static final MagicPermanentActivation CUNNING_SPARKMAGE = new MagicPermanentActivation(
			"Cunning Sparkmage",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {

			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {

			return new MagicEvent(source,source.getController(),MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(1),
				new Object[]{source},this,"Cunning Sparkmage deals 1 damage to target creature or player$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,1,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
	private static final MagicPermanentActivation CURSECATCHER=new MagicPermanentActivation(
			"Cursecatcher",
            null,
            new MagicActivationHints(MagicTiming.Counter),
            "Counter"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {

			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {

			return new MagicEvent(source,source.getController(),MagicTargetChoice.NEG_TARGET_INSTANT_OR_SORCERY_SPELL,
				new Object[]{source},this,"Counter target instant or sorcery spell$ unless its controller pays {1}.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack targetSpell=event.getTarget(game,choiceResults,0);
			if (targetSpell!=null) {
				game.addEvent(new MagicCounterUnlessEvent((MagicSource)data[0],targetSpell,MagicManaCost.ONE)); 
			}
		}
	};
	
	private static final MagicPermanentActivation DAUNTLESS_ESCORT=new MagicPermanentActivation(
			"Dauntless Escort",
            null,
            new MagicActivationHints(MagicTiming.Pump),
            "Indestr"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {

			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {

			final MagicPlayer player=source.getController();
			return new MagicEvent(source,player,new Object[]{player},this,"Creatures you control are indestructible this turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final Collection<MagicTarget> creatures=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget creature : creatures) {
				
				game.doAction(new MagicSetAbilityAction((MagicPermanent)creature,MagicAbility.Indestructible));
			}
		}
	};

	private static final MagicPermanentActivation DEATHLESS_ANGEL = new MagicPermanentActivation(
			"Deathless Angel",
            new MagicCondition[]{MagicManaCost.WHITE_WHITE.getCondition()},
            new MagicActivationHints(MagicTiming.Pump,true),
            "Indestr"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.WHITE_WHITE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicIndestructibleTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Target creature$ is indestructible this turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Indestructible));
			}
		}
	};
	
	private static final MagicPermanentActivation DRANA_KALASTRIA_BLOODCHIEF=new MagicPermanentActivation(
            "Drana, Kalastria Bloodchief",
			new MagicCondition[]{MagicManaCost.X_BLACK_BLACK.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Pump"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.X_BLACK_BLACK)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final int amount=payedCost.getX();
			return new MagicEvent(source,source.getController(),MagicTargetChoice.TARGET_CREATURE,
				new MagicWeakenTargetPicker(0,1),new Object[]{source,amount},this,
				"Target creature$ gets -0/-"+amount+" until end of turn and Drana, Kalastria Bloodchief gets +"+amount+"/+0 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final int amount=(Integer)data[1];
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,0,-amount));
			}
			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],amount,0));
		}
	};

	private static final MagicPermanentActivation DRUDGE_REAVERS = 
        new MagicRegenerationActivation("Drudge Reavers",MagicManaCost.BLACK);
	
	private static final MagicPermanentActivation ECHO_MAGE1 = 
        new MagicLevelUpActivation("Echo Mage",MagicManaCost.ONE_BLUE,4);
	
	private static final MagicPermanentActivation ECHO_MAGE2=new MagicPermanentActivation("Echo Mage",
			new MagicCondition[]{
                MagicCondition.TWO_CHARGE_COUNTERS_CONDITION,
                MagicCondition.CAN_TAP_CONDITION,MagicManaCost.BLUE_BLUE.getCondition()},
			new MagicActivationHints(MagicTiming.Spell),
            "Copy"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.BLUE_BLUE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			final int amount=source.getCounters(MagicCounterType.Charge)>=4?2:1;
			final String description = amount == 2 ?
					"Copy target instant or sorcery spell$ twice. You may choose new targets for the copies.":
					"Copy target instant or sorcery spell$. You may choose new targets for the copy.";
			return new MagicEvent(
                    source,
                    player,
                    MagicTargetChoice.TARGET_INSTANT_OR_SORCERY_SPELL,
                    new Object[]{player,amount},
                    this,
                    description);
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicCardOnStack targetSpell=event.getTarget(game,choiceResults,0);
			if (targetSpell!=null) {
				final MagicPlayer player=(MagicPlayer)data[0];
				final int amount=(Integer)data[1];
				for (int count=amount;count>0;count--) {
					game.doAction(new MagicCopyCardOnStackAction(player,targetSpell));
				}
			}
		}
	};
	
	private static final MagicPermanentActivation EMBER_HAULER=new MagicPermanentActivation(
            "Ember Hauler",
			new MagicCondition[]{MagicManaCost.ONE.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostSacrificeEvent(source,source.getController(),MagicManaCost.ONE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(2),
                    new Object[]{source},
                    this,
                    "Ember Hauler deals 2 damage to target creature or player$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,2,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
	private static final MagicPermanentActivation ESPER_BATTLEMAGE1=new MagicPermanentActivation(
            "Esper Battlemage",
			new MagicCondition[]{
                MagicCondition.CAN_TAP_CONDITION,
                MagicManaCost.WHITE.getCondition()},
            new MagicActivationHints(MagicTiming.Pump),
            "Prevent") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.WHITE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{player},
                    this,
                    "Prevent the next 2 damage that would be dealt to you this turn.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicPreventDamageAction((MagicPlayer)data[0],2));
		}
	};

	private static final MagicPermanentActivation ESPER_BATTLEMAGE2=new MagicPermanentActivation(
            "Esper Battlemage",
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.BLACK.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "-1/-1") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.BLACK)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicWeakenTargetPicker(1,1),
				    MagicEvent.NO_DATA,
                    this,
                    "Target creature$ gets -1/-1 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,-1,-1));
			}
		}
	};
	
	private static final MagicPermanentActivation ETHERSWORN_ADJUDICATOR1=new MagicPermanentActivation(
            "Ethersworn Adjudicator",
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.ONE_WHITE_BLACK.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Destroy") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.ONE_WHITE_BLACK)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),MagicTargetChoice.NEG_TARGET_CREATURE_OR_ENCHANTMENT,new MagicDestroyTargetPicker(false),
				MagicEvent.NO_DATA,this,"Destroy target creature or enchantment$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				game.doAction(new MagicDestroyAction(permanent));
			}
		}
	};

	private static final MagicPermanentActivation ETHERSWORN_ADJUDICATOR2=new MagicPermanentActivation(
            "Ethersworn Adjudicator",
			new MagicCondition[]{
                MagicCondition.TAPPED_CONDITION,
                MagicManaCost.TWO_BLUE.getCondition(),
                new MagicSingleActivationCondition()},
			new MagicActivationHints(MagicTiming.Tapping),
            "Untap") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_BLUE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),new Object[]{source},this,"Untap Ethersworn Adjudicator.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			game.doAction(new MagicUntapAction((MagicPermanent)data[0]));
		}
	};

	private static final MagicPermanentActivation FALLEN_ANGEL=new MagicPermanentActivation(
			"Fallen Angel",
            new MagicCondition[]{MagicCondition.TWO_CREATURES_CONDITION},
            new MagicActivationHints(MagicTiming.Pump),
            "Pump"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificePermanentEvent(source,source.getController(),MagicTargetChoice.SACRIFICE_CREATURE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),new Object[]{source},this,"Fallen Angel gets +2/+1 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],2,1));
		}
	};
	
	private static final MagicPermanentActivation FEMEREF_ARCHERS=new MagicPermanentActivation(
			"Femeref Archers",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Attack),
            "Damage"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_ATTACKING_CREATURE_WITH_FLYING,
                    new MagicDamageTargetPicker(4),
                    new Object[]{source},this,"Femeref Archers deals 4 damage to target attacking creature$ with flying.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],creature,4,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
	private static final MagicPermanentActivation FIRESLINGER=new MagicPermanentActivation(
			"Fireslinger",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(
                    source,
                    player,
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(1),
                    new Object[]{source,player},
                    this,
                    "Fireslinger deals 1 damage to target creature or player$ and 1 damage to you.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicSource source=(MagicSource)data[0];
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage1=new MagicDamage(source,target,1,false);
				game.doAction(new MagicDealDamageAction(damage1));
			}
			final MagicDamage damage2=new MagicDamage(source,(MagicTarget)data[1],1,false);
			game.doAction(new MagicDealDamageAction(damage2));
		}
	};

	private static final MagicPermanentActivation FUME_SPITTER=new MagicPermanentActivation(
            "Fume Spitter",
            null,
            new MagicActivationHints(MagicTiming.Removal),
            "-1/-1") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),MagicTargetChoice.NEG_TARGET_CREATURE,new MagicWeakenTargetPicker(1,1),
				MagicEvent.NO_DATA,this,"Put a -1/-1 counter on target creature$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPermanent creature=(MagicPermanent)event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.MinusOne,1,true));
			}
		}
	};
	
	private static final MagicPermanentActivation FURNACE_WHELP=new MagicPumpActivation("Furnace Whelp",MagicManaCost.RED,1,0);

	private static final MagicPermanentActivation GELECTRODE=new MagicPermanentActivation(
			"Gelectrode",
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
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(1),
                    new Object[]{source},
                    this,
                    "Gelectrode deals 1 damage to target creature or player$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,1,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
	private static final MagicPermanentActivation GHOST_COUNCIL_OF_ORZHOVA=new MagicPermanentActivation(
            "Ghost Council of Orzhova",
			new MagicCondition[]{MagicManaCost.ONE.getCondition(),MagicCondition.TWO_CREATURES_CONDITION},
            new MagicActivationHints(MagicTiming.Removal,false,1),
            "Exile"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicPlayer player=source.getController();
			return new MagicEvent[]{					
				new MagicPayManaCostEvent(source,player,MagicManaCost.ONE),
				new MagicSacrificePermanentEvent(source,player,MagicTargetChoice.SACRIFICE_CREATURE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),new Object[]{source},this,
				"Exile Ghost Council of Orzhova. Return it to the battlefield under its owner's control at end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			game.doAction(new MagicExileUntilEndOfTurnAction((MagicPermanent)data[0]));
		}
	};
	
	private static final MagicPermanentActivation GLEN_ELENDRA_ARCHMAGE=new MagicPermanentActivation(
			"Glen Elendra Archmage",
            new MagicCondition[]{MagicManaCost.BLUE.getCondition()},
            new MagicActivationHints(MagicTiming.Counter),
            "Counter") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostSacrificeEvent(source,source.getController(),MagicManaCost.BLUE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),MagicTargetChoice.NEG_TARGET_NONCREATURE_SPELL,
				MagicEvent.NO_DATA,this,"Counter target noncreature spell$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicCardOnStack targetSpell=event.getTarget(game,choiceResults,0);
			if (targetSpell!=null) {
				game.doAction(new MagicCounterItemOnStackAction(targetSpell));
			}
		}
	};
	
	private static final MagicPermanentActivation GOBLIN_ARTILLERY=new MagicPermanentActivation(
			"Goblin Artillery",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(
                    source,
                    player,
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(2),
                    new Object[]{source,player},
                    this,
                    "Goblin Artillery deals 2 damage to target creature or player$ and 3 damage to you.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicSource source=(MagicSource)data[0];
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage1=new MagicDamage(source,target,2,false);
				game.doAction(new MagicDealDamageAction(damage1));
				final MagicDamage damage2=new MagicDamage(source,(MagicTarget)data[1],3,false);
				game.doAction(new MagicDealDamageAction(damage2));
			}
		}
	};

	private static final MagicPermanentActivation GODSIRE=new MagicPermanentActivation(
			"Godsire",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Token),
            "Token") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{player},
                    this,
                    "Put an 8/8 Beast creature token that's red, green and white onto the battlefield.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.BEAST8_TOKEN_CARD));
		}
	};
	
	private static final MagicPermanentActivation HATEFLAYER=new MagicPermanentActivation(
            "Hateflayer",
			new MagicCondition[]{MagicCondition.CAN_UNTAP_CONDITION,MagicManaCost.TWO_RED.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
				new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_RED),
				new MagicUntapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(5),
                    new Object[]{source},
                    this,
                    "Hateflayer deals damage equal to its power to target creature or player$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicPermanent permanent=(MagicPermanent)data[0];
				final MagicDamage damage=new MagicDamage(permanent,target,permanent.getPower(game),false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};

	private static final MagicPermanentActivation HELLKITE_OVERLORD1 = 
        new MagicPumpActivation("Hellkite Overlord",MagicManaCost.RED,1,0);

	private static final MagicPermanentActivation HELLKITE_OVERLORD2 =
        new MagicRegenerationActivation("Hellkite Overlord",MagicManaCost.BLACK_GREEN);
	
	private static final MagicPermanentActivation JHESSIAN_BALMGIVER1=new MagicPermanentActivation(
			"Jhessian Balmgiver",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Pump),
            "Prevent") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.POS_TARGET_CREATURE_OR_PLAYER,
                    MagicPreventTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Prevent the next 1 damage that would be dealt to target creature or player$ this turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				game.doAction(new MagicPreventDamageAction(target,1));
			}
		}
	};

	private static final MagicPermanentActivation JHESSIAN_BALMGIVER2=new MagicPermanentActivation(
			"Jhessian Balmgiver",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Attack),
            "Unblockable") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),MagicTargetChoice.POS_TARGET_CREATURE,MagicUnblockableTargetPicker.getInstance(),
				MagicEvent.NO_DATA,this,"Target creature$ is unblockable this turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Unblockable));
			}
		}	
	};

	private static final MagicPermanentActivation KABUTO_MOTH=new MagicPermanentActivation(
			"Kabuto Moth",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Pump),
            "Pump") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicPumpTargetPicker.getInstance(),
				    MagicEvent.NO_DATA,
                    this,
                    "Target creature$ gets +1/+2 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,1,2));
			}
		}
	};
	
	private static final MagicPermanentActivation KIKI_JIKI_MIRROR_BREAKER=new MagicPermanentActivation(
			"Kiki-Jiki, Mirror Breaker",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Token),
            "Copy") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(source,player,MagicTargetChoice.TARGET_NON_LEGENDARY_CREATURE_YOU_CONTROL,
				MagicCopyTargetPicker.getInstance(),new Object[]{player},this,
				"Put a token that's a copy of target nonlegendary creature$ you control onto the battlefield. "+
				"That token has haste. Sacrifice it at end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				final MagicPlayer player=(MagicPlayer)data[0];
				final MagicCard card=MagicCard.createTokenCard(creature.getCardDefinition(),player);
				game.doAction(new MagicPlayCardAction(card,player,MagicPlayCardAction.HASTE_SACRIFICE_AT_END_OF_TURN));
			}
		}
	};
	
	private static final MagicPermanentActivation LORD_OF_SHATTERSKULL_PASS=
        new MagicLevelUpActivation("Lord of Shatterskull Pass",MagicManaCost.ONE_RED,6);
		
	private static final MagicPermanentActivation LOXODON_HIERARCH=new MagicPermanentActivation(
			"Loxodon Hierarch",
            new MagicCondition[]{MagicManaCost.GREEN_WHITE.getCondition()},
            new MagicActivationHints(MagicTiming.Pump),
            "Regen") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
                new MagicPayManaCostSacrificeEvent(source,source.getController(),
                MagicManaCost.GREEN_WHITE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(source,player,new Object[]{player},this,"Regenerate each creature you control.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				final MagicPermanent permanent=(MagicPermanent)target;
				if (permanent.canRegenerate()) {
					game.doAction(new MagicRegenerateAction(permanent));
				}
			}
		}
	};
	
	private static final MagicPermanentActivation MERFOLK_SEASTALKERS=new MagicPermanentActivation(
            "Merfolk Seastalkers",
			new MagicCondition[]{MagicManaCost.TWO_BLUE.getCondition()},
            new MagicActivationHints(MagicTiming.Tapping),
            "Tap") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_BLUE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE_WITHOUT_FLYING,
                    new MagicTapTargetPicker(true,false),
				    MagicEvent.NO_DATA,
                    this,
                    "Tap target creature$ without flying.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null&&!creature.isTapped()) {
				game.doAction(new MagicTapAction(creature,true));
			}
		}
	};
		
	private static final MagicPermanentActivation MIRE_BOA = 
        new MagicRegenerationActivation("Mire Boa",MagicManaCost.GREEN);

	private static final class MirrorEntityLocalVariable extends MagicDummyLocalVariable {
		
		private final int x;
		
		public MirrorEntityLocalVariable(final int x) {
			this.x=x;
		}

		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.power=x;
			pt.toughness=x;
		}

		@Override
		public int getSubTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicSubType.ALL_CREATURES;
		}
	}
	
	private static final MagicPermanentActivation MIRROR_ENTITY = new MagicPermanentActivation(
            "Mirror Entity",
			new MagicCondition[]{MagicManaCost.X.getCondition()},
            new MagicActivationHints(MagicTiming.Pump,true,1),
            "X/X"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.X)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final int x=payedCost.getX();
			return new MagicEvent(source,source.getController(),new Object[]{source,x},this,
				"Creatures you control become "+x+"/"+x+" and gain all creature types until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicLocalVariable localVariable=new MirrorEntityLocalVariable((Integer)data[1]);
			final Collection<MagicTarget> creatures=game.filterTargets(permanent.getController(),MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget creature : creatures) {
				game.doAction(new MagicBecomesCreatureAction((MagicPermanent)creature,localVariable));
			}
			game.doAction(new MagicPlayAbilityAction(permanent));
		}
	};
	
	private static final MagicPermanentActivation MOGG_FANATIC=new MagicPermanentActivation(
            "Mogg Fanatic",
            null,
            new MagicActivationHints(MagicTiming.Removal),
            "Damage"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(1),
				    new Object[]{source},
                    this,
                    "Mogg Fanatic deals 1 damage to target creature or player$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,1,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};

	private static final MagicPermanentActivation MORDANT_DRAGON = 
        new MagicPumpActivation("Mordant Dragon",MagicManaCost.ONE_RED,1,0);
	
	private static final MagicPermanentActivation NANTUKO_SHADE = 
        new MagicPumpActivation("Nantuko Shade",MagicManaCost.BLACK,1,1);

	private static final MagicPermanentActivation NIRKANA_CUTTHROAT = 
        new MagicLevelUpActivation("Nirkana Cutthroat",MagicManaCost.TWO_BLACK,3);
	
	private static final MagicLocalVariable OMNIBIAN_VARIABLE=new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.power=3;
			pt.toughness=3;
		}

		@Override
		public int getSubTypeFlags(final MagicPermanent permanent,final int flags) {
			return MagicSubType.Frog.getMask() ;
		}
	};
	
	private static final MagicPermanentActivation OMNIBIAN=new MagicPermanentActivation(
			"Omnibian",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Frog") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.TARGET_CREATURE,
                    new MagicBecomeTargetPicker(3,3,false),
                    MagicEvent.NO_DATA,this,"Target creature$ becomes a 3/3 Frog until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicBecomesCreatureAction(creature,OMNIBIAN_VARIABLE));
			}
		}
	};
	
	private static final MagicPermanentActivation ORACLE_OF_NECTARS = new MagicPermanentActivation(
            "Oracle of Nectars",
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.X.getCondition()},
            new MagicActivationHints(MagicTiming.Draw),
            "Life+X") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.X)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final int amount=payedCost.getX();
			final MagicPlayer player=source.getController();
			return new MagicEvent(source,player,new Object[]{player,amount},this,"You gain "+amount+" life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicChangeLifeAction(player,(Integer)data[1]));
		}
	};
		
	private static final MagicPermanentActivation PUTRID_LEECH=new MagicPermanentActivation(
            "Putrid Leech",
			new MagicCondition[]{MagicCondition.ABILITY_ONCE_CONDITION,MagicCondition.TWO_LIFE_CONDITION},
            new MagicActivationHints(MagicTiming.Pump),
            "Pump") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayLifeEvent(source,source.getController(),2),new MagicPlayAbilityEvent(source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Putrid Leech gets +2/+2 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent permanent=(MagicPermanent)data[0];
			game.doAction(new MagicChangeTurnPTAction(permanent,2,2));
		}
	};
	
	private static final MagicPermanentActivation QASALI_PRIDEMAGE=new MagicPermanentActivation(
			"Qasali Pridemage",
            new MagicCondition[]{MagicManaCost.ONE.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Destory") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostSacrificeEvent(source,source.getController(),MagicManaCost.ONE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_ARTIFACT_OR_ENCHANTMENT,
                    new MagicDestroyTargetPicker(false),
                    MagicEvent.NO_DATA,
                    this,
                    "Destroy target artifact or enchantment$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				game.doAction(new MagicDestroyAction(permanent));
			}
		}
	};
	
	private static final MagicPermanentActivation RAGE_NIMBUS=new MagicPermanentActivation(
            "Rage Nimbus",
			new MagicCondition[]{MagicManaCost.ONE_RED.getCondition()},
            new MagicActivationHints(MagicTiming.MustAttack),
            "Attacks"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_RED)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),MagicTargetChoice.NEG_TARGET_CREATURE,MagicMustAttackTargetPicker.getInstance(),
				MagicEvent.NO_DATA,this,"Target creature$ attacks this turn if able.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				game.doAction(new MagicSetAbilityAction((MagicPermanent)target,MagicAbility.AttacksEachTurnIfAble));
			}
		}
	};

	private static final MagicPermanentActivation RAKDOS_GUILDMAGE1=new MagicPermanentActivation(
            "Rakdos Guildmage",
			new MagicCondition[]{MagicCondition.HAS_CARD_CONDITION,MagicManaCost.THREE_BLACK.getCondition()},
            new MagicActivationHints(MagicTiming.Removal,true),
            "-2/-2") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
				new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.THREE_BLACK),
				new MagicDiscardEvent(source,source.getController(),1,false)
			};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicWeakenTargetPicker(2,2),
                    MagicEvent.NO_DATA,
                    this,
                    "Target creature$ gets -2/-2 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,-2,-2));
			}
		}
	};

	private static final MagicPermanentActivation RAKDOS_GUILDMAGE2=new MagicPermanentActivation(
			"Rakdos Guildmage",
            new MagicCondition[]{MagicManaCost.THREE_RED.getCondition()},
            new MagicActivationHints(MagicTiming.Token,true),
            "Token") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.THREE_RED)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(source,player,new Object[]{player},this,
				"Put a 2/1 red Goblin creature token with haste onto the battlefield. Exile it at end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPlayer player=(MagicPlayer)data[0];
			final MagicCard card=MagicCard.createTokenCard(TokenCardDefinitions.GOBLIN2_TOKEN_CARD,player);
			game.doAction(new MagicPlayCardAction(card,player,MagicPlayCardAction.REMOVE_AT_END_OF_TURN));
		}
	};
	
	private static final MagicPermanentActivation RAVENOUS_BALOTH=new MagicPermanentActivation(
			"Ravenous Baloth",
            new MagicCondition[]{MagicCondition.CONTROL_BEAST_CONDITION},
            new MagicActivationHints(MagicTiming.Pump,true),
            "Life+4") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificePermanentEvent(source,source.getController(),MagicTargetChoice.SACRIFICE_BEAST)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(source,player,new Object[]{player},this,"You gain 4 life.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],4));
		}
	};
	
	private static final MagicPermanentActivation RIVER_BOA = 
        new MagicRegenerationActivation("River Boa",MagicManaCost.GREEN);
	
	private static final MagicPermanentActivation SCATTERSHOT_ARCHER=new MagicPermanentActivation(
			"Scattershot Archer",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),new Object[]{source},this,
				"Scattershot Archer deals 1 damage to each creature with flying.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent permanent=(MagicPermanent)data[0];
			final Collection<MagicTarget> targets=game.filterTargets(permanent.getController(),MagicTargetFilter.TARGET_CREATURE_WITH_FLYING);
			for (final MagicTarget target : targets) {
				final MagicDamage damage=new MagicDamage(permanent,target,1,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
	private static final MagicPermanentActivation SIEGE_GANG_COMMANDER=new MagicPermanentActivation(
            "Siege-Gang Commander",
			new MagicCondition[]{MagicManaCost.ONE_RED.getCondition(),MagicCondition.CONTROL_GOBLIN_CONDITION},
			new MagicActivationHints(MagicTiming.Removal,true),
            "Damage") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicPlayer player=source.getController();
			return new MagicEvent[]{					
				new MagicPayManaCostEvent(source,player,MagicManaCost.ONE_RED),
				new MagicSacrificePermanentEvent(source,player,MagicTargetChoice.SACRIFICE_GOBLIN)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(2),
				new Object[]{source},this,"Siege-Gang Commander deals 2 damage to target creature or player$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,2,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
	private static final MagicPermanentActivation SILKBIND_FAERIE=new MagicPermanentActivation("Silkbind Faerie",
			new MagicCondition[]{MagicCondition.CAN_UNTAP_CONDITION,MagicManaCost.ONE_WHITE_OR_BLUE.getCondition()},
			new MagicActivationHints(MagicTiming.Tapping),
            "Tap") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
				new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_WHITE_OR_BLUE),
				new MagicUntapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicTapTargetPicker(true,false),
                    MagicEvent.NO_DATA,
                    this,
                    "Tap target creature$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null&&!creature.isTapped()) {
				game.doAction(new MagicTapAction(creature,true));
			}
		}
	};
	
	private static final MagicPermanentActivation SILVOS_ROGUE_ELEMENTAL=
        new MagicRegenerationActivation("Silvos, Rogue Elemental",MagicManaCost.GREEN);

	private static final MagicPermanentActivation SKELETAL_VAMPIRE1=new MagicPermanentActivation(
            "Skeletal Vampire",
			new MagicCondition[]{MagicManaCost.THREE_BLACK_BLACK.getCondition(),MagicCondition.CONTROL_BAT_CONDITION},
			new MagicActivationHints(MagicTiming.Token,true),
            "Token") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicPlayer player=source.getController();
			return new MagicEvent[]{					
				new MagicPayManaCostEvent(source,player,MagicManaCost.THREE_BLACK_BLACK),
				new MagicSacrificePermanentEvent(source,player,MagicTargetChoice.SACRIFICE_BAT)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(source,player,new Object[]{player},this,"Put two 1/1 black Bat creature tokens with flying onto the battlefield.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.BAT_TOKEN_CARD));
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.BAT_TOKEN_CARD));			
		}
	};

	private static final MagicPermanentActivation SHIVAN_DRAGON = 
        new MagicPumpActivation("Shivan Dragon",MagicManaCost.RED,1,0);
	
	private static final MagicPermanentActivation SKELETAL_VAMPIRE2=new MagicPermanentActivation(
            "Skeletal Vampire",
			new MagicCondition[]{
                MagicCondition.CAN_REGENERATE_CONDITION,
                MagicCondition.CONTROL_BAT_CONDITION,
                new MagicSingleActivationCondition()},
			new MagicActivationHints(MagicTiming.Pump),
            "Regen") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificePermanentEvent(source,source.getController(),MagicTargetChoice.SACRIFICE_BAT)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),new Object[]{source},this,"Regenerate Skeletal Vampire.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			game.doAction(new MagicRegenerateAction((MagicPermanent)data[0]));
		}
	};
	
	private static final MagicPermanentActivation SKITHIRYX1=new MagicGainActivation(
			"Skithiryx, the Blight Dragon",
            MagicManaCost.BLACK,
            MagicAbility.Haste,
            new MagicActivationHints(MagicTiming.FirstMain,false,1)
            );
	
	private static final MagicPermanentActivation SKITHIRYX2=
        new MagicRegenerationActivation("Skithiryx, the Blight Dragon",MagicManaCost.BLACK_BLACK);
	
	private static final MagicPermanentActivation SPHINX_OF_MAGOSI=new MagicPermanentActivation(
			"Sphinx of Magosi",
            new MagicCondition[]{MagicManaCost.TWO_BLUE.getCondition()},
            new MagicActivationHints(MagicTiming.Draw),
            "Draw") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_BLUE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{player,source},
                    this,
                    "Draw a card, then put a +1/+1 counter on Magosi Sphinx.");
        }
		
        @Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            game.doAction(new MagicDrawAction((MagicPlayer)data[0],1));
            game.doAction(new MagicChangeCountersAction((MagicPermanent)data[1],MagicCounterType.PlusOne,1,true));
        }
	};
	
	private static final MagicPermanentActivation SPIKETAIL_HATCHLING=new MagicPermanentActivation(
			"Spiketail Hatchling",
            null,
            new MagicActivationHints(MagicTiming.Counter),
            "Counter") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),MagicTargetChoice.NEG_TARGET_SPELL,
				new Object[]{source},this,"Counter target spell$ unless its controller pays {1}.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicCardOnStack targetSpell=event.getTarget(game,choiceResults,0);
			if (targetSpell!=null) {
				game.addEvent(new MagicCounterUnlessEvent((MagicSource)data[0],targetSpell,MagicManaCost.ONE));
			}
		}
	};

	private static final MagicPermanentActivation SPIRITMONGER1 = 
        new MagicRegenerationActivation("Spiritmonger",MagicManaCost.BLACK);

	private static final MagicPermanentActivation SPIRITMONGER2 = new MagicPermanentActivation(
			"Spiritmonger",
            new MagicCondition[]{MagicManaCost.GREEN.getCondition()},
			new MagicActivationHints(MagicTiming.Pump,false,1),
            "Color") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.GREEN)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicColorChoice.BLUE_RED_WHITE_INSTANCE,
                    new Object[]{source},
                    this,
                    "Spiritmonger becomes the color$ of your choice until end of turn.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent permanent=(MagicPermanent)data[0];
			game.doAction(new MagicSetTurnColorAction(permanent,(MagicColor)choiceResults[0]));
			game.doAction(new MagicPlayAbilityAction(permanent));
		}
	};
	
	private static final MagicPermanentActivation STUDENT_OF_WARFARE = 
        new MagicLevelUpActivation("Student of Warfare",MagicManaCost.WHITE,7);
	
	private static final MagicPermanentActivation STUFFY_DOLL=new MagicPermanentActivation(
			"Stuffy Doll",
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
                    new Object[]{source},
                    this,
                    "Stuffy Doll deals 1 damage to itself.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicDamage damage=new MagicDamage(permanent,permanent,1,false);
			game.doAction(new MagicDealDamageAction(damage));
		}
	};
	
	private static final MagicPermanentActivation STUN_SNIPER=new MagicPermanentActivation(
            "Stun Sniper",
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.ONE.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.ONE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicTapTargetPicker(true,false),
                    new Object[]{source},
                    this,
                    "Stun Sniper deals 1 damage to target creature$. Tap that creature.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {			
				final MagicDamage damage=new MagicDamage((MagicPermanent)data[0],creature,1,false);
				game.doAction(new MagicDealDamageAction(damage));
				if (!creature.isTapped()) {
					game.doAction(new MagicTapAction(creature,true));
				}
			}
		}
	};
	
	private static final MagicPermanentActivation THRUN_THE_LAST_TROLL=
        new MagicRegenerationActivation("Thrun, the Last Troll",MagicManaCost.ONE_GREEN);

	private static final MagicPermanentActivation THUNDERSONG_TRUMPETER=new MagicPermanentActivation(
			"Thundersong Trumpeter",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Tapping),
            "Disable") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicNoCombatTargetPicker(true,true,false),
				    MagicEvent.NO_DATA,
                    this,
                    "Target creature$ can't attack or block this turn.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.CannotAttackOrBlock));
			}
		}
	};
	
	private static final MagicPermanentActivation TOLSIMIR_WOLFBLOOD=new MagicPermanentActivation(
			"Tolsimir Wolfblood",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Token),
            "Token") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(source,player,new Object[]{player},this,
				"Put a legendary 2/2 green and white Wolf creature token named Voja onto the battlefield.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.VOJA_TOKEN_CARD));
		}
	};
	
	private static final MagicPermanentActivation TROLL_ASCETIC = 
        new MagicRegenerationActivation("Troll Ascetic",MagicManaCost.ONE_GREEN);

	private static final MagicPermanentActivation TWINBLADE_SLASHER=new MagicPermanentActivation(
            "Twinblade Slasher",
			new MagicCondition[]{MagicCondition.ABILITY_ONCE_CONDITION,MagicManaCost.ONE_GREEN.getCondition()},
            new MagicActivationHints(MagicTiming.Pump),
            "Pump") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
                new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_GREEN),
                new MagicPlayAbilityEvent(source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Twinblade Slasher gets +2/+2 until end of turn.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			game.doAction(new MagicChangeTurnPTAction(permanent,2,2));
		}
	};
	
	private static final MagicPermanentActivation URSAPINE=new MagicPermanentActivation(
			"Ursapine",
            new MagicCondition[]{MagicManaCost.GREEN.getCondition()},
            new MagicActivationHints(MagicTiming.Pump,true),
            "Pump") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.GREEN)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicPumpTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Target creature$ gets +1/+1 until end of turn.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,1,1));
			}
		}
	};

	private static final MagicPermanentActivation VAMPIRE_HEXMAGE=new MagicPermanentActivation(
			"Vampire Hexmage",
            null,
            new MagicActivationHints(MagicTiming.Removal),
            "Remove") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.TARGET_PERMANENT,
                    MagicCountersTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Remove all counters from target permanent$.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				for (final MagicCounterType counterType : MagicCounterType.values()) {
					final int amount=permanent.getCounters(counterType);
					if (amount>0) {
						game.doAction(new MagicChangeCountersAction(permanent,counterType,-amount,true));
					}
				}
			}
		}
	};
	
	private static final MagicPermanentActivation VEDALKEN_MASTERMIND=new MagicPermanentActivation(
            "Vedalken Mastermind",
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.BLUE.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Return"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.BLUE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.TARGET_PERMANENT_YOU_CONTROL,
                    MagicBounceTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Return target permanent you control$ to its owner's hand.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.OwnersHand));
			}
		}
	};
	
	private static final MagicPermanentActivation VISARA_THE_DREADFUL=new MagicPermanentActivation(
            "Visara the Dreadful",
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Destroy"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicDestroyTargetPicker(true),
				    MagicEvent.NO_DATA,
                    this,
                    "Destroy target creature$. It can't be regenerated.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeStateAction(creature,MagicPermanentState.CannotBeRegenerated,true));
				game.doAction(new MagicDestroyAction(creature));
			}
		}
	};
	
	private static final MagicPermanentActivation WALL_OF_BONE = 
        new MagicRegenerationActivation("Wall of Bone",MagicManaCost.BLACK);

	private static final MagicPermanentActivation ANGELIC_SHIELD=new MagicPermanentActivation(
			"Angelic Shield",
            null,
            new MagicActivationHints(MagicTiming.Removal),
            "Return") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.TARGET_CREATURE,
                    MagicBounceTargetPicker.getInstance(),
				    MagicEvent.NO_DATA,
                    this,
                    "Return target creature$ to its owner's hand.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.OwnersHand));
			}
		}
	};
	
	private static final MagicPermanentActivation ASCETICISM=new MagicPermanentActivation(
			"Asceticism",
            new MagicCondition[]{MagicManaCost.ONE_GREEN.getCondition()},
            new MagicActivationHints(MagicTiming.Pump,true),
            "Regen") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_GREEN)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicRegenerateTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Regenerate target creature$.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicRegenerateAction(creature));
			}
		}
	};
	
	private static final MagicPermanentActivation CAPTIVE_FLAME=new MagicPermanentActivation(
			"Captive Flame",
            new MagicCondition[]{MagicManaCost.RED.getCondition()},
            new MagicActivationHints(MagicTiming.Pump,true),
            "Pump") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.RED)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicPumpTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Target creature$ gets +1/+0 until end of turn.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,1,0));
			}
		}
	};
	
	private static final MagicPermanentActivation DRAGON_ROOST=new MagicPermanentActivation(
			"Dragon Roost",
            new MagicCondition[]{MagicManaCost.FIVE_RED_RED.getCondition()},
            new MagicActivationHints(MagicTiming.Token),
            "Token") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.FIVE_RED_RED)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{player},
                    this,
                    "Put a 5/5 red Dragon creature token with flying onto the battlefield.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {

			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.DRAGON5_TOKEN_CARD));
		}
	};
	
	private static final MagicPermanentActivation FIRES_OF_YAVIMAYA=new MagicPermanentActivation(
			"Fires of Yavimaya",
            null,
            new MagicActivationHints(MagicTiming.Pump),
            "Pump") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicPumpTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Target creature$ gets +2/+2 until end of turn.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,2,2));
			}
		}
	};

	private static final MagicPermanentActivation QUEST_FOR_THE_GEMBLADES=new MagicPermanentActivation(
			"Quest for the Gemblades",
            new MagicCondition[]{MagicCondition.CHARGE_COUNTER_CONDITION},
            new MagicActivationHints(MagicTiming.Pump),
            "Pump") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
				new MagicRemoveCounterEvent((MagicPermanent)source,MagicCounterType.Charge,1),
				new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicPumpTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Put four +1/+1 counters on target creature$.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.PlusOne,4,true));
			}
		}
	};
	
	private static final MagicPermanentActivation QUEST_FOR_THE_GRAVELORD=new MagicPermanentActivation(
            "Quest for the Gravelord",
			new MagicCondition[]{MagicCondition.THREE_CHARGE_COUNTERS_CONDITION},
            new MagicActivationHints(MagicTiming.Token),
            "Token") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
				new MagicRemoveCounterEvent((MagicPermanent)source,MagicCounterType.Charge,3),
				new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{player},
                    this,
                    "Put a 5/5 black Zombie Giant creature token onto the battlefield.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.ZOMBIE_GIANT_TOKEN_CARD));
		}
	};
	
	private static final MagicPermanentActivation RISE_OF_THE_HOBGOBLINS=new MagicPermanentActivation(
			"Rise of the Hobgoblins",
            new MagicCondition[]{MagicManaCost.RED_OR_WHITE.getCondition()},
            new MagicActivationHints(MagicTiming.Block,true),
            "First strike") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.RED_OR_WHITE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{player},
                    this,
                    "Red creatures and white creatures you control gain first strike until end of turn.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			final Collection<MagicTarget> targets=game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				final MagicPermanent creature=(MagicPermanent)target;
				final int colorFlags=creature.getColorFlags();
				if (MagicColor.Red.hasColor(colorFlags)||MagicColor.White.hasColor(colorFlags)) {
					game.doAction(new MagicSetAbilityAction(creature,MagicAbility.FirstStrike));
				}
			}
		}
	};

	private static final MagicPermanentActivation SEAL_OF_DOOM = new MagicPermanentActivation(
            "Seal of Doom",
            null,
            new MagicActivationHints(MagicTiming.Removal),
            "Destroy") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_NONBLACK_CREATURE,
                    new MagicDestroyTargetPicker(true),
                    MagicEvent.NO_DATA,
                    this,
                    "Destroy target nonblack creature$. It can't be regenerated.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeStateAction(creature,MagicPermanentState.CannotBeRegenerated,true));
				game.doAction(new MagicDestroyAction(creature));
			}
		}
	};
	
	private static final MagicPermanentActivation SEAL_OF_FIRE=new MagicPermanentActivation(
			"Seal of Fire",
            null,
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(2),
                    new Object[]{source},
                    this,
                    "Seal of Fire deals 2 damage to target creature or player$.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {

			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,2,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
	
	private static final MagicPermanentActivation BRITTLE_EFFIGY=new MagicPermanentActivation(
            "Brittle Effigy",
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.FOUR.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Exile") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
				new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.FOUR),
				new MagicExileEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    MagicExileTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Exile target creature$.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.Exile));
			}
		}
	};

	private static final MagicLocalVariable CHIMERIC_MASS_VARIABLE=new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			final int charge=permanent.getCounters(MagicCounterType.Charge);
			pt.power=charge;
			pt.toughness=charge;
		}
		
		@Override
		public int getSubTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicSubType.Construct.getMask();
		}
		
        @Override
		public int getTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicType.Creature.getMask();
		}
	};
	
	private static final MagicPermanentActivation CHIMERIC_MASS=new MagicPermanentActivation(
            "Chimeric Mass",
            new MagicCondition[]{MagicManaCost.ONE.getCondition()},
            new MagicActivationHints(MagicTiming.Animate,false,1),
            "Animate") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Until end of turn, Chimeric Mass becomes a Construct artifact creature with \"This creature's "+
    				"power and toughness are each equal to the number of charge counters on it.\"");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicBecomesCreatureAction((MagicPermanent)data[0],CHIMERIC_MASS_VARIABLE));
		}
	};

	private static final MagicPermanentActivation MIND_STONE=new MagicPermanentActivation(
            "Mind Stone",
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.TWO.getCondition()},
            new MagicActivationHints(MagicTiming.Draw),
            "Draw") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return new MagicEvent[]{
				new MagicTapEvent(permanent),
				new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE),
				new MagicSacrificeEvent(permanent)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(source,player,new Object[]{player},this,"Draw a card.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicDrawAction((MagicPlayer)data[0],1));
		}
	};
	
	private static final MagicPermanentActivation MOONGLOVE_EXTRACT=new MagicPermanentActivation(
			"Moonglove Extract",
            null,
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(2),
                    new Object[]{source},
                    this,
                    "Moonglove Extract deals 2 damage to target creature or player$.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {

			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,2,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
    private static final MagicPermanentActivation TUMBLE_MAGNET=new MagicPermanentActivation(
            "Tumble Magnet",
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicCondition.CHARGE_COUNTER_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Tap") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return new MagicEvent[]{
				new MagicTapEvent(permanent),
				new MagicRemoveCounterEvent(permanent,MagicCounterType.Charge,1)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_ARTIFACT_OR_CREATURE,
                    new MagicTapTargetPicker(true,false),
                    MagicEvent.NO_DATA,
                    this,
                    "Tap target artifact or creature$.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent creature=(MagicPermanent)event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicTapAction(creature,true));
			}
		}
	};

	private static final MagicPermanentActivation SERRATED_ARROWS=new MagicPermanentActivation(
            "Serrated Arrows",
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicCondition.CHARGE_COUNTER_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "-1/-1") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return new MagicEvent[]{
				new MagicTapEvent(permanent),
				new MagicRemoveCounterEvent(permanent,MagicCounterType.Charge,1)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicWeakenTargetPicker(1,1),
                    MagicEvent.NO_DATA,
                    this,
                    "Put a -1/-1 counter on target creature$.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent creature=(MagicPermanent)event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.MinusOne,1,true));
			}
		}
	};

	private static final MagicPermanentActivation SHRINE_OF_BURNING_RAGE=new MagicPermanentActivation(
            "Shrine of Burning Rage",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.THREE.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return new MagicEvent[]{
				new MagicTapEvent(permanent),
				new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.THREE),
				new MagicSacrificeEvent(permanent)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(source.getCounters(MagicCounterType.Charge)),
                    new Object[]{source},
                    this,
                    "Shrine of Burning Rage deals damage equal to the number of charge counters on it to target creature or player$.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {

			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicPermanent source=(MagicPermanent)data[0];
				final int amount=source.getCounters(MagicCounterType.Charge);
				if (amount>0) {
					final MagicDamage damage=new MagicDamage(source,target,amount,false);
					game.doAction(new MagicDealDamageAction(damage));
				}
			}
		}
	};
	
	private static final MagicPermanentActivation TRIP_NOOSE=new MagicPermanentActivation(
            "Trip Noose",
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.TWO.getCondition()},
            new MagicActivationHints(MagicTiming.Tapping),
            "Tap") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.TWO)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicTapTargetPicker(true,false),
                    MagicEvent.NO_DATA,
                    this,
                    "Tap target creature$.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null&&!creature.isTapped()) {
				game.doAction(new MagicTapAction(creature,true));
			}
		}
	};
	
	private static final MagicLocalVariable CELESTIAL_COLONNADE_VARIABLE=new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.power=4;
			pt.toughness=4;
		}
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return flags|MagicAbility.Flying.getMask()|MagicAbility.Vigilance.getMask();
		}
		@Override
		public int getSubTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicSubType.Elemental.getMask();
		}
        @Override
		public int getTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicType.Creature.getMask();
		}
		@Override
		public int getColorFlags(final MagicPermanent permanent,final int flags) {
			return MagicColor.White.getMask()|MagicColor.Blue.getMask();
		}		
	};

	private static final MagicPermanentActivation CELESTIAL_COLONNADE=new MagicPermanentActivation(
			"Celestial Colonnade",
            new MagicCondition[]{new MagicArtificialCondition(
                MagicManaCost.THREE_WHITE_BLUE.getCondition(),
                MagicManaCost.TWO_WHITE_WHITE_BLUE_BLUE.getCondition())},
			new MagicActivationHints(MagicTiming.Animate),
            "Animate") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.THREE_WHITE_BLUE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Until end of turn, Celestial Colonnade becomes a 4/4 white and blue Elemental creature with flying and vigilance. It's still a land.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicBecomesCreatureAction((MagicPermanent)data[0],CELESTIAL_COLONNADE_VARIABLE));
		}
	};

	private static final MagicLocalVariable CREEPING_TAR_PIT_VARIABLE=new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.power=3;
			pt.toughness=2;
		}
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return flags|MagicAbility.Unblockable.getMask();
		}
		@Override
		public int getSubTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicSubType.Elemental.getMask();
		}
        @Override
		public int getTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicType.Creature.getMask();
		}
		@Override
		public int getColorFlags(final MagicPermanent permanent,final int flags) {
			return MagicColor.Blue.getMask()|MagicColor.Black.getMask();
		}		
	};

	private static final MagicPermanentActivation CREEPING_TAR_PIT=new MagicPermanentActivation(
			"Creeping Tar Pit",
            new MagicCondition[]{new MagicArtificialCondition(
			    MagicManaCost.ONE_BLUE_BLACK.getCondition(),
                MagicManaCost.BLUE_BLUE_BLACK_BLACK.getCondition())},
			new MagicActivationHints(MagicTiming.Animate),
            "Animate") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_BLUE_BLACK)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Until end of turn, Creeping Tar Pit becomes a 3/2 blue and black Elemental creature and is unblockable. It's still a land.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicBecomesCreatureAction((MagicPermanent)data[0],CREEPING_TAR_PIT_VARIABLE));
		}
	};
	
    private static final MagicLocalVariable INKMOTH_NEXUS_VARIABLE=new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.power=1;
			pt.toughness=1;
		}
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return flags|MagicAbility.Flying.getMask()|MagicAbility.Infect.getMask();
		}
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicType.Artifact.getMask()|MagicType.Creature.getMask();
		}
	};

	
    private static final MagicPermanentActivation INKMOTH_NEXUS=new MagicPermanentActivation(
			"Inkmoth Nexus",
            new MagicCondition[]{new MagicArtificialCondition(
					MagicManaCost.ONE.getCondition(),
                    MagicManaCost.ONE.getCondition())},
			new MagicActivationHints(MagicTiming.Animate),
            "Animate") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this, 
                    "Inkmoth Nexus becomes a 1/1 Blinkmoth artifact creature with flying and infect until end of turn." + 
                    " It's still a land.");
		}

		@Override
        public void executeEvent(final MagicGame game,final MagicEvent event,
                final Object[] data,final Object[] choiceResults) {
			final MagicPermanent permanent=(MagicPermanent)data[0];
			game.doAction(new MagicBecomesCreatureAction(permanent,INKMOTH_NEXUS_VARIABLE));
		}
	};
	
    private static final MagicLocalVariable RAGING_RAVINE_VARIABLE=new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.power=3;
			pt.toughness=3;
		}
		@Override
		public int getSubTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicSubType.Elemental.getMask();
		}
        @Override
		public int getTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicType.Creature.getMask();
		}
		@Override
		public int getColorFlags(final MagicPermanent permanent,final int flags) {
			return MagicColor.Red.getMask()|MagicColor.Green.getMask();
		}		
	};
	
    private static final MagicTrigger RAGING_RAVINE_TRIGGER=new MagicTrigger(MagicTriggerType.WhenAttacks,"Raging Ravine") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			if (permanent==data&&permanent.isCreature()) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,"Put a +1/+1 counter on Raging Ravine.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.PlusOne,1,true));
		}
    };

	private static final MagicPermanentActivation RAGING_RAVINE=new MagicPermanentActivation(
			"Raging Ravine",new MagicCondition[]{new MagicArtificialCondition(
					MagicManaCost.TWO_RED_GREEN.getCondition(),
                    MagicManaCost.ONE_RED_RED_GREEN_GREEN.getCondition())},
			new MagicActivationHints(MagicTiming.Animate)) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_RED_GREEN)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),new Object[]{source},this,
				"Until end of turn, Raging Ravine becomes a 3/3 red and green Elemental creature with \"" +
				"Whenever this creature attacks, put a +1/+1 counter on it.\" It's still a land.");
		}

		@Override
        public void executeEvent(final MagicGame game,final MagicEvent
                event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent permanent=(MagicPermanent)data[0];
			game.doAction(new MagicBecomesCreatureAction(permanent,RAGING_RAVINE_VARIABLE));
			game.doAction(new MagicAddTurnTriggerAction(permanent,RAGING_RAVINE_TRIGGER));
		}
	};

	private static final MagicLocalVariable STIRRING_WILDWOOD_VARIABLE=new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.power=3;
			pt.toughness=4;
		}
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return flags|MagicAbility.Reach.getMask();
		}
		@Override
		public int getSubTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags|MagicSubType.Elemental.getMask();
		}
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicType.Creature.getMask();
		}
				
		@Override
		public int getColorFlags(final MagicPermanent permanent,final int flags) {
			return MagicColor.Green.getMask()|MagicColor.White.getMask();
		}		
	};

	private static final MagicPermanentActivation STIRRING_WILDWOOD=new MagicPermanentActivation(
			"Stirring Wildwood",
            new MagicCondition[]{new MagicArtificialCondition(
					MagicManaCost.ONE_GREEN_WHITE.getCondition(),
                    MagicManaCost.GREEN_GREEN_WHITE_WHITE.getCondition())},
			new MagicActivationHints(MagicTiming.Animate),
            "Animate") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_GREEN_WHITE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},this,
                    "Until end of turn, Stirring Wildwood becomes a 3/4 green and white Elemental creature with reach. It's still a land.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,
                final Object[] data,final Object[] choiceResults) {
			game.doAction(new MagicBecomesCreatureAction((MagicPermanent)data[0],STIRRING_WILDWOOD_VARIABLE));
		}
	};
	
    private static final MagicPermanentActivation TECTONIC_EDGE=new MagicPermanentActivation("Tectonic Edge",
			new MagicCondition[]{
                MagicManaCost.TWO.getCondition(),  //add ONE for the card itself
                MagicCondition.CAN_TAP_CONDITION,
                MagicCondition.OPP_FOUR_LANDS_CONDITION
            },
			new MagicActivationHints(MagicTiming.Removal),
            "Destroy") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
			    new MagicTapEvent((MagicPermanent)source),
			    new MagicSacrificeEvent((MagicPermanent)source),
				new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.ONE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.TARGET_NONBASIC_LAND,
                    new MagicDestroyTargetPicker(false),
                    MagicEvent.NO_DATA,
                    this,
                    "Destroy target nonbasic land$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,
                final Object[] data,final Object[] choiceResults) {
			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				game.doAction(new MagicDestroyAction(permanent));
			}
		}
	};
	
	public static void addPermanentActivations() {
        Class c = PermanentActivationDefinitions.class;
        Field[] fields = c.getDeclaredFields();
        int cnt = 0;
        for (final Field field : fields) {
            try {
                final Object obj = field.get(null);
                if (obj instanceof MagicPermanentActivation) {
                    final MagicPermanentActivation pact = (MagicPermanentActivation)obj;
                    final MagicCardDefinition card=pact.getCardDefinition();
                    card.addActivation(pact);
                    cnt++;
                }
            } catch (IllegalAccessException err) {
            }
        }

		System.err.println("Added " + cnt + " activations");
	}
}
