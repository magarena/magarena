package magic.model;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import magic.ai.ArtificialScoringSystem;
import magic.data.CardDefinitions;
import magic.model.action.AttachAction;
import magic.model.action.ChangeControlAction;
import magic.model.action.ChangeCountersAction;
import magic.model.action.ChangeStateAction;
import magic.model.action.DestroyAction;
import magic.model.action.RemoveFromPlayAction;
import magic.model.action.SoulbondAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicActivation;
import magic.model.event.MagicBestowActivation;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSourceActivation;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicPermanentStatic;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;
import magic.model.trigger.EntersBattlefieldTrigger;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class MagicPermanent extends MagicObjectImpl implements MagicSource, MagicTarget, Comparable<MagicPermanent>, MagicMappable<MagicPermanent>, IRenderableCard {

    public static final int NO_COLOR_FLAGS = -1;

    private final long id;
    private final MagicCardDefinition cardDefinition;
    private final MagicCard card;
    private final MagicPlayer firstController;
    private MagicPermanent equippedCreature = MagicPermanent.NONE;
    private final MagicPermanentSet equipmentPermanents;
    private MagicPermanent enchantedPermanent = MagicPermanent.NONE;
    private final MagicPermanentSet auraPermanents;
    private MagicPermanent blockedCreature = MagicPermanent.NONE;
    private final MagicPermanentList blockingCreatures;
    private MagicPermanent pairedCreature = MagicPermanent.NONE;
    private final MagicCardList exiledCards;
    private MagicPlayer chosenPlayer = MagicPlayer.NONE;
    private Map<MagicCounterType, Integer> counters;
    private int stateFlags =
        MagicPermanentState.Summoned.getMask() |
        MagicPermanentState.MustPayEchoCost.getMask();
    private int abilityPlayedThisTurn;
    private int damage;
    private int preventDamage;
    private int fixedScore;
    private int score;

    // Allows cached retrieval of controller, type, subtype, color, abilites, and p/t
    // also acts as last known information
    private MagicPlayer cachedController;
    private int cachedTypeFlags;
    private Set<MagicSubType> cachedSubTypeFlags;
    private int cachedColorFlags;
    private Set<MagicAbility> cachedAbilityFlags;
    private MagicPowerToughness cachedPowerToughness;
    private List<MagicActivation<MagicPermanent>> cachedActivations;
    private List<MagicManaActivation> cachedManaActivations;
    private List<MagicTrigger<?>> cachedTriggers;
    private List<EntersBattlefieldTrigger> etbTriggers;

    // remember order among blockers (blockedName + id + block order)
    private String blockedName;
    private long stateId;

    public MagicPermanent(final long aId, final MagicCard aCard, final MagicPlayer aController) {
        this(aId, aCard, aCard.getCardDefinition(), aController);
    }

    public MagicPermanent(final long aId, final MagicCard aCard, final MagicCardDefinition aCardDef, final MagicPlayer aController) {
        id = aId;
        card = aCard;
        cardDefinition = aCardDef;
        firstController = aController;

        counters = new EnumMap<MagicCounterType, Integer>(MagicCounterType.class);
        equipmentPermanents = new MagicPermanentSet();
        auraPermanents = new MagicPermanentSet();
        blockingCreatures = new MagicPermanentList();
        exiledCards = new MagicCardList();

        cachedController = firstController;
        cachedTypeFlags = getCardDefinition().getTypeFlags();
        cachedSubTypeFlags = getCardDefinition().genSubTypes();
        cachedColorFlags = getCardDefinition().getColorFlags();
        cachedAbilityFlags = getCardDefinition().genAbilityFlags();
        cachedPowerToughness = getCardDefinition().genPowerToughness();
        cachedActivations = new LinkedList<MagicActivation<MagicPermanent>>();
        cachedManaActivations = new LinkedList<MagicManaActivation>();
        cachedTriggers = new LinkedList<MagicTrigger<?>>();
        etbTriggers = new LinkedList<EntersBattlefieldTrigger>();
    }

    private MagicPermanent(final MagicCopyMap copyMap, final MagicPermanent sourcePermanent) {
        id = sourcePermanent.id;
        cardDefinition = sourcePermanent.cardDefinition;

        copyMap.put(sourcePermanent, this);

        card = copyMap.copy(sourcePermanent.card);
        firstController = copyMap.copy(sourcePermanent.firstController);
        stateFlags = sourcePermanent.stateFlags;
        counters = new EnumMap<MagicCounterType, Integer>(sourcePermanent.counters);
        abilityPlayedThisTurn = sourcePermanent.abilityPlayedThisTurn;
        equippedCreature = copyMap.copy(sourcePermanent.equippedCreature);
        equipmentPermanents = new MagicPermanentSet(copyMap, sourcePermanent.equipmentPermanents);
        enchantedPermanent = copyMap.copy(sourcePermanent.enchantedPermanent);
        auraPermanents = new MagicPermanentSet(copyMap, sourcePermanent.auraPermanents);
        blockedCreature = copyMap.copy(sourcePermanent.blockedCreature);
        blockingCreatures = new MagicPermanentList(copyMap, sourcePermanent.blockingCreatures);
        pairedCreature = copyMap.copy(sourcePermanent.pairedCreature);
        exiledCards = new MagicCardList(copyMap, sourcePermanent.exiledCards);
        chosenPlayer = copyMap.copy(sourcePermanent.chosenPlayer);
        damage = sourcePermanent.damage;
        preventDamage = sourcePermanent.preventDamage;
        fixedScore = sourcePermanent.fixedScore;
        score = sourcePermanent.score;
        stateId = sourcePermanent.stateId;

        cachedController = copyMap.copy(sourcePermanent.cachedController);
        cachedTypeFlags = sourcePermanent.cachedTypeFlags;
        cachedSubTypeFlags = sourcePermanent.cachedSubTypeFlags;
        cachedColorFlags = sourcePermanent.cachedColorFlags;
        cachedAbilityFlags = sourcePermanent.cachedAbilityFlags;
        cachedPowerToughness = sourcePermanent.cachedPowerToughness;
        cachedActivations = new LinkedList<MagicActivation<MagicPermanent>>(sourcePermanent.cachedActivations);
        cachedManaActivations = new LinkedList<MagicManaActivation>(sourcePermanent.cachedManaActivations);
        cachedTriggers = new LinkedList<MagicTrigger<?>>(sourcePermanent.cachedTriggers);
        etbTriggers = new LinkedList<EntersBattlefieldTrigger>(sourcePermanent.etbTriggers);
    }

    @Override
    public MagicPermanent copy(final MagicCopyMap copyMap) {
        return new MagicPermanent(copyMap, this);
    }

    @Override
    public MagicPermanent map(final MagicGame game) {
        final MagicPlayer mappedController = getController().map(game);
        final MagicPermanent found = mappedController.getPermanents().getPermanent(id);
        if (found.isValid()) {
            return found;
        } else {
            return mappedController.getOpponent().getPermanents().getPermanent(id);
        }
    }

    public long getId() {
        return id;
    }

    public boolean isValid() {
        return getController().controlsPermanent(this);
    }

    public boolean isInvalid() {
        return !isValid();
    }

    public long getStateId() {
        stateId = stateId != 0 ? stateId : MurmurHash3.hash(new long[]{
            cardDefinition.getIndex(),
            card.getStateId(),
            stateFlags,
            damage,
            preventDamage,
            equippedCreature.getStateId(),
            enchantedPermanent.getStateId(),
            blockingCreatures.getStateId(),
            //pairedCreature.getStateId(),
            exiledCards.getUnorderedStateId(),
            chosenPlayer.getId(),
            getCountersHash(),
            abilityPlayedThisTurn,
            cachedController.getId(),
            cachedTypeFlags,
            cachedSubTypeFlags.hashCode(),
            cachedColorFlags,
            cachedAbilityFlags.hashCode(),
            cachedPowerToughness.power(),
            cachedPowerToughness.toughness(),
            cachedActivations.hashCode(),
            cachedManaActivations.hashCode(),
            cachedTriggers.hashCode(),
            etbTriggers.hashCode()
        });
        return stateId;
    }

    public long getRenderKey() {
        return getStateId();
    }

    private long getCountersHash() {
        final long[] keys = new long[counters.size() * 2];
        int idx = 0;
        for (final Entry<MagicCounterType, Integer> entry : counters.entrySet()) {
            keys[idx + 0] = entry.getKey().ordinal();
            keys[idx + 1] = entry.getValue();
            idx += 2;
        }
        return MurmurHash3.hash(keys);
    }

    /**
     * Determines uniqueness of a mana permanent, e.g. for producing mana, all Mountains are equal.
     */
    public int getManaId() {
        // Creatures or lands that can be animated are unique
        // Enchanted/equipped permanents are unique
        // 'Summoned' permanents are unique
        if (hasExcludeManaOrCombat() || isEnchanted() || isEquipped() || hasState(MagicPermanentState.Summoned)) {
            return (int) id;
        }
        // Uniqueness is determined by card definition and number of charge counters.
        return -((cardDefinition.getIndex() << 16) + getCounters(MagicCounterType.Charge));
    }

    public boolean hasExcludeManaOrCombat() {
        return getCardDefinition().hasExcludeManaOrCombat() || (producesMana() && isCreature());
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

    public boolean isDoubleFaced() {
        return card.isDoubleFaced();
    }

    public boolean isFlipCard() {
        return card.isFlipCard();
    }

    @Override
    public MagicCardDefinition getCardDefinition() {
        if (isFaceDown()) {
            if (hasState(MagicPermanentState.Manifest)) {
                return CardDefinitions.getCard("Manifest");
            } else {
                return CardDefinitions.getCard("Morph");
            }
        } else if (isFlipped()) {
            return cardDefinition.getFlippedDefinition();
        } else if (isTransformed()) {
            return cardDefinition.getTransformedDefinition();
        } else {
            return cardDefinition;
        }
    }

    // only for rendering the card image popup
    final public MagicCardDefinition getRealCardDefinition() {
        if (isFaceDown()) {
            return cardDefinition;
        } else {
            return getCardDefinition();
        }
    }

    @Override
    public Collection<MagicSourceActivation<? extends MagicSource>> getSourceActivations() {
        List<MagicSourceActivation<? extends MagicSource>> sourceActs = new LinkedList<>();
        for (final MagicActivation<MagicPermanent> act : cachedActivations) {
            sourceActs.add(MagicSourceActivation.create(this, act));
        }
        return sourceActs;
    }

    public void loseAllAbilities() {
        cachedActivations.clear();
        cachedManaActivations.clear();
        cachedTriggers.clear();
        cachedAbilityFlags.clear();
        etbTriggers.clear();
    }

    public void addAbility(final MagicAbility ability, final Set<MagicAbility> flags) {
        final MagicAbilityList abilityList = new MagicAbilityList();
        ability.addAbility(abilityList);
        abilityList.giveAbility(this, flags);
    }

    public void addAbility(final MagicAbilityList abilityList) {
        abilityList.giveAbility(this, cachedAbilityFlags);
    }

    public void addAbility(final MagicAbility ability) {
        final MagicAbilityList abilityList = new MagicAbilityList();
        ability.addAbility(abilityList);
        abilityList.giveAbility(this, cachedAbilityFlags);
    }

    public void addAbility(final MagicActivation<MagicPermanent> act) {
        cachedActivations.add(act);
    }

    public void addAbility(final MagicTrigger<?> trig) {
        if (trig instanceof EntersBattlefieldTrigger) {
            etbTriggers.add((EntersBattlefieldTrigger) trig);
        } else {
            cachedTriggers.add(trig);
        }
    }

    public void addAbility(final MagicManaActivation act) {
        cachedManaActivations.add(act);
    }

    public Collection<MagicActivation<MagicPermanent>> getActivations() {
        return cachedActivations;
    }

    public Collection<MagicManaActivation> getManaActivations() {
        return cachedManaActivations;
    }

    public Collection<MagicStatic> getStatics() {
        return getCardDefinition().getStatics();
    }

    public Collection<MagicTrigger<?>> getTriggers() {
        return cachedTriggers;
    }

    public Collection<EntersBattlefieldTrigger> getComeIntoPlayTriggers() {
        return etbTriggers;
    }

    public int getConvertedCost() {
        if (isTransformed() && isDoubleFaced()) {
            return getCardDefinition().getTransformedDefinition().getConvertedCost();
        }
        return getCardDefinition().getConvertedCost();
    }

    public int getDevotion(final MagicColor... colors) {
        return getCardDefinition().getCost().getDevotion(colors);
    }

    public boolean producesMana() {
        return !cachedManaActivations.isEmpty();
    }

    public int countManaActivations() {
        return cachedManaActivations.size();
    }

    public boolean isName(final String other) {
        final String name = getName();
        return name.isEmpty() == false && name.equalsIgnoreCase(other);
    }

    public String getName() {
        final String name = getCardDefinition().getName();
        if (name.isEmpty() && getGame().isReal()) {
            return "Permanent #" + (id % 1000);
        } else {
            return name;
        }
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

    @Override
    public MagicPlayer getController() {
        assert cachedController != null : "cachedController is null in " + this;
        return cachedController;
    }

    public boolean isOwner(final MagicTarget player) {
        return getOwner() == player;
    }

    public boolean isController(final MagicTarget player) {
        return getController() == player;
    }

    public boolean isOpponent(final MagicTarget player) {
        return getOpponent() == player;
    }

    public static void update(final MagicGame game) {
        updateProperties(game);
        updateScoreFixController(game);
    }

    private static void updateScoreFixController(final MagicGame game) {
        for (final MagicPlayer player : game.getPlayers()) {
            for (final MagicPermanent perm : player.getPermanents()) {
                final MagicPlayer curr = perm.getController();
                if (curr != player) {
                    game.addDelayedAction(new ChangeControlAction(curr, perm, perm.getScore()));
                }
                perm.updateScore();
            }
        }
    }

    private static void updateProperties(final MagicGame game) {
        for (final MagicLayer layer : MagicLayer.PERMANENT) {
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
                    }
                }
            }
        }
    }

    private void apply(final MagicLayer layer) {
        switch (layer) {
            case Card:
                cachedController = firstController;
                cachedTypeFlags = getCardDefinition().getTypeFlags();
                cachedSubTypeFlags = getCardDefinition().genSubTypes();
                cachedColorFlags = getCardDefinition().getColorFlags();
                cachedAbilityFlags = getCardDefinition().genAbilityFlags();
                cachedPowerToughness = getCardDefinition().genPowerToughness();
                cachedActivations = new LinkedList<MagicActivation<MagicPermanent>>(getCardDefinition().getActivations());
                cachedActivations.addAll(cardDefinition.getMorphActivations());
                cachedManaActivations = new LinkedList<MagicManaActivation>(getCardDefinition().getManaActivations());
                cachedTriggers = new LinkedList<MagicTrigger<?>>(getCardDefinition().getTriggers());
                etbTriggers = new LinkedList<EntersBattlefieldTrigger>(getCardDefinition().getETBTriggers());
                break;
            case CDASubtype:
                getCardDefinition().applyCDASubType(getGame(), getController(), cachedSubTypeFlags);
                break;
            case CDAColor:
                cachedColorFlags = getCardDefinition().applyCDAColor(getGame(), getController(), cachedColorFlags);
                break;
            case CDAPT:
                getCardDefinition().applyCDAPowerToughness(getGame(), getController(), this, cachedPowerToughness);
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
            case AbilityCond:
                mstatic.modAbilityFlags(source, this, cachedAbilityFlags);
                break;
            case SetPT:
            case ModPT:
            case CountersPT:
            case SwitchPT:
                mstatic.modPowerToughness(source, this, cachedPowerToughness);
                break;
            case Game:
                mstatic.modAbilityFlags(source, this, cachedAbilityFlags);
                mstatic.modPowerToughness(source, this, cachedPowerToughness);
                break;
            default:
                break;
        }
    }

    public void setState(final MagicPermanentState state) {
        stateFlags |= state.getMask();
    }

    public void clearState(final MagicPermanentState state) {
        stateFlags &= Integer.MAX_VALUE - state.getMask();
    }

    public boolean hasState(final MagicPermanentState state) {
        return state.hasState(stateFlags);
    }

    public int getStateFlags() {
        return stateFlags;
    }

    public void setStateFlags(final int flags) {
        stateFlags = flags;
    }

    public boolean isTapped() {
        return hasState(MagicPermanentState.Tapped);
    }

    public boolean isUntapped() {
        return !hasState(MagicPermanentState.Tapped);
    }

    @Override
    public int getColorFlags() {
        return cachedColorFlags;
    }

    @Override
    public boolean hasColor(final MagicColor color) {
        return color.hasColor(getColorFlags());
    }

    @Override
    public int getCounters(final MagicCounterType counterType) {
        final Integer cnt = counters.get(counterType);
        return cnt != null ? cnt : 0;
    }

    @Override
    public void changeCounters(final MagicCounterType counterType, final int amount) {
        final int oldAmt = getCounters(counterType);
        final int newAmt = oldAmt + amount;
        if (newAmt == 0) {
            counters.remove(counterType);
        } else {
            counters.put(counterType, newAmt);
        }
    }

    public Collection<MagicCounterType> getCounterTypes() {
        return counters.keySet();
    }

    public boolean hasCounters() {
        return counters.size() > 0;
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

    public int getPowerValue() {
        return getPowerToughness().power();
    }

    public int getToughness() {
        return getPowerToughness().getPositiveToughness();
    }

    public int getToughnessValue() {
        return getPowerToughness().toughness();
    }

    public Set<MagicAbility> getAbilityFlags() {
        return cachedAbilityFlags;
    }

    @Override
    public boolean hasAbility(final MagicAbility ability) {
        return cachedAbilityFlags.contains(ability);
    }

    private void updateScore() {
        stateId = 0;
        fixedScore = ArtificialScoringSystem.getFixedPermanentScore(this);
        score = fixedScore + ArtificialScoringSystem.getVariablePermanentScore(this);
    }

    public int getScore() {
        return score;
    }

    public int getStaticScore() {
        return cardDefinition.getStaticType().getScore(this);
    }

    public int getCountersScore() {
        int amount = 0;
        for (final Entry<MagicCounterType, Integer> entry : counters.entrySet()) {
            amount += entry.getKey().getScore() * entry.getValue();
        }
        return amount;
    }

    public int getCardScore() {
        return cardDefinition.getScore();
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(final int aDamage) {
        damage = aDamage;
    }

    @Override
    public int getPreventDamage() {
        return preventDamage;
    }

    @Override
    public void setPreventDamage(final int amount) {
        preventDamage = amount;
    }

    public int getLethalDamage(final int toughness) {
        return toughness <= damage ? 0 : toughness - damage;
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
        return !hasState(MagicPermanentState.Regenerated) && !hasState(MagicPermanentState.CannotBeRegenerated);
    }

    public boolean isRegenerated() {
        return hasState(MagicPermanentState.Regenerated) && !hasState(MagicPermanentState.CannotBeRegenerated);
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

    public MagicCard getExiledCard() {
        return exiledCards.getCardAtTop();
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

    public MagicPlayer getChosenPlayer() {
        return chosenPlayer;
    }

    public void setChosenPlayer(final MagicPlayer player) {
        chosenPlayer = player;
    }

    void generateStateBasedActions() {
        final MagicGame game = getGame();
        if (isCreature()) {
            final int toughness = getToughness();
            if (toughness <= 0) {
                game.logAppendMessage(
                    getController(),
                    MagicMessage.format("%s is put into its owner's graveyard.", this)
                );
                game.addDelayedAction(new RemoveFromPlayAction(this, MagicLocationType.Graveyard));
            } else if (hasState(MagicPermanentState.Destroyed)) {
                game.addDelayedAction(ChangeStateAction.Clear(this, MagicPermanentState.Destroyed));
                game.addDelayedAction(new DestroyAction(this));
            } else if (toughness - damage <= 0) {
                game.addDelayedAction(new DestroyAction(this));
            }

            // Soulbond
            if (pairedCreature.isValid() && pairedCreature.isCreature() == false) {
                game.logAppendMessage(
                    getController(),
                    MagicMessage.format("%s becomes unpaired as %s is no longer a creature.", this, pairedCreature)
                );
                game.addDelayedAction(new SoulbondAction(this, pairedCreature, false));
            }
        }

        if (isAura()) {
            //not targeting since Aura is already attached
            final MagicTargetChoice tchoice = new MagicTargetChoice(getAuraTargetChoice(), false);
            String reason = "";
            if (isCreature()) {
                reason = "it is a creature.";
            } else if (enchantedPermanent.isValid() == false
                || game.isLegalTarget(getController(), this, tchoice, enchantedPermanent) == false) {
                reason = "it no longer enchants a valid permanent.";
            } else if (enchantedPermanent.hasProtectionFrom(this)) {
                reason = MagicMessage.format("%s has protection.", enchantedPermanent);
            }

            if (reason.isEmpty() == false) {
                // 702.102e If an Aura with bestow is attached to an illegal object or player, it becomes unattached.
                // This is an exception to rule 704.5n.
                if (hasAbility(MagicAbility.Bestow)) {
                    game.logAppendMessage(
                        getController(),
                        MagicMessage.format("%s becomes unattached as %s", this, reason)
                    );
                    game.addDelayedAction(new AttachAction(this, MagicPermanent.NONE));
                } else {
                    // 704.5n
                    game.logAppendMessage(
                        getController(),
                        MagicMessage.format("%s is put into its owner's graveyard as %s", this, reason)
                    );
                    game.addDelayedAction(new RemoveFromPlayAction(this, MagicLocationType.Graveyard));
                }
            }
        }

        if (isEquipment() && equippedCreature.isValid()) {
            String reason = "";
            if (isCreature()) {
                reason = "it is a creature.";
            } else if (equippedCreature.isCreature() == false) {
                reason = MagicMessage.format("%s is no longer a creature.", equippedCreature);
            } else if (equippedCreature.hasProtectionFrom(this)) {
                reason = MagicMessage.format("%s has protection.", equippedCreature);
            }
            if (reason.isEmpty() == false) {
                game.logAppendMessage(
                    getController(),
                    MagicMessage.format("%s becomes unattached as %s", this, reason)
                );
                game.addDelayedAction(new AttachAction(this, MagicPermanent.NONE));
            }
        }

        // rule 704.5i If a planeswalker has loyalty 0, it's put into its owner's graveyard.
        if (isPlaneswalker() && getCounters(MagicCounterType.Loyalty) == 0) {
            game.logAppendMessage(
                getController(),
                MagicMessage.format("%s is put into its owner's graveyard.", this)
            );
            game.addDelayedAction(new RemoveFromPlayAction(this, MagicLocationType.Graveyard));
        }

        // +1/+1 and -1/-1 counters cancel each other out.
        final int plusCounters = getCounters(MagicCounterType.PlusOne);
        if (plusCounters > 0) {
            final int minusCounters = getCounters(MagicCounterType.MinusOne);
            if (minusCounters > 0) {
                final int amount = -Math.min(plusCounters, minusCounters);
                game.addDelayedAction(ChangeCountersAction.Enters(this, MagicCounterType.PlusOne, amount));
                game.addDelayedAction(ChangeCountersAction.Enters(this, MagicCounterType.MinusOne, amount));
            }
        }
    }

    public boolean hasProtectionFrom(final MagicSource source) {
        // from everything
        if (hasAbility(MagicAbility.ProtectionFromEverything)) {
            return true;
        }

        // from a color
        int numColors = 0;
        for (final MagicColor color : MagicColor.values()) {
            if (source.hasColor(color)) {
                numColors++;
                if (hasAbility(color.getProtectionAbility()) ||
                    hasAbility(MagicAbility.ProtectionFromAllColors)) {
                    return true;
                } else if (source.isSpell() && hasAbility(MagicAbility.ProtectionFromColoredSpells)) {
                    return true;
                }
            }
        }

        // from monocolored
        if (numColors == 1 &&
            hasAbility(MagicAbility.ProtectionFromMonoColored)) {
            return true;
        }

        // from multicolored
        if (numColors > 1 &&
            hasAbility(MagicAbility.ProtectionFromMultiColored)) {
            return true;
        }

        if (!source.isPermanent()) {
            return false;
        }

        final MagicPermanent permanent = (MagicPermanent) source;

        for (MagicTrigger<?> trigger : cachedTriggers) {
            if (trigger.getType() == MagicTriggerType.Protection) {
                @SuppressWarnings("unchecked")
                final MagicTrigger<MagicPermanent> protection = (MagicTrigger<MagicPermanent>) trigger;
                if (protection.accept(this, permanent)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean canAttack() {
        if (hasAbility(MagicAbility.CannotAttack) ||
            hasAbility(MagicAbility.CannotAttackOrBlock) ||
            hasState(MagicPermanentState.ExcludeFromCombat) ||
            hasState(MagicPermanentState.CannotAttack) ||
            (hasAbility(MagicAbility.Defender) && !hasAbility(MagicAbility.CanAttackWithDefender))) {
            return false;
        }
        return isCreature() && canTap();
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
        for (final MagicSubType basicLand : MagicSubType.ALL_BASIC_LANDS) {
            if (hasAbility(basicLand.getLandwalkAbility()) &&
                defendingPlayer.controlsPermanent(basicLand)) {
                return false;
            }
        }
        if (hasAbility(MagicAbility.Snowlandwalk) && defendingPlayer.controlsPermanent(MagicTargetFilterFactory.SNOW_LAND)) {
            return false;
        }
        if (hasAbility(MagicAbility.Snowswampwalk) && defendingPlayer.controlsPermanent(MagicTargetFilterFactory.SNOW_SWAMP)) {
            return false;
        }
        if (hasAbility(MagicAbility.Snowforestwalk) && defendingPlayer.controlsPermanent(MagicTargetFilterFactory.SNOW_FOREST)) {
            return false;
        }
        if (hasAbility(MagicAbility.LegendaryLandwalk) && defendingPlayer.controlsPermanent(MagicTargetFilterFactory.LEGENDARY_LAND)) {
            return false;
        }
        if (hasAbility(MagicAbility.NonbasicLandwalk) && defendingPlayer.controlsPermanent(MagicTargetFilterFactory.NONBASIC_LAND)) {
            return false;
        }
        return true;
    }

    public boolean canBlock(final MagicPermanent attacker) {
        // Fear-types and Intimidate
        if (!isArtifact()) {
            if (attacker.hasAbility(MagicAbility.Fear) &&
                !hasColor(MagicColor.Black)) {
                return false;
            }
            if (attacker.hasAbility(MagicAbility.WhiteFear) &&
                !hasColor(MagicColor.White)) {
                return false;
            }
            if (attacker.hasAbility(MagicAbility.RedFear) &&
                !hasColor(MagicColor.Red)) {
                return false;
            }
            if (attacker.hasAbility(MagicAbility.Intimidate) &&
                !shareColor(attacker)) {
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

        // Horsemanship
        if (attacker.hasAbility(MagicAbility.Horsemanship) &&
            !hasAbility(MagicAbility.Horsemanship)) {
            return false;
        }

        //cannot be blocked by ...
        for (MagicTrigger<?> trigger : attacker.getTriggers()) {
            if (trigger.getType() == MagicTriggerType.CannotBeBlocked) {
                @SuppressWarnings("unchecked")
                final MagicTrigger<MagicPermanent> cannotBeBlocked = (MagicTrigger<MagicPermanent>) trigger;
                if (cannotBeBlocked.accept(attacker, this)) {
                    return false;
                }
            }
        }

        //can't block ...
        for (MagicTrigger<?> trigger : getTriggers()) {
            if (trigger.getType() == MagicTriggerType.CantBlock) {
                @SuppressWarnings("unchecked")
                final MagicTrigger<MagicPermanent> cantBlock = (MagicTrigger<MagicPermanent>) trigger;
                if (cantBlock.accept(this, attacker)) {
                    return false;
                }
            }
        }

        // Protection
        return !attacker.hasProtectionFrom(this);
    }

    public MagicPermanent getEquippedCreature() {
        return equippedCreature;
    }

    public void setEquippedCreature(final MagicPermanent creature) {
        equippedCreature = creature;
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
        return equipmentPermanents.size() > 0;
    }

    public MagicPermanent getEnchantedPermanent() {
        return enchantedPermanent;
    }

    public void setEnchantedPermanent(final MagicPermanent perm) {
        enchantedPermanent = perm;
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
        return auraPermanents.size() > 0;
    }

    public int getAbilityPlayedThisTurn() {
        return abilityPlayedThisTurn;
    }

    public void setAbilityPlayedThisTurn(final int amount) {
        abilityPlayedThisTurn = amount;
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

    public boolean isEquipment() {
        return isArtifact() && hasSubType(MagicSubType.Equipment);
    }

    public boolean isArtifact() {
        return hasType(MagicType.Artifact);
    }

    public boolean isEnchantment() {
        return hasType(MagicType.Enchantment);
    }

    public boolean isAura() {
        return isEnchantment() && hasSubType(MagicSubType.Aura);
    }

    public boolean isFaceDown() {
        return hasState(MagicPermanentState.FaceDown);
    }

    public boolean isFlipped() {
        return hasState(MagicPermanentState.Flipped);
    }

    public boolean isSplitCard() {
        return false;
    }

    public boolean isTransformed() {
        return hasState(MagicPermanentState.Transformed);
    }

    public MagicTargetChoice getAuraTargetChoice() {
        if (isAura()) {
            final MagicPlayAuraEvent auraEvent = cardDefinition.isAura() ?
                (MagicPlayAuraEvent) cardDefinition.getCardEvent() :
                MagicBestowActivation.BestowEvent;
            return auraEvent.getTargetChoice();
        } else {
            return MagicTargetChoice.NONE;
        }
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
        if (hasAbility(MagicAbility.Hexproof) && isEnemy(source)) {
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

        // Can't be the target of nongreen spells or abilities from nongreen sources
        if (hasAbility(MagicAbility.CannotBeTheTargetOfNonGreen) && source.hasColor(MagicColor.Green) == false) {
            return false;
        }

        // Can't be the target of black or red spell your opponent control
        if (hasAbility(MagicAbility.CannotBeTheTargetOfBlackOrRedOpponentSpell) &&
            (source.hasColor(MagicColor.Black) || source.hasColor(MagicColor.Red)) &&
            source.isSpell() && isEnemy(source)) {
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

    @Override
    public boolean isLegalTarget(final MagicPlayer player, final MagicTargetFilter<? extends MagicTarget> targetFilter) {
        return getController().controlsPermanent(this);
    }

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
        public MagicPermanent map(final MagicGame game) {
            return this;
        }

        @Override
        public MagicPowerToughness getPowerToughness() {
            return new MagicPowerToughness(0, 0);
        }

        @Override
        public MagicPlayer getController() {
            return MagicPlayer.NONE;
        }

        @Override
        public MagicGame getGame() {
            throw new RuntimeException("getGame called for MagicPermanent.NONE");
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

        @Override
        public long getStateId() {
            return hashCode();
        }

        @Override
        public int getCounters(final MagicCounterType counterType) {
            return 0;
        }

        @Override
        public boolean hasCounters() {
            return false;
        }

        @Override
        public boolean canTap() {
            return false;
        }

        @Override
        public void changeCounters(final MagicCounterType counterType, final int amount) {
            //do nothing
        }

        @Override
        public void addEquipment(final MagicPermanent equipment) {
            //do nothing
        }

        @Override
        public void addAura(final MagicPermanent equipment) {
            //do nothing
        }

        @Override
        public void addAbility(final MagicActivation<MagicPermanent> act) {
        }

        @Override
        public void addAbility(final MagicTrigger<?> trig) {
            //do nothing
        }

        @Override
        public void addAbility(final MagicManaActivation act) {
            //do nothing
        }

        @Override
        public void addExiledCard(final MagicCard card) {
            //do nothing
        }

        @Override
        public void removeExiledCard(final MagicCard card) {
            //do nothing
        }
    };

    @Override
    public Set<MagicSubType> getSubTypes() {
        return EnumSet.copyOf(cachedSubTypeFlags);
    }

    @Override
    public String getSubTypeText() {
        if (cachedSubTypeFlags.containsAll(MagicSubType.ALL_CREATURES)) {
            return getCardDefinition().getSubTypeText();
        } else {
            StringBuilder subtypes = new StringBuilder();
            for (final MagicSubType subType: cachedSubTypeFlags) {
                subtypes.append(subType).append(" ");
            }
            return subtypes.toString();
        }
    }

    @Override
    public String getPowerToughnessText() {
        return isCreature() || hasSubType(MagicSubType.Vehicle) ? getPower() + "/" + getToughness() : "";
    }
}
