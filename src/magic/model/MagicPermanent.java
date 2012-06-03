package magic.model;

import magic.ai.ArtificialScoringSystem;
import magic.data.IconImages;
import magic.model.action.MagicAction;
import magic.model.action.MagicAttachEquipmentAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicGainControlAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.action.MagicSoulbondAction;
import magic.model.event.MagicActivation;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.target.MagicTarget;
import magic.model.choice.MagicTargetChoice;
import magic.model.mstatic.MagicLayer;

import javax.swing.ImageIcon;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

public class MagicPermanent implements MagicSource,MagicTarget,Comparable<MagicPermanent> {

    public static final int NO_COLOR_FLAGS=-1;
    public static final MagicPermanent NONE = new MagicPermanent(-1L, MagicCard.NONE, MagicPlayer.NONE) {
        @Override
        public boolean isValid() {
            return false;
        }
        @Override
        public String toString() {
            return "MagicPermanent.NONE";
        }
        @Override
        public MagicPermanent copy(final MagicCopyMap copyMap) {
            return this;
        }
        @Override
        public MagicPowerToughness getPowerToughness(final MagicGame game,final boolean turn) {
            return new MagicPowerToughness(0,0);
        }
        @Override
        public boolean hasAbility(final MagicAbility ability) {
            return false;
        }
    };
        
    private final long id;
    private final MagicCardDefinition cardDefinition;
    private final MagicCard card;
    private MagicPlayer controller;
    private MagicPermanent equippedCreature = MagicPermanent.NONE;
    private final MagicPermanentSet equipmentPermanents;    
    private MagicPermanent enchantedCreature = MagicPermanent.NONE;
    private final MagicPermanentSet auraPermanents;    
    private MagicPermanent blockedCreature = MagicPermanent.NONE;
    private final MagicPermanentList blockingCreatures;    
    private MagicPermanent pairedCreature = MagicPermanent.NONE;
    private MagicCardList exiledCards;
    private MagicTarget chosenTarget;
    private int counters[]=new int[MagicCounterType.NR_COUNTERS];
    private int stateFlags = 
            MagicPermanentState.Summoned.getMask() |
            MagicPermanentState.MustPayEchoCost.getMask();
    private long turnAbilityFlags=0;
    private int turnPowerIncr=0;
    private int turnToughnessIncr=0;
    private int turnColorFlags=NO_COLOR_FLAGS;
    private int abilityPlayedThisTurn=0;
    private int damage=0;
    private int preventDamage=0;
    private final int fixedScore;

    // last known information
    private EnumSet<MagicSubType> lastKnownSubTypeFlags;
    private MagicPowerToughness lastKnownPowerToughness;
    private long lastKnownAbilityFlags = 0;
    private int lastKnownTypeFlags = 0;
    private int lastKnownColorFlags = 0;

    // Allows cached retrieval of power, toughness and abilities.
    private boolean cached=false;
    private MagicPowerToughness cachedTurnPowerToughness;
    private long cachedTurnAbilityFlags=0;
    private EnumSet<MagicSubType> cachedSubTypeFlags;
    private int cachedTypeFlags=0;
    private int cachedColorFlags=0;

    // remember order among blockers (blockedName + id + block order)
    private String blockedName;

    public MagicPermanent(final long id,final MagicCard card,final MagicPlayer controller) {
        this.id=id;
        this.card=card;
        this.cardDefinition=card.getCardDefinition();
        this.controller=controller;
        equipmentPermanents=new MagicPermanentSet();
        auraPermanents=new MagicPermanentSet();
        blockingCreatures=new MagicPermanentList();
        exiledCards = new MagicCardList();
        fixedScore=ArtificialScoringSystem.getFixedPermanentScore(this);
    }

