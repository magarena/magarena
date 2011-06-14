package magic.model;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class MagicCardList extends ArrayList<MagicCard> {
	
	private static final long serialVersionUID = 1L;
	
	public MagicCardList() {}
	
	public MagicCardList(final MagicCardList cardList) {
		super(cardList);
	}
	
	public MagicCardList copy(final MagicCopyMap copyMap) {
		final MagicCardList cardList=new MagicCardList();
		for (final MagicCard card : this) {
			cardList.add(copyMap.copy(card));
		}
		return cardList;
	}
	
	public long getCardsId() {
		long id = 0;
		for (final MagicCard card : this) {
			id = id * 31 + card.getCardDefinition().getIndex();
		}
		return id;
	}
	
	public void addToBottom(final MagicCard card) {
		add(0,card);
	}
	
	public void addToTop(final MagicCard card) {
		add(card);
	}
	
	public MagicCard getCardAtBottom() {
		return get(0);
	}
	
	public MagicCard getCardAtTop() {
		return this.get(size()-1);
	}
	
	public MagicCard removeCardAtTop() {
		final int index=size()-1;
		final MagicCard card=get(index);
		remove(index);
		return card;
	}
	
	public int removeCard(final MagicCard card) {
		final int index=indexOf(card);
		remove(index);
		return index;
	}
	
	public MagicCard getCard(final int id) {
		for (final MagicCard card : this) {
			if (card.getId()==id) {
				return card;
			}
		}
		return null;
	}
	
	public MagicCard getRandomCard() {
		return get(MagicRandom.nextInt(size()));
	}
	
	public void setCards(final MagicCardList cardList) {
		clear();
		addAll(cardList);
	}
	
	public void setKnown(final boolean known) {
		for (final MagicCard card : this) {
			card.setKnown(known);
		}
	}
	
	public int getNrOfLands() {
		int lands=0;
		for (final MagicCard card : this) {
			if (card.getCardDefinition().isLand()) {
				lands++;
			}
		}
		return lands;
	}
	
	public boolean useSmartShuffle() {
		final int lands=getNrOfLands();
		final int total=size();
		return lands==16&&total==40;
	}
	
	public void shuffle() {
        shuffle(size());
	}
	
    public void shuffle(final long seed) {
		final MagicCardList oldCards=new MagicCardList(this);
		clear();
        final Random rng = new Random(seed);
		for (int size=oldCards.size();size>0;size--) {
			final int index=rng.nextInt(size);
			final MagicCard card=oldCards.get(index);
			oldCards.remove(index);			
			add(card);
		}
	}
	
	public void smartShuffle() {
		
		final int size=size();
		final List<MagicCard> lands=new ArrayList<MagicCard>();
		final List<MagicCard> spells=new ArrayList<MagicCard>();
		int lowLeft=0;
		for (final MagicCard card : this) {
		
			final MagicCardDefinition cardDefinition=card.getCardDefinition();
			if (cardDefinition.isLand()) {
				lands.add(card);
			} else {
				spells.add(card);
				if (card.getCardDefinition().getConvertedCost()<=4) {
					lowLeft++;
				}
			}
		}
		
	    clear();
		for (int blocks=size/5;blocks>0;blocks--) {

			int landCount=0;
			int spellCount=0;
			int highCount=0;
			while (landCount+spellCount<5) {

				final int type=MagicRandom.nextInt(5);
				if (type<2) {
					if (landCount<2) {
						final int index=MagicRandom.nextInt(lands.size());
						add(lands.get(index));
						lands.remove(index);
						landCount++;
					}
				} else if (spellCount<3) {
					final int index=MagicRandom.nextInt(spells.size());
					final MagicCard card=spells.get(index);
					final boolean high=card.getCardDefinition().getConvertedCost()>4;
					if (!high||lowLeft==0||highCount==0||blocks==1) {
						add(card);
						spells.remove(index);
 						spellCount++;
 						if (high) {
 							highCount++;
 						} else {
 							lowLeft--;
 						}
					}
				}
			}
		}
	}
}
