package magic.model.stack;

import javax.swing.ImageIcon;

import magic.model.MagicCopyMap;
import magic.model.MagicCopyable;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicActivation;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;

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
        final long[] keys = {
            event.getEventId(),
            ((choiceResults.length > 0 && choiceResults[0] instanceof MagicTarget) ? 
                ((MagicTarget)choiceResults[0]).getId() : -1L),
            ((choiceResults.length > 1 && choiceResults[1] instanceof MagicTarget) ? 
                ((MagicTarget)choiceResults[1]).getId() : -1L),
            ((choiceResults.length > 2 && choiceResults[2] instanceof MagicTarget) ? 
                ((MagicTarget)choiceResults[2]).getId() : -1L),
        };
        return magic.MurmurHash3.hash(keys);
    }

	public abstract boolean isSpell();

	public abstract boolean canBeCountered();

	public abstract ImageIcon getIcon();
}