    private MagicPermanent(final MagicCopyMap copyMap, final MagicPermanent sourcePermanent) {
        id = sourcePermanent.id;
        cardDefinition = sourcePermanent.cardDefinition;
        
        copyMap.put(sourcePermanent, this);
        
        card = copyMap.copy(sourcePermanent.card);
        controller = copyMap.copy(sourcePermanent.controller);
        stateFlags=sourcePermanent.stateFlags;
        turnColorFlags=sourcePermanent.turnColorFlags;
        turnAbilityFlags=sourcePermanent.turnAbilityFlags;
        turnPowerIncr=sourcePermanent.turnPowerIncr;
        turnToughnessIncr=sourcePermanent.turnToughnessIncr;
        counters=Arrays.copyOf(sourcePermanent.counters,MagicCounterType.NR_COUNTERS);
        abilityPlayedThisTurn=sourcePermanent.abilityPlayedThisTurn;
        equippedCreature=copyMap.copy(sourcePermanent.equippedCreature);
        equipmentPermanents=new MagicPermanentSet(copyMap,sourcePermanent.equipmentPermanents);
        enchantedCreature=copyMap.copy(sourcePermanent.enchantedCreature);
        auraPermanents=new MagicPermanentSet(copyMap,sourcePermanent.auraPermanents);
        blockedCreature=copyMap.copy(sourcePermanent.blockedCreature);
        blockingCreatures=new MagicPermanentList(copyMap,sourcePermanent.blockingCreatures);
        pairedCreature = copyMap.copy(sourcePermanent.pairedCreature);
        exiledCards = new MagicCardList(copyMap,sourcePermanent.exiledCards);
        chosenTarget = copyMap.copy(sourcePermanent.chosenTarget);
        damage=sourcePermanent.damage;
        preventDamage=sourcePermanent.preventDamage;
        fixedScore=sourcePermanent.fixedScore;
        
        lastKnownPowerToughness = sourcePermanent.lastKnownPowerToughness;
        lastKnownAbilityFlags = sourcePermanent.lastKnownAbilityFlags;
        lastKnownSubTypeFlags = sourcePermanent.lastKnownSubTypeFlags;
        lastKnownTypeFlags = sourcePermanent.lastKnownTypeFlags;
        lastKnownColorFlags = sourcePermanent.lastKnownColorFlags;
    }
    
    @Override
    public MagicPermanent copy(final MagicCopyMap copyMap) {
        return new MagicPermanent(copyMap, this);
    }
    
    @Override
    public MagicPermanent map(final MagicGame game) {
        final MagicPlayer mappedController=controller.map(game);
        return mappedController.getPermanents().getPermanent(id);
    }
    
    public long getId() {
        return id;
    }

    public boolean isValid() {
        return true;
    }
    
    public boolean isInvalid() {
        return !isValid();
    }
    
    private boolean isOnBattlefield(final MagicGame game) {
        return getController().controlsPermanent(this);
    }
    
