package magic.model;

import java.util.ArrayList;
import java.util.List;

import magic.data.GeneralConfig;
import magic.data.TournamentConfig;
import magic.model.choice.MagicBuilderManaCost;
import magic.model.event.MagicActivationPriority;
import magic.model.event.MagicActivationMap;
import magic.model.event.MagicSourceManaActivation;
import magic.model.target.MagicTarget;
import magic.model.variable.MagicStaticLocalVariable;

public class MagicPlayer implements MagicTarget {
	
	private static final long ID_FACTOR=13;
	
	private MagicPlayerDefinition playerDefinition;
	private MagicCardList hand;
	private MagicCardList library;
	private MagicCardList exile;
	private MagicCardList graveyard;
	private MagicPermanentSet permanents;
	private MagicPermanentSet manaPermanents;
	private int stateFlags=0;
	private int index;
	private int life;
	private int poison;
	private int preventDamage=0;
	private int extraTurns=0;
	private int attackers=0;
	private MagicCardCounter cardCounter;
	private MagicActivationMap activationMap;
	private MagicBuilderManaCost builderCost;
	private MagicActivationPriority activationPriority;
	
	public MagicPlayer(final TournamentConfig configuration,final MagicPlayerDefinition playerDefinition,final int index) {
		
		this.playerDefinition=playerDefinition;
		this.index=index;
		hand=new MagicCardList();
		library=new MagicCardList();
		graveyard=new MagicCardList();
		exile=new MagicCardList();
		permanents=new MagicPermanentSet();
		manaPermanents=new MagicPermanentSet();
		life=configuration.getStartLife();
		if (index!=0) {
			life+=GeneralConfig.getInstance().getExtraLife();
		}
		poison=0;
		cardCounter=new MagicCardCounter();
		activationMap=new MagicActivationMap();
		builderCost=new MagicBuilderManaCost();
		activationPriority=new MagicActivationPriority();
		createHandAndLibrary(configuration.getHandSize());
	}
	
	private MagicPlayer() {
		
	}
	
	@Override
	public MagicCopyable create() {

		return new MagicPlayer();
	}
	
	@Override
	public void copy(final MagicCopyMap copyMap,final MagicCopyable source) {

		final MagicPlayer sourcePlayer=(MagicPlayer)source;
		playerDefinition=sourcePlayer.playerDefinition;
		stateFlags=sourcePlayer.stateFlags;
		index=sourcePlayer.index;
		life=sourcePlayer.life;
		poison=sourcePlayer.poison;
		preventDamage=sourcePlayer.preventDamage;
		extraTurns=sourcePlayer.extraTurns;
		attackers=sourcePlayer.attackers;
		hand=sourcePlayer.hand.copy(copyMap);
		library=sourcePlayer.library.copy(copyMap);
		graveyard=sourcePlayer.graveyard.copy(copyMap);
		exile=sourcePlayer.exile.copy(copyMap);
		permanents=new MagicPermanentSet(copyMap,sourcePlayer.permanents);
		manaPermanents=new MagicPermanentSet(copyMap,sourcePlayer.manaPermanents);
		cardCounter=new MagicCardCounter(sourcePlayer.cardCounter);
		activationMap=new MagicActivationMap(copyMap,sourcePlayer.activationMap);
		builderCost=new MagicBuilderManaCost(sourcePlayer.builderCost);
		activationPriority=new MagicActivationPriority();
	}
	
	@Override
	public Object map(final MagicGame game) {

		return game.getPlayer(index);
	}
	
	public long getPlayerId(final long id) {
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

    public String getIdString() {
        return life + "," + 
               poison + "," + 
               builderCost.getMinimumAmount() + "," + 
               permanents.getPermanentsId() + "," +
               hand.getCardsId() + "," +
               graveyard.getCardsId();
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
	
	public boolean canLose() {
		
		return getCount(MagicStaticLocalVariable.platinumAngel)==0;
	}

	/** Life to use when determining if a player has lost. */
	public int getLosingLife() {
		
		if (life>0||canLose()) {
			return life;
		}
		return 1;
	}
	
	/** Poison to use when determining if a player has lost. */
	public int getLosingPoison() {
		
		if (poison<MagicGame.LOSING_POISON||canLose()) {
			return poison;
		}
		return MagicGame.LOSING_POISON-1;
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
	
	public void addCardToHand(final MagicCard card,final int index) {
		
		hand.add(index,card);
		activationMap.addActivations(card);
	}
		
	public int removeCardFromHand(final MagicCard card) {
		
		activationMap.removeActivations(card);
		return hand.removeCard(card);
	}
	
	public void removeAllCardsFromHand() {
		
		activationMap.removeActivations(hand);
		hand.clear();
	}
	
	public void setHandToUnknown() {
		activationMap.removeActivations(hand);
		hand.setKnown(false);
		activationMap.addActivations(hand);
	}

	private void createHandAndLibrary(final int handSize) {
		int id=0;
		for (final MagicCardDefinition cardDefinition : playerDefinition.getDeck()) {
			library.add(new MagicCard(cardDefinition,this,id++));
		}

		if (library.useSmartShuffle()) {
			library.smartShuffle();
		} else {
			library.shuffle(MagicRandom.nextInt(library.size()));
		}

		for (int count=handSize;count>0&&!library.isEmpty();count--) {
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
	
	public int getNrOfPermanentsWithType(final MagicType type) {
		
		int count=0;
		for (final MagicPermanent permanent : permanents) {
			
			if (permanent.hasType(type)) {
				count++;
			}
		}
		return count;
	}

	public int getNrOfPermanentsWithSubType(final MagicSubType subType) {
		
		int count=0;
		for (final MagicPermanent permanent : permanents) {
			
			if (permanent.hasSubType(subType)) {
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

	public void setNrOfAttackers(final int attackers) {
		
		this.attackers=attackers;
	}
	
	public int getNrOfAttackers() {
		
		return attackers;
	}
	
	public boolean controlsPermanentWithType(final MagicType type) {
		
		for (final MagicPermanent permanent : permanents) {
			
			if (permanent.hasType(type)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean controlsPermanentWithSubType(final MagicSubType subType) {
		
		for (final MagicPermanent permanent : permanents) {
			
			if (permanent.hasSubType(subType)) {
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
	public void setPreventDamage(int amount) {

		preventDamage=amount;
	}

	@Override
	public MagicPlayer getController() {

		return this;
	}
	
	@Override
	public boolean isValidTarget(final MagicGame game,final MagicSource source) {

		if (source.getController()!=this&&getCount(MagicStaticLocalVariable.spiritOfTheHearth)>0) {
			return false;
		}
		return true;
	}	
}
