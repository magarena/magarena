package magic.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.EnumSet;

import javax.swing.ImageIcon;

import magic.ai.ArtificialScoringSystem;
import magic.data.IconImages;
import magic.model.action.MagicAction;
import magic.model.action.MagicAttachEquipmentAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.action.MagicGainControlAction;
import magic.model.event.MagicActivation;
import magic.model.target.MagicTarget;
import magic.model.variable.MagicLocalVariable;
import magic.model.variable.MagicLocalVariableList;
import magic.model.MagicColor;

public class MagicPermanent implements MagicSource,MagicTarget,Comparable<MagicPermanent> {

	public static final int NO_COLOR_FLAGS=-1;
		
	private long id;
	private MagicCard card;
	private MagicCardDefinition cardDefinition;
	private MagicPlayer controller;
	private MagicLocalVariableList localVariables;
	private MagicPermanent equippedCreature=null;
	private MagicPermanentSet equipmentPermanents;
	private MagicPermanent enchantedCreature=null;
	private MagicPermanentSet auraPermanents;
	private MagicPermanent blockedCreature=null;
	private MagicPermanentList blockingCreatures;
	private int counters[]=new int[MagicCounterType.NR_COUNTERS];
	private int stateFlags=MagicPermanentState.Summoned.getMask();
	private long turnAbilityFlags=0;
	private int turnPowerIncr=0;
	private int turnToughnessIncr=0;
	private int turnColorFlags=NO_COLOR_FLAGS;
	private int abilityPlayedThisTurn=0;
	private int turnLocalVariables=0;
	private int damage=0;
	private int preventDamage=0;
	private int fixedScore;

    // Allows cached retrieval of power, toughness and abilities.
	private boolean cached=false;
	private MagicPowerToughness cachedTurnPowerToughness=null;
	private long cachedTurnAbilityFlags=0;
	private EnumSet<MagicSubType> cachedSubTypeFlags;
	private int cachedTypeFlags=0;
	private int cachedColorFlags=0;
	
	public MagicPermanent(final long id,final MagicCard card,final MagicPlayer controller) {
		this.id=id;
		this.card=card;
		this.cardDefinition=card.getCardDefinition();
		this.controller=controller;
		localVariables=new MagicLocalVariableList(cardDefinition.getLocalVariables());
		equipmentPermanents=new MagicPermanentSet();
		auraPermanents=new MagicPermanentSet();
		blockingCreatures=new MagicPermanentList();
		fixedScore=ArtificialScoringSystem.getFixedPermanentScore(this);
	}
	
	private MagicPermanent() {}
	
	@Override
	public MagicCopyable create() {
		return new MagicPermanent();
	}

	@Override
	public void copy(final MagicCopyMap copyMap,final MagicCopyable source) {
		final MagicPermanent sourcePermanent=(MagicPermanent)source;
		id=sourcePermanent.id;
		cardDefinition=sourcePermanent.cardDefinition; // Must be before the rest for compareTo!
		card=copyMap.copy(sourcePermanent.card);
		controller=copyMap.copy(sourcePermanent.controller);
		stateFlags=sourcePermanent.stateFlags;
		turnColorFlags=sourcePermanent.turnColorFlags;
		turnAbilityFlags=sourcePermanent.turnAbilityFlags;
		turnPowerIncr=sourcePermanent.turnPowerIncr;
		turnToughnessIncr=sourcePermanent.turnToughnessIncr;
		counters=Arrays.copyOf(sourcePermanent.counters,MagicCounterType.NR_COUNTERS);
		abilityPlayedThisTurn=sourcePermanent.abilityPlayedThisTurn;
		localVariables=new MagicLocalVariableList(sourcePermanent.localVariables);
		turnLocalVariables=sourcePermanent.turnLocalVariables;
		equippedCreature=copyMap.copy(sourcePermanent.equippedCreature);
		equipmentPermanents=new MagicPermanentSet(copyMap,sourcePermanent.equipmentPermanents);
		enchantedCreature=copyMap.copy(sourcePermanent.enchantedCreature);
		auraPermanents=new MagicPermanentSet(copyMap,sourcePermanent.auraPermanents);
		blockedCreature=copyMap.copy(sourcePermanent.blockedCreature);
		blockingCreatures=new MagicPermanentList(copyMap,sourcePermanent.blockingCreatures);
		damage=sourcePermanent.damage;
		preventDamage=sourcePermanent.preventDamage;
		fixedScore=sourcePermanent.fixedScore;
	}
	
