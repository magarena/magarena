package magic.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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
import magic.model.action.MagicAttachEquipmentAction;
import magic.model.action.MagicBecomesCreatureAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicChangePoisonAction;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicCounterItemOnStackAction;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicDrawAction;
import magic.model.action.MagicGainControlAction;
import magic.model.action.MagicMillLibraryAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.action.MagicReanimateAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.action.MagicRemoveItemFromStackAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.action.MagicTapAction;
import magic.model.action.MagicUntapAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicColorChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.choice.MagicPayManaCostResult;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicDrawEvent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicPlayOgreUnlessEvent;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicTapEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicBecomeTargetPicker;
import magic.model.target.MagicBounceTargetPicker;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.target.MagicExileTargetPicker;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.target.MagicNoCombatTargetPicker;
import magic.model.target.MagicPowerTargetPicker;
import magic.model.target.MagicPumpTargetPicker;
import magic.model.target.MagicSacrificeTargetPicker;
import magic.model.target.MagicTapTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicUnblockableTargetPicker;
import magic.model.target.MagicWeakenTargetPicker;
import magic.model.trigger.MagicDevourTrigger;
import magic.model.trigger.MagicFromGraveyardToLibraryTrigger;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicLivingWeaponTrigger;
import magic.model.trigger.MagicRavnicaLandTrigger;
import magic.model.trigger.MagicRefugeLandTrigger;
import magic.model.trigger.MagicSpecterTrigger;
import magic.model.trigger.MagicTappedIntoPlayTrigger;
import magic.model.trigger.MagicTappedIntoPlayUnlessTrigger;
import magic.model.trigger.MagicTappedIntoPlayUnlessTwoTrigger;
import magic.model.trigger.MagicThiefTrigger;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;
import magic.model.trigger.MagicVeteranTrigger;
import magic.model.trigger.MagicVividLandTrigger;
import magic.model.variable.MagicLocalVariable;
import magic.model.variable.MagicDummyLocalVariable;

public class TriggerDefinitions {
	
	


