package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicCopyMap;
import magic.model.MagicPermanent;
import magic.model.MagicSource;

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.TreeMap;

public class MagicActivationMap extends TreeMap<MagicActivation,SortedSet<MagicSource>> {

	private static final long serialVersionUID = 1L;
	
	public MagicActivationMap() {}
	
	public MagicActivationMap(final MagicCopyMap copyMap,final MagicActivationMap map) {
        for (final Map.Entry<MagicActivation,SortedSet<MagicSource>> entry : map.entrySet()) {
            final SortedSet<MagicSource> sources=new TreeSet<MagicSource>();
			copyMap.copyCollection(entry.getValue(),sources);
            put(entry.getKey(), sources);
		}
	}
	
	public Set<MagicActivation> getActivations() {
		return keySet();
	}
	
	private void addActivation(final MagicActivation activation,final MagicSource source) {
		SortedSet<MagicSource> sources=get(activation);
		if (sources==null) {
			sources=new TreeSet<MagicSource>();
			put(activation,sources);
		}
		sources.add(source);
	}
	
	private void removeActivation(final MagicActivation activation,final MagicSource source) {
		final Set<MagicSource> sources=get(activation);		
		if (sources!=null) {
			sources.remove(source);
			if (sources.isEmpty()) {
				remove(activation);
			}
		}
	}
	
	public void addActivations(final MagicCard card) {
		addActivation(card.getCardDefinition().getCardActivation(),card);
	}
	
	public void addActivations(final MagicCardList cardList) {
		for (final MagicCard card : cardList) {
			addActivations(card);
		}
	}

	public void addActivations(final MagicPermanent permanent) {
		for (final MagicActivation activation : permanent.getCardDefinition().getActivations()) {
			addActivation(activation,permanent);
		}
	}
	
	public void removeActivations(final MagicCard card) {
		removeActivation(card.getCardDefinition().getCardActivation(),card);
	}
	
	public void removeActivations(final MagicCardList cardList) {
		for (final MagicCard card : cardList) {
			removeActivations(card);
		}
	}
	
	public void removeActivations(final MagicPermanent permanent) {
		for (final MagicActivation activation : permanent.getCardDefinition().getActivations()) {
			removeActivation(activation,permanent);
		}
	}
		
	private void print() {
		System.err.println(getClass().getName());
        for (final Map.Entry<MagicActivation,SortedSet<MagicSource>> entry : entrySet()) {
			System.err.print(entry.getKey().getClass().getName()+" :");
			for (final MagicSource source : entry.getValue()) {
				System.err.print(" "+source.getName());
			}
			System.err.println();
		}
		System.err.println();
	}
}
