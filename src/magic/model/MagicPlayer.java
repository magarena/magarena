package magic.model;

import magic.data.GeneralConfig;
import magic.data.TournamentConfig;
import magic.data.CardDefinitions;
import magic.model.choice.MagicBuilderManaCost;
import magic.model.event.MagicActivationMap;
import magic.model.event.MagicActivationPriority;
import magic.model.event.MagicSourceManaActivation;
import magic.model.target.MagicTarget;

import java.util.ArrayList;
import java.util.List;

public class MagicPlayer implements MagicTarget {

    public static final MagicPlayer NONE = new MagicPlayer(-1, new MagicPlayerDefinition(), -1) {
        @Override
        public String toString() {
            return "";
        }
        @Override
        public boolean controlsPermanent(final MagicPermanent permanent) {
            return false;
        }
        @Override
        public boolean isValid() {
            return false;
        }
        @Override
        public MagicPlayer copy(final MagicCopyMap copyMap) {
            return this;
        }
    };

	private static final long ID_FACTOR=31;
	
    private final MagicPlayerDefinition playerDefinition;
	private final int index;
	
    private int life;
	private int poison;
	private int stateFlags;
	private int preventDamage;
	private int extraTurns;
	private int attackers;
	private int blockers;
    private final MagicCardList hand;
	private final MagicCardList library;
	private final MagicCardList graveyard;
	private final MagicCardList exile;
	private final MagicPermanentSet permanents;
	private final MagicPermanentSet manaPermanents;
	private final MagicCardCounter cardCounter;
	private final MagicActivationMap activationMap;
    private MagicBuilderManaCost builderCost;
	private MagicActivationPriority activationPriority;

    private long[] keys;

	MagicPlayer(final int aLife,final MagicPlayerDefinition aPlayerDefinition,final int aIndex) {
		playerDefinition = aPlayerDefinition;
		index = aIndex;
		life = aLife;
		poison = 0;
		
        hand=new MagicCardList();
		library=new MagicCardList();
		graveyard=new MagicCardList();
		exile=new MagicCardList();
		permanents=new MagicPermanentSet();
		manaPermanents=new MagicPermanentSet();
		cardCounter=new MagicCardCounter();
		activationMap=new MagicActivationMap();
		builderCost=new MagicBuilderManaCost();
		activationPriority=new MagicActivationPriority();
	}
	
    private MagicPlayer(final MagicCopyMap copyMap, final MagicPlayer sourcePlayer) {
        copyMap.put(sourcePlayer, this);
		
        playerDefinition = sourcePlayer.playerDefinition;
		index = sourcePlayer.index;
		life = sourcePlayer.life;
		poison=sourcePlayer.poison;
		stateFlags=sourcePlayer.stateFlags;
		preventDamage=sourcePlayer.preventDamage;
		extraTurns=sourcePlayer.extraTurns;
		attackers=sourcePlayer.attackers;
		blockers=sourcePlayer.blockers;
		hand=new MagicCardList(copyMap, sourcePlayer.hand);
		library=new MagicCardList(copyMap, sourcePlayer.library);
		graveyard=new MagicCardList(copyMap, sourcePlayer.graveyard);
		exile=new MagicCardList(copyMap, sourcePlayer.exile);
		permanents=new MagicPermanentSet(copyMap,sourcePlayer.permanents);
		manaPermanents=new MagicPermanentSet(copyMap,sourcePlayer.manaPermanents);
		cardCounter=new MagicCardCounter(sourcePlayer.cardCounter);
		activationMap=new MagicActivationMap(copyMap,sourcePlayer.activationMap);
		builderCost=new MagicBuilderManaCost(sourcePlayer.builderCost);
        activationPriority=new MagicActivationPriority(sourcePlayer.activationPriority);
    }
	
	@Override
	public MagicPlayer copy(final MagicCopyMap copyMap) {
        return new MagicPlayer(copyMap, this);
	}
	
	@Override
	public MagicPlayer map(final MagicGame game) {
		return game.getPlayer(index);
	}
	
    long getPlayerId() {
        keys = new long[] {
            life,
            poison,
            stateFlags,
            preventDamage,
            extraTurns,
            attackers,
            blockers,
            hand.getSortedCardsId(),
            library.getCardsId(),
            graveyard.getCardsId(),
            exile.getCardsId(),
            permanents.getPermanentsId(),
            builderCost.getMinimumAmount(),
            activationPriority.getPriority(),
            activationPriority.getActivationId(),
        };
		return magic.MurmurHash3.hash(keys);
    }
    