	@Override
	public Object map(final MagicGame game) {
		final MagicPlayer mappedController=(MagicPlayer)controller.map(game);
		return mappedController.getPermanents().getPermanent(id);
	}
	
	public long getId() {
		return id;
	}
	
	public long getPermanentId() {
        long[] input = {
            card.getId(),
            cardDefinition.getIndex(),
            stateFlags,
            damage,
            preventDamage,
            localVariables.size(),
            (equippedCreature != null ? equippedCreature.getId() : 1),
            (enchantedCreature != null ? enchantedCreature.getId() : 1),
            (blockedCreature != null ? blockedCreature.getId() : 1),
            counters[0],
            counters[1],
            counters[2],
            counters[3],
	        turnAbilityFlags,
            turnPowerIncr,
            turnToughnessIncr,
            turnColorFlags,
            abilityPlayedThisTurn,
            turnLocalVariables,
	        damage,
            preventDamage,
        };
		return magic.MurmurHash3.hash(input);
        /*		
		if (equippedCreature!=null) {
			pid+=equippedCreature.cardDefinition.getIndex();
		} else if (enchantedCreature!=null) {
			pid+=enchantedCreature.cardDefinition.getIndex();
		}
        */
 	}
	
	/** Determines uniqueness of a mana permanent, e.g. for producing mana, all Mountains are equal. */
	public int getManaId() {
		// Creatures or lands that can be animated are unique by default.
		if (cardDefinition.hasExcludeManaOrCombat()) {
			return (int)id;
		} 
		// Uniqueness is determined by card definition and number of charge counters.
		return -((cardDefinition.getIndex()<<16)+getCounters(MagicCounterType.Charge));
	}
	
	public MagicCard getCard() {
		return card;
	}
	
	@Override
	public MagicCardDefinition getCardDefinition() {
		return cardDefinition;
	}
	
	@Override
	public Collection<MagicActivation> getActivations() {
		return cardDefinition.getActivations();
	}

	public boolean producesMana() {
		return !cardDefinition.getManaActivations().isEmpty();
	}
	
	public String getName() {
		return card.getName();
	}
	
	@Override
	public String toString() {
		return getName();
	}
		
	public void setController(final MagicPlayer controller) {
		this.controller=controller;
	}
	
	public MagicPlayer getController() {
		return controller;
	}
			
	public void setState(final MagicPermanentState state) {
		stateFlags|=state.getMask();
	}
	
	public void clearState(final MagicPermanentState state) {
		stateFlags&=Integer.MAX_VALUE-state.getMask();
	}
	
	public boolean hasState(final MagicPermanentState state) {
		return state.hasState(stateFlags);
	}
	
	public int getStateFlags() {
		return stateFlags;
	}
	
	public void setStateFlags(final int flags) {
		stateFlags=flags;
	}
	
	public boolean isTapped() {
		return hasState(MagicPermanentState.Tapped);
	}
	
	public void addLocalVariable(final MagicLocalVariable localVariable) {
		localVariables.add(localVariable);
	}

	public void addTurnLocalVariable(final MagicLocalVariable localVariable) {
		localVariables.add(cardDefinition.getTurnLocalVariableIndex()+turnLocalVariables,localVariable);
		turnLocalVariables++;
	}

	public void removeTurnLocalVariable() {
		turnLocalVariables--;
		localVariables.remove(cardDefinition.getTurnLocalVariableIndex()+turnLocalVariables);
	}
	
	public void addTurnLocalVariables(final List<MagicLocalVariable> localVariablesList) {
		for (final MagicLocalVariable localVariable : localVariablesList) {
			addTurnLocalVariable(localVariable);
		}
	}
	
	public List<MagicLocalVariable> removeTurnLocalVariables() {
		if (turnLocalVariables>0) {
			final List<MagicLocalVariable> localVariablesList=new ArrayList<MagicLocalVariable>();
			final int index=cardDefinition.getTurnLocalVariableIndex();
			for (;turnLocalVariables>0;turnLocalVariables--) {
				localVariablesList.add(localVariables.remove(index));
			}
			return localVariablesList;
		}
		return Collections.emptyList();
	}
		