    long getPermanentId() {
        final long[] input = {
            cardDefinition.getIndex(),
            stateFlags,
            damage,
            preventDamage,
            equippedCreature.getId(),
            enchantedCreature.getId(),
            blockedCreature.getId(),
            counters[0],
            counters[1],
            counters[2],
            counters[3],
            turnAbilityFlags,
            turnPowerIncr,
            turnToughnessIncr,
            turnColorFlags,
            abilityPlayedThisTurn,
            damage,
            preventDamage,
        };
        return magic.MurmurHash3.hash(input);
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

    private MagicGame getGame() {
        return getOwner().getGame();
    }

    public MagicPlayer getOwner() {
        return card.getOwner();
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
    
    public boolean isUntapped() {
        return !hasState(MagicPermanentState.Tapped);
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
        
        int flags = cardDefinition.getColorFlags();

        flags = MagicLayer.getColorFlags(getGame(), this, flags);
        
        return flags;
    }
        
    public void changeCounters(final MagicCounterType counterType,final int amount) {
        counters[counterType.ordinal()]+=amount;
        if (cached) {
            switch (counterType) {
                case PlusOne:
                    cachedTurnPowerToughness.add(amount,amount);
                    break;
                case MinusOne:
                    cachedTurnPowerToughness.add(-amount,-amount);
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
    
    public EnumSet<MagicSubType> getSubTypeFlags(final MagicGame game) {
        // Check if cached.
        if (cached) {
            return cachedSubTypeFlags;
        }
        
        EnumSet<MagicSubType> flags=cardDefinition.getSubTypeFlags();
        
        if (getCardDefinition().hasAbility(MagicAbility.Changeling)) {
            flags.addAll(MagicSubType.ALL_CREATURES);
        }
        
        flags = MagicLayer.getSubTypeFlags(game,this,flags);

        return flags;
    }

    public boolean hasSubType(final MagicSubType subType, final MagicGame game) {
        return subType.hasSubType(getSubTypeFlags(game));
    }

    public MagicPowerToughness getPowerToughness(final MagicGame game,final boolean turn) {
        if (!isOnBattlefield(game)) {
            assert lastKnownPowerToughness != null : "last known p/t is null";
            return lastKnownPowerToughness;
        }
        
        // Check if cached.
        if (cached&&turn) {
            assert cachedTurnPowerToughness != null : "cached p/t is null";
            return cachedTurnPowerToughness;
        }
        
        //get starting P/T from card def (includes CDA)
        final MagicPowerToughness pt = cardDefinition.genPowerToughness(game, getController(), this);

        //apply global effects
        MagicLayer.getPowerToughness(game, this, pt);

        //apply turn effects
        if (turn) {
            pt.add(turnPowerIncr, turnToughnessIncr);
        }
            
        assert pt != null : "p/t is null";
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
            cachedTurnPowerToughness.add(amount,0);
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
            cachedTurnPowerToughness.add(0,amount);
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
    
    public void setLastKnownInfo(final MagicGame game) {
        lastKnownPowerToughness = getPowerToughness(game);
        lastKnownAbilityFlags = getAllAbilityFlags(game);
        lastKnownSubTypeFlags = getSubTypeFlags(game);
        lastKnownTypeFlags = getTypeFlags(game);
        lastKnownColorFlags = getColorFlags();
    }
    
    public long getAllAbilityFlags(final MagicGame game,final boolean turn) {
        // Check if cached.
        if (cached&&turn) {
            return cachedTurnAbilityFlags;
        }    
        long flags = getCurrentAbilityFlags(game);
        if (turn) {
            flags|=turnAbilityFlags;
        }
        return flags&MagicAbility.EXCLUDE_MASK;
    }

    private long getCurrentAbilityFlags(final MagicGame game) {
        long flags=cardDefinition.getAbilityFlags();

        //apply global effects
        return MagicLayer.getAbilityFlags(game, this, flags);
    }
    
    public long getAllAbilityFlags(final MagicGame game) {
        return getAllAbilityFlags(game,true);
    }

    @Override
    public boolean hasAbility(final MagicAbility ability) {
        final MagicGame game = getGame();
        if (!isOnBattlefield(game)) {
            return ability.hasAbility(lastKnownAbilityFlags);
        }
        
        // Check if cached.
        if (cached) {
            return ability.hasAbility(cachedTurnAbilityFlags);
        }
        if (ability.hasAbility(turnAbilityFlags)) {
            return true;
        }
        long flags = getCurrentAbilityFlags(game);
        return ability.hasAbility(flags);
    }
    
    public int getScore(final MagicGame game) {
        return fixedScore + ArtificialScoringSystem.getVariablePermanentScore(game,this);
    }
    
    public int getStaticScore(final MagicGame game) {
        return cardDefinition.getStaticType().getScore(game,this);
    }
    
    void setCached(final MagicGame game,final boolean aCached) {
        if (aCached) {
            cachedTurnPowerToughness=getPowerToughness(game,true);
            cachedTurnAbilityFlags=getAllAbilityFlags(game,true);
            cachedSubTypeFlags=getSubTypeFlags(game);
            cachedTypeFlags=getTypeFlags(game);
            cachedColorFlags=getColorFlags();
        } 
        this.cached=aCached;
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
             hasAbility(MagicAbility.Haste)
            );
    }
    
    // Untap symbol.
    public boolean canUntap(final MagicGame game) {
        return hasState(MagicPermanentState.Tapped) && 
            (!hasState(MagicPermanentState.Summoned) || 
             !isCreature() || 
             hasAbility(MagicAbility.Haste)
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
        if (creature.isValid()) {
            blockedName = creature.getName() + creature.getId() + (100 + creature.numBlockingCreatures());
        }
        blockedCreature = creature;
    }

    public String getBlockedName() {
        return blockedName;
    }

    public MagicPermanentList getBlockingCreatures() {
        return blockingCreatures;
    }
    
    public int numBlockingCreatures() {
        return blockingCreatures.size();
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
    
    public MagicPermanent getPairedCreature() {
        return pairedCreature;
    }
    
    public void setPairedCreature(final MagicPermanent creature) {
        pairedCreature = creature;
    }
    
    public boolean isPaired() {
        return pairedCreature != MagicPermanent.NONE;
    }
    
    public MagicCardList getExiledCards() {
        return exiledCards;
    }
    
    public void addExiledCard(final MagicCard card) {
        // only non tokens can be added
        if (!card.isToken()) {
            exiledCards.add(card);
        }
    }
    
    public void removeExiledCard(final MagicCard card) {
        exiledCards.remove(card);
    }
    
    public MagicTarget getChosenTarget() {
        return chosenTarget;
    }
    
    public void setChosenTarget(final MagicTarget target) {
        chosenTarget = target;
    }
    
    void checkState(final MagicGame game, final List<MagicAction> actions) {
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
            
            // Soulbond
            if (pairedCreature.isValid() &&
                !pairedCreature.isCreature()) {
                game.doAction(new MagicSoulbondAction(this,pairedCreature,false));
            }
        } 
                
        if (cardDefinition.isAura()) {
            final MagicPlayAuraEvent auraEvent = (MagicPlayAuraEvent)cardDefinition.getCardEvent();
            //not targeting since Aura is already attached
            final MagicTargetChoice tchoice = new MagicTargetChoice(auraEvent.getTargetChoice(), false);
            if (!enchantedCreature.isValid() || 
                !game.isLegalTarget(getController(),this,tchoice,enchantedCreature) ||
                enchantedCreature.hasProtectionFrom(game,this)) {
                game.logAppendMessage(controller,getName()+" is put into its owner's graveyard.");
                actions.add(new MagicRemoveFromPlayAction(this,MagicLocationType.Graveyard));
            }
        } 
        
        if (cardDefinition.isEquipment() && equippedCreature.isValid()) {
            if (isCreature() || !equippedCreature.isCreature() || equippedCreature.hasProtectionFrom(game,this)) {
                actions.add(new MagicAttachEquipmentAction(this,MagicPermanent.NONE));
            }
        }
        
        // +1/+1 and -1/-1 counters cancel each other out.
        final int plusCounters=getCounters(MagicCounterType.PlusOne);
        if (plusCounters>0) {
            final int minusCounters=getCounters(MagicCounterType.MinusOne);
            if (minusCounters>0) {
                final int amount=-Math.min(plusCounters,minusCounters);
                actions.add(new MagicChangeCountersAction(this,MagicCounterType.PlusOne,amount,false));
                actions.add(new MagicChangeCountersAction(this,MagicCounterType.MinusOne,amount,false));
            }
        }
    }
    
    private static boolean hasProtectionFrom(final long abilityFlags,final MagicSource source, final MagicGame game) {
        
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
                if (sourcePermanent.hasSubType(MagicSubType.Demon,game) &&
                    MagicAbility.ProtectionFromDemons.hasAbility(abilityFlags)) {
                    return true;
                }
                // From Dragons.
                if (sourcePermanent.hasSubType(MagicSubType.Dragon,game) &&
                    MagicAbility.ProtectionFromDragons.hasAbility(abilityFlags)) {
                    return true;
                }
                // From Vampires.
                if (sourcePermanent.hasSubType(MagicSubType.Vampire,game) &&
                    MagicAbility.ProtectionFromVampires.hasAbility(abilityFlags)) {
                    return true;
                }
                // From Werewolves.
                if (sourcePermanent.hasSubType(MagicSubType.Werewolf,game) &&
                    MagicAbility.ProtectionFromWerewolves.hasAbility(abilityFlags)) {
                    return true;
                }
                // From Zombies.
                if (sourcePermanent.hasSubType(MagicSubType.Zombie,game) &&
                    MagicAbility.ProtectionFromZombies.hasAbility(abilityFlags)) {
                    return true;
                }
            }
        }

        return false;
    }
    
    public boolean hasProtectionFrom(final MagicGame game,final MagicSource source) {
        final long abilityFlags=getAllAbilityFlags(game);
        return hasProtectionFrom(abilityFlags,source,game);
    }
    
    public boolean canAttack(final MagicGame game) {
        if (!isCreature() || 
            !canTap(game) || 
            hasState(MagicPermanentState.ExcludeFromCombat) ||
            hasState(MagicPermanentState.CannotAttack)) {
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
            if (color.getLandwalkAbility().hasAbility(flags) && 
                player.controlsPermanentWithSubType(color.getLandSubType(),game)) {
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

        // Fear and Intimidate
        if (!isArtifact()) {
            final int colorFlags=getColorFlags();
            if (MagicAbility.Fear.hasAbility(attackerFlags)&&!MagicColor.Black.hasColor(colorFlags)) {
                return false;
            }
            if (MagicAbility.Intimidate.hasAbility(attackerFlags)&&((colorFlags&attacker.getColorFlags())==0)) {
                return false;
            }
        }
        
        // Shadow
        final long blockerFlags = getAllAbilityFlags(game);
        if (MagicAbility.Shadow.hasAbility(attackerFlags)) {
            if (!MagicAbility.Shadow.hasAbility(blockerFlags) &&
                !MagicAbility.CanBlockShadow.hasAbility(blockerFlags)) {
                return false;
            }
        } else if (MagicAbility.Shadow.hasAbility(blockerFlags)){
            return false;
        }
        
        // Flying and Reach
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
        
        // Subtype
        if (MagicAbility.CannotBeBlockedByHumans.hasAbility(attackerFlags)) {
            if (this.hasSubType(MagicSubType.Human,game)) {
                return false;
            }
        }

        // Can't be blocked by a color
        for (final MagicColor color : MagicColor.values()) {
            if (color.hasColor(this.getColorFlags()) &&
                color.getCannotBeBlockedByAbility().hasAbility(attackerFlags)) {
                return false;
            }
        }
             
        // Protection
        return !hasProtectionFrom(attackerFlags,this,game);
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
    }
    
    public void removeEquipment(final MagicPermanent equipment) {
        equipmentPermanents.remove(equipment);
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
    }
    
    public void removeAura(final MagicPermanent aura) {
        auraPermanents.remove(aura);
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
    
    private int getTypeFlags(final MagicGame game) {
        // Check if cached.
        if (cached) {
            return cachedTypeFlags;
        }
        
        int flags = cardDefinition.getTypeFlags();

        flags = MagicLayer.getTypeFlags(game,this,flags);

        return flags;
    }
    
    public boolean hasType(final MagicType type, final MagicGame game) {
        return type.hasType(getTypeFlags(game));
    }
    
    public boolean isLand() {
        return cardDefinition.isLand();
    }
    
    public boolean isCreature() {
        return MagicType.Creature.hasType(getTypeFlags(getGame()));
    }
    
    public boolean isEquipment() {
        return cardDefinition.isEquipment();
    }
    
    public boolean isArtifact() {
        return MagicType.Artifact.hasType(getTypeFlags(getGame()));
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
        if (MagicAbility.CannotBeTheTarget.hasAbility(flags) && source.getController() != controller) {
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
        return !hasProtectionFrom(flags,source,game);
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
            game.doAction(new MagicChangeStateAction(this,MagicPermanentState.ReturnToOwnerAtEndOfTurn,false));
            game.doAction(new MagicGainControlAction(getOwner(),this));
            return true;
        }
        return false;
    }
    
    public ImageIcon getIcon(final MagicGame game) {
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
