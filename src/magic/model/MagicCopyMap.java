package magic.model;

import magic.model.event.MagicEvent;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;

public class MagicCopyMap extends HashMap<MagicCopyable,MagicCopyable> {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public <E extends MagicCopyable> E copy(final E source) {
		if (source == null || 
            source==MagicPlayer.NONE || 
            source == MagicCard.NONE || 
            source == MagicPermanent.NONE ||
            source == MagicEvent.NONE) {
			return source;
		}		
		E target=(E)get(source);
		if (target==null) {			
			target=(E)source.create();
			put(source,target);
			target.copy(this,source);
		}
		return target;
	}
		
	public Object copyObject(final Object source) {
		if (source == null || 
            source==MagicPlayer.NONE || 
            source == MagicCard.NONE || 
            source == MagicPermanent.NONE ||
            source == MagicEvent.NONE) {
			return source;
        } else if (source instanceof MagicCopyable) {
			return copy((MagicCopyable)source);
		} else {
			return source;
		}
	}
	
	@SuppressWarnings("unchecked")
	public <E> E[] copyObjects(final E[] sources,final Class<E> clazz) {
		if (sources==null||sources.length==0) {
			return sources;
		}
		final E targets[]=(E[])Array.newInstance(clazz,sources.length);
		for (int index=0;index<targets.length;index++) {
			targets[index]=(E)copyObject(sources[index]);
		}
		return targets;
	}
	
	@SuppressWarnings("unchecked")
	public <E> void copyCollection(final Collection<E> sourceCollection,final Collection<E> targetCollection) {
		for (final E object : sourceCollection) {
			targetCollection.add((E)copyObject(object));
		}
	}
}
