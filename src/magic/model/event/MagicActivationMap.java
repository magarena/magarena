package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicCopyMap;
import magic.model.MagicPermanent;
import magic.model.MagicSource;

import java.util.HashMap;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class MagicActivationMap extends HashMap<MagicActivation,SortedSet<MagicSource>> {

	private static final long serialVersionUID = 1L;
	
	private final SortedSet<MagicActivation> activations; // Must be ordered.
	
	public MagicActivationMap() {
		activations=new TreeSet<MagicActivation>();
	}
	
	public MagicActivationMap(final MagicCopyMap copyMap,final MagicActivationMap map) {
		activations=new TreeSet<MagicActivation>(map.activations);
		for (final MagicActivation activation : activations) {
			final SortedSet<MagicSource> sources=new TreeSet<MagicSource>();
			copyMap.copyCollection(map.get(activation),sources);
			put(activation,sources);
		}
	}
	
	public SortedSet<MagicActivation> getActivations() {
		return activations;
	}
	
	private void addActivation(final MagicActivation activation,final MagicSource source) {
		SortedSet<MagicSource> sources=get(activation);
		if (sources==null) {
			sources=new TreeSet<MagicSource>();
			put(activation,sources);
			activations.add(activation);
		}
		sources.add(source);
	}
	
	private void removeActivation(final MagicActivation activation,final MagicSource source) {
		final Set<MagicSource> sources=get(activation);		
		if (sources!=null) {
			sources.remove(source);
			if (sources.isEmpty()) {
				remove(activation);
				activations.remove(activation);
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
		
	public void print() {
		System.err.println(getClass().getName());
		for (final MagicActivation activation : activations) {
			System.err.print(activation.getClass().getName()+" :");
			for (final MagicSource source : get(activation)) {
				System.err.print(" "+source.getName());
			}
			System.err.println();
		}
		System.err.println();
	}
}
