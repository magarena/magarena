package magic.model.mstatic;

import magic.model.MagicCopyMap;
import magic.model.MagicPermanent;

public class MagicPermanentStatic implements Comparable<MagicPermanentStatic> {

	private final long id;
	private final MagicPermanent permanent;
	private final MagicStatic mstatic;
	
	public MagicPermanentStatic(final long id,final MagicPermanent permanent,final MagicStatic mstatic) {
		this.id=id;
		this.permanent=permanent;
		this.mstatic=mstatic;
	}
	
	public MagicPermanentStatic(final MagicCopyMap copyMap,final MagicPermanentStatic source) {
		this(source.id, copyMap.copy(source.permanent), source.mstatic);
	}
	
	public long getId() {
		return id;
	}
	
	public MagicPermanent getPermanent() {
		return permanent;
	}
	
	public MagicStatic getStatic() {
		return mstatic;
	}
	
	@Override
	public int compareTo(final MagicPermanentStatic permanentStatic) {
        //sort by layer
		final int layerDif = mstatic.getLayer().compareTo(permanentStatic.mstatic.getLayer());
		if (layerDif != 0) {
			return layerDif;
		} 
        
        //break ties by id
		return Long.signum(id-permanentStatic.id);
	}
}
