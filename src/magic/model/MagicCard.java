package magic.model;

import java.util.Collection;
import java.util.Collections;

import magic.model.event.MagicActivation;
import magic.model.target.MagicTarget;

public class MagicCard implements MagicSource,MagicTarget,Comparable<MagicCard> {

	public static final int TOKEN_ID=-1;
	
	private MagicCardDefinition cardDefinition;
	private MagicPlayer owner;
	private boolean token=false;
	private boolean known=true;
	private int id;
	
	public MagicCard(final MagicCardDefinition cardDefinition,final MagicPlayer owner,final int id) {

		this.cardDefinition=cardDefinition;
		this.owner=owner;
		this.id=id;
	}
	
	public MagicCard(final MagicDeckCard card,final MagicPlayer owner,final int id) {

		this(card.getCardDefinition(),owner,id);
	}
	
	private MagicCard() {
		
	}
	
	@Override
	public MagicCopyable create() {

		return new MagicCard();
	}

	@Override
	public void copy(final MagicCopyMap copyMap,final MagicCopyable source) {

		final MagicCard sourceCard=(MagicCard)source;
		cardDefinition=sourceCard.cardDefinition;
		owner=copyMap.copy(sourceCard.owner);
		token=sourceCard.token;
		known=sourceCard.known;
		id=sourceCard.id;
	}
	
	@Override
	public Object map(final MagicGame game) {

		final MagicPlayer mappedOwner=(MagicPlayer)owner.map(game);
		final MagicCard card=mappedOwner.getHand().getCard(id);
		if (card!=null) {
			return card;
		}
		return mappedOwner.getGraveyard().getCard(id);
	}
	
	public long getId() {
		
		return id;
	}

	public MagicCardDefinition getCardDefinition() {

		if (known) {
			return cardDefinition;
		} else {
			return MagicCardDefinition.EMPTY;
		}
	}
		
	public MagicPlayer getOwner() {
		
		return owner;
	}
			
	public void setToken() {
		
		token=true;
	}
	
	public boolean isToken() {
		
		return token;
	}
	
	public static MagicCard createTokenCard(final MagicCardDefinition cardDefinition,final MagicPlayer owner) {
		
		final MagicCard card=new MagicCard(cardDefinition,owner,MagicCard.TOKEN_ID);	
		card.setToken();
		return card;
	}

	public void setKnown(final boolean known) {
		
		this.known=known;
	}
	
	public boolean isKnown() {
	
		return known;
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
	public void setPreventDamage(int amount) {
		
	}
	
	@Override
	public boolean isValidTarget(final MagicGame game,final MagicSource source) {

		return true;
	}

	@Override
	public boolean isPermanent() {
		
		return false;
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
	public MagicColoredType getColoredType() {
		
		return getCardDefinition().getColoredType();
	}

	@Override
	public int getColorFlags() {
		
		return getCardDefinition().getColorFlags();
	}
	
	@Override
	public boolean hasAbility(final MagicGame game,final MagicAbility ability) {

		return getCardDefinition().hasAbility(ability);
	}
	
	@Override
	public Collection<MagicActivation> getActivations() {

		return Collections.singletonList(cardDefinition.getCardActivation());
	}

	@Override
	public int compareTo(final MagicCard card) {

		return id-card.id;
	}
}