package magic.model.stack;

import magic.model.MagicCopyMap;
import magic.model.MagicCopyable;
import magic.model.MagicGame;
import magic.model.MagicMappable;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicActivation;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;

import javax.swing.ImageIcon;

public abstract class MagicItemOnStack implements MagicTarget {
	
	private MagicSource source;
	private MagicPlayer controller;
	private MagicActivation activation=null;
	private MagicEvent event;
	private Object choiceResults[]=MagicEvent.NO_CHOICE_RESULTS;
	private long id;
	
	@Override
	public void copy(final MagicCopyMap copyMap,final MagicCopyable copySource) {
		final MagicItemOnStack sourceItem=(MagicItemOnStack)copySource;
		source=(MagicSource)copyMap.copyObject(sourceItem.source);
		controller=copyMap.copy(sourceItem.controller);
		activation=sourceItem.activation;
		event=copyMap.copy(sourceItem.event);
		choiceResults=copyMap.copyObjects(sourceItem.choiceResults,Object.class);
		id=sourceItem.id;
	}
	
	@Override
	public Object map(final MagicGame game) {
		return game.getStack().getItemOnStack(id);
	}

	public void setSource(final MagicSource source) {
		this.source=source;
	}
		
	public MagicSource getSource() {
		return source;
	}
	
	public void setController(final MagicPlayer controller) {
		this.controller=controller;
	}
	
	@Override
	public MagicPlayer getController() {
		return controller;
	}
	
	public void setActivation(final MagicActivation activation) {
		this.activation=activation;
	}
	
	public MagicActivation getActivation() {
		return activation;
	}
			
	public void setEvent(final MagicEvent event) {
		this.event=event;
	}
	
	public MagicEvent getEvent() {
		return event;
	}
	
	public boolean hasChoices() {
		return event.hasChoice();
	}

	public void setChoiceResults(final Object choiceResults[]) {
		this.choiceResults=choiceResults;
	}
	
	public Object[] getChoiceResults() {
		return choiceResults;
	}
	
	public boolean containsInChoiceResults(final MagicPermanent permanent) {
		if (choiceResults!=null) {
			for (final Object choiceResult : choiceResults) {
				if (choiceResult==permanent) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void setId(final long id) {
		this.id=id;
	}
	
	public long getId() {
		return id;
	}
	
	public void resolve(final MagicGame game) {
		game.executeEvent(event,choiceResults);
	}
	
	@Override
	public String getName() {
		return source.getName();
	}
	
	public String getDescription() {
		return event.getDescription(choiceResults);
	}
	
	@Override
	public String toString() {
		return getName();
	}
		
	@Override
	public boolean isPermanent() {
		return false;
	}

	@Override
	public boolean isPlayer() {
		return false;
	}

	@Override
	public int getPreventDamage() {
		return 0;
	}
	
	@Override
	public void setPreventDamage(final int amount) {
		
	}

	@Override
	public boolean isValidTarget(final MagicGame game,final MagicSource source) {
		return source!=this.source;
	}

    public long getItemId() {
        final Object[] CR = choiceResults;
        final long[] keys = {
            id,
	        source != null ?  source.getId() : -1L,
            controller != null ? controller.getId() : -1L,
            activation != null ? activation.hashCode() : -1L,
            event != null ? event.getEventId() : -1L,
            (CR.length > 0 && CR[0] != null) ?
                ((CR[0] instanceof MagicMappable) ? ((MagicMappable)CR[0]).getId() : CR[0].hashCode()) :
                -1L,
            (CR.length > 1 && CR[1] != null) ?
                ((CR[1] instanceof MagicMappable) ? ((MagicMappable)CR[1]).getId() : CR[1].hashCode()) :
                -1L,
            (CR.length > 2 && CR[2] != null) ?
                ((CR[2] instanceof MagicMappable) ? ((MagicMappable)CR[2]).getId() : CR[2].hashCode()) :
                -1L,
        };
        return magic.MurmurHash3.hash(keys);
    }

	public abstract boolean isSpell();

	public abstract boolean canBeCountered();

	public abstract ImageIcon getIcon();
}
