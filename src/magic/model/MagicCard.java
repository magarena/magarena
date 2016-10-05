package magic.model;

import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import magic.exception.GameException;
import magic.model.event.MagicActivation;
import magic.model.event.MagicEvent;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicSourceActivation;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicItemOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetType;
import magic.cardBuilder.IRenderableCard;

public class MagicCard
    extends MagicObjectImpl
    implements MagicSource,MagicTarget,Comparable<MagicCard>,MagicMappable<MagicCard>,IRenderableCard {

    public static final MagicCard NONE = new MagicCard(MagicCardDefinition.UNKNOWN, MagicPlayer.NONE, 0) {
        @Override
        public MagicCard copy(final MagicCopyMap copyMap) {
            return this;
        }
        @Override
        public String getName() {
            return "NONE";
        }
        @Override
        public boolean isInHand() {
            final boolean found = getOwner().getHand().contains(this);
            if (found) {
                throw new RuntimeException("Card NONE is in hand");
            }
            return found;
        }
        @Override
        public boolean isInGraveyard() {
            final boolean found = getOwner().getGraveyard().contains(this);
            if (found) {
                throw new RuntimeException("Card NONE is in graveyard");
            }
            return found;
        }
        @Override
        public boolean isInExile() {
            final boolean found = getOwner().getExile().contains(this);
            if (found) {
                throw new RuntimeException("Card NONE is in exile");
            }
            return found;
        }
        @Override
        public boolean isInLibrary() {
            final boolean found = getOwner().getLibrary().contains(this);
            if (found) {
                throw new RuntimeException("Card NONE is in library");
            }
            return found;
        }
        @Override
        public boolean isOnBattlefield() {
            return false;
        }
        @Override
        public boolean isOnStack() {
            return false;
        }
        @Override
        public boolean isValid() {
            return false;
        }
    };

    private static final int TOKEN_ID=-1;

    private final MagicCardDefinition cardDefinition;
    private final MagicPlayer owner;
    private final long id;
    private final Map<MagicCounterType, Integer> counters;
    private final boolean token;
    private boolean aiKnown = true;
    private boolean gameKnown = false;

    public MagicCard(final MagicCardDefinition aCardDefinition,final MagicPlayer aOwner,final long aId) {
        this(aCardDefinition, aOwner, aId, false);
    }

    public static MagicCard createTokenCard(final MagicCardDefinition cardDefinition, final MagicPlayer owner) {
        return new MagicCard(cardDefinition, owner, MagicCard.TOKEN_ID, true);
    }

    public static MagicCard createTokenCard(final MagicObject obj, final MagicPlayer owner) {
        return new MagicCard(obj.getCardDefinition(), owner, MagicCard.TOKEN_ID, true);
    }

    private MagicCard(final MagicCardDefinition aCardDefinition,final MagicPlayer aOwner,final long aId, final boolean aToken) {
        aCardDefinition.loadAbilities();
        cardDefinition = aCardDefinition;
        counters = new EnumMap<MagicCounterType, Integer>(MagicCounterType.class);
        owner = aOwner;
        id = aId;
        token = aToken;
    }

    private MagicCard(final MagicCopyMap copyMap, final MagicCard sourceCard) {
        copyMap.put(sourceCard, this);

        cardDefinition = sourceCard.cardDefinition;
        counters = new EnumMap<MagicCounterType,Integer>(sourceCard.counters);
        owner = copyMap.copy(sourceCard.owner);
        id = sourceCard.id;
        token = sourceCard.token;
        aiKnown = sourceCard.aiKnown;
        gameKnown = sourceCard.gameKnown;
    }

    @Override
    public MagicCard copy(final MagicCopyMap copyMap) {
        return new MagicCard(copyMap, this);
    }

    @Override
    public MagicCard map(final MagicGame game) {
        final MagicPlayer mappedOwner=owner.map(game);
        MagicCard card = MagicCard.NONE;
        if (card == MagicCard.NONE) {
            card = mappedOwner.getPrivateHand().getCard(id);
        }
        if (card == MagicCard.NONE) {
            card = mappedOwner.getGraveyard().getCard(id);
        }
        if (card == MagicCard.NONE) {
            card = mappedOwner.getExile().getCard(id);
        }
        if (card == MagicCard.NONE) {
            card = mappedOwner.getLibrary().getCard(id);
        }
        if (card == MagicCard.NONE) {
            throw new GameException("Mapping card failed, card " + getName() + " " + id + " not found", game);
        }
        return card;
    }

    public long getId() {
        return id;
    }

    public long getStateId() {
        return (getCardDefinition().getIndex() * 10L + (aiKnown ? 1 : 0) + (gameKnown ? 2 : 0) + (token ? 4 : 0)) ^ counters.hashCode();
    }

    public MagicCardDefinition getCardDefinition() {
        return isKnown() ? cardDefinition : MagicCardDefinition.UNKNOWN;
    }

    public MagicPlayer getOwner() {
        return owner;
    }

    @Override
    public boolean isToken() {
        return token;
    }

    public boolean isDoubleFaced() {
        return getCardDefinition().isDoubleFaced();
    }

    public boolean isFlipCard() {
        return getCardDefinition().isFlipCard();
    }

    public boolean isSplitCard() {
        return getCardDefinition().isSplitCard();
    }

    public boolean isNameless() {
        return getName().isEmpty();
    }

    public int getPower() {
        return genPowerToughness().power();
    }

    public int getToughness() {
        return genPowerToughness().toughness();
    }

    public MagicPowerToughness genPowerToughness() {
        return genPowerToughness(MagicPermanent.NONE);
    }

    public MagicPowerToughness genPowerToughness(final MagicPermanent perm) {
        final MagicPowerToughness pt = getCardDefinition().genPowerToughness();
        getCardDefinition().applyCDAPowerToughness(
            getGame(),
            perm.isValid() ? perm.getController() : getOwner(),
            perm,
            pt
        );
        return pt;
    }

    public int getConvertedCost() {
        return getCost().getConvertedCost();
    }

    public MagicManaCost getGameCost() {
        return getGame().modCost(this, getCost());
    }

    public Iterable<MagicEvent> getCostEvent() {
        return getCardDefinition().getCostEvent(this);
    }

    public Iterable<MagicEvent> getAdditionalCostEvent() {
        return getCardDefinition().getAdditionalCostEvent(this);
    }

    public boolean isGameKnown() {
        return gameKnown;
    }

    public void setGameKnown(final boolean bool) {
        gameKnown = bool;
    }

    public boolean isKnown() {
        return aiKnown || gameKnown;
    }

    public void setAIKnown(final boolean bool) {
        aiKnown = bool;
    }

    public boolean isIn(final MagicLocationType loc) {
        switch (loc) {
            case Stack:
                return isOnStack();
            case Battlefield:
                return isOnBattlefield();
            case OwnersHand:
                return isInHand();
            case OwnersLibrary:
                return isInLibrary();
            case TopOfOwnersLibrary:
                return getOwner().getLibrary().getCardAtTop() == this;
            case BottomOfOwnersLibrary:
                return getOwner().getLibrary().getCardAtBottom() == this;
            case Graveyard:
                return isInGraveyard();
            case OpponentsGraveyard:
                return false;
            case Exile:
                return isInExile();
            default:
                throw new RuntimeException("unknown location: \"" + loc + "\"");
        }
    }

    public boolean isInHand() {
        return getOwner().getHand().contains(this);
    }

    public boolean isInGraveyard() {
        return getOwner().getGraveyard().contains(this);
    }

    public boolean isInExile() {
        return getOwner().getExile().contains(this);
    }

    public boolean isInLibrary() {
        return getOwner().getLibrary().contains(this);
    }

    public boolean isOnBattlefield() {
        for (final MagicPlayer player : getGame().getPlayers()) {
        for (final MagicPermanent perm : player.getPermanents()) {
            if (perm.getCard() == this) {
                return true;
            }
        }
        }
        return false;
    }

    public boolean isOnStack() {
        for (final MagicItemOnStack item : getGame().getStack()) {
            if (item.isSpell()) {
                final MagicCardOnStack spell = (MagicCardOnStack)item;
                if (spell.getCard() == this) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
    public boolean isSuspended() {
        return isInExile() && hasAbility(MagicAbility.Suspend) && hasCounters(MagicCounterType.Time);
    }
    */

    public MagicLocationType getLocation() {
        if (isInHand()) {
            return MagicLocationType.OwnersHand;
        } else if (isInGraveyard()) {
            return MagicLocationType.Graveyard;
        } else if (isInExile()) {
            return MagicLocationType.Exile;
        } else if (isOnBattlefield()) {
            return MagicLocationType.Battlefield;
        } else if (isInLibrary()) {
            return MagicLocationType.OwnersLibrary;
        } else if (isOnStack()) {
            return MagicLocationType.Stack;
        } else {
            throw new RuntimeException(this + " not found");
        }
    }


    @Override
    public String getName() {
        return getCardDefinition().getName();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public MagicPlayer getController() {
        return owner;
    }

    @Override
    public int getPreventDamage() {
        return 0;
    }

    @Override
    public void setPreventDamage(final int amount) {

    }

    @Override
    public boolean isValidTarget(final MagicSource source) {
        return true;
    }

    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isPermanent() {
        return false;
    }

    public boolean isPermanentCard() {
        return getCardDefinition().isPermanent();
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public boolean isSpell() {
        return true;
    }

    @Override
    public int getColorFlags() {
        final int init = getCardDefinition().getColorFlags();
        return getCardDefinition().applyCDAColor(getGame(), getOwner(), init);
    }

    @Override
    public boolean hasColor(final MagicColor color) {
        if (isSplitCard()) {
            return color.hasColor(getColorFlags()) || getCardDefinition().getSplitDefinition().hasColor(color);
        }
        return color.hasColor(getColorFlags());
    }

    @Override
    public boolean hasAbility(final MagicAbility ability) {
        return getCardDefinition().hasAbility(ability);
    }

    public Set<MagicAbility> getAbilityFlags() {
        return getCardDefinition().genAbilityFlags();
    }

    @Override
    public boolean hasType(final MagicType type) {
        return getCardDefinition().hasType(type);
    }

    @Override
    public boolean hasSubType(final MagicSubType subType) {
        return getCardDefinition().hasSubType(subType);
    }

    @Override
    public Set<MagicSubType> getSubTypes() {
        return getCardDefinition().getSubTypes();
    }

    @Override
    public Collection<MagicSourceActivation<? extends MagicSource>> getSourceActivations() {
        List<MagicSourceActivation<? extends MagicSource>> sourceActs = new LinkedList<>();
        final Collection<MagicActivation<MagicCard>> activations = isInHand() ?
            getCardDefinition().getHandActivations() :
            getCardDefinition().getGraveyardActivations();
        for (final MagicActivation<MagicCard> act : activations) {
            sourceActs.add(MagicSourceActivation.create(this, act));
        }
        return sourceActs;
    }

    @Override
    public int compareTo(final MagicCard card) {
        final int diff = getCardDefinition().getIndex() - card.getCardDefinition().getIndex();
        if (diff != 0) {
            return diff;
        } else {
            return Long.signum(id - card.id);
        }
    }

    @Override
    public MagicGame getGame() {
        return owner.getGame();
    }

    @Override
    public boolean isLegalTarget(final MagicPlayer player, final MagicTargetFilter<? extends MagicTarget> targetFilter) {
        // Card in graveyard
        if (targetFilter.acceptType(MagicTargetType.Graveyard) &&
            player.getGraveyard().contains(this)) {
            return true;
        }

        // Card in opponent's graveyard
        if (targetFilter.acceptType(MagicTargetType.OpponentsGraveyard) &&
            player.getOpponent().getGraveyard().contains(this)) {
            return true;
        }

        // Card in hand
        if (targetFilter.acceptType(MagicTargetType.Hand) &&
            player.getHand().contains(this)) {
            return true;
        }

        // Card in library
        if (targetFilter.acceptType(MagicTargetType.Library) &&
            player.getLibrary().contains(this)) {
            return true;
        }

        return false;
    }

    public boolean hasCounters() {
        return counters.size() > 0;
    }

    @Override
    public int getCounters(final MagicCounterType counterType) {
        final Integer cnt = counters.get(counterType);
        return cnt != null ? cnt : 0;
    }

    @Override
    public void changeCounters(final MagicCounterType counterType,final int amount) {
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

    @Override
    public Collection<MagicManaActivation> getManaActivations() {
        // Returning from CardDefinition - Cards technically don't, also no in-game changes
        return getCardDefinition().getManaActivations();
    }

    @Override
    public String getPowerToughnessText() {
        if (isCreature()) {
            return getPower()+"/"+getToughness();
        } else {
            return "";
        }
    }
}
