package magic.model;

import magic.ai.ArtificialScoringSystem;
import magic.data.IconImages;
import magic.model.action.MagicAttachEquipmentAction;
import magic.model.action.MagicChangeControlAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicGainControlAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.action.MagicSoulbondAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicActivation;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicPermanentStatic;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import javax.swing.ImageIcon;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.Collections;

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
        public MagicPowerToughness getPowerToughness() {
            return new MagicPowerToughness(0,0);
        }
        @Override
        public MagicPlayer getController() {
            return MagicPlayer.NONE;
        }
        @Override
        public boolean hasColor(final MagicColor color) {
            return false;
        }
        @Override
        public boolean hasType(final MagicType type) {
            return false;
        }
        @Override
        public boolean hasSubType(final MagicSubType type) {
            return false;
        }
        @Override
        public Set<MagicAbility> getAbilityFlags() {
            return Collections.emptySet();
        }
        @Override
        public boolean hasAbility(final MagicAbility ability) {
            return false;
        }
    };
        
    private final long id;
    private final MagicCardDefinition cardDefinition;
    private final MagicCard card;
    private final MagicPlayer firstController;
    private MagicPermanent equippedCreature = MagicPermanent.NONE;
    private final MagicPermanentSet equipmentPermanents;    
    private MagicPermanent enchantedCreature = MagicPermanent.NONE;
    private final MagicPermanentSet auraPermanents;    
    private MagicPermanent blockedCreature = MagicPermanent.NONE;
    private final MagicPermanentList blockingCreatures;    
    private MagicPermanent pairedCreature = MagicPermanent.NONE;
    private final MagicCardList exiledCards;
    private MagicTarget chosenTarget;
    private int[] counters=new int[MagicCounterType.NR_COUNTERS];
    private int stateFlags = 
            MagicPermanentState.Summoned.getMask() |
            MagicPermanentState.MustPayEchoCost.getMask();
    private int abilityPlayedThisTurn;
    private int damage;
    private int preventDamage;
    private int kicker;
    private final int fixedScore;
    private int score;

    // Allows cached retrieval of controller, type, subtype, color, abilites, and p/t
    // also acts as last known information
    private MagicPlayer cachedController;
    private int cachedTypeFlags;
    private Set<MagicSubType> cachedSubTypeFlags;
    private int cachedColorFlags;
    private Set<MagicAbility> cachedAbilityFlags;
    private MagicPowerToughness cachedPowerToughness;

    // remember order among blockers (blockedName + id + block order)
    private String blockedName;

    public MagicPermanent(final long aId,final MagicCard aCard,final MagicPlayer aController) {
        id = aId;
        card = aCard;
        cardDefinition = card.getCardDefinition();
        firstController = aController;
        
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
        firstController = copyMap.copy(sourcePermanent.firstController);
        stateFlags=sourcePermanent.stateFlags;
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
        kicker=sourcePermanent.kicker;
        fixedScore=sourcePermanent.fixedScore;
    
        cachedController     = copyMap.copy(sourcePermanent.cachedController);
        cachedTypeFlags      = sourcePermanent.cachedTypeFlags;
        cachedSubTypeFlags   = sourcePermanent.cachedSubTypeFlags;
        cachedColorFlags     = sourcePermanent.cachedColorFlags;
        cachedAbilityFlags   = sourcePermanent.cachedAbilityFlags;
        cachedPowerToughness = sourcePermanent.cachedPowerToughness;
    }
    
    @Override
    public MagicPermanent copy(final MagicCopyMap copyMap) {
        return new MagicPermanent(copyMap, this);
    }
    
    @Override
    public MagicPermanent map(final MagicGame game) {
        final MagicPlayer mappedController=getController().map(game);
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
    
    long getPermanentId() {
        final long[] input = {
            cardDefinition.getIndex(),
            stateFlags,
            damage,
            preventDamage,
            kicker,
            equippedCreature.getId(),
            enchantedCreature.getId(),
            blockedCreature.getId(),
            counters[0],
            counters[1],
            counters[2],
            counters[3],
            abilityPlayedThisTurn,
            cachedController.getId(),
            cachedTypeFlags,
            cachedSubTypeFlags.hashCode(),
            cachedColorFlags,
            cachedAbilityFlags.hashCode(),
            cachedPowerToughness.power(),
            cachedPowerToughness.toughness(),
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
    
    public boolean isToken() {
        return card.isToken();
    }
    
    public boolean isNonToken() {
        return !card.isToken();
    }
    
    @Override
    public MagicCardDefinition getCardDefinition() {
        return cardDefinition;
    }
    
    @Override
    public Collection<MagicActivation> getActivations() {
        return cardDefinition.getActivations();
    }
    
    public Collection<MagicManaActivation> getManaActivations() {
        return cardDefinition.getManaActivations();
    }

    public Collection<MagicStatic> getStatics() {
        return cardDefinition.getStatics();
    }
    
    public Collection<MagicTrigger<?>> getTriggers() {
        return cardDefinition.getTriggers();
    }
    
    public Collection<MagicWhenComesIntoPlayTrigger> getComeIntoPlayTriggers() {
        return cardDefinition.getComeIntoPlayTriggers();
    }

    public int getConvertedCost() {
        return cardDefinition.getConvertedCost();
    }

    public boolean producesMana() {
        return !cardDefinition.getManaActivations().isEmpty();
    }
    
    public int countManaActivations() {
        return cardDefinition.getManaActivations().size();
    }
    
    public String getName() {
        return card.getName();
    }
    
    @Override
    public String toString() {
        return getName();
    }

    @Override
    public MagicGame getGame() {
        return getOwner().getGame();
    }

    public MagicPlayer getOwner() {
        return card.getOwner();
    }

    public MagicPlayer getFirstController() {
        return firstController;
    }
    
    public MagicPlayer getController() {
        assert cachedController != null : "cachedController is null in " + this;
        return cachedController;
    }
    
    public MagicPlayer getOpponent() {
        return getController().getOpponent();
    }

    public boolean isFriend(final MagicObject other) {
        return getController() == other.getController();
    }
    
    public boolean isEnemy(final MagicObject other) {
        return getController() != other.getController();
    }
    
    public boolean isController(final MagicTarget player) {
        return getController() == player;
    }
    
    public boolean isOpponent(final MagicTarget player) {
        return getOpponent() == player;
    }
    
    public static void update(final MagicGame game) {
        MagicPermanent.updateProperties(game);
        MagicPermanent.updateScoreFixController(game);
    }
    
    public static void updateScoreFixController(final MagicGame game) {
        for (final MagicPlayer player : game.getPlayers()) {
        for (final MagicPermanent perm : player.getPermanents()) {
            final MagicPlayer curr = perm.getController();
            if (!curr.controlsPermanent(perm)) {
                game.addDelayedAction(new MagicChangeControlAction(curr, perm, perm.getScore()));
            } 
            perm.updateScore();
        }}
    }

    public static void updateProperties(final MagicGame game) {
        for (final MagicLayer layer : MagicLayer.PermanentLayers) {
            for (final MagicPlayer player : game.getPlayers()) {
                for (final MagicPermanent perm : player.getPermanents()) {
                    perm.apply(layer);
                }
            }
            for (final MagicPermanentStatic mpstatic : game.getStatics(layer)) {
                final MagicStatic mstatic = mpstatic.getStatic();
                final MagicPermanent source = mpstatic.getPermanent();
                for (final MagicPlayer player : game.getPlayers()) {
                for (final MagicPermanent perm : player.getPermanents()) {
                    if (mstatic.accept(game, source, perm)) {
                       perm.apply(source, mstatic);
                    }
                }}
            }
        }
    }
    
    private void apply(final MagicLayer layer) {
        switch (layer) {
            case Card:
                cachedController = firstController;
                cachedTypeFlags = cardDefinition.getTypeFlags();
                cachedSubTypeFlags = cardDefinition.genSubTypeFlags();
                cachedColorFlags = cardDefinition.getColorFlags();
                cachedAbilityFlags = cardDefinition.genAbilityFlags();
                cachedPowerToughness = cardDefinition.genPowerToughness();
                break;
            case CDASubtype:
                cardDefinition.applyCDASubType(getGame(), getController(), cachedSubTypeFlags);
                break;
            case CDAPT:
                cardDefinition.applyCDAPowerToughness(getGame(), getController(), this, cachedPowerToughness);
                break;
            default:
                break;
        }
    }

    private void apply(final MagicPermanent source, final MagicStatic mstatic) {
        final MagicLayer layer = mstatic.getLayer();
        switch (layer) {
            case Control:
                cachedController = mstatic.getController(source, this, cachedController);
                break;
            case Type:
                cachedTypeFlags = mstatic.getTypeFlags(this, cachedTypeFlags);
                mstatic.modSubTypeFlags(this, cachedSubTypeFlags);
                break;
            case Color:
                cachedColorFlags = mstatic.getColorFlags(this, cachedColorFlags);
                break;
            case Ability:
                mstatic.modAbilityFlags(source, this, cachedAbilityFlags);
                break;
            case SetPT:
            case ModPT:
            case CountersPT:
            case SwitchPT:
                mstatic.modPowerToughness(source, this, cachedPowerToughness);
                break;
            default:
                throw new RuntimeException("No case for " + layer + " in MagicPermanent.apply");
        }
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
    
    private int getColorFlags() {
        return cachedColorFlags;
    }

    @Override
    public boolean hasColor(final MagicColor color) {
        return color.hasColor(getColorFlags()); 
    }
        
    public void changeCounters(final MagicCounterType counterType,final int amount) {
        counters[counterType.ordinal()]+=amount;
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
    
    public boolean hasSubType(final MagicSubType subType) {
        return cachedSubTypeFlags.contains(subType);
    }

    public boolean hasAllCreatureTypes() {
        return cachedSubTypeFlags.equals(MagicSubType.ALL_CREATURES);
    }

    public MagicPowerToughness getPowerToughness() {
        return cachedPowerToughness;
    }
    
    public int getPower() {
        return getPowerToughness().getPositivePower();
    }
    
    public int getToughness() {
        return getPowerToughness().getPositiveToughness();
    }
    
    public Set<MagicAbility> getAbilityFlags() {
        return cachedAbilityFlags;
    }

    @Override
    public boolean hasAbility(final MagicAbility ability) {
        return cachedAbilityFlags.contains(ability);
    }
        
    private void updateScore() {
        score = fixedScore + ArtificialScoringSystem.getVariablePermanentScore(this);
    }
    
    public int getScore() {
        return score;
    }
    
    public int getStaticScore() {
        return cardDefinition.getStaticType().getScore(this);
    }
    
    public int getCardScore() {
        return cardDefinition.getScore();
    }
    
    void setCached(final boolean aCached) {
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

    public void setKicker(final int aKicker) {
        kicker = aKicker;
    }

    public int getKicker() {
        return kicker;
    }
    
    public boolean isKicked() {
        return kicker > 0;
    }

    public int getLethalDamage(final int toughness) {
        return toughness<=damage?0:toughness-damage;
    }
    
    // Tap symbol.
    public boolean canTap() {
        return !hasState(MagicPermanentState.Tapped) && 
            (!hasState(MagicPermanentState.Summoned) || 
             !isCreature() || 
             hasAbility(MagicAbility.Haste)
            );
    }
    
    // Untap symbol.
    public boolean canUntap() {
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
    
    void generateStateBasedActions() {
        final MagicGame game = getGame();
        if (isCreature()) {
            final int toughness=getToughness();
            if (toughness<=0) {
                game.logAppendMessage(getController(),getName()+" is put into its owner's graveyard.");
                game.addDelayedAction(new MagicRemoveFromPlayAction(this,MagicLocationType.Graveyard));
            } else if (hasState(MagicPermanentState.Destroyed)) {
                game.addDelayedAction(new MagicChangeStateAction(this,MagicPermanentState.Destroyed,false));
                game.addDelayedAction(new MagicDestroyAction(this));
            } else if (toughness-damage<=0) {
                game.addDelayedAction(new MagicDestroyAction(this));
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
                enchantedCreature.hasProtectionFrom(this)) {
                game.logAppendMessage(getController(),getName()+" is put into its owner's graveyard.");
                game.addDelayedAction(new MagicRemoveFromPlayAction(this,MagicLocationType.Graveyard));
            }
        } 
        
        if (cardDefinition.isEquipment() && equippedCreature.isValid()) {
            if (isCreature() || !equippedCreature.isCreature() || equippedCreature.hasProtectionFrom(this)) {
                game.addDelayedAction(new MagicAttachEquipmentAction(this,MagicPermanent.NONE));
            }
        }
        
        // +1/+1 and -1/-1 counters cancel each other out.
        final int plusCounters=getCounters(MagicCounterType.PlusOne);
        if (plusCounters>0) {
            final int minusCounters=getCounters(MagicCounterType.MinusOne);
            if (minusCounters>0) {
                final int amount=-Math.min(plusCounters,minusCounters);
                game.addDelayedAction(new MagicChangeCountersAction(this,MagicCounterType.PlusOne,amount,false));
                game.addDelayedAction(new MagicChangeCountersAction(this,MagicCounterType.MinusOne,amount,false));
            }
        }
    }
    
    public boolean hasProtectionFrom(final MagicSource source) {
        // From a color.
        int numColors = 0;
        for (final MagicColor color : MagicColor.values()) {
            if (source.hasColor(color)) {
                numColors++;
                if (hasAbility(color.getProtectionAbility()) ||
                    hasAbility(MagicAbility.ProtectionFromAllColors)) {
                    return true;
                }
            }
        }
        
        // From monocolored.
        if (numColors == 1 &&
            hasAbility(MagicAbility.ProtectionFromMonoColored)) {
            return true;
        }

        if (!source.isCreature()) {
            return false;
        }

        final MagicPermanent creature = (MagicPermanent)source;
                
        // From creatures.
        if (hasAbility(MagicAbility.ProtectionFromCreatures)) {
            return true;
        }
        // From Demons.
        if (creature.hasSubType(MagicSubType.Demon) &&
            hasAbility(MagicAbility.ProtectionFromDemons)) {
            return true;
        }
        // From Dragons.
        if (creature.hasSubType(MagicSubType.Dragon) &&
            hasAbility(MagicAbility.ProtectionFromDragons)) {
            return true;
        }
        // From Vampires.
        if (creature.hasSubType(MagicSubType.Vampire) &&
            hasAbility(MagicAbility.ProtectionFromVampires)) {
            return true;
        }
        // From Werewolves.
        if (creature.hasSubType(MagicSubType.Werewolf) &&
            hasAbility(MagicAbility.ProtectionFromWerewolves)) {
            return true;
        }
        // From Zombies.
        if (creature.hasSubType(MagicSubType.Zombie) &&
            hasAbility(MagicAbility.ProtectionFromZombies)) {
            return true;
        }
        
        return false;
    }
    
    public boolean canAttack() {
        if (!isCreature() || 
            !canTap() || 
            hasState(MagicPermanentState.ExcludeFromCombat) ||
            hasState(MagicPermanentState.CannotAttack)) {
            return false;
        }
        return !hasAbility(MagicAbility.CannotAttackOrBlock) && 
               !hasAbility(MagicAbility.Defender);
    }
    
    public boolean canBlock() {
        if (!isCreature() || 
            isTapped() || 
            hasState(MagicPermanentState.ExcludeFromCombat)) {
            return false;
        }
        return !hasAbility(MagicAbility.CannotAttackOrBlock) &&
               !hasAbility(MagicAbility.CannotBlock);
    }
    
    public boolean canBeBlocked(final MagicPlayer defendingPlayer) {
        // Unblockable
        if (hasAbility(MagicAbility.Unblockable)) {
            return false;
        }        
        
        // Landwalk
        for (final MagicColor color : MagicColor.values()) {
            if (hasAbility(color.getLandwalkAbility()) && 
                defendingPlayer.controlsPermanentWithSubType(color.getLandSubType())) {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean canBlock(final MagicPermanent attacker) {
        // Fear and Intimidate
        if (!isArtifact()) {
            if (attacker.hasAbility(MagicAbility.Fear) &&
                !hasColor(MagicColor.Black)) {
                return false;
            }
            if (attacker.hasAbility(MagicAbility.Intimidate) &&
                ((getColorFlags() & attacker.getColorFlags())==0)) {
                return false;
            }
        }
        
        // Shadow
        if (attacker.hasAbility(MagicAbility.Shadow)) {
            if (!hasAbility(MagicAbility.Shadow) &&
                !hasAbility(MagicAbility.CanBlockShadow)) {
                return false;
            }
        } else if (hasAbility(MagicAbility.Shadow)) {
            return false;
        }
        
        // Flying
        if (attacker.hasAbility(MagicAbility.CannotBeBlockedByFlying) &&
            hasAbility(MagicAbility.Flying)) {
            return false;
        }
            
        if (attacker.hasAbility(MagicAbility.CannotBeBlockedExceptWithFlying) &&
            !hasAbility(MagicAbility.Flying)) {
            return false;
        }
        
        if (!attacker.hasAbility(MagicAbility.Flying) &&
            hasAbility(MagicAbility.CannotBlockWithoutFlying)) {
            return false;
        }

        // Reach
        if (attacker.hasAbility(MagicAbility.Flying) &&
            !hasAbility(MagicAbility.Flying) &&
            !hasAbility(MagicAbility.Reach)) {
            return false;
        } 

        if (attacker.hasAbility(MagicAbility.CannotBeBlockedExceptWithFlyingOrReach) &&
            !hasAbility(MagicAbility.Flying) &&
            !hasAbility(MagicAbility.Reach)) {
            return false;
        }
        
        // Subtype
        if (attacker.hasAbility(MagicAbility.CannotBeBlockedByHumans) &&
            hasSubType(MagicSubType.Human)) {
            return false;
        }
        
        if (attacker.hasAbility(MagicAbility.CannotBeBlockedExceptBySliver) &&
            !hasSubType(MagicSubType.Sliver)) {
            return false;
        }

        // Can't be blocked by a color
        for (final MagicColor color : MagicColor.values()) {
            if (attacker.hasAbility(color.getCannotBeBlockedByAbility()) &&
                hasColor(color)) {
                return false;
            }
        }
             
        // Protection
        return !attacker.hasProtectionFrom(this);
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
    
    private int getTypeFlags() {
        return cachedTypeFlags;
    }
    
    public boolean hasType(final MagicType type) {
        return type.hasType(getTypeFlags());
    }
    
    public boolean isBasic() {
        return hasType(MagicType.Basic);
    }
    
    public boolean isLand() {
        return hasType(MagicType.Land);
    }
    
    public boolean isCreature() {
        return hasType(MagicType.Creature);
    }
    
    public boolean isEquipment() {
        return isArtifact() && hasSubType(MagicSubType.Equipment);
    }
    
    public boolean isArtifact() {
        return hasType(MagicType.Artifact);
    }
    
    public boolean isEnchantment() {
        return hasType(MagicType.Enchantment);
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
    public boolean isValidTarget(final MagicSource source) {
        // Can't be the target of spells or abilities.
        if (hasAbility(MagicAbility.Shroud)) {
            return false;
        }

        // Can't be the target of spells or abilities your opponents controls.
        if (hasAbility(MagicAbility.Hexproof) && source.getController() != getController()) {
            return false;
        }

        // Can't be the target of spells or abilities player 0 controls.
        if (hasAbility(MagicAbility.CannotBeTheTarget0) && source.getController().getIndex() == 0) {
            return false;
        }
        
        // Can't be the target of spells or abilities player 1 controls.
        if (hasAbility(MagicAbility.CannotBeTheTarget1) && source.getController().getIndex() == 1) {
            return false;
        }

        // Protection.
        return !hasProtectionFrom(source);
    }

    @Override
    public int compareTo(final MagicPermanent permanent) {
        // Important for sorting of permanent mana activations.
        final int diff = cardDefinition.getIndex() - permanent.cardDefinition.getIndex();
        if (diff != 0) {
            return diff;
        } else {
            return Long.signum(id - permanent.id);
        }
    }

    public boolean endOfTurn(final MagicGame game) {
        if (MagicPermanentState.RemoveAtEndOfTurn.hasState(stateFlags)) {
            game.logAppendMessage(getController(),"Exile "+this.getName()+" (end of turn).");
            game.doAction(new MagicRemoveFromPlayAction(this,MagicLocationType.Exile));
            return true;
        } else if (MagicPermanentState.RemoveAtEndOfYourTurn.hasState(stateFlags) && getController()==game.getTurnPlayer()) {
            game.logAppendMessage(getController(),"Exile "+this.getName()+" (end of turn).");
            game.doAction(new MagicRemoveFromPlayAction(this,MagicLocationType.Exile));
            return true;
        } else if (MagicPermanentState.SacrificeAtEndOfTurn.hasState(stateFlags)) {
            game.logAppendMessage(getController(),"Sacrifice "+this.getName()+" (end of turn).");
            game.doAction(new MagicSacrificeAction(this));
            return true;
        } else if (MagicPermanentState.ReturnToOwnerAtEndOfTurn.hasState(stateFlags)) {
            game.logAppendMessage(getController(),"Return "+this.getName()+" to its owner (end of turn).");
            game.doAction(new MagicChangeStateAction(this,MagicPermanentState.ReturnToOwnerAtEndOfTurn,false));
            game.doAction(new MagicGainControlAction(getOwner(),this));
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
    
    @Override
    public boolean isLegalTarget(final MagicPlayer player, final MagicTargetFilter<? extends MagicTarget> targetFilter) {
        return getController().controlsPermanent(this);
    }
}