    String getIdString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(keys[0]);
        for (int i = 1; i < keys.length; i++) {
            sb.append(' ');
            sb.append(keys[i]);
        }
        return sb.toString();
    }
	
	long getPlayerId(final long id) {
		// Exile is not used for id.
		long playerId=id;
        playerId=playerId*ID_FACTOR+life;
		playerId=playerId*ID_FACTOR+poison;
		playerId=playerId*ID_FACTOR+builderCost.getMinimumAmount();
		playerId=playerId*ID_FACTOR+permanents.getPermanentsId();
		playerId=playerId*ID_FACTOR+hand.getCardsId();
		playerId=playerId*ID_FACTOR+graveyard.getCardsId();
		return playerId;
	}
	
	@Override
	public String toString() {
		return playerDefinition.getName();
	}
	
	public MagicActivationMap getActivationMap() {
		return activationMap;
	}
			
	public MagicPlayerDefinition getPlayerDefinition() {
		return playerDefinition;
	}
	
	public int getIndex() {
		return index;
	}

    public long getId() {
        return 1000000000L + index;
    }
	
	public void setState(final MagicPlayerState state) {
		stateFlags|=state.getMask();
	}
	
	public void clearState(final MagicPlayerState state) {
		stateFlags&=Integer.MAX_VALUE-state.getMask();
	}
	
	public boolean hasState(final MagicPlayerState state) {
		return state.hasState(stateFlags);
	}
	
	public int getStateFlags() {
		return stateFlags;
	}
	
	public void setStateFlags(final int flags) {
		stateFlags=flags;
	}
					
	public void setLife(final int life) {
		this.life=life;
	}
			
	public int getLife() {
		return life;
	}
	
	public void setPoison(final int poison) {
		this.poison=poison;
	}
	
	public int getPoison() {
		return poison;
	}
	
	public void changeExtraTurns(final int amount) {
		extraTurns+=amount;
	}
	
	public int getExtraTurns() {
		return extraTurns;
	}
		
	public int getHandSize() {
		return hand.size();
	}
	
	public MagicCardList getHand() {
		return hand;
	}

	public void addCardToHand(final MagicCard card) {
		hand.addToTop(card);
		activationMap.addActivations(card);
	}
	
	public void addCardToHand(final MagicCard card,final int aIndex) {
		hand.add(aIndex,card);
		activationMap.addActivations(card);
	}
		
	public int removeCardFromHand(final MagicCard card) {
		activationMap.removeActivations(card);
		return hand.removeCard(card);
	}
	
	private void removeAllCardsFromHand() {
		activationMap.removeActivations(hand);
		hand.clear();
	}
	
	void setHandToUnknown() {
		activationMap.removeActivations(hand);
		hand.setKnown(false);
		activationMap.addActivations(hand);
	}

	void createHandAndLibrary(final int handSize) {
		for (final MagicCardDefinition cardDefinition : playerDefinition.getDeck()) {
            final long id = MagicGame.getInstance().getUniqueId();
			library.add(new MagicCard(cardDefinition,this,id));
		}

        //library order depends on player index, game no, random seed
        final long seed = magic.MurmurHash3.hash(new long[] {
            2 * index - 1,
            MagicGame.getCount(),
            (System.getProperty("rndSeed") != null) ? 
                Long.parseLong(System.getProperty("rndSeed")) : 
                System.currentTimeMillis()
        });

		if (library.useSmartShuffle()) {
			library.smartShuffle(seed);
		} else {
			library.shuffle(seed);
		}

		for (int count = handSize; count > 0 && !library.isEmpty(); count--) {
			addCardToHand(library.removeCardAtTop());
		}
	}
	
	public MagicCardList getLibrary() {
		return library;
	}
	
	public MagicCardList getGraveyard() {
		return graveyard;
	}
	
	public MagicCardList getExile() {
		return exile;
	}
	
	public MagicPermanentSet getPermanents() {
		return permanents;
	}

	public void addPermanent(final MagicPermanent permanent) {
		permanents.add(permanent);
		if (permanent.producesMana()) {
			manaPermanents.add(permanent);
		}
		cardCounter.incrementCount(permanent.getCardDefinition());
		activationMap.addActivations(permanent);
	}

	public void removePermanent(final MagicPermanent permanent) {
		permanents.remove(permanent);
		if (permanent.producesMana()) {
			manaPermanents.remove(permanent);
		}
		cardCounter.decrementCount(permanent.getCardDefinition());
		activationMap.removeActivations(permanent);
	}
	
	public boolean controlsPermanent(final MagicPermanent permanent) {
		return permanents.contains(permanent);
	}
	
	public List<MagicSourceManaActivation> getManaActivations(final MagicGame game) {
		final List<MagicSourceManaActivation> activations=new ArrayList<MagicSourceManaActivation>();
		for (final MagicPermanent permanent : manaPermanents) {
						
			if (game.isArtificial()&&permanent.hasState(MagicPermanentState.ExcludeManaSource)) {
				continue;
			}
			
			final MagicSourceManaActivation sourceActivation=new MagicSourceManaActivation(game,permanent);
			if (sourceActivation.available) {
				activations.add(sourceActivation);
			}
		}
		return activations;
	}
	
	private int getManaActivationsCount(final MagicGame game) {
		int count=0;
		for (final MagicPermanent permanent : manaPermanents) {

			final MagicSourceManaActivation sourceActivation=new MagicSourceManaActivation(game,permanent);
			if (sourceActivation.available) {
				count++;
			}
		}
		return count;
	}
	
	public int getCount(final int cardDefinitionIndex) {
		return cardCounter.getCount(cardDefinitionIndex);
	}
	
	public int getNrOfPermanentsWithType(final MagicType type, final MagicGame game) {
		int count=0;
		for (final MagicPermanent permanent : permanents) {
			if (permanent.hasType(type,game)) {
				count++;
			}
		}
		return count;
	}

	private int getNrOfPermanentsWithSubType(final MagicSubType subType, final MagicGame game) {
		int count=0;
		for (final MagicPermanent permanent : permanents) {
			if (permanent.hasSubType(subType, game)) {
				count++;
			}
		}
		return count;
	}
	
	public void setCached(final MagicGame game,final boolean cached) {
		for (final MagicPermanent permanent : permanents) {
			permanent.setCached(game,cached);
		}
	}
	
	public void setBuilderCost(final MagicBuilderManaCost builderCost) {
		this.builderCost=builderCost;
	}
	
	public MagicBuilderManaCost getBuilderCost() {
		return builderCost;
	}

	public void setActivationPriority(final MagicActivationPriority abilityPriority) {
		this.activationPriority=abilityPriority;
	}
	
	public MagicActivationPriority getActivationPriority() {
		return activationPriority;
	}
	
	public int getMaximumX(final MagicGame game,final MagicManaCost cost) {
		return getManaActivationsCount(game)-builderCost.getMinimumAmount()-cost.getConvertedCost();
	}

	public void setNrOfAttackers(final int aAttackers) {
		this.attackers=aAttackers;
	}
	
	public int getNrOfAttackers() {
		return attackers;
	}
	
    public void setNrOfBlockers(final int aBlockers) {
		this.blockers=aBlockers;
	}
	
	public int getNrOfBlockers() {
		return blockers;
	}
	
	public boolean controlsPermanentWithType(final MagicType type, final MagicGame game) {
		for (final MagicPermanent permanent : permanents) {
			if (permanent.hasType(type,game)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean controlsPermanentWithSubType(final MagicSubType subType, final MagicGame game) {
		for (final MagicPermanent permanent : permanents) {
			if (permanent.hasSubType(subType,game)) {
				return true;
			}
		}
		return false;		
	}
	
	@Override
	public String getName() {
		return playerDefinition.getName();
	}
	
	@Override
	public boolean isPermanent() {
		return false;
	}

	@Override
	public boolean isPlayer() {
		return true;
	}

	@Override
	public boolean isSpell() {
		return false;
	}

	@Override
	public int getPreventDamage() {
		return preventDamage;
	}

	@Override
	public void setPreventDamage(final int amount) {
		preventDamage=amount;
	}

	@Override
	public MagicPlayer getController() {
		return this;
	}

    public boolean isValid() {
        return true;
    }
	
	@Override
	public boolean isValidTarget(final MagicGame game,final MagicSource source) {
        final int SPIRIT_OF_THE_HEARTH = CardDefinitions.getInstance().getCard("Spirit of the Hearth").getIndex();
		return source.getController() == this || getCount(SPIRIT_OF_THE_HEARTH) == 0;
	}	
}
