package magic.model.trigger;

import magic.model.MagicCopyMap;
import magic.model.MagicPermanent;

public class MagicPermanentTrigger implements Comparable<MagicPermanentTrigger> {

	private final long id;
	private final MagicPermanent permanent;
	private final MagicTrigger<?> trigger;
	
	public MagicPermanentTrigger(final long id,final MagicPermanent permanent,final MagicTrigger<?> trigger) {
		this.id=id;
		this.permanent=permanent;
		this.trigger=trigger;
	}
	
	public MagicPermanentTrigger(final MagicCopyMap copyMap,final MagicPermanentTrigger source) {
		id=source.id;
		permanent=copyMap.copy(source.permanent);
		trigger=source.trigger;
	}
	
	public long getId() {
		return id;
	}
	
	public MagicPermanent getPermanent() {
		return permanent;
	}
	
	public MagicTrigger<?> getTrigger() {
		return trigger;
	}
	
	@Override
	public int compareTo(final MagicPermanentTrigger permanentTrigger) {
		final int priorityDif=trigger.getPriority()-permanentTrigger.trigger.getPriority();
		if (priorityDif<0) {
			return -1;
		} else if (priorityDif>0) {
			return 1;
		}
		final long idDif=id-permanentTrigger.id;
		if (idDif<0) {
			return -1;
		} else if (idDif>0) {
			return 1;
		} else {
			return 0;
		}
	}
}
