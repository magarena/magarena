package magic.model.stack;

import magic.model.MagicCopyMap;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicCard;

import java.util.Arrays;
import java.util.LinkedList;

public class MagicStack extends LinkedList<MagicItemOnStack> {

	private static final long serialVersionUID = 1L;
	
	private final int spells[];
	private final int counts[];
    private MagicItemOnStack oldTop; 
	
	public MagicStack() {
		spells=new int[2];
		counts=new int[2];
	}
	
	public MagicStack(final MagicCopyMap copyMap,final MagicStack source) {
		spells=Arrays.copyOf(source.spells,2);
		counts=Arrays.copyOf(source.counts,2);
		for (final MagicItemOnStack itemOnStack : source) {
			add(copyMap.copy(itemOnStack));
		}
	}

	private void addCount(final MagicItemOnStack itemOnStack) {
		final int index=itemOnStack.getController().getIndex();
		counts[index]++;
		if (itemOnStack.isSpell()) {
			spells[index]++;
		}
	}
	
	private void removeCount(final MagicItemOnStack itemOnStack) {
		final int index=itemOnStack.getController().getIndex();
		counts[index]--;
		if (itemOnStack.isSpell()) {
			spells[index]--;
		}		
	}
	
	public void addToTop(final MagicItemOnStack itemOnStack) {
		addFirst(itemOnStack);
		addCount(itemOnStack);
	}
	
	public MagicItemOnStack removeFromTop() {
		oldTop = removeFirst();
		removeCount(oldTop);
		return oldTop;
	}
	
	public void addTo(final int index,final MagicItemOnStack itemOnStack) {
		add(index,itemOnStack);
		addCount(itemOnStack);
	}
	
	public void removeFrom(final int index) {
		final MagicItemOnStack itemOnStack=remove(index);
		removeCount(itemOnStack);
	}
	
	public void removeAllItems() {
		clear();
		Arrays.fill(counts,0);
		Arrays.fill(spells,0);
	}
	
    public MagicCard getCard(final long id) {
        if (oldTop != null && 
            oldTop instanceof MagicCardOnStack &&
            ((MagicCardOnStack)oldTop).getCard().getId() == id) {
            return ((MagicCardOnStack)oldTop).getCard();
        }
        return MagicCard.NONE;
    }
		
	public MagicItemOnStack getItemOnStack(final long id) {
        if (oldTop != null && oldTop.getId() == id) {
            return oldTop;
        }
		for (final MagicItemOnStack itemOnStack : this) {
			if (itemOnStack.getId()==id) {
				return itemOnStack;
			}
		}
        throw new RuntimeException("No corresponding MagicItemOnStack with id " + id);
	}	
	
	public boolean hasActivationOnTop(final MagicSource source,final long actId) {
		if (isEmpty()) {
			return false;
		}		
		final MagicItemOnStack itemOnStack=getFirst();
		return itemOnStack.getSource()==source && itemOnStack.getActivation().getId() == actId;		
	}
	
	public boolean hasItemOnTopOfPlayer(final MagicPlayer player) {
		if (isEmpty()) {
			return false;
		}
        final MagicItemOnStack top = getFirst();
		return top.getController()==player;
	}
	
	public boolean isResponse(final MagicPlayer player) {
		return counts[1-player.getIndex()]>0;
	}
	
	public boolean containsSpells() {
		return spells[0]>0||spells[1]>0;
	}
	
	public boolean containsOpponentSpells(final MagicPlayer player) {
		return spells[1-player.getIndex()]>0;
	}

    public long getItemsId() {
        int idx = 0;
        long[] input = new long[size() + 1];
        for (MagicItemOnStack item : this) {
            input[idx] = item.getItemId();
            idx++;
        }
        return magic.MurmurHash3.hash(input);
    }
}
