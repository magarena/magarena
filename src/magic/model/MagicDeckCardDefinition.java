package magic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// basically a wrapper for MagicCardDefinition that adds number of copies of card
public class MagicDeckCardDefinition {
	private int copies;
	private MagicCardDefinition card;
	
	public MagicDeckCardDefinition(MagicCardDefinition card) {
		this.card = card;
		copies = 1;
	}
	
	public MagicCardDefinition getCard() {
		return card;
	}
	
	public void incrementNumCopies() {
		copies++;
	}
	
	public void decrementNumCopies() {
		if (copies > 0) {
			copies--;
		}
	}
	
	public void setNumCopies(int i) {
		copies = i;
	}
	
	public int getNumCopies() {
		return copies;
	}
	
	public static List<MagicDeckCardDefinition> simpleCopyCardList(List<MagicCardDefinition> list) {
		ArrayList<MagicDeckCardDefinition> newList = new ArrayList<MagicDeckCardDefinition>();
		for(int i = 0; i < list.size(); i++) {
			newList.add(new MagicDeckCardDefinition(list.get(i)));
		}
		return newList;
	}
	
	public static List<MagicDeckCardDefinition> condenseCopyCardList(List<MagicCardDefinition> list) {
		Collections.sort(list, MagicCardDefinition.NAME_COMPARATOR_DESC);
	
		ArrayList<MagicDeckCardDefinition> newList = new ArrayList<MagicDeckCardDefinition>();
		MagicDeckCardDefinition lastDeckCard = null;
		
		for(int i = 0; i < list.size(); i++) {
			// increment copies count if more than one of the same card
			if(lastDeckCard != null && MagicCardDefinition.NAME_COMPARATOR_DESC.compare(lastDeckCard.getCard(), list.get(i)) == 0) {
				lastDeckCard.incrementNumCopies();
			} else {
				lastDeckCard = new MagicDeckCardDefinition(list.get(i));
				newList.add(lastDeckCard);
			}
		}
		return newList;
	}	

	public static final Comparator<MagicDeckCardDefinition> NUM_COPIES_COMPARATOR_DESC=new Comparator<MagicDeckCardDefinition>() {
		@Override
		public int compare(final MagicDeckCardDefinition cardDefinition1,final MagicDeckCardDefinition cardDefinition2) {
			return cardDefinition1.getNumCopies() - cardDefinition2.getNumCopies();
		}
	};

	public static final Comparator<MagicDeckCardDefinition> NUM_COPIES_COMPARATOR_ASC=new Comparator<MagicDeckCardDefinition>() {
		@Override
		public int compare(final MagicDeckCardDefinition cardDefinition1,final MagicDeckCardDefinition cardDefinition2) {
			return MagicDeckCardDefinition.NUM_COPIES_COMPARATOR_DESC.compare(cardDefinition2, cardDefinition1);
		}
	};
	
	public static final Comparator<MagicDeckCardDefinition> NAME_COMPARATOR_DESC=new Comparator<MagicDeckCardDefinition>() {
		@Override
		public int compare(final MagicDeckCardDefinition cardDefinition1,final MagicDeckCardDefinition cardDefinition2) {
			return MagicCardDefinition.NAME_COMPARATOR_DESC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
		}
	};

	public static final Comparator<MagicDeckCardDefinition> NAME_COMPARATOR_ASC=new Comparator<MagicDeckCardDefinition>() {
		@Override
		public int compare(final MagicDeckCardDefinition cardDefinition1,final MagicDeckCardDefinition cardDefinition2) {
			return MagicCardDefinition.NAME_COMPARATOR_ASC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
		}
	};
	
	public static final Comparator<MagicDeckCardDefinition> CONVERTED_COMPARATOR_DESC=new Comparator<MagicDeckCardDefinition>() {
		@Override
		public int compare(final MagicDeckCardDefinition cardDefinition1,final MagicDeckCardDefinition cardDefinition2) {
			return MagicCardDefinition.CONVERTED_COMPARATOR_DESC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
		}
	};
	
	public static final Comparator<MagicDeckCardDefinition> CONVERTED_COMPARATOR_ASC=new Comparator<MagicDeckCardDefinition>() {
		@Override
		public int compare(final MagicDeckCardDefinition cardDefinition1,final MagicDeckCardDefinition cardDefinition2) {
			return MagicCardDefinition.CONVERTED_COMPARATOR_ASC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
		}
	};

	public static final Comparator<MagicDeckCardDefinition> TYPE_COMPARATOR_DESC=new Comparator<MagicDeckCardDefinition>() {
		@Override
		public int compare(final MagicDeckCardDefinition cardDefinition1,final MagicDeckCardDefinition cardDefinition2) {
			return MagicCardDefinition.TYPE_COMPARATOR_DESC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
		}
	};

	public static final Comparator<MagicDeckCardDefinition> TYPE_COMPARATOR_ASC=new Comparator<MagicDeckCardDefinition>() {
		@Override
		public int compare(final MagicDeckCardDefinition cardDefinition1,final MagicDeckCardDefinition cardDefinition2) {
			return MagicCardDefinition.TYPE_COMPARATOR_ASC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
		}
	};

	public static final Comparator<MagicDeckCardDefinition> RARITY_COMPARATOR_DESC=new Comparator<MagicDeckCardDefinition>() {
		@Override
		public int compare(final MagicDeckCardDefinition cardDefinition1,final MagicDeckCardDefinition cardDefinition2) {
			return MagicCardDefinition.RARITY_COMPARATOR_DESC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
		}
	};

	public static final Comparator<MagicDeckCardDefinition> RARITY_COMPARATOR_ASC=new Comparator<MagicDeckCardDefinition>() {
		@Override
		public int compare(final MagicDeckCardDefinition cardDefinition1,final MagicDeckCardDefinition cardDefinition2) {
			return MagicCardDefinition.RARITY_COMPARATOR_ASC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
		}
	};

	public static final Comparator<MagicDeckCardDefinition> POWER_COMPARATOR_DESC=new Comparator<MagicDeckCardDefinition>() {
		@Override
		public int compare(final MagicDeckCardDefinition cardDefinition1,final MagicDeckCardDefinition cardDefinition2) {
			return MagicCardDefinition.POWER_COMPARATOR_DESC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
		}
	};

	public static final Comparator<MagicDeckCardDefinition> POWER_COMPARATOR_ASC=new Comparator<MagicDeckCardDefinition>() {
		@Override
		public int compare(final MagicDeckCardDefinition cardDefinition1,final MagicDeckCardDefinition cardDefinition2) {
			return MagicCardDefinition.POWER_COMPARATOR_ASC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
		}
	};

	public static final Comparator<MagicDeckCardDefinition> TOUGHNESS_COMPARATOR_DESC=new Comparator<MagicDeckCardDefinition>() {
		@Override
		public int compare(final MagicDeckCardDefinition cardDefinition1,final MagicDeckCardDefinition cardDefinition2) {
			return MagicCardDefinition.TOUGHNESS_COMPARATOR_DESC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
		}
	};

	public static final Comparator<MagicDeckCardDefinition> TOUGHNESS_COMPARATOR_ASC=new Comparator<MagicDeckCardDefinition>() {
		@Override
		public int compare(final MagicDeckCardDefinition cardDefinition1,final MagicDeckCardDefinition cardDefinition2) {
			return MagicCardDefinition.POWER_COMPARATOR_ASC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
		}
	};
}