	@Override
	public MagicColoredType getColoredType() {
		return cardDefinition.getColoredType();
	}
	
	public void setTurnColorFlags(final int colorFlags) {
		turnColorFlags=colorFlags;
	}
	
	public int getTurnColorFlags() {
		return turnColorFlags;
	}
	
	@Override
	public int getColorFlags() {
		// Check if cached.
		if (cached) {
			return cachedColorFlags;
		}
		// Check if until end of turn color flags.
		if (turnColorFlags!=NO_COLOR_FLAGS) {
			return turnColorFlags;
		}
		if (isCreature()) {
			int flags=cardDefinition.getColorFlags();
			for (final MagicLocalVariable localVariable : localVariables) {
				
				flags=localVariable.getColorFlags(this,flags);
			}
			return flags;
		}
		return cardDefinition.getColorFlags();
	}
		
	public void changeCounters(final MagicCounterType counterType,final int amount) {
		counters[counterType.ordinal()]+=amount;
		if (cached) {
			switch (counterType) {
				case PlusOne:
					cachedTurnPowerToughness.add(amount);
					break;
				case MinusOne:
					cachedTurnPowerToughness.add(-amount);
					break;
			}
		}
	}
	
	public int getCounters(final MagicCounterType counterType) {
		return counters[counterType.ordinal()];
	}
	
	public boolean hasCounters() {
		for (final int amount : counters) {
			if (amount>0) {
				return true;
			}
		}
		return false;
	}
	
	public EnumSet<MagicSubType> getSubTypeFlags() {
		// Check if cached.
		if (cached) {
			return cachedSubTypeFlags;
		}
		
		EnumSet<MagicSubType> flags=cardDefinition.getSubTypeFlags();
		for (final MagicLocalVariable localVariable : localVariables) {
			flags=localVariable.getSubTypeFlags(this,flags);
		}
		return flags;
	}

	public boolean hasSubType(final MagicSubType subType) {
		return subType.hasSubType(getSubTypeFlags());
	}

	public MagicPowerToughness getPowerToughness(final MagicGame game,final boolean turn) {
		// Check if cached.
		if (cached&&turn) {
			return cachedTurnPowerToughness;
		}
		
		final MagicPowerToughness pt=new MagicPowerToughness(cardDefinition.getPower(),cardDefinition.getToughness());
		for (final MagicLocalVariable localVariable : localVariables) {
			localVariable.getPowerToughness(game,this,pt);
		}		
		if (turn) {
			pt.power+=turnPowerIncr;
			pt.toughness+=turnToughnessIncr;
		}
		return pt;
	}
	
	public MagicPowerToughness getPowerToughness(final MagicGame game) {
		return getPowerToughness(game,true);
	}
		
	public int getPower(final MagicGame game) {
		return getPowerToughness(game,true).getPositivePower();
	}
	
	public int getToughness(final MagicGame game) {
		return getPowerToughness(game,true).getPositiveToughness();
	}
	
	public int getTurnPower() {
		return turnPowerIncr;
	}
	
	public void setTurnPower(final int power) {
		turnPowerIncr=power;
	}
	
	public void changeTurnPower(final int amount) {
		turnPowerIncr+=amount;
		if (cached) {
			cachedTurnPowerToughness.power+=amount;
		}
	}
	
	public int getTurnToughness() {
		return turnToughnessIncr;
	}
	
	public void setTurnToughness(final int toughness) {
		turnToughnessIncr=toughness;
	}
	
	public void changeTurnToughness(final int amount) {
		turnToughnessIncr+=amount;
		if (cached) {
			cachedTurnPowerToughness.toughness+=amount;
		}
	}
			
	public void setTurnAbilityFlags(final long flags) {
		turnAbilityFlags=flags;
		if (cached) {
			cachedTurnAbilityFlags|=flags;
		}
	}
	
	public long getTurnAbilityFlags() {
		return turnAbilityFlags;
	}
	
	public long getAllAbilityFlags(final MagicGame game,final boolean turn) {
		// Check if cached.
		if (cached&&turn) {
			return cachedTurnAbilityFlags;
		}		
		// Only creatures can have abilities.
		if (!isCreature()) {
			return 0;
		}
		long flags=cardDefinition.getAbilityFlags();
		for (final MagicLocalVariable localVariable : localVariables) {
			
			flags=localVariable.getAbilityFlags(game,this,flags);
		}
		if (turn) {
			flags|=turnAbilityFlags;
		}
		return flags&MagicAbility.EXCLUDE_MASK;
	}
	