	private static final MagicTrigger ACIDIC_SLIME=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Acidic Slime") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_ARTIFACT_OR_ENCHANTMENT_OR_LAND,
				new MagicDestroyTargetPicker(false),MagicEvent.NO_DATA,this,"Destroy target artifact, enchantment or land$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				game.doAction(new MagicDestroyAction(permanent));
			}
		}
    };
	
    private static final MagicTrigger AFFA_GUARD_HOUND=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Affa Guard Hound") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_CREATURE,MagicPumpTargetPicker.getInstance(),
				MagicEvent.NO_DATA,this,"Target creature$ gets +0/+3 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,0,3));
			}
		}
    };
	
	private static final MagicTrigger ANGEL_OF_DESPAIR=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Angel of Despair") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_PERMANENT,new MagicDestroyTargetPicker(false),
				MagicEvent.NO_DATA,this,"Destroy target permanent$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				game.doAction(new MagicDestroyAction(permanent));
			}
		}
    };
    
	private static final MagicTrigger ARCHON_OF_JUSTICE=new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Archon of Justice") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_PERMANENT,MagicExileTargetPicker.getInstance(),
						MagicEvent.NO_DATA,this,"Exile target permanent$.");
			}
			return null;
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.Exile));
			}
		}
    };
     
    private static final MagicTrigger ARCHON_OF_REDEMPTION1=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Archon of Redemption") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player,permanent},this,"You gain life equal to Archon of Redemption's power.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[1];
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],permanent.getPower(game)));
		}		
    };

    private static final MagicTrigger ARCHON_OF_REDEMPTION2=new MagicTrigger(MagicTriggerType.WhenOtherComesIntoPlay,"Archon of Redemption") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPermanent otherPermanent=(MagicPermanent)data;
			final MagicPlayer player=permanent.getController();
			if (otherPermanent!=permanent&&otherPermanent.getController()==player&&otherPermanent.isCreature()&&
				otherPermanent.hasAbility(game,MagicAbility.Flying)) {
				return new MagicEvent(permanent,player,new Object[]{player,otherPermanent},this,
					"You gain life equal to the power of "+otherPermanent.getName()+'.');
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[1];
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],permanent.getPower(game)));
		}		
    };
    
    private static final MagicTrigger AVEN_MIMEOMANCER=new MagicTrigger(MagicTriggerType.AtUpkeep,"Aven Mimeomancer") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent.getController()==data) {
				return new MagicEvent(permanent,permanent.getController(),
	new MagicMayChoice(
			"You may put a feather counter on target creature.",MagicTargetChoice.TARGET_CREATURE),
    new MagicBecomeTargetPicker(3,1,true),
					MagicEvent.NO_DATA,this,"You may$ put a feather counter on target creature$.");
			}
			return null;
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicPermanent creature=event.getTarget(game,choiceResults,1);
				if (creature!=null) {
					game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.Feather,1,true));
				}
			}
		}
    };
    
    private static final MagicTrigger BALEFIRE_LIEGE1=new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed,"Balefire Liege") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			final MagicCard card=((MagicCardOnStack)data).getCard();
			if (card.getOwner()==player&&MagicColor.Red.hasColor(card.getColorFlags())) {
				return new MagicEvent(permanent,player,MagicTargetChoice.NEG_TARGET_PLAYER,
					new Object[]{permanent},this,"Balefire Liege deals 3 damage to target player$.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				final MagicDamage damage=new MagicDamage((MagicPermanent)data[0],player,3,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}		
    };
    
    private static final MagicTrigger BALEFIRE_LIEGE2=new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed,"Balefire Liege") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicCard card=((MagicCardOnStack)data).getCard();
			if (card.getOwner()==player&&MagicColor.White.hasColor(card.getColorFlags())) {
				return new MagicEvent(permanent,player,new Object[]{player},this,"You gain 3 life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {			
			
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],3));
		}
    };

    private static final MagicTrigger BATTLEGRACE_ANGEL=new MagicTrigger(MagicTriggerType.WhenAttacks,"Battlegrace Angel") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPermanent creature=(MagicPermanent)data;
			final MagicPlayer player=permanent.getController();
			if (creature.getController()==player&&player.getNrOfAttackers()==1) {
				return new MagicEvent(permanent,player,new Object[]{creature},this,creature.getName()+" gains lifelink until end of turn.");
			}
			return null;
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {			

			game.doAction(new MagicSetAbilityAction((MagicPermanent)data[0],MagicAbility.LifeLink));
		}
    };
    
    private static final MagicTrigger BLAZING_SPECTER=new MagicSpecterTrigger("Blazing Specter",true,false);
    
    private static final MagicTrigger BROODMATE_DRAGON=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Broodmate Dragon") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You put a 4/4 red Dragon creature token with flying onto the battlefield.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.DRAGON4_TOKEN_CARD));
		}
    };

    private static final MagicTrigger BUTCHER_OF_MALAKIR1=new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Butcher of Malakir") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				final MagicPlayer controller=permanent.getController();
				return new MagicEvent(permanent,controller,new Object[]{permanent,game.getOpponent(controller)},this,"Your opponent sacrifices a creature.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer opponent=(MagicPlayer)data[1];
			if (opponent.controlsPermanentWithType(MagicType.Creature)) {
				game.addEvent(new MagicSacrificePermanentEvent((MagicPermanent)data[0],opponent,MagicTargetChoice.SACRIFICE_CREATURE));
			}
		}
    };
    
    private static final MagicTrigger BUTCHER_OF_MALAKIR2=new MagicTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay,"Butcher of Malakir") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPermanent otherPermanent=(MagicPermanent)data;
			final MagicPlayer controller=permanent.getController();
			if (otherPermanent!=permanent&&otherPermanent.getController()==controller&&otherPermanent.isCreature()) {
				return new MagicEvent(permanent,controller,new Object[]{permanent,game.getOpponent(controller)},this,"Your opponent sacrifices a creature.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer opponent=(MagicPlayer)data[1];
			if (opponent.controlsPermanentWithType(MagicType.Creature)) {
				game.addEvent(new MagicSacrificePermanentEvent((MagicPermanent)data[0],opponent,MagicTargetChoice.SACRIFICE_CREATURE));
			}
		}
    };

    private static final MagicTrigger CAPTAIN_OF_THE_WATCH=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Captain of the Watch") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You put three 1/1 white Soldier creature tokens onto the battlefield.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			for (int count=3;count>0;count--) {
				
				game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.SOLDIER_TOKEN_CARD));
			}
		}		
    };

    private static final MagicTrigger CARNIFEX_DEMON=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Carnifex Demon") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,
					"Carnifex Demon enters the battlefield with two -1/-1 counters on it.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.MinusOne,2,false));
		}

		@Override
		public boolean usesStack() {

			return false;
		}
    };
    
    private static final MagicTrigger CHANDRAS_SPITFIRE=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Chandra's Spitfire") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			final MagicDamage damage=(MagicDamage)data;
			final MagicTarget target=damage.getTarget();
			if (!damage.isCombat()&&target.isPlayer()&&target!=player) {
				return new MagicEvent(permanent,player,new Object[]{permanent},this,"Chandra's Spitfire gets +3/+0 until end of turn.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],3,0));
		}
    };
    
    private static final MagicTrigger CREAKWOOD_LIEGE=new MagicTrigger(MagicTriggerType.AtUpkeep,"Creakwood Liege") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,new Object[]{player},this,"You put a 1/1 black and green Worm creature token onto the battlefield.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.WORM_TOKEN_CARD));
		}		
    };
    
    private static final MagicTrigger DARKSLICK_DRAKE=new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Darkslick Drake") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				return new MagicDrawEvent(permanent,permanent.getController(),1);
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

		}
    };
    
    private static final MagicTrigger DEATHBRINGER_LIEGE1=new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed,"Deathbringer Liege") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicCard card=((MagicCardOnStack)data).getCard();
			if (card.getOwner()==player&&MagicColor.Black.hasColor(card.getColorFlags())) {
				return new MagicEvent(permanent,player,
	new MagicMayChoice(
			"You may destroy target creature if it's tapped.",MagicTargetChoice.NEG_TARGET_CREATURE),
    new MagicDestroyTargetPicker(false),
					MagicEvent.NO_DATA,this,"You may$ destroy target creature$ if it's tapped.");
			}			
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicPermanent creature=event.getTarget(game,choiceResults,1);
				if (creature!=null&&creature.isTapped()) {					
					game.doAction(new MagicDestroyAction(creature));
				}
			}
		}
    };
    
    private static final MagicTrigger DEATHBRINGER_LIEGE2=new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed,"Deathbringer Liege") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			final MagicCard card=((MagicCardOnStack)data).getCard();
			if (card.getOwner()==player&&MagicColor.White.hasColor(card.getColorFlags())) {
				return new MagicEvent(permanent,player,
	new MagicMayChoice("You may tap target creature.",MagicTargetChoice.NEG_TARGET_CREATURE),
                        new MagicTapTargetPicker(true,false),
					MagicEvent.NO_DATA,this,"You may$ tap target creature$.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicPermanent creature=event.getTarget(game,choiceResults,1);
				if (creature!=null&&!creature.isTapped()) {
					game.doAction(new MagicTapAction(creature,true));
				}
			}
		}
    };
    
    
    private static final MagicTrigger DOOMGAPE=new MagicTrigger(MagicTriggerType.AtUpkeep,"Doomgape") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			if (player==data) {				
				return new MagicEvent(permanent,player,new Object[]{permanent,player},this,"Sacrifice a creature. You gain life equal to that creature's toughness.");
			}
			return null;
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[1];
			if (player.controlsPermanentWithType(MagicType.Creature)) {
				game.addEvent(new MagicEvent((MagicPermanent)data[0],player, MagicTargetChoice.SACRIFICE_CREATURE,MagicSacrificeTargetPicker.getInstance(),
					new Object[]{player},
    new MagicEventAction() {
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				final int toughness=creature.getToughness(game);
				game.doAction(new MagicSacrificeAction(creature));
				game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],toughness));
			}
		}
	},"Choose a creature to sacrifice$."));
			}
		}
    };

    private static final MagicTrigger DRAGONMASTER_OUTCAST=new MagicTrigger(MagicTriggerType.AtUpkeep,"Dragonmaster Outcast") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			if (player==data&&player.getNrOfPermanentsWithType(MagicType.Land)>=6) {
				return new MagicEvent(permanent,player,new Object[]{player},this,"You put a 5/5 red Dragon creature token with flying onto the battlefield.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.DRAGON5_TOKEN_CARD));
		}
    };
    
    private static final MagicTrigger DRAINING_WHELK=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Draining Whelk") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_SPELL,new Object[]{permanent},this,
				"Counter target spell$. Put X +1/+1 counters on Draining Whelk, where X is that spell's converted mana cost.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicCardOnStack card=event.getTarget(game,choiceResults,0);
			if (card!=null) {
				game.doAction(new MagicCounterItemOnStackAction(card));
				game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.PlusOne,card.getConvertedCost(),true));
			}
		}
    };

    private static final MagicTrigger DREAD1=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Dread") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			final MagicDamage damage=(MagicDamage)data;
			if (damage.getTarget()==player&&damage.getSource().isPermanent()) {
				final MagicPermanent source=(MagicPermanent)damage.getSource();
				if (source.isCreature()) {
					return new MagicEvent(permanent,player,new Object[]{source},this,"Destroy "+source.getName()+".");
				}
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			game.doAction(new MagicDestroyAction((MagicPermanent)data[0]));
		}
    };
    
    private static final MagicTrigger DREAD2=new MagicFromGraveyardToLibraryTrigger("Dread");

    private static final MagicTrigger DROMAR_THE_BANISHER=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Dromar, the Banisher") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) {				
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,
	new MagicMayChoice(
			"You may pay {2}{U}.",new MagicPayManaCostChoice(MagicManaCost.TWO_BLUE),MagicColorChoice.UNSUMMON_INSTANCE),new Object[]{player},this,
						"You may$ pay {2}{U}$. If you do, choose a color$. Return all creatures of that color to their owner's hand.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicColor color=(MagicColor)choiceResults[2];
				final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_CREATURE);
				for (final MagicTarget target : targets) {
					
					final MagicPermanent creature=(MagicPermanent)target;
					if (color.hasColor(creature.getColorFlags())) {
						game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.OwnersHand));
					}
				}
			}			
		}
    };
    
    private static final MagicTrigger EMPYRIAL_ARCHANGEL=new MagicTrigger(MagicTriggerType.IfDamageWouldBeDealt,"Empyrial Archangel",2) {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getTarget()==permanent.getController()) {
				// Replacement effect. Generates no event or action.
				damage.setTarget(permanent);				
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
		}
    };
    
    private static final MagicTrigger EZURIS_ARCHERS=new MagicTrigger(MagicTriggerType.WhenBlocks,"Ezuri's Archers") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent==data) {
				final MagicPermanent blocked=permanent.getBlockedCreature();
				if (blocked!=null&&blocked.hasAbility(game,MagicAbility.Flying)) {
					return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,"Ezuri's Archers gets +3/+0 until end of turn.");
				}
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],3,0));
		}
    };
    
    private static final MagicTrigger FIRE_SERVANT=new MagicTrigger(MagicTriggerType.IfDamageWouldBeDealt,"Fire Servant",3) {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			final MagicSource source=damage.getSource();
			if (source.getController()==permanent.getController()&&source.isSpell()&&
				MagicColor.Red.hasColor(source.getColorFlags())&&source.getCardDefinition().isSpell()) {
				// Generates no event or action.
				damage.setAmount(damage.getAmount()<<1);
			}			
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
		}
    };

    private static final MagicTrigger FLAMEBLAST_DRAGON=new MagicTrigger(MagicTriggerType.WhenAttacks,"Flameblast Dragon") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,
	new MagicMayChoice(
			"You may pay {X}{R}.",new MagicPayManaCostChoice(MagicManaCost.X_RED),MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER), new MagicDamageTargetPicker(player.getMaximumX(game,MagicManaCost.X_RED)),
					new Object[]{permanent},this,"You may pay$ {X}{R}$. If you do, Flameblast Dragon deals X damage to target creature or player$.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicTarget target=event.getTarget(game,choiceResults,2);
				if (target!=null) {
					final MagicPayManaCostResult payedManaCost=(MagicPayManaCostResult)choiceResults[1];
					final MagicDamage damage=new MagicDamage((MagicPermanent)data[0],target,payedManaCost.getX(),false);
					game.doAction(new MagicDealDamageAction(damage));
				}
			}
		}
    };

	private static final MagicTrigger FLAME_KIN_ZEALOT=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Flame-Kin Zealot") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"Creatures you control get +1/+1 and gain haste until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				
				final MagicPermanent creature=(MagicPermanent)target;
				game.doAction(new MagicChangeTurnPTAction(creature,1,1));
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Haste));
			}
		}
    };
    
    private static final MagicTrigger FLAMETONGUE_KAVU=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Flametongue Kavu") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_CREATURE,new MagicDamageTargetPicker(4),
				new Object[]{permanent},this,"Flametongue Kavu deals 4 damage to target creature$.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				final MagicDamage damage=new MagicDamage((MagicPermanent)data[0],creature,4,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };

    private static final MagicTrigger FOUL_IMP=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Foul Imp") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You lose 2 life.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],-2));
		}
    };
    
    private static final MagicTrigger FRENZIED_GOBLIN=new MagicTrigger(MagicTriggerType.WhenAttacks,"Frenzied Goblin") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,
	new MagicMayChoice(
			"You may pay {R}.",new MagicPayManaCostChoice(MagicManaCost.RED),MagicTargetChoice.NEG_TARGET_CREATURE),new MagicNoCombatTargetPicker(false,true,false),
					MagicEvent.NO_DATA,this,"You may$ pay {R}$. If you do, target creature$ can't block this turn.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicPermanent creature=event.getTarget(game,choiceResults,2);
				if (creature!=null) {
					game.doAction(new MagicSetAbilityAction(creature,MagicAbility.CannotBlock));
				}
			}
		}
    };
    
    private static final MagicTrigger GELECTRODE=new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed,"Gelectrode") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data;
			if (cardOnStack.getController()==player&&cardOnStack.getCardDefinition().isSpell()) {
				return new MagicEvent(permanent,player,new Object[]{permanent},this,"Untap Gelectrode.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicUntapAction((MagicPermanent)data[0]));
		}
    };
    
    private static final MagicTrigger GHOST_COUNCIL_OF_ORZHOVA=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Ghost Council of Orzhova") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,MagicTargetChoice.TARGET_OPPONENT,
				new Object[]{player},this,"Target opponent$ loses 1 life and you gain 1 life.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				game.doAction(new MagicChangeLifeAction(player,-1));
				game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],1));
			}
		}		
    };

    private static final MagicTrigger GOBLIN_PILEDRIVER=new MagicTrigger(MagicTriggerType.WhenAttacks,"Goblin Piledriver") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			if (permanent==data) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,
					"Goblin Piledriver gets +2/+0 until end of turn for each other attacking Goblin.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			int power=0;
			final MagicPermanent creature=(MagicPermanent)data[0];
			final Collection<MagicTarget> targets=game.filterTargets(creature.getController(),MagicTargetFilter.TARGET_ATTACKING_CREATURE);
			for (final MagicTarget target : targets) {

				if (creature!=target) {
					final MagicPermanent attacker=(MagicPermanent)target;
					if (attacker.hasSubType(MagicSubType.Goblin)) {
						power+=2;
					}
				}
			}
			if (power>0) {
				game.doAction(new MagicChangeTurnPTAction(creature,power,0));
			}
		}
    };
    
    private static final MagicTrigger GUARD_GOMAZOA=new MagicTrigger(MagicTriggerType.IfDamageWouldBeDealt,"Guard Gomazoa",1) {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getTarget()==permanent&&damage.isCombat()) {
				// Replacement effect. Generates no event or action.
				damage.setAmount(0);	
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
		}
    };
    
    private static final MagicTrigger GUARDIAN_SERAPH=new MagicTrigger(MagicTriggerType.IfDamageWouldBeDealt,"Guardian Seraph",5) {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicDamage damage=(MagicDamage)data;
			final int amount=damage.getAmount();
			if (!damage.isUnpreventable()&&amount>0&&damage.getSource().getController()!=player&&damage.getTarget()==player) {
				// Prevention effect.
				damage.setAmount(amount-1);
			}			
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
		}
    };

    private static final MagicTrigger GOBLIN_SHORTCUTTER=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Goblin Shortcutter") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_CREATURE,new MagicNoCombatTargetPicker(false,true,false),
				MagicEvent.NO_DATA,this,"Target creature$ can't block this turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.CannotBlock));
			}
		}
    };

    private static final MagicTrigger GRAVE_TITAN1=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Grave Titan") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You put two 2/2 black Zombie creature tokens onto the battlefield.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.ZOMBIE_TOKEN_CARD));
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.ZOMBIE_TOKEN_CARD));
		}		
    };

    private static final MagicTrigger GRAVE_TITAN2=new MagicTrigger(MagicTriggerType.WhenAttacks,"Grave Titan") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,"You put two 2/2 black Zombie creature tokens onto the battlefield.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.ZOMBIE_TOKEN_CARD));
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.ZOMBIE_TOKEN_CARD));
		}		
    };
    
    private static final MagicTrigger GUUL_DRAZ_SPECTER=new MagicSpecterTrigger("Guul Draz Specter",true,false);
    
	private static final MagicTrigger HAVOC_DEMON=new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Havoc Demon") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,"All creatures get -5/-5 until end of turn.");
			}
			return null;
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_CREATURE);
			for (final MagicTarget target : targets) {
				
				game.doAction(new MagicChangeTurnPTAction((MagicPermanent)target,-5,-5));
			}
		}
    };
    
    private static final MagicTrigger HERO_OF_BLADEHOLD=new MagicTrigger(MagicTriggerType.WhenAttacks,"Hero of Bladehold") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,
					"You put two 1/1 white Soldier creature tokens onto the battlefield tapped and attacking.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			for (int count=2;count>0;count--) {
			
				final MagicCard card=MagicCard.createTokenCard(TokenCardDefinitions.SOLDIER_TOKEN_CARD,player);
				game.doAction(new MagicPlayCardAction(card,player,MagicPlayCardAction.TAPPED_ATTACKING));
			}
		}		
    };
    
    private static final MagicTrigger HUNGRY_SPRIGGAN=new MagicTrigger(MagicTriggerType.WhenAttacks,"Hungry Spriggan") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			if (permanent==data) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,"Hungry Spriggan gets +3/+3 until end of turn.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],3,3));
		}
    };

    private static final MagicTrigger HYPNOTIC_SPECTER=new MagicSpecterTrigger("Hypnotic Specter",false,true);
    
    private static final MagicTrigger JUNIPER_ORDER_RANGER=new MagicTrigger(MagicTriggerType.WhenOtherComesIntoPlay,"Juniper Order Ranger") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPermanent otherPermanent=(MagicPermanent)data;
			final MagicPlayer player=permanent.getController();
			if (otherPermanent!=permanent&&otherPermanent.isCreature()&&otherPermanent.getController()==player) {
				return new MagicEvent(permanent,player,new Object[]{otherPermanent,permanent},this,
					"Put a +1/+1 counter on "+otherPermanent.getName()+" and a +1/+1 counter on Juniper Order Ranger.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.PlusOne,1,true));
			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[1],MagicCounterType.PlusOne,1,true));			
		}		
    };
    
    private static final MagicTrigger KAERVEK_THE_MERCILESS=new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed,"Kaervek the Merciless") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data;
			final MagicPlayer player=permanent.getController();
			if (cardOnStack.getController()!=player) {
				final int damage=cardOnStack.getCardDefinition().getConvertedCost();
				return new MagicEvent(permanent,player,MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(damage),
					new Object[]{permanent,damage},this,"Kaervek the Merciless deals "+damage+" damage to target creature or player$.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,(Integer)data[1],false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };

    private static final MagicTrigger KAZUUL_TYRANT_OF_THE_CLIFFS=new MagicTrigger(MagicTriggerType.WhenAttacks,"Kazuul, Tyrant of the Cliffs") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicPermanent creature=(MagicPermanent)data;
			if (creature.getController()!=player) {
				return new MagicEvent(permanent,player,new Object[]{permanent,player},this,
					"Put a 3/3 red Ogre creature token onto the battlefield unless your opponent pays {3}.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[1];
			game.addEvent(new MagicPlayOgreUnlessEvent((MagicPermanent)data[0],game.getOpponent(player),player,MagicManaCost.THREE));
		}
    };
    
    private static final MagicTrigger KEENING_BANSHEE=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Keening Banshee") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_CREATURE,new MagicWeakenTargetPicker(-2,-2),
				MagicEvent.NO_DATA,this,"Target creature$ gets -2/-2 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,-2,-2));
			}
		}
    };

    private static final MagicTrigger KEIGA_THE_TIDE_STAR=new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Keiga, the Tide Star") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,MagicTargetChoice.TARGET_CREATURE,MagicExileTargetPicker.getInstance(),
					new Object[]{player},this,"Gain control of target creature$.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicGainControlAction((MagicPlayer)data[0],creature));
			}
		}
    };

    private static final MagicTrigger KITCHEN_FINKS=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Kitchen Finks") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You gain 2 life.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],2));
		}
    };
    
    private static final MagicTrigger KOKUSHO_THE_EVENING_STAR=new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Kokusho, the Evening Star") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
		
			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,"Your opponent loses 5 life. You gain 5 life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicChangeLifeAction(game.getOpponent(player),-5));
			game.doAction(new MagicChangeLifeAction(player,5));
		}
    };

    private static final MagicTrigger LAVABORN_MUSE=new MagicTrigger(MagicTriggerType.AtUpkeep,"Lavaborn Muse") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=(MagicPlayer)data;
			if (permanent.getController()!=player&&player.getHandSize()<3) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent,player},this,
					"Lavaborn Muse deals 3 damage to your opponent.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicDamage damage=new MagicDamage((MagicSource)data[0],(MagicTarget)data[1],3,false);
			game.doAction(new MagicDealDamageAction(damage));
		}
    };

    private static final MagicTrigger LIGHTNING_REAVER1=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Lightning Reaver") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,"Put a charge counter on Lightning Reaver.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.Charge,1,true));
		}
    };

    private static final MagicTrigger LIGHTNING_REAVER2=new MagicTrigger(MagicTriggerType.AtEndOfTurn,"Lightning Reaver") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			if (player==data) {
				final int counters=permanent.getCounters(MagicCounterType.Charge);
				if (counters>0) {
					return new MagicEvent(permanent,player,new Object[]{permanent,game.getOpponent(player)},this,
						"Lightning Reaver deals damage equal to the number of charge counters on it to your opponent.");
				}
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			final int counters=permanent.getCounters(MagicCounterType.Charge);
			final MagicDamage damage=new MagicDamage(permanent,(MagicTarget)data[1],counters,false);
			game.doAction(new MagicDealDamageAction(damage));
		}		
    };
    
    private static final MagicTrigger LILIANAS_CARESS=new MagicTrigger(MagicTriggerType.WhenDiscarded,"Liliana's Caress") {

    	@Override
    	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
    		final MagicPlayer otherController = ((MagicCard)data).getOwner();
    		final MagicPlayer player = permanent.getController();
    		if (otherController != player) {		
    			return new MagicEvent(permanent,player,new Object[]{otherController},this,"Your opponent loses 2 life.");
    		}
    		return null;
    	}

    	@Override
    	public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
    		game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],-2));
    	}
    };
    	    
    private static final MagicTrigger LILIANAS_SPECTER=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Liliana's Specter") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,"Your opponent discards a card.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			game.addEvent(new MagicDiscardEvent(permanent,game.getOpponent(permanent.getController()),1,false));
		}		
    };
    
    private static final MagicTrigger LONE_MISSIONARY=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Lone Missionary") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You gain 4 life.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],4));
		}
    };
    
    private static final MagicTrigger LORD_OF_SHATTERSKULL_PASS=new MagicTrigger(MagicTriggerType.WhenAttacks,"Lord of Shatterskull Pass") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent==data&&permanent.getCounters(MagicCounterType.Charge)>=6) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{permanent,game.getOpponent(player)},this,
					"Lord of Shatterskull Pass deals 6 damage to each creature defending player controls.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicSource source=(MagicSource)data[0];
			final MagicPlayer defendingPlayer=(MagicPlayer)data[1];
			final Collection<MagicTarget> creatures=game.filterTargets(defendingPlayer,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget creature : creatures) {
				
				final MagicDamage damage=new MagicDamage(source,creature,6,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
    
    private static final MagicTrigger LOXODON_HIERARCH=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Loxodon Hierarch") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You gain 4 life.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],4));
		}
    };

    private static final MagicTrigger MAN_O_WAR=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Man-o'-War") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_CREATURE,MagicBounceTargetPicker.getInstance(),
				MagicEvent.NO_DATA,this,"Return target creature$ to its owner's hand.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.OwnersHand));
			}
		}
    };
    
	private static final MagicTrigger MASSACRE_WURM1=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Massacre Wurm") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{game.getOpponent(player)},this,
				"Creatures your opponent controls get -2/-2 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				
				game.doAction(new MagicChangeTurnPTAction((MagicPermanent)target,-2,-2));
			}
		}
    };

    private static final MagicTrigger MASSACRE_WURM2=new MagicTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay,"Massacre Wurm") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicPermanent otherPermanent=(MagicPermanent)data;
			final MagicPlayer otherController=otherPermanent.getController();
			if (otherController!=player&&otherPermanent.isCreature()) {			
				return new MagicEvent(permanent,player,new Object[]{otherController},this,"Your opponent loses 2 life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],-2));
		}
    };

    private static final MagicTrigger MEGLONOTH=new MagicTrigger(MagicTriggerType.WhenBlocks,"Meglonoth") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent==data) {
				final MagicPermanent blocked=permanent.getBlockedCreature();
				if (blocked!=null) {
					return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent,blocked.getController()},this,
						"Megnoloth deals damage to the blocked creature's controller equal to Megnoloth's power.");
				}
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicDamage damage=new MagicDamage(permanent,(MagicTarget)data[1],permanent.getPower(game),false);
			game.doAction(new MagicDealDamageAction(damage));
		}
    };

    private static final MagicTrigger MESSENGER_FALCONS=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Messenger Falcons") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicDrawEvent(permanent,permanent.getController(),1);
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

		}
    };

    private static final MagicTrigger MIDNIGHT_BANSHEE=new MagicTrigger(MagicTriggerType.AtUpkeep,"Midnight Banshee") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,new Object[]{player},this,"Put a -1/-1 counter on each nonblack creature.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_NONBLACK_CREATURE);
			for (final MagicTarget target : targets) {
				
				game.doAction(new MagicChangeCountersAction((MagicPermanent)target,MagicCounterType.MinusOne,1,true));			
			}
		}
    };

    private static final MagicTrigger MIRRI_THE_CURSED=new MagicVeteranTrigger("Mirri the Cursed",true);

    private static final MagicTrigger MNEMONIC_WALL=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Mnemonic Wall") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),
                    
	new MagicMayChoice(
			"You may return target instant or sorcery card from your graveyard to your hand.",MagicTargetChoice.TARGET_INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD),
    MagicGraveyardTargetPicker.getInstance(),
				MagicEvent.NO_DATA,this,"You may$ return target instant or sorcery card$ from your graveyard to your hand.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicCard card=event.getTarget(game,choiceResults,1);		
				if (card!=null) {
					game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
					game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
				}
			}
		}
    };
    
    private static final MagicTrigger MORDANT_DRAGON=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Mordant Dragon") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final int amount=damage.getAmount();
				return new MagicEvent(permanent,permanent.getController(),
	new MagicMayChoice(
			"You may have Mordant Dragon deal that much damage to target creature.",MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS),
    new MagicDamageTargetPicker(amount),
					new Object[]{permanent,amount},this,"You may$ have Mordant Dragon deal "+amount+" damage to target creature$ your opponent controls.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicPermanent creature=event.getTarget(game,choiceResults,1);
				if (creature!=null) {
					final MagicDamage damage=new MagicDamage((MagicPermanent)data[0],creature,(Integer)data[1],false);
					game.doAction(new MagicDealDamageAction(damage));
				}
			}
		}
    };
    
    private static final MagicTrigger MOROII=new MagicTrigger(MagicTriggerType.AtUpkeep,"Moroii") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,new Object[]{player},this,"You lose 1 life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],-1));
		}
    };

    private static final MagicTrigger MURDEROUS_REDCAP=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Murderous Redcap") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final int power=permanent.getPower(game);
			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(power),
				new Object[]{permanent},this,"Murderous Redcap deals damage equal to its power to target creature or player$.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage(permanent,target,permanent.getPower(game),false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
    
    private static final MagicTrigger MYCOID_SHEPHERD1=new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Mycoid Shepherd") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
		
			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,"You gain 5 life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],5));
		}
    };

    private static final MagicTrigger MYCOID_SHEPHERD2=new MagicTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay,"Mycoid Shepherd") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicPermanent otherPermanent=(MagicPermanent)data;
			if (otherPermanent!=permanent&&otherPermanent.getController()==player&&otherPermanent.isCreature()&&otherPermanent.getPower(game)>=5) {			
				return new MagicEvent(permanent,player,new Object[]{player},this,"You gain 5 life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],5));
		}
    };
    
    private static final MagicTrigger MURKFIEND_LIEGE=new MagicTrigger(MagicTriggerType.AtUpkeep,"Murkfiend Liege") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			if (player!=data) {
				return new MagicEvent(permanent,player,new Object[]{player},this,"Untap all green and/or blue creatures you control.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				
				final MagicPermanent creature=(MagicPermanent)target;
				final int colorFlags=creature.getColorFlags();
				if (creature.isTapped()&&(MagicColor.Blue.hasColor(colorFlags)||MagicColor.Green.hasColor(colorFlags))) {
					game.doAction(new MagicUntapAction(creature));
				}
			}
		}

		@Override
		public boolean usesStack() {

			return false;
		}
    };
    
    private static final MagicTrigger MYSTIC_SNAKE=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Mystic Snake") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_SPELL,MagicEvent.NO_DATA,this,"Counter target spell$.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicCardOnStack targetSpell=event.getTarget(game,choiceResults,0);
			if (targetSpell!=null) {
				game.doAction(new MagicCounterItemOnStackAction(targetSpell));
			}
		}
    };
        
    private static final MagicTrigger NEMESIS_OF_REASON=new MagicTrigger(MagicTriggerType.WhenAttacks,"Nemesis of Reason") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{game.getOpponent(player)},this,
					"Defending player puts the top ten cards of his or her library into his or her graveyard.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicMillLibraryAction((MagicPlayer)data[0],10));
		}
    };

    private static final MagicTrigger NEUROK_INVISIMANCER=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Neurok Invisimancer") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_CREATURE,MagicUnblockableTargetPicker.getInstance(),
				MagicEvent.NO_DATA,this,"Target creature$ is unblockable this turn.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Unblockable));
			}
		}
    };
    
    private static final MagicTrigger NOVABLAST_WURM=new MagicTrigger(MagicTriggerType.WhenAttacks,"Novablast Wurm") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			if (permanent==data) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,"Destroy all creatures other than Novablast Wurm.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			final Collection<MagicTarget> targets=game.filterTargets(permanent.getController(),MagicTargetFilter.TARGET_CREATURE);
			for (final MagicTarget target : targets) {
				
				if (target!=permanent) {
					game.doAction(new MagicDestroyAction((MagicPermanent)target));
				}
			}
		}
    };

	private static final MagicTrigger NULLTREAD_GARGANTUAN=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Nulltread Gargantuan") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.CREATURE_YOU_CONTROL,
				MagicBounceTargetPicker.getInstance(),MagicEvent.NO_DATA,this,"Put a creature you control$ on top of its owner's library.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.TopOfOwnersLibrary));
			}
		}
    };
    
    private static final MagicTrigger OROS_THE_AVENGER=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Oros, the Avenger") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();		
				return new MagicEvent(permanent,player,
	new MagicMayChoice("You may pay {2}{W}.",new MagicPayManaCostChoice(MagicManaCost.TWO_WHITE)),
    new Object[]{player,permanent},this,
					"You may$ pay {2}{W}$. If you do, Oros deals 3 damage to each nonwhite creature.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicPermanent permanent=(MagicPermanent)data[1];
				final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_NONWHITE_CREATURE);
				for (final MagicTarget target : targets) {
					final MagicDamage damage=new MagicDamage(permanent,target,3,false);
					game.doAction(new MagicDealDamageAction(damage));
				}
			}
		}
    };

    private static final MagicTrigger PELAKKA_WURM1=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Pelakka Wurm") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You gain 7 life.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],7));
		}
    };
    
    private static final MagicTrigger PELAKKA_WURM2=new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Pelakka Wurm") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				return new MagicDrawEvent(permanent,permanent.getController(),1);
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

		}
    };
    
    private static final MagicTrigger PERILOUS_MYR=new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Perilous Myr") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(2),
					new Object[]{permanent},this,"Perilous Myr deals 2 damage to target creature or player$.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicPermanent)data[0],target,2,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
    
    private static final MagicTrigger PERIMETER_CAPTAIN=new MagicTrigger(MagicTriggerType.WhenBlocks,"Perimeter Captain") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPermanent creature=(MagicPermanent)data;
			final MagicPlayer player=permanent.getController();
			if (creature.getController()==player&&creature.hasAbility(game,MagicAbility.Defender)) {
				return new MagicEvent(permanent,player,new Object[]{player},this,"You gain 2 life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],2));
		}
    };
    
    private static final MagicTrigger PHYREXIAN_RAGER=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Phyrexian Rager") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You draw a card and you lose 1 life.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicDrawAction(player,1));
			game.doAction(new MagicChangeLifeAction(player,-1));
		}
    };
    
    private static final MagicTrigger PHYREXIAN_VATMOTHER=new MagicTrigger(MagicTriggerType.AtUpkeep,"Phyrexian Vatmother") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,new Object[]{player},this,"You get a poison counter.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangePoisonAction((MagicPlayer)data[0],1));
		}
    };
    
    private static final MagicTrigger PIERCE_STRIDER=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Pierce Strider") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_OPPONENT,
				MagicEvent.NO_DATA,this,"Target opponent$ loses 3 life.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				game.doAction(new MagicChangeLifeAction(player,-3));
			}
		}		
    };
    
    private static final MagicTrigger PREDATOR_DRAGON=new MagicDevourTrigger("Predator Dragon",2);
    
    private static final MagicTrigger PUPPETEER_CLIQUE=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Puppeteer Clique") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,MagicTargetChoice.TARGET_CREATURE_CARD_FROM_OPPONENTS_GRAVEYARD,MagicGraveyardTargetPicker.getInstance(),
				new Object[]{player},this,
				"Put target creature card$ in an opponent's graveyard onto the battlefield under your control. "+
				"It has haste. At the end of your turn, exile it.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicCard card=(MagicCard)event.getTarget(game,choiceResults,0);
			if (card!=null) {
				game.doAction(new MagicReanimateAction((MagicPlayer)data[0],card,MagicPlayCardAction.HASTE_REMOVE_AT_END_OF_YOUR_TURN));
			}
		}
    };
        
    private static final MagicTrigger RAFIQ_OF_THE_MANY=new MagicTrigger(MagicTriggerType.WhenAttacks,"Rafiq of the Many") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPermanent creature=(MagicPermanent)data;
			final MagicPlayer player=permanent.getController();
			if (creature.getController()==player&&player.getNrOfAttackers()==1) {
				return new MagicEvent(permanent,player,new Object[]{creature},this,creature.getName()+" gains double strike until end of turn.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicSetAbilityAction((MagicPermanent)data[0],MagicAbility.DoubleStrike));
		}
    };

    private static final MagicTrigger RAMPAGING_BALOTHS=new MagicTrigger(MagicTriggerType.WhenOtherComesIntoPlay,"Rampaging Baloths") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicPermanent played=(MagicPermanent)data;
			if (player==played.getController()&&played.isLand()) {
				return new MagicEvent(permanent,player,new Object[]{player},this,"Put a 4/4 green Beast creature token onto the battlefield.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.BEAST4_TOKEN_CARD));
		}		
    };
    
    private static final MagicTrigger RETALIATOR_GRIFFIN=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Retaliator Griffin") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			final int amount=damage.getDealtAmount();
			final MagicPlayer player=permanent.getController();
			if (amount>0&&damage.getTarget()==player&&damage.getSource().getController()!=player) {
				return new MagicEvent(permanent,player,new Object[]{permanent,amount},this,"Put "+amount+" +1/+1 counters on Retaliator Griffin.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.PlusOne,(Integer)data[1],true));
		}		
    };

    private static final MagicTrigger RITH_THE_AWAKENER=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Rith, the Awakener") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,
	new MagicMayChoice(
			"You may pay {2}{G}.",new MagicPayManaCostChoice(MagicManaCost.TWO_GREEN),MagicColorChoice.MOST_INSTANCE),
    new Object[]{player},this,
					"You may$ pay {2}{G}$. If you do, choose a color$. "+
					"Put a 1/1 green Saproling creature token onto the battlefield for each permanent of that color.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicPlayer player=(MagicPlayer)data[0];
				final MagicColor color=(MagicColor)choiceResults[2];
				final Collection<MagicTarget> targets=game.filterTargets(player,MagicTargetFilter.TARGET_PERMANENT);
				for (final MagicTarget target : targets) {
					
					final MagicPermanent permanent=(MagicPermanent)target;
					if (color.hasColor(permanent.getColorFlags())) {
						game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.SAPROLING_TOKEN_CARD));
					}
				}
			}
		}
    };
    
    private static final MagicTrigger SANGROMANCER1=new MagicTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay,"Sangromancer") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player = permanent.getController();
			final MagicPermanent otherPermanent = (MagicPermanent)data;
			final MagicPlayer otherController = otherPermanent.getController();
			if (otherController != player && otherPermanent.isCreature()) {			
				return new MagicEvent(permanent,player,new Object[]{player},this,"You gain 3 life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],3));
		}
    };
    
    private static final MagicTrigger SANGROMANCER2=new MagicTrigger(MagicTriggerType.WhenDiscarded,"Sangromancer") {

    	@Override
    	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
    		final MagicPlayer otherController = ((MagicCard)data).getOwner();
    		final MagicPlayer player = permanent.getController();
    		if (otherController != player) {		
    			return new MagicEvent(permanent,player,new Object[]{player},this,"You gain 3 life.");
    		}
    		return null;
    	}

    	@Override
    	public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
    		game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],3));
    	}
    };
    
    private static final MagicTrigger SEPTIC_RATS=new MagicTrigger(MagicTriggerType.WhenAttacks,"Septic Rats") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			if (permanent==data) {
				final MagicPlayer player=permanent.getController();
				if (game.getOpponent(player).getPoison()>0) {
					return new MagicEvent(permanent,player,new Object[]{permanent},this,"Septic Rats gets +1/+1 until end of turn.");
				}
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],1,1));
		}
    };
    
    private static final MagicTrigger SERENDIB_EFREET=new MagicTrigger(MagicTriggerType.AtUpkeep,"Serendib Efreet") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,new Object[]{permanent,player},this,"Serendib Efreet deals 1 damage to you.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicDamage damage=new MagicDamage((MagicSource)data[0],(MagicTarget)data[1],1,false);
			game.doAction(new MagicDealDamageAction(damage));
		}
    };
    
    private static final MagicTrigger SIEGE_GANG_COMMANDER=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Siege-Gang Commander") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You put three 1/1 red Goblin creature tokens onto the battlefield.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			for (int count=3;count>0;count--) {
				
				game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.GOBLIN1_TOKEN_CARD));
			}
		}		
    };

    private static final MagicTrigger SKELETAL_VAMPIRE=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Skeletal Vampire") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You put two 1/1 black Bat creature tokens with flying onto the battlefield.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.BAT_TOKEN_CARD));			
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.BAT_TOKEN_CARD));
		}		
    };
        
    private static final MagicTrigger SHADOWMAGE_INFILTRATOR=new MagicThiefTrigger("Shadowmage Infiltrator",true,1);
    
    private static final MagicTrigger SOULS_OF_THE_FAULTLESS=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Souls of the Faultless") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getTarget()==permanent&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				final MagicPlayer opponent=damage.getSource().getController();
				final int amount=damage.getDealtAmount();
				return new MagicEvent(permanent,player,new Object[]{player,opponent,amount},this,
					"You gain "+amount+" life and attacking player loses "+amount+" life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final int life=(Integer)data[2];
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],life));
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[1],-life));
		}
    };

    private static final MagicTrigger SPITEMARE=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Spitemare") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getTarget()==permanent) {
				final int amount=damage.getDealtAmount();
				return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(amount),
					new Object[]{permanent,amount},this,"Spitemare deals "+amount+" damage to target creature or player$.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,(Integer)data[1],false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
    
    private static final MagicTrigger STEPPE_LYNX=new MagicTrigger(MagicTriggerType.WhenOtherComesIntoPlay,"Steppe Lynx") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicPermanent played=(MagicPermanent)data;
			if (player==played.getController()&&played.isLand()) {
				return new MagicEvent(permanent,player,new Object[]{permanent},this,"Steppe Lynx gets +2/+2 until end of turn.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],2,2));
		}		
    };
    
    private static final MagicTrigger STUFFY_DOLL=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Stuffy Doll") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getTarget()==permanent) {
				final MagicPlayer player=permanent.getController();
				final int amount=damage.getDealtAmount();
				return new MagicEvent(permanent,player,new Object[]{permanent,game.getOpponent(player),amount},this,
					"Stuffy Doll deals "+amount+" damage to your opponent.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicDamage damage=new MagicDamage((MagicSource)data[0],(MagicTarget)data[1],(Integer)data[2],false);
			game.doAction(new MagicDealDamageAction(damage));
		}
    };
    
	private static final MagicTrigger SUNBLAST_ANGEL=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Sunblast Angel") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"Destroy all tapped creatures.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_TAPPED_CREATURE);
			for (final MagicTarget target : targets) {

				game.doAction(new MagicDestroyAction((MagicPermanent)target));
			}
		}
    };
    
    private static final MagicTrigger SUTURE_PRIEST=new MagicTrigger(
            MagicTriggerType.WhenOtherComesIntoPlay,
            "Suture Priest") {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPermanent otherPermanent=(MagicPermanent)data;
			if (otherPermanent!=permanent&&otherPermanent.isCreature()) {
				final MagicPlayer player=permanent.getController();
				final MagicPlayer controller=otherPermanent.getController();
				final boolean same=controller==player;
				return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{controller,same?1:-1},
                        this,
                        controller.getName()+(same?" gains 1 life.":" loses 1 life."));
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,
                final Object data[],final Object[] choiceResults) {
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],(Integer)data[1]));
		}		
    };
    
    private static final MagicTrigger RUMBLING_SLUM=new MagicTrigger(MagicTriggerType.AtUpkeep,"Rumbling Slum") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=(MagicPlayer)data;
			if (permanent.getController()==player) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,"Rumbling Slum deals 1 damage to each player.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicSource source=(MagicSource)data[0];
			for (final MagicPlayer player : game.getPlayers()) {
			
				final MagicDamage damage=new MagicDamage(source,player,1,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
    
    private static final MagicTrigger SKINRENDER=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Skinrender") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_CREATURE,new MagicWeakenTargetPicker(-3,-3),
				MagicEvent.NO_DATA,this,"Put three -1/-1 counters on target creature$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.MinusOne,3,true));
			}
		}
    };
    
    private static final MagicTrigger SHEOLDRED_WHISPERING_ONE1=new MagicTrigger(MagicTriggerType.AtUpkeep,"Sheoldred, Whispering One") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,MagicGraveyardTargetPicker.getInstance(),
					new Object[]{player},this,"Return target creature card$ from your graveyard to the battlefield.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicCard card=event.getTarget(game,choiceResults,0);
			if (card!=null) {
				game.doAction(new MagicReanimateAction((MagicPlayer)data[0],card,MagicPlayCardAction.NONE));
			}
		}
    };
    
    private static final MagicTrigger SHEOLDRED_WHISPERING_ONE2=new MagicTrigger(MagicTriggerType.AtUpkeep,"Sheoldred, Whispering One") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			if (player!=data) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent,data},this,"Your opponent sacrifices a creature.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer opponent=(MagicPlayer)data[1];
			if (opponent.controlsPermanentWithType(MagicType.Creature)) {
				game.addEvent(new MagicSacrificePermanentEvent((MagicPermanent)data[0],opponent,MagicTargetChoice.SACRIFICE_CREATURE));
			}
		}
    };
    
	private static final MagicTrigger SHIVAN_WURM=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Shivan Wurm") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.RED_OR_GREEN_CREATURE_YOU_CONTROL,
				MagicBounceTargetPicker.getInstance(),MagicEvent.NO_DATA,this,"Return a red or green creature you control$ to its owner's hand.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.OwnersHand));
			}
		}
    };

    private static final MagicTrigger SPHINX_OF_LOST_TRUTHS=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Sphinx of Lost Truths") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			final boolean kicked=permanent.hasState(MagicPermanentState.Kicked);
			return new MagicEvent(permanent,player,new Object[]{player,permanent,kicked},this,
				kicked?"You draw three cards.":"You draw three cards. Then you discard three cards.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicDrawAction(player,3));
			final boolean kicked=(Boolean)data[2];
			if (!kicked) {
				game.addEvent(new MagicDiscardEvent((MagicPermanent)data[1],player,3,false));
			}
		}		
    };
    
    private static final MagicTrigger SPIRITMONGER=new MagicVeteranTrigger("Spiritmonger",false);

    private static final MagicTrigger SPROUTING_THRINAX=new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Sprouting Thrinax") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
		
			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,"You put three 1/1 green Saproling creature tokens onto the battlefield.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			final MagicPlayer player=(MagicPlayer)data[0];
			for (int count=3;count>0;count--) {
				
				game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.SAPROLING_TOKEN_CARD));
			}
		}
    };
    
    private static final MagicTrigger STONEBROW_KROSAN_HERO=new MagicTrigger(MagicTriggerType.WhenAttacks,"Stonebrow, Krosan Hero") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicPermanent creature=(MagicPermanent)data;
			if (creature.getController()==player&&creature.hasAbility(game,MagicAbility.Trample)) {
				return new MagicEvent(permanent,player,new Object[]{creature},this,creature.getName()+" gets +2/+2 until end of turn.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],2,2));
		}
    };
    
    private static final MagicTrigger SURVEILLING_SPRITE=new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Surveilling Sprite") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				return new MagicDrawEvent(permanent,permanent.getController(),1);
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

		}
    };

    private static final MagicTrigger SZADEK_LORD_OF_SECRETS=new MagicTrigger(MagicTriggerType.IfDamageWouldBeDealt,"Szadek, Lord of Secrets",6) {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			final int amount=damage.getAmount();
			if (amount>0&&damage.isCombat()&&permanent==damage.getSource()&&damage.getTarget().isPlayer()) {
				// Replacement effect.
				damage.setAmount(0);
				game.doAction(new MagicChangeCountersAction(permanent,MagicCounterType.PlusOne,amount,true));
				game.doAction(new MagicMillLibraryAction((MagicPlayer)damage.getTarget(),amount));
			}			
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
		}
    };
    
    private static final MagicTrigger TAUREAN_MAULER=new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed,"Taurean Mauler") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data;
			if (cardOnStack.getController()!=player) {
				return new MagicEvent(permanent,player,new Object[]{permanent},this,"Put a +1/+1 counter on Taurean Mauler.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.PlusOne,1,true));
		}
    };
    
    private static final MagicTrigger TENEB_THE_HARVESTER=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Teneb, the Harvester") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,
	new MagicMayChoice(
			"You may pay {2}{B}.",new MagicPayManaCostChoice(MagicManaCost.TWO_BLACK),MagicTargetChoice.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS),
    new Object[]{player},this,
					"You may$ pay {2}{B}$. If you do, put target creature card$ in a graveyard into play under your control.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicCard card=event.getTarget(game,choiceResults,2);
				if (card!=null) {
					game.doAction(new MagicReanimateAction((MagicPlayer)data[0],card,MagicPlayCardAction.NONE));
				}				
			}
		}
    };
    
    private static final MagicTrigger THIEVING_MAGPIE=new MagicThiefTrigger("Thieving Magpie",false,1);

    private static final MagicTrigger THUNDER_DRAGON=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Thunder Dragon") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{permanent,player},this,"Thunder Dragon deals 3 damage to each creature without flying.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicSource source=(MagicSource)data[0];
			final Collection<MagicTarget> creatures=game.filterTargets((MagicPlayer)data[1],MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING);
			for (final MagicTarget creature : creatures) {
				
				final MagicDamage damage=new MagicDamage(source,creature,3,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
    
    private static final MagicTrigger TREVA_THE_RENEWER=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Treva, the Renewer") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,
	new MagicMayChoice(
			"You may pay {2}{W}.",new MagicPayManaCostChoice(MagicManaCost.TWO_WHITE),MagicColorChoice.MOST_INSTANCE),
    new Object[]{player},this,
					"You may$ pay {2}{W}$. If you do, choose a color$. You gain 1 life for each permanent of that color.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				int life=0;
				final MagicPlayer player=(MagicPlayer)data[0];
				final MagicColor color=(MagicColor)choiceResults[2];
				final Collection<MagicTarget> targets=game.filterTargets(player,MagicTargetFilter.TARGET_PERMANENT);
				for (final MagicTarget target : targets) {
					
					final MagicPermanent permanent=(MagicPermanent)target;
					if (color.hasColor(permanent.getColorFlags())) {
						life++;
					}
				}				
				if (life>0) {
					game.doAction(new MagicChangeLifeAction(player,life));
				}
			}
		}		
    };
    
    private static final MagicTrigger TRYGON_PREDATOR=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Trygon Predator") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				return new MagicEvent(permanent,permanent.getController(),
	new MagicMayChoice(
			"You may destroy target artifact or enchantment.",MagicTargetChoice.TARGET_ARTIFACT_OR_ENCHANTMENT_YOUR_OPPONENT_CONTROLS),
    new MagicDestroyTargetPicker(false),
					MagicEvent.NO_DATA,this,"You may$ destroy target artifact or enchantment$ your opponent controls.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicPermanent permanent=event.getTarget(game,choiceResults,1);
				if (permanent!=null) {
					game.doAction(new MagicDestroyAction(permanent));
				}
			}
		}
    };

    private static final MagicTrigger TUKTUK_THE_EXPLORER=new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Tuktuk the Explorer") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
		
			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,
					"You put a legendary 5/5 colorless Goblin Golem artifact creature token named Tuktuk the Returned onto the battlefield.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.TUKTUK_THE_RETURNED_TOKEN_CARD));
		}
    };

    private static final MagicTrigger URABRASK_THE_HIDDEN=new MagicTrigger(MagicTriggerType.WhenOtherComesIntoPlay,"Urabrask the Hidden") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPermanent otherPermanent=(MagicPermanent)data;
			if (otherPermanent.isCreature()&&otherPermanent.getController()!=permanent.getController()) {
				return new MagicTapEvent(otherPermanent);
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

		}

		@Override
		public boolean usesStack() {

			return false;
		}		
    };
    
    private static final MagicTrigger VENSER_SHAPER_SAVANT=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Venser, Shaper Savant") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.NEG_TARGET_SPELL_OR_PERMANENT,MagicBounceTargetPicker.getInstance(),
				MagicEvent.NO_DATA,this,"Return target spell or permanent$ to its owner's hand.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				if (target.isPermanent()) {
					game.doAction(new MagicRemoveFromPlayAction((MagicPermanent)target,MagicLocationType.OwnersHand));
				} else {
					final MagicCardOnStack cardOnStack=(MagicCardOnStack)target;
					game.doAction(new MagicRemoveItemFromStackAction(cardOnStack));
					game.doAction(new MagicMoveCardAction(cardOnStack.getCard(),MagicLocationType.Stack,MagicLocationType.OwnersHand));
				}
			}
		}
    };

    private static final MagicTrigger VICTORYS_HERALD=new MagicTrigger(MagicTriggerType.WhenAttacks,"Victory's Herald") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,"Attacking creatures gain flying and lifelink until end of turn.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_ATTACKING_CREATURE);
			for (final MagicTarget target : targets) {
				
				game.doAction(new MagicSetAbilityAction((MagicPermanent)target,MagicAbility.VICTORYS_HERALD_FLAGS));
			}
		}		
    };
    
    private static final MagicTrigger VIGOR1=new MagicTrigger(MagicTriggerType.IfDamageWouldBeDealt,"Vigor",4) {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicDamage damage=(MagicDamage)data;
			final MagicTarget target=damage.getTarget();
			final int amount=damage.getAmount();
			if (!damage.isUnpreventable()&&amount>0&&target!=permanent&&target.isPermanent()&&target.getController()==player) {
				final MagicPermanent creature=(MagicPermanent)target;
				if (creature.isCreature()) {
					// Prevention effect.
					damage.setAmount(0);
					return new MagicEvent(permanent,player,new Object[]{creature,amount},this,"Put "+amount+" +1/+1 counters on "+creature.getName()+".");
				}
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.PlusOne,(Integer)data[1],true));
		}
    };
    
    private static final MagicTrigger VIGOR2=new MagicFromGraveyardToLibraryTrigger("Vigor");

    private static final MagicTrigger VOROSH_THE_HUNTER=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Vorosh, the Hunter") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,
	new MagicMayChoice("You may pay {2}{G}.",new MagicPayManaCostChoice(MagicManaCost.TWO_GREEN)),
    new Object[]{permanent},this,
					"You may$ pay {2}{G}$. If you do, put six +1/+1 counters on Vorosh.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.PlusOne,6,true));
			}
		}
    };

    private static final MagicTrigger WALL_OF_FROST=new MagicTrigger(MagicTriggerType.WhenBlocks,"Wall of Frost") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent==data) {
				final MagicPermanent blocked=permanent.getBlockedCreature();
				if (blocked!=null) {
					return new MagicEvent(permanent,permanent.getController(),new Object[]{blocked},this,
						blocked.getName()+" doesn't untap during its controller's next untap step.");
				}
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
			game.doAction(new MagicChangeStateAction((MagicPermanent)data[0],MagicPermanentState.DoesNotUntap,true));
		}
    };

    private static final MagicTrigger WALL_OF_REVERENCE=new MagicTrigger(MagicTriggerType.AtEndOfTurn,"Wall of Reverence") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,MagicPowerTargetPicker.getInstance(),
					new Object[]{player},this,"You gain life equal to the power of target creature$ you control.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],creature.getPower(game)));
			}
		}
    };

    private static final MagicTrigger WORT_BOGGART_AUNTIE=new MagicTrigger(MagicTriggerType.AtUpkeep,"Wort, Boggart Auntie") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,
	new MagicMayChoice(
			"You may return target Goblin card from your graveyard to your hand.",MagicTargetChoice.TARGET_GOBLIN_CARD_FROM_GRAVEYARD),
    MagicGraveyardTargetPicker.getInstance(),MagicEvent.NO_DATA,this,
					"You may$ return target Goblin card$ from your graveyard to your hand.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicCard card=event.getTarget(game,choiceResults,1);		
				if (card!=null) {
					game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
					game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
				}
			}
		}
    };
    
    private static final MagicTrigger WREXIAL_THE_RISEN_DEEP=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Wrexial, the Risen Deep") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,
	new MagicMayChoice(
			"You may cast target instant or sorcery card from your opponent's graveyard.",
			MagicTargetChoice.TARGET_INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD),
    MagicGraveyardTargetPicker.getInstance(),new Object[]{player},this,
					"You may$ cast target instant or sorcery card$ from your opponent's graveyard without paying its mana cost. "+
					"If that card would be put into a graveyard this turn, exile it instead.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicCard card=event.getTarget(game,choiceResults,1);
				if (card!=null) {
					game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
					final MagicCardOnStack cardOnStack=new MagicCardOnStack(card,(MagicPlayer)data[0],MagicPayedCost.NO_COST);
					cardOnStack.setMoveLocation(MagicLocationType.Exile);
					game.doAction(new MagicPutItemOnStackAction(cardOnStack));
				}
			}
		}
    };

    private static final MagicTrigger WURMCOIL_ENGINE=new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Wurmcoil Engine") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
		
			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,
					"You put a 3/3 colorless Wurm artifact creature token with deathtouch and "+
					"a 3/3 colorless Wurm artifact creature token with lifelink onto the battlefield.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.WURM1_TOKEN_CARD));
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.WURM2_TOKEN_CARD));			
		}
    };
    
    private static final MagicTrigger FLAYER_HUSK=new MagicLivingWeaponTrigger("Flayer Husk");
    
    private static final MagicTrigger MAGE_SLAYER=new MagicTrigger(MagicTriggerType.WhenAttacks,"Mage Slayer",1) {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPermanent equippedCreature=permanent.getEquippedCreature();
			if (equippedCreature!=null&&equippedCreature==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{equippedCreature,game.getOpponent(player)},this,
						equippedCreature.getName()+" deals damage equal to its power to defending player.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicDamage damage=new MagicDamage(permanent,(MagicTarget)data[1],permanent.getPower(game),false);
			game.doAction(new MagicDealDamageAction(damage));
		}
    };
    
    private static final MagicTrigger MASK_OF_MEMORY=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Mask of Memory") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (permanent.getEquippedCreature()==damage.getSource()&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,
    new MagicSimpleMayChoice(
            "You may draw two cards.",MagicSimpleMayChoice.DRAW_CARDS,2),
    new Object[]{permanent,player},this,
					"You may$ draw two cards. If you do, discard a card.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicChoice.isYesChoice(choiceResults[0])) {
				final MagicPlayer player=(MagicPlayer)data[1];
				game.doAction(new MagicDrawAction(player,2));
				game.addEvent(new MagicDiscardEvent((MagicPermanent)data[0],player,1,false));
			}
		}
    };

    private static final MagicTrigger MASK_OF_RIDDLES=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Mask of Riddles") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (permanent.getEquippedCreature()==damage.getSource()&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				return new MagicDrawEvent(permanent,permanent.getController(),1);
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
		}
    };
        
    private static final MagicTrigger QUIETUS_SPIKE=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Quietus Spike") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			final MagicTarget target=damage.getTarget();
			if (permanent.getEquippedCreature()==damage.getSource()&&target.isPlayer()&&damage.isCombat()) {
				return new MagicEvent(permanent,(MagicPlayer)target,new Object[]{target},this,"You lose half your life, rounded up.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicChangeLifeAction(player,-(player.getLife()+1)/2));
		}
    };
    
    private static final MagicTrigger RONIN_WARCLUB=new MagicTrigger(MagicTriggerType.WhenOtherComesIntoPlay,"Ronin Warclub") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPermanent otherPermanent=(MagicPermanent)data;
			final MagicPlayer player=permanent.getController();
			if (otherPermanent.isCreature()&&otherPermanent.getController()==player) {
				return new MagicEvent(permanent,player,new Object[]{permanent,otherPermanent},this,"Attach Ronin Warclub to "+otherPermanent.getName()+'.');
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicAttachEquipmentAction((MagicPermanent)data[0],(MagicPermanent)data[1]));
		}
    };

    private static final MagicTrigger SICKLESLICER=new MagicLivingWeaponTrigger("Sickleslicer");
    
    private static final MagicTrigger SHIELD_OF_THE_RIGHTEOUS=new MagicTrigger(MagicTriggerType.WhenBlocks,"Shield of the Righteous") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPermanent equippedCreature=permanent.getEquippedCreature();
			if (equippedCreature==data&&equippedCreature!=null) {
				final MagicPermanent blocked=equippedCreature.getBlockedCreature();
				if (blocked!=null) {
					return new MagicEvent(permanent,permanent.getController(),new Object[]{blocked},this,
							blocked.getName()+" doesn't untap during its controller's next untap step.");
				}
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeStateAction((MagicPermanent)data[0],MagicPermanentState.DoesNotUntap,true));
		}
    };
    
    private static final MagicTrigger SKULLCLAMP=new MagicTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay,"Skullclamp") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent.getEquippedCreature()==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,"You draw two cards.");
			}			
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicDrawAction((MagicPlayer)data[0],2));
		}
    };

    private static final MagicTrigger SPECTERS_SHROUD=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Specter's Shroud") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			final MagicPermanent equippedCreature=permanent.getEquippedCreature();
			if (damage.getSource()==equippedCreature&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer opponent=(MagicPlayer)damage.getTarget();
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent,opponent},this,"Your opponent discards a card.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
			game.addEvent(new MagicDiscardEvent((MagicPermanent)data[0],(MagicPlayer)data[1],1,false));
		}
    };
    
    private static final MagicTrigger STRANDWALKER=new MagicLivingWeaponTrigger("Strandwalker");
    
    private static final MagicTrigger SWORD_OF_BODY_AND_MIND=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Sword of Body and Mind") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent.getEquippedCreature()&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				final MagicTarget targetPlayer=damage.getTarget();
				return new MagicEvent(permanent,player,new Object[]{player,targetPlayer},this,
					"You put a 2/2 green Wolf creature token onto the battlefield and "+targetPlayer.getName()+
					" puts the top ten cards of his or her library into his or her graveyard.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.WOLF_TOKEN_CARD));
			game.doAction(new MagicMillLibraryAction((MagicPlayer)data[1],10));
		}
    };
    
    private static final MagicTrigger SWORD_OF_FEAST_AND_FAMINE = new MagicTrigger(
            MagicTriggerType.WhenDamageIsDealt,
            "Sword of Feast and Famine") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent.getEquippedCreature() && damage.getTarget().isPlayer() && damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				final MagicPlayer damagedPlayer=(MagicPlayer)damage.getTarget();
				return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,player,damagedPlayer},
                        this,
                        damagedPlayer + " discards a card and you untap all lands you control.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.addEvent(new MagicDiscardEvent((MagicPermanent)data[0],(MagicPlayer)data[2],1,false));
			final Collection<MagicTarget> targets = 
                game.filterTargets((MagicPlayer)data[1],MagicTargetFilter.TARGET_LAND_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				game.doAction(new MagicUntapAction((MagicPermanent)target));
			}
		}
    };
    
    private static final MagicTrigger SWORD_OF_FIRE_AND_ICE=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Sword of Fire and Ice") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent.getEquippedCreature()&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(2),
					new Object[]{permanent,player},this,"Sword of Fire and Ice deals 2 damage to target creature or player$ and you draw a card.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,2,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			game.doAction(new MagicDrawAction((MagicPlayer)data[1],1));
		}
    };

    private static final MagicTrigger SWORD_OF_LIGHT_AND_SHADOW=new MagicTrigger(
            MagicTriggerType.WhenDamageIsDealt,
            "Sword of Light and Shadow") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent.getEquippedCreature()&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,
	new MagicMayChoice(
			"You may return target creature card from your graveyard to your hand.",
            MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD),
    MagicGraveyardTargetPicker.getInstance(),new Object[]{player},this,
					"You gain 3 life and you may$ return target creature card$ from your graveyard to your hand.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],3));
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicCard card=event.getTarget(game,choiceResults,1);
				if (card!=null) {
					game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard)); 
					game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
				}
			}
		}
    };
    
    private static final MagicTrigger SWORD_OF_WAR_AND_PEACE=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Sword of War and Peace") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent.getEquippedCreature()&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				final MagicTarget targetPlayer=damage.getTarget();
				return new MagicEvent(permanent,player,new Object[]{permanent,player,targetPlayer},this,
					"Sword of War and Peace deals damage to "+targetPlayer.getName()+" equal to the number of cards in his or her hand and "+
					"you gain 1 life for each card in your hand.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer targetPlayer=(MagicPlayer)data[2];
			final int amount1=targetPlayer.getHand().size();
			if (amount1>0) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],targetPlayer,amount1,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			final MagicPlayer player=(MagicPlayer)data[1];
			final int amount2=player.getHand().size();
			if (amount2>0) {
				game.doAction(new MagicChangeLifeAction(player,amount2));
			}
		}
    };
    
    private static final MagicTrigger SYLVOK_LIFESTAFF=new MagicTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay,"Sylvok Lifestaff") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent.getEquippedCreature()==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,"You gain 3 life.");
			}			
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],3));
		}
    };
    
    
    private static final MagicTrigger BLOOD_CRYPT=new MagicRavnicaLandTrigger("Blood Crypt");

    private static final MagicTrigger BREEDING_POOL=new MagicRavnicaLandTrigger("Breeding Pool");
    
    private static final MagicTrigger GODLESS_SHRINE=new MagicRavnicaLandTrigger("Godless Shrine");

    private static final MagicTrigger HALLOWED_FOUNTAIN=new MagicRavnicaLandTrigger("Hallowed Fountain");

    private static final MagicTrigger OVERGROWN_TOMB=new MagicRavnicaLandTrigger("Overgrown Tomb");
    
    private static final MagicTrigger SACRED_FOUNDRY=new MagicRavnicaLandTrigger("Sacred Foundry");

    private static final MagicTrigger STEAM_VENTS=new MagicRavnicaLandTrigger("Steam Vents");
    
    private static final MagicTrigger STOMPING_GROUND=new MagicRavnicaLandTrigger("Stomping Ground");
    
    private static final MagicTrigger TEMPLE_GARDEN=new MagicRavnicaLandTrigger("Temple Garden");

    private static final MagicTrigger WATERY_GRAVE=new MagicRavnicaLandTrigger("Watery Grave");

    private static final MagicTrigger ARCANE_SANCTUM=new MagicTappedIntoPlayTrigger("Arcane Sanctum");
    
    private static final MagicTrigger CELESTIAL_COLONNADE=new MagicTappedIntoPlayTrigger("Celestial Colonnade");

    private static final MagicTrigger CRUMBLING_NECROPOLIS=new MagicTappedIntoPlayTrigger("Crumbling Necropolis");
    
    private static final MagicTrigger STIRRING_WILDWOOD=new MagicTappedIntoPlayTrigger("Stirring Wildwood");

    private static final MagicTrigger JUNGLE_SHRINE=new MagicTappedIntoPlayTrigger("Jungle Shrine");

    private static final MagicTrigger CREEPING_TAR_PIT=new MagicTappedIntoPlayTrigger("Creeping Tar Pit");

    private static final MagicTrigger AKOUM_REFUGE1=new MagicTappedIntoPlayTrigger("Akoum Refuge");
	private static final MagicTrigger AKOUM_REFUGE2=new MagicRefugeLandTrigger("Akoum Refuge");

    private static final MagicTrigger GRAYPELT_REFUGE1=new MagicTappedIntoPlayTrigger("Graypelt Refuge");
	private static final MagicTrigger GRAYPELT_REFUGE2=new MagicRefugeLandTrigger("Graypelt Refuge");

    private static final MagicTrigger JWAR_ISLE_REFUGE1=new MagicTappedIntoPlayTrigger("Jwar Isle Refuge");
	private static final MagicTrigger JWAR_ISLE_REFUGE2=new MagicRefugeLandTrigger("Jwar Isle Refuge");

    private static final MagicTrigger KAZANDU_REFUGE1=new MagicTappedIntoPlayTrigger("Kazandu Refuge");
	private static final MagicTrigger KAZANDU_REFUGE2=new MagicRefugeLandTrigger("Kazandu Refuge");

    private static final MagicTrigger SEJIRI_REFUGE1=new MagicTappedIntoPlayTrigger("Sejiri Refuge");
	private static final MagicTrigger SEJIRI_REFUGE2=new MagicRefugeLandTrigger("Sejiri Refuge");

    private static final MagicTrigger SAVAGE_LANDS=new MagicTappedIntoPlayTrigger("Savage Lands");
    
    private static final MagicTrigger SEASIDE_CITADEL=new MagicTappedIntoPlayTrigger("Seaside Citadel");
            
    private static final MagicTrigger RUPTURE_SPIRE1=new MagicTappedIntoPlayTrigger("Rupture Spire");
     
    private static final MagicTrigger RUPTURE_SPIRE2=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Rupture Spire") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),
	new MagicMayChoice("You may pay {1}.",new MagicPayManaCostChoice(MagicManaCost.ONE)),
    new Object[]{permanent},this,
				"You may$ pay {1}. If you don't, sacrifice Rupture Spire.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isNoChoice(choiceResults[0])) {
				game.doAction(new MagicSacrificeAction((MagicPermanent)data[0]));
			}
		}
    };
    
    private static final MagicTrigger VIVID_CRAG=new MagicVividLandTrigger("Vivid Crag");
  
    private static final MagicTrigger VIVID_CREEK=new MagicVividLandTrigger("Vivid Creek"); 
      
    private static final MagicTrigger VIVID_GROVE=new MagicVividLandTrigger("Vivid Grove");
  
    private static final MagicTrigger VIVID_MARSH=new MagicVividLandTrigger("Vivid Marsh");
  
    private static final MagicTrigger VIVID_MEADOW=new MagicVividLandTrigger("Vivid Meadow");

    private static final MagicTrigger DRAGONSKULL_SUMMIT=new MagicTappedIntoPlayUnlessTrigger("Dragonskull Summit",MagicSubType.Swamp,MagicSubType.Mountain);

    private static final MagicTrigger DROWNED_CATACOMB=new MagicTappedIntoPlayUnlessTrigger("Drowned Catacomb",MagicSubType.Island,MagicSubType.Swamp);

    private static final MagicTrigger GLACIAL_FORTRESS=new MagicTappedIntoPlayUnlessTrigger("Glacial Fortress",MagicSubType.Plains,MagicSubType.Island);

    private static final MagicTrigger ROOTBOUND_CRAG=new MagicTappedIntoPlayUnlessTrigger("Rootbound Crag",MagicSubType.Mountain,MagicSubType.Forest);

    private static final MagicTrigger SUNPETAL_GROVE=new MagicTappedIntoPlayUnlessTrigger("Sunpetal Grove",MagicSubType.Forest,MagicSubType.Plains);

    private static final MagicTrigger BLACKCLEAVE_CLIFFS=new MagicTappedIntoPlayUnlessTwoTrigger("Blackcleave Cliffs");

    private static final MagicTrigger COPPERLINE_GORGE=new MagicTappedIntoPlayUnlessTwoTrigger("Copperline Gorge");

    private static final MagicTrigger DARKSLICK_SHORES=new MagicTappedIntoPlayUnlessTwoTrigger("Darkslick Shores");

    private static final MagicTrigger RAZORVERGE_THICKET=new MagicTappedIntoPlayUnlessTwoTrigger("Razorverge Thicket");

    private static final MagicTrigger SEACHROME_COAST=new MagicTappedIntoPlayUnlessTwoTrigger("Seachrome Coast");
        
    private static final MagicTrigger RAGING_RAVINE=new MagicTappedIntoPlayTrigger("Raging Ravine");

    private static final MagicTrigger AWAKENING_ZONE=new MagicTrigger(MagicTriggerType.AtUpkeep,"Awakening Zone") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,new Object[]{player},this,
					"You put a 0/1 colorless Eldrazi Spawn creature token onto the battlefield. "+
					"It has \"Sacrifice this creature: Add {1} to your mana pool.\"");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.ELDRAZI_SPAWN_TOKEN_CARD));
		}		
    };
    
    private static final MagicTrigger BITTERBLOSSOM=new MagicTrigger(MagicTriggerType.AtUpkeep,"Bitterblossom") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,new Object[]{player},this,
					"You lose 1 life and put a 1/1 black Faerie Rogue creature token with flying onto the battlefield.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicChangeLifeAction(player,-1));
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.FAERIE_ROGUE_TOKEN_CARD));
		}		
    };
    
    private static final MagicTrigger DEBTORS_KNELL=new MagicTrigger(MagicTriggerType.AtUpkeep,"Debtors' Knell") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,MagicTargetChoice.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS,MagicGraveyardTargetPicker.getInstance(),
					new Object[]{player},this,"Put target creature card$ in a graveyard onto the battlefield under your control.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicCard card=event.getTarget(game,choiceResults,0);
			if (card!=null) {
				game.doAction(new MagicReanimateAction((MagicPlayer)data[0],card,MagicPlayCardAction.NONE));
			}
		}
    };
    
    private static final MagicTrigger DISSIPATION_FIELD=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Dissipation Field") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			final MagicDamage damage=(MagicDamage)data;
			final MagicSource source=damage.getSource();
			if (damage.getTarget()==player&&source.isPermanent()) {
				return new MagicEvent(permanent,player,new Object[]{source},this,"Return "+source.getName()+" to its owner's hand.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			game.doAction(new MagicRemoveFromPlayAction((MagicPermanent)data[0],MagicLocationType.OwnersHand));
		}
    };
        
    private static final MagicTrigger GRATUITOUS_VIOLENCE=new MagicTrigger(MagicTriggerType.IfDamageWouldBeDealt,"Gratuitous Violence",3) {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			final MagicSource source=damage.getSource();
			if (source.getController()==permanent.getController()&&source.isPermanent()) {
				final MagicPermanent sourcePermanent=(MagicPermanent)source;
				if (sourcePermanent.isCreature()) {
					// Generates no event or action.
					damage.setAmount(damage.getAmount()<<1);
				}
			}			
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
		}
    };
    
	
    private static final MagicTrigger HALCYON_GLAZE=new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed,"Halcyon Glaze") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data;
			if (cardOnStack.getController()==player&&cardOnStack.getCardDefinition().isCreature()) {
				return new MagicEvent(permanent,player,new Object[]{permanent},this,
					"Halcyon Glaze becomes a 4/4 Illusion creature with flying until end of turn. It's still an enchantment.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicBecomesCreatureAction((MagicPermanent)data[0],
	new MagicDummyLocalVariable() {
		
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.power=4;
			pt.toughness=4;
		}

		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return flags|MagicAbility.Flying.getMask();
		}

		@Override
		public EnumSet<MagicSubType> getSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags) {
            final EnumSet<MagicSubType> mod = flags.clone();
            mod.add(MagicSubType.Illusion);
			return mod;
		}
		
        @Override
		public int getTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicType.Creature.getMask();
		}
	}));
		}
    };
    
    private static final MagicTrigger HISSING_MIASMA=new MagicTrigger(MagicTriggerType.WhenAttacks,"Hissing Miasma") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPermanent creature=(MagicPermanent)data;
			final MagicPlayer controller=creature.getController();
			if (controller!=permanent.getController()) {
				return new MagicEvent(permanent,controller,new Object[]{controller},this,"You lose 1 life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],-1));
		}
    };

    private static final MagicTrigger PHYREXIAN_ARENA=new MagicTrigger(MagicTriggerType.AtUpkeep,"Phyrexian Arena") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,new Object[]{player},this,"You draw a card and you lose 1 life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicDrawAction(player,1));
			game.doAction(new MagicChangeLifeAction(player,-1));
		}
    };
    
    private static final MagicTrigger PROMISE_OF_BUNREI=new MagicTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay,"Promise of Bunrei") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicPermanent otherPermanent=(MagicPermanent)data;
			if (otherPermanent.isCreature()&&otherPermanent.getController()==player) {
				return new MagicEvent(permanent,player,new Object[]{permanent,player},this,
					"Sacrifice Promise of Bunrei. If you do, put four 1/1 colorless Spirit creature tokens onto the battlefield.");
			}			
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicPlayer player=(MagicPlayer)data[1];
			if (player.controlsPermanent(permanent)) {
				game.doAction(new MagicSacrificeAction(permanent));
			
				for (int count=4;count>0;count--) {
				
					game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.SPIRIT_TOKEN_CARD));
				}
			}
		}
    };
    
    private static final MagicTrigger QUEST_FOR_THE_GEMBLADES=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Quest for the Gemblades") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			final MagicPlayer player=permanent.getController();
			final MagicSource source=damage.getSource();
			final MagicTarget target=damage.getTarget();
			if (damage.isCombat()&&source.getController()==player&&source.isPermanent()&&target.isPermanent()&&
				((MagicPermanent)source).isCreature()&&((MagicPermanent)target).isCreature()) {				
				return new MagicEvent(permanent,player,new Object[]{permanent},this,"Put a quest counter on Quest for the Gemblades.");			
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.Charge,1,true));
		}
    };
    
    private static final MagicTrigger QUEST_FOR_THE_GRAVELORD=new MagicTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay,"Quest for the Gravelord") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPermanent otherPermanent=(MagicPermanent)data;
			if (otherPermanent.isCreature()) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,"Put a quest counter on Quest for the Gravelord.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.Charge,1,true));
		}
    };
        
    private static final MagicTrigger RAKING_CANOPY=new MagicTrigger(MagicTriggerType.WhenAttacks,"Raking Canopy") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPermanent creature=(MagicPermanent)data;
			if (creature.getController()!=permanent.getController()&&creature.hasAbility(game,MagicAbility.Flying)) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent,creature},this,
					"Raking Canopy deals 4 damage to "+creature.getName()+".");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
			final MagicDamage damage=new MagicDamage((MagicSource)data[0],(MagicTarget)data[1],4,false);
			game.doAction(new MagicDealDamageAction(damage));
		}
    };

    private static final MagicTrigger RISE_OF_THE_HOBGOBLINS=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Rise of the Hobgoblins") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,
	new MagicMayChoice("You may pay {X}.",new MagicPayManaCostChoice(MagicManaCost.X)),
    new Object[]{player},this,
				"You may pay$ {X}$. If you do, put X 1/1 red and white Goblin Soldier creature tokens onto the battlefield.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicPlayer player=(MagicPlayer)data[0];
				final MagicPayManaCostResult payedManaCost=(MagicPayManaCostResult)choiceResults[1];
				
				for (int count=payedManaCost.getX();count>0;count--) {
					
					game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.GOBLIN_SOLDIER_TOKEN_CARD));
				}
			}
		}
    };
     
    private static final MagicTrigger ELDRAZI_MONUMENT=new MagicTrigger(MagicTriggerType.AtUpkeep,"Eldrazi Monument") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,new Object[]{permanent,player},this,"Sacrifice a creature. If you can't, sacrifice Eldrazi Monument.");
			}
			return null;
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicPlayer player=(MagicPlayer)data[1];
			if (player.controlsPermanentWithType(MagicType.Creature)) {
				game.addEvent(new MagicSacrificePermanentEvent(permanent,player,MagicTargetChoice.SACRIFICE_CREATURE));
			} else {
				game.doAction(new MagicSacrificeAction(permanent));				
			}			
		}
    };
    
    private static final MagicTrigger TUMBLE_MAGNET=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Tumble Magnet") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{permanent},this,
					"Tumble Magnet enters the battlefield with three charge counters on it.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.Charge,3,false));
		}

		@Override
		public boolean usesStack() {
			return false;
		}
    };
    
    private static final MagicTrigger SERRATED_ARROWS1=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Serrated Arrows") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{permanent},this,
					"Serrated Arrows enters the battlefield with three arrowhead counters on it.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.Charge,3,false));
		}

		@Override
		public boolean usesStack() {

			return false;
		}
    };

    private static final MagicTrigger SERRATED_ARROWS2=new MagicTrigger(MagicTriggerType.AtUpkeep,"Serrated Arrows") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			if (player==data&&permanent.getCounters(MagicCounterType.Charge)==0) {
				return new MagicEvent(permanent,player,new Object[]{permanent},this,"Sacrifice Serrated Arrows.");
			}
			return null;
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
			game.doAction(new MagicSacrificeAction((MagicPermanent)data[0]));
		}
    };

    private static final MagicTrigger SHRINE_OF_BURNING_RAGE1=new MagicTrigger(MagicTriggerType.AtUpkeep,"Shrine of Burning Rage") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,new Object[]{permanent},this,"Put a charge counter on Shrine of Burning Rage.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.Charge,1,true));
		}		
    };
    
    private static final MagicTrigger SHRINE_OF_BURNING_RAGE2=new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed,"Shrine of Burning Rage") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			final MagicCard card=((MagicCardOnStack)data).getCard();
			if (card.getOwner()==player&&MagicColor.Red.hasColor(card.getColorFlags())) {
				return new MagicEvent(permanent,player,new Object[]{permanent},this,"Put a charge counter on Shrine of Burning Rage.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.Charge,1,true));
		}		
    };
    
    private static final MagicTrigger SKULLCAGE=new MagicTrigger(MagicTriggerType.AtUpkeep,"Skullcage") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			if (player!=data) {
				return new MagicEvent(permanent,player,new Object[]{permanent,data},this,
					"Skullcage deals 2 damage to your opponent unless your opponent has exactly three or exactly four cards in hand.");
			}
			return null;
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer opponent=(MagicPlayer)data[1];
			final int amount=opponent.getHandSize();
			if (amount<3||amount>4) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],opponent,2,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
    
    private static final MagicTrigger ARMADILLO_CLOAK=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Armadillo Cloak") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (permanent.getEnchantedCreature()==damage.getSource()) {
				final MagicPlayer player=permanent.getController();
				final int amount=damage.getDealtAmount();
				return new MagicEvent(permanent,player,new Object[]{player,amount},this,"You gain "+amount+" life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],(Integer)data[1]));
		}
    };
        
    private static final MagicTrigger ELEPHANT_GUIDE=new MagicTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay,"Elephant Guide") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent.getEnchantedCreature()==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,"Put a 3/3 green Elephant creature token onto the battlefield.");
			}			
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.ELEPHANT_TOKEN_CARD));
		}
    };
    
    private static final MagicTrigger FERVENT_CHARGE=new MagicTrigger(MagicTriggerType.WhenAttacks,"Fervent Charge") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicPermanent creature=(MagicPermanent)data;
			if (creature.getController()==player) {
				return new MagicEvent(permanent,player,new Object[]{creature},this,creature.getName()+" gets +2/+2 until end of turn.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],2,2));
		}
    };
    
    private static final MagicTrigger FISTS_OF_IRONWOOD=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Fists of Ironwood") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You put two 1/1 green Saproling creature tokens onto the battlefield.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.SAPROLING_TOKEN_CARD));
			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.SAPROLING_TOKEN_CARD));
		}
    };
    
    private static final MagicTrigger FLIGHT_OF_FANCY=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Flight of Fancy") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You draw two cards.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			game.doAction(new MagicDrawAction((MagicPlayer)data[0],2));
		}		
    };
    
    private static final MagicTrigger FOLLOWED_FOOTSTEPS=new MagicTrigger(MagicTriggerType.AtUpkeep,"Followed Footsteps") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			final MagicPermanent enchantedCreature=permanent.getEnchantedCreature();
			if (player==data&&enchantedCreature!=null) {
				return new MagicEvent(permanent,player,new Object[]{permanent,player},this,
					"You put a token that's a copy of enchanted creature onto the battlefield.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicPermanent enchantedCreature=permanent.getEnchantedCreature();
			if (enchantedCreature!=null) {
				game.doAction(new MagicPlayTokenAction((MagicPlayer)data[1],enchantedCreature.getCardDefinition()));
			}
		}		
    };
    
    private static final MagicTrigger GALVANIC_ARC=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Galvanic Arc") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,new MagicDamageTargetPicker(3),
				new Object[]{permanent},this,"Galvanic Arc deals 3 damage to target creature or player$.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,3,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
    
    private static final MagicTrigger GRIFFIN_GUIDE=new MagicTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay,"Griffin Guide") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent.getEnchantedCreature()==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,"Put a 2/2 white Griffin creature token with flying onto the battlefield.");
			}			
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.GRIFFIN_TOKEN_CARD));
		}
    };
    
    private static final MagicTrigger NARCOLEPSY=new MagicTrigger(MagicTriggerType.AtUpkeep,"Narcolepsy") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPermanent enchantedCreature=permanent.getEnchantedCreature();
			if (enchantedCreature!=null&&!enchantedCreature.isTapped()) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,
					"If "+enchantedCreature.getName()+" is untapped, tap it.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicPermanent enchantedCreature=permanent.getEnchantedCreature();
			if (enchantedCreature!=null&&!enchantedCreature.isTapped()) {
				game.doAction(new MagicTapAction(enchantedCreature,true));
			}
		}		
    };
    
    private static final MagicTrigger PILLORY_OF_THE_SLEEPLESS=new MagicTrigger(MagicTriggerType.AtUpkeep,"Pillory of the Sleepless") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPermanent enchanted=permanent.getEnchantedCreature();
			if (enchanted!=null) {
				final MagicPlayer player=enchanted.getController();
				if (player==data) {
					return new MagicEvent(enchanted,player,new Object[]{player},this,"You lose 1 life.");
				}
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],-1));
		}
    };
    
    private static final MagicTrigger RANCOR=new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Rancor") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				final MagicCard card=triggerData.card;
				return new MagicEvent(card,card.getController(),new Object[]{card},this,"Return Rancor to its owner's hand.");
			}
			return null;
		}
    	
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicCard card=(MagicCard)data[0];
			game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
			game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
		}
	};
	
    private static final MagicTrigger SNAKE_UMBRA=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Snake Umbra") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			final MagicDamage damage=(MagicDamage)data;
			final MagicTarget target=damage.getTarget();
			if (damage.getSource()==permanent.getEnchantedCreature()&&target.isPlayer()&&target!=player) {
				return new MagicEvent(permanent,player,
	new MagicSimpleMayChoice("You may draw a card.",MagicSimpleMayChoice.DRAW_CARDS,1),
    new Object[]{player},this,"You may$ draw a card.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			if (MagicChoice.isYesChoice(choiceResults[0])) {
				game.doAction(new MagicDrawAction((MagicPlayer)data[0],1));
			}
		}
    };
    
    private static final MagicTrigger SOUL_LINK1=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Soul Link") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent.getEnchantedCreature()) {
				final MagicPlayer player=permanent.getController();
				final int amount=damage.getDealtAmount();
				return new MagicEvent(permanent,player,new Object[]{player,amount},this,"You gain "+amount+" life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],(Integer)data[1]));
		}
    };
    
    private static final MagicTrigger SOUL_LINK2=new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Soul Link") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getTarget()==permanent.getEnchantedCreature()) {
				final MagicPlayer player=permanent.getController();
				final int amount=damage.getDealtAmount();
				return new MagicEvent(permanent,player,new Object[]{player,amount},this,"You gain "+amount+" life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],(Integer)data[1]));
		}
    };
    
    private static final MagicTrigger UNQUESTIONED_AUTHORITY=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Unquestioned Authority") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{player},this,"You draw a card.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicDrawAction((MagicPlayer)data[0],1));
		}
    };
    
    //private static final "DELETEME"
	
	public static void addTriggers() {
        Class c = TriggerDefinitions.class;
        Field[] fields = c.getDeclaredFields();
        int cnt = 0;
        for (final Field field : fields) {
            try { //reflection
                final Object obj = field.get(null);
                if (obj instanceof MagicTrigger) {
                    final MagicTrigger trigger = (MagicTrigger)obj;
                    final MagicCardDefinition card = trigger.getCardDefinition();
                    card.addTrigger(trigger);
                    cnt++;
                }
            } catch (final IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
		System.err.println("Added " + cnt + " triggers");
	}
}
