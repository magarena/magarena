package magic.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MagicCardList extends ArrayList<MagicCard> {
	
	private static final long serialVersionUID = 1L;
	
	public MagicCardList() {}
	
	public MagicCardList(final MagicCardList cardList) {
		super(cardList);
	}
	
	public MagicCardList(final MagicCopyMap copyMap, final MagicCardList cardList) {
		for (final MagicCard card : cardList) {
			add(copyMap.copy(card));
		}
	}
	
	public long getCardsId() {
        int idx = 0;
		long[] keys = new long[size() + 1];
		for (final MagicCard card : this) {
			keys[idx] = card.getCardDefinition().getIndex();
            idx++;
		}
		return magic.MurmurHash3.hash(keys);
	}
	
    public long getSortedCardsId() {
        int idx = 0;
		long[] keys = new long[size() + 1];
		for (final MagicCard card : this) {
			keys[idx] = card.getCardDefinition().getIndex();
            idx++;
		}
        Arrays.sort(keys);
		return magic.MurmurHash3.hash(keys);
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
		if (index >= 0) {
            remove(index);
        } else {
            System.err.println("WARNING. Card " + card.getName() + " not found in hand.");
        }
		return index;
	}
	
	public MagicCard getCard(final long id) {
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
		final int lands = getNrOfLands();
		final int total = size();
		return lands == 16 && total == 40;
	}
	
    public void shuffle(final long seed) {
        final magic.MersenneTwisterFast rng = new magic.MersenneTwisterFast(seed);
		final MagicCardList oldCards = new MagicCardList(this);
		clear();
		for (int size = oldCards.size(); size > 0; size--) {
			final int index=rng.nextInt(size);
			final MagicCard card=oldCards.get(index);
			oldCards.remove(index);			
			add(card);
		}
	}
	
	public void smartShuffle(final long seed) {
        final magic.MersenneTwisterFast rng = new magic.MersenneTwisterFast(seed);
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
				final int type = rng.nextInt(5);
				if (type<2) {
					if (landCount<2) {
						final int index = rng.nextInt(lands.size());
						add(lands.get(index));
						lands.remove(index);
						landCount++;
					}
				} else if (spellCount<3) {
					final int index = rng.nextInt(spells.size());
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