	public long getAllAbilityFlags(final MagicGame game) {
		return getAllAbilityFlags(game,true);
	}

	@Override
	public boolean hasAbility(final MagicGame game,final MagicAbility ability) {
		// Check if cached.
		if (cached) {
			return ability.hasAbility(cachedTurnAbilityFlags);
		}
		// Only creatures can have abilities.
		if (!isCreature()) {
			return false;
		}
		if (ability.hasAbility(turnAbilityFlags)) {
			return true;
		}
		long flags=cardDefinition.getAbilityFlags();
		for (final MagicLocalVariable localVariable : localVariables) {
			
			flags=localVariable.getAbilityFlags(game,this,flags);
		}
		return ability.hasAbility(flags);
	}
	
	public int getScore(final MagicGame game) {
		return fixedScore + ArtificialScoringSystem.getVariablePermanentScore(game,this);
	}
	
	public int getStaticScore(final MagicGame game) {
		
		return cardDefinition.getStaticType().getScore(game,this);
	}
	
	public void setCached(final MagicGame game,final boolean cached) {
		
		if (cached) {
			cachedTurnPowerToughness=getPowerToughness(game,true);
			cachedTurnAbilityFlags=getAllAbilityFlags(game,true);
			cachedSubTypeFlags=getSubTypeFlags();
			cachedTypeFlags=getTypeFlags();
			cachedColorFlags=getColorFlags();
			this.cached=true;
		} else {
			cachedTurnPowerToughness=null;
			this.cached=false;
		}
	}
	
	public int getDamage() {
		return damage;
	}
	
	public void setDamage(final int damage) {
		this.damage=damage;
	}
		
	@Override
	public int getPreventDamage() {
		return preventDamage;
	}

	@Override
	public void setPreventDamage(final int amount) {
		preventDamage=amount;
	}

	public int getLethalDamage(final int toughness) {
		return toughness<=damage?0:toughness-damage;
	}
	
	// Tap symbol.
	public boolean canTap(final MagicGame game) {
		return !hasState(MagicPermanentState.Tapped) && 
            (!hasState(MagicPermanentState.Summoned) || 
             !isCreature() || 
             hasAbility(game,MagicAbility.Haste)
            );
	}
	
	// Untap symbol.
	public boolean canUntap(final MagicGame game) {
		return hasState(MagicPermanentState.Tapped) && 
            (!hasState(MagicPermanentState.Summoned) || 
             !isCreature() || 
             hasAbility(game,MagicAbility.Haste)
            );		
	}
	
	public boolean canRegenerate() {
		return !hasState(MagicPermanentState.Regenerated)&&!hasState(MagicPermanentState.CannotBeRegenerated);
	}
	
	public boolean isRegenerated() {
		return hasState(MagicPermanentState.Regenerated)&&!hasState(MagicPermanentState.CannotBeRegenerated);
	}
	
	public boolean isAttacking() {
		return hasState(MagicPermanentState.Attacking);
	}
	
	public boolean isBlocked() {
		return hasState(MagicPermanentState.Blocked);
	}
	
	public boolean isBlocking() {
		return hasState(MagicPermanentState.Blocking);
	}
	
	public MagicPermanent getBlockedCreature() {
		return blockedCreature;
	}
	
	public void setBlockedCreature(final MagicPermanent creature) {
		blockedCreature=creature;
	}

	public MagicPermanentList getBlockingCreatures() {
		return blockingCreatures;
	}
	
	public void setBlockingCreatures(final MagicPermanentList creatures) {
		blockingCreatures.clear();
		blockingCreatures.addAll(creatures);
	}
		
	public void addBlockingCreature(final MagicPermanent creature) {
		blockingCreatures.add(creature);
	}
	
	public void removeBlockingCreature(final MagicPermanent creature) {
		blockingCreatures.remove(creature);
	}
	
	public void removeBlockingCreatures() {
		blockingCreatures.clear();
	}
	
