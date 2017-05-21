package magic.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class MagicCardList extends ArrayList<MagicCard> implements MagicCopyable {

    public static final MagicCardList NONE = new MagicCardList();

    public MagicCardList() {}

    public MagicCardList(final List<MagicCard> cardList) {
        super(cardList);
    }

    MagicCardList(final MagicCopyMap copyMap, final List<MagicCard> cardList) {
        for (final MagicCard card : cardList) {
            add(copyMap.copy(card));
        }
    }

    @Override
    public MagicCardList copy(final MagicCopyMap copyMap) {
        return new MagicCardList(copyMap, this);
    }

    public long getStateId() {
        final long[] keys = new long[size()];
        int idx = 0;
        for (final MagicCard card : this) {
            keys[idx] = card.getStateId();
            idx++;
        }
        return MurmurHash3.hash(keys);
    }

    public long getUnorderedStateId() {
        final long[] keys = new long[size()];
        int idx = 0;
        for (final MagicCard card : this) {
            keys[idx] = card.getStateId();
            idx++;
        }
        Arrays.sort(keys);
        return MurmurHash3.hash(keys);
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
        final int size = size();
        return size > 0 ? get(size-1) : MagicCard.NONE;
    }

    public MagicCardList getRandomCards(final int amount) {
        final MagicRandom rng = new MagicRandom(getStateId());
        final MagicCardList copy = new MagicCardList(this);
        final MagicCardList choiceList = new MagicCardList();
        final int actual = Math.min(amount, copy.size());
        for (int i = 1; i <= actual; i++) {
            final int index = rng.nextInt(copy.size());
            choiceList.add(copy.remove(index));
        }
        return choiceList;
    }

    public MagicCardList getCardsFromTop(final int amount) {
        final int size = size();
        final MagicCardList choiceList = new MagicCardList();
        final int actual = Math.min(amount, size);
        for (int i = 1; i <= actual; i++) {
            choiceList.add(get(size-i));
        }
        return choiceList;
    }

    public MagicCard removeCardAtTop() {
        final int index=size()-1;
        final MagicCard card=get(index);
        remove(index);
        return card;
    }

    public MagicCard removeCardAtBottom() {
        final MagicCard card=get(0);
        remove(0);
        return card;
    }

    public int removeCard(final MagicCard card) {
        final int index=indexOf(card);
        if (index >= 0) {
            remove(index);
        } else {
            throw new RuntimeException("Card " + card.getName() + " not found.");
        }
        return index;
    }

    public MagicCard getCard(final long id) {
        for (final MagicCard card : this) {
            if (card.getId()==id) {
                return card;
            }
        }
        return MagicCard.NONE;
    }

    public boolean containsType(final MagicType type) {
        for (final MagicCard card : this) {
            if (card.hasType(type)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(final MagicCard card) {
        for (final MagicCard cardList : this) {
            if (cardList.equals(card)) {
                return true;
            }
        }
        return false;
    }

    public void setCards(final MagicCardList cardList) {
        clear();
        addAll(cardList);
    }

    void setAIKnown(final boolean known) {
        for (final MagicCard card : this) {
            card.setAIKnown(known);
        }
    }

    public int getNrOf(MagicType type) {
        int amount=0;
        for (final MagicCard card : this) {
            if (card.getCardDefinition().hasType(type)) {
                amount++;
            }
        }
        return amount;
    }

    private boolean useSmartShuffle() {
        final int lands = getNrOf(MagicType.Land);
        final int total = size();
        return lands == 16 && total == 40;
    }

    //use smart shuffle if possible
    public void initialShuffle(final long seed) {
        if (useSmartShuffle()) {
            smartShuffle(seed);
        } else {
            shuffle(seed);
        }
    }

    public void shuffle() {
        shuffle(getStateId());
    }

    public void shuffle(final long seed) {
        final MagicRandom rng = new MagicRandom(seed);
        final MagicCardList oldCards = new MagicCardList(this);
        clear();
        for (int size = oldCards.size(); size > 0; size--) {
            final int index=rng.nextInt(size);
            final MagicCard card=oldCards.get(index);
            oldCards.remove(index);
            add(card);
        }
    }

    private void smartShuffle(final long seed) {
        final MagicRandom rng = new MagicRandom(seed);
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