	public void checkState(final MagicGame game, final List<MagicAction> actions) {
		// +1/+1 and -1/-1 counters cancel each other out.
		int plusCounters=getCounters(MagicCounterType.PlusOne);
		if (plusCounters>0) {
			int minusCounters=getCounters(MagicCounterType.MinusOne);
			if (minusCounters>0) {
				final int amount=-Math.min(plusCounters,minusCounters);
				actions.add(new MagicChangeCountersAction(this,MagicCounterType.PlusOne,amount,false));
				actions.add(new MagicChangeCountersAction(this,MagicCounterType.MinusOne,amount,false));
			}
		}
		
		if (isCreature()) {
			final int toughness=getToughness(game);
			if (toughness<=0) {
				game.logAppendMessage(controller,getName()+" is put into its owner's graveyard.");
				actions.add(new MagicRemoveFromPlayAction(this,MagicLocationType.Graveyard));
			} else if (hasState(MagicPermanentState.Destroyed)) {
				actions.add(new MagicChangeStateAction(this,MagicPermanentState.Destroyed,false));
				actions.add(new MagicDestroyAction(this));
			} else if (toughness-damage<=0) {
				actions.add(new MagicDestroyAction(this));
			}
		} else if (cardDefinition.isAura()) {
			if (enchantedCreature==null||!enchantedCreature.isCreature()||enchantedCreature.hasProtectionFrom(game,this)) {
				game.logAppendMessage(controller,getName()+" is put into its owner's graveyard.");
				actions.add(new MagicRemoveFromPlayAction(this,MagicLocationType.Graveyard));
			}
		} else if (cardDefinition.isEquipment()) {
			if (equippedCreature!=null&&(!equippedCreature.isCreature()||equippedCreature.hasProtectionFrom(game,this))) {
				actions.add(new MagicAttachEquipmentAction(this,null));
			}
		}
	}
	
	private static boolean hasProtectionFrom(final long abilityFlags,final MagicSource source) {
		
		// Check if there is a protection ability.
		if ((abilityFlags&MagicAbility.PROTECTION_FLAGS)==0) {
			return false;
		}
		
		// From monocolored.
		if (//source.getColoredType() == MagicColoredType.MonoColored &&
            //added to fix bug with Raging Raving creature not able to block
            //creature with protection from monocolored
            MagicColor.isMono(source.getColorFlags()) &&
            MagicAbility.ProtectionFromMonoColored.hasAbility(abilityFlags)) {
			return true;
		}
		
		final int colorFlags=source.getColorFlags();
		if (colorFlags>0) {
			// From all colors.
			if (MagicAbility.ProtectionFromAllColors.hasAbility(abilityFlags)) {
				return true;
			}
			
			// From a color.
			for (final MagicColor color : MagicColor.values()) {
				
				if (color.hasColor(colorFlags)&&color.getProtectionAbility().hasAbility(abilityFlags)) {
					return true;
				}
			}
		}
				
		if (source.isPermanent()) {
			final MagicPermanent sourcePermanent=(MagicPermanent)source;
			if (sourcePermanent.isCreature()) {
				// From creatures.
				if (MagicAbility.ProtectionFromCreatures.hasAbility(abilityFlags)) {
					return true;
				}
				// From Demons.
				if (sourcePermanent.hasSubType(MagicSubType.Demon)&&MagicAbility.ProtectionFromDemons.hasAbility(abilityFlags)) {
					return true;
				}
				// From Dragons.
				if (sourcePermanent.hasSubType(MagicSubType.Dragon)&&MagicAbility.ProtectionFromDragons.hasAbility(abilityFlags)) {
					return true;
				}
			}
		}

		return false;
	}
	
	public boolean hasProtectionFrom(final MagicGame game,final MagicSource source) {
	
		final long abilityFlags=getAllAbilityFlags(game);
		return hasProtectionFrom(abilityFlags,source);
	}
	
	public boolean canAttack(final MagicGame game) {
		if (!isCreature() || 
            !canTap(game) || 
            hasState(MagicPermanentState.ExcludeFromCombat)) {
			return false;
		}
		final long flags=getAllAbilityFlags(game);
		return !MagicAbility.CannotAttackOrBlock.hasAbility(flags) && !MagicAbility.Defender.hasAbility(flags);
	}
	
	public boolean canBeBlocked(final MagicGame game,final MagicPlayer player) {

		final long flags=getAllAbilityFlags(game);
		
		// Unblockable
		if (MagicAbility.Unblockable.hasAbility(flags)) {
			return false;
		}		
		
		// Landwalk
		for (final MagicColor color : MagicColor.values()) {
			
			if (color.getLandwalkAbility().hasAbility(flags)&&player.controlsPermanentWithSubType(color.getLandSubType())) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean canBlock(final MagicGame game) {

		if (!isCreature()||isTapped()||hasState(MagicPermanentState.ExcludeFromCombat)) {
			return false;
		}
		final long flags=getAllAbilityFlags(game);		
		return !MagicAbility.CannotAttackOrBlock.hasAbility(flags)&&!MagicAbility.CannotBlock.hasAbility(flags);
	}
	
	public boolean canBlock(final MagicGame game,final MagicPermanent attacker) {
		
		final long attackerFlags=attacker.getAllAbilityFlags(game);

		// Fear & intimidate
		if (!isArtifact()) {
			final int colorFlags=getColorFlags();
			if (MagicAbility.Fear.hasAbility(attackerFlags)&&!MagicColor.Black.hasColor(colorFlags)) {
				return false;
			}
			if (MagicAbility.Intimidate.hasAbility(attackerFlags)&&((colorFlags&attacker.getColorFlags())==0)) {
				return false;
			}
		}
		
		// Flying and Reach
		final long blockerFlags=getAllAbilityFlags(game);
		final boolean blockerFlying=MagicAbility.Flying.hasAbility(blockerFlags);
		if (blockerFlying) {
			if (MagicAbility.CannotBeBlockedByFlying.hasAbility(attackerFlags)) {
				return false;
			}
		} else {
			if (MagicAbility.CannotBeBlockedExceptWithFlying.hasAbility(attackerFlags)) {
				return false;
			}						
		}
		if (MagicAbility.Flying.hasAbility(attackerFlags)) {
			if (!blockerFlying&&!MagicAbility.Reach.hasAbility(blockerFlags)) {
				return false;
			}
		} else {
			if (MagicAbility.CannotBlockWithoutFlying.hasAbility(blockerFlags)) {
				return false;
			}
		}
        if (MagicAbility.CannotBeBlockedExceptWithFlyingOrReach.hasAbility(attackerFlags)) {
            if (!MagicAbility.Flying.hasAbility(blockerFlags) && !MagicAbility.Reach.hasAbility(blockerFlags)) {
                return false;
            }
        }

		// Protection
		return !hasProtectionFrom(attackerFlags,this);
	}
	
	public MagicLocalVariable getAttachmentLocalVariable() {
		
		return cardDefinition.getAttachmentLocalVariable();
	}
		
	public MagicPermanent getEquippedCreature() {
		
		return equippedCreature;
	}

	public void setEquippedCreature(final MagicPermanent creature) {
		
		equippedCreature=creature;
	}
	
	public MagicPermanentSet getEquipmentPermanents() {
		
		return equipmentPermanents;
	}
	
	public void addEquipment(final MagicPermanent equipment) {
		
		equipmentPermanents.add(equipment);
		localVariables.add(equipment.getAttachmentLocalVariable());
	}
	
	public void removeEquipment(final MagicPermanent equipment) {
		
		equipmentPermanents.remove(equipment);
		localVariables.remove(equipment.getAttachmentLocalVariable());
	}
	
	public boolean isEquipped() {
		
		return equipmentPermanents.size()>0;
	}
	
	public MagicPermanent getEnchantedCreature() {
		
		return enchantedCreature;
	}

	public void setEnchantedCreature(final MagicPermanent creature) {
		
		enchantedCreature=creature;
	}
	
	public MagicPermanentSet getAuraPermanents() {
		
		return auraPermanents;
	}
	
	public void addAura(final MagicPermanent aura) {
		
		auraPermanents.add(aura);
		localVariables.add(aura.getAttachmentLocalVariable());
	}
	
	public void removeAura(final MagicPermanent aura) {
		
		auraPermanents.remove(aura);
		localVariables.remove(aura.getAttachmentLocalVariable());
	}
			
	public boolean isEnchanted() {
		
		return auraPermanents.size()>0;
	}
	
	public int getAbilityPlayedThisTurn() {
		
		return abilityPlayedThisTurn;
	}
	
	public void setAbilityPlayedThisTurn(final int amount) {
		
		abilityPlayedThisTurn=amount;
	}
	
	public void incrementAbilityPlayedThisTurn() {
		
		abilityPlayedThisTurn++;
	}

	public void decrementAbilityPlayedThisTurn() {
		
		abilityPlayedThisTurn--;
	}
	
    public int getTypeFlags() {
		// Check if cached.
		if (cached) {
			return cachedTypeFlags;
		}
		
        int flags=cardDefinition.getTypeFlags();
		for (final MagicLocalVariable localVariable : localVariables) {
			flags=localVariable.getTypeFlags(this,flags);
		}
		return flags;
	}
	
	public boolean hasType(final MagicType type) {
		return type.hasType(getTypeFlags());				
	}
	
	public boolean isLand() {
		return cardDefinition.isLand();
	}
	
	public boolean isCreature() {
		return MagicType.Creature.hasType(getTypeFlags());
	}
	
	public boolean isArtifact() {
		
		return cardDefinition.isArtifact();
	}
	
	public boolean isEnchantment() {
		
		return cardDefinition.isEnchantment();
	}
	
	@Override
	public boolean isSpell() {
		
		return false;
	}

	@Override
	public boolean isPlayer() {

		return false;
	}
	
	@Override
	public boolean isPermanent() {

		return true;
	}	
	
	@Override
	public boolean isValidTarget(final MagicGame game,final MagicSource source) {

		final long flags=getAllAbilityFlags(game);
		// Can't be the target of spells or abilities.
		if (MagicAbility.Shroud.hasAbility(flags)) {
			return false;
		}
		// Can't be the target of spells or abilities your opponents controls.
		if (MagicAbility.CannotBeTheTarget.hasAbility(flags)&&source.getController()!=controller) {
			return false;
		}

		// Can't be the target of spells or abilities player 0 controls.
        if (MagicAbility.CannotBeTheTarget0.hasAbility(flags) && source.getController().getIndex() == 0) {
            return false;
        }
		
        // Can't be the target of spells or abilities player 1 controls.
        if (MagicAbility.CannotBeTheTarget1.hasAbility(flags) && source.getController().getIndex() == 1) {
            return false;
        }

		// Protection.
		return !hasProtectionFrom(flags,source);
	}

	@Override
	public int compareTo(final MagicPermanent permanent) {

		// Important for sorting of permanent mana activations.
		final int dif=cardDefinition.getIndex()-permanent.cardDefinition.getIndex();
		if (dif!=0) {
			return dif;
		}
				
		if (id<permanent.id) {
			return -1;
		} else if (id>permanent.id) {
			return 1;
		} else {
			return 0;
		}
	}

	public boolean endOfTurn(final MagicGame game) {
		if (MagicPermanentState.RemoveAtEndOfTurn.hasState(stateFlags)) {
			game.logAppendMessage(controller,"Exile "+this.getName()+" (end of turn).");
			game.doAction(new MagicRemoveFromPlayAction(this,MagicLocationType.Exile));
			return true;
		} else if (MagicPermanentState.RemoveAtEndOfYourTurn.hasState(stateFlags)&&controller==game.getTurnPlayer()) {
			game.logAppendMessage(controller,"Exile "+this.getName()+" (end of turn).");
			game.doAction(new MagicRemoveFromPlayAction(this,MagicLocationType.Exile));
			return true;
		} else if (MagicPermanentState.SacrificeAtEndOfTurn.hasState(stateFlags)) {
			game.logAppendMessage(controller,"Sacrifice "+this.getName()+" (end of turn).");
			game.doAction(new MagicSacrificeAction(this));
			return true;
		} else if (MagicPermanentState.ReturnToOwnerAtEndOfTurn.hasState(stateFlags)) {
			game.logAppendMessage(controller,"Return "+this.getName()+" to its owner (end of turn).");
	        clearState(MagicPermanentState.ReturnToOwnerAtEndOfTurn);
            game.doAction(new MagicGainControlAction(card.getOwner(),this));
			return true;
        }
		return false;
	}
	
	public ImageIcon getIcon() {

		if (isAttacking()) {
			return IconImages.ATTACK;
		} 
		if (isBlocking()) {
			return IconImages.BLOCK;
		}
		if (isCreature()) {
			return IconImages.CREATURE;
		}
		return cardDefinition.getIcon();
	}
}
