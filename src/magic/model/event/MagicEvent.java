package magic.model.event;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCard;
import magic.model.MagicCopyMap;
import magic.model.MagicCopyable;
import magic.model.MagicGame;
import magic.model.MagicMappable;
import magic.model.MagicMessage;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicCardOnStackAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayerAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicPayManaCostResult;
import magic.model.choice.MagicTargetChoice;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDefaultTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetNone;
import magic.model.target.MagicTargetPicker;

import java.util.List;

public class MagicEvent implements MagicCopyable {

	public static final MagicEvent NO_EVENTS[] = new MagicEvent[0];
	public static final MagicChoice NO_CHOICES = MagicChoice.NONE;
	public static final Object NO_CHOICE_RESULTS[] = new Object[0];
	public static final Object NO_DATA[] = new Object[0];

	private MagicSource source;
	private MagicPlayer player;
	private MagicChoice choice;
	private MagicTargetPicker targetPicker;
	private Object data[];
	private MagicEventAction action;
	private String description;

	public MagicEvent(
            final MagicSource source,
            final MagicPlayer player,
            final MagicChoice choice,
			final MagicTargetPicker targetPicker,
            final Object data[],
            final MagicEventAction action,
            final String description) {
        this.source=source;
		this.player=player;
		this.choice=choice;
		this.targetPicker=targetPicker;
		this.data=data;
		this.action=action;
		this.description=description;
	}

	public MagicEvent(
            final MagicSource source,
            final MagicPlayer player,
            final MagicChoice choice,
			final Object data[],
            final MagicEventAction action,
            final String description) {
		this(source,player,choice,MagicDefaultTargetPicker.getInstance(),data,action,description);
	}
	
	public MagicEvent(
            final MagicSource source,
            final MagicPlayer player,
            final Object data[],
            final MagicEventAction action,
            final String description) {
		this(source,player,NO_CHOICES,MagicDefaultTargetPicker.getInstance(),data,action,description);
	}
	
	private MagicEvent() {
		
	}

	@Override
	public MagicCopyable create() {
		return new MagicEvent();
	}
	
	@Override
	public void copy(final MagicCopyMap copyMap,final MagicCopyable copySource) {
		final MagicEvent sourceEvent=(MagicEvent)copySource;
		source=(MagicSource)copyMap.copyObject(sourceEvent.source);
		player=copyMap.copy(sourceEvent.player);
		choice=sourceEvent.choice;
		targetPicker=sourceEvent.targetPicker;
		data=copyMap.copyObjects(sourceEvent.data,Object.class);
		action=sourceEvent.action;
		description=sourceEvent.description;
	}
	
	public final MagicSource getSource() {
		return source;
	}
	
	public final MagicPlayer getPlayer() {
		return player;
	}
		
	public final boolean hasChoice() {
		return choice != MagicChoice.NONE;
	}
	
	public final MagicChoice getChoice() {
		return choice;
	}
	
	public final MagicTargetPicker getTargetPicker() {
		return targetPicker;
	}
	
	public final List<Object[]> getArtificialChoiceResults(final MagicGame game) {
        final long start = System.currentTimeMillis();
        final List<Object[]> choices = choice.getArtificialChoiceResults(game,this,player,source);
		final long time = System.currentTimeMillis() - start;
        if (time > 1000) {
            System.err.println("WARNING. ACR:  " + choice.getDescription() + description + " time: " + time);
        }
        return choices;
	}
	
    public final Object[] getSimulationChoiceResult(final MagicGame game) {
        final long start = System.currentTimeMillis();
        final Object[] res = choice.getSimulationChoiceResult(game,this,player,source);
		final long time = System.currentTimeMillis() - start;
        if (time > 1000) {
            System.err.println("WARNING. RCR:  " + choice.getDescription() + description + " time: " + time);
        }
        return res;
	}

	public final MagicTargetChoice getTargetChoice() {
		return choice.getTargetChoice();
	}
	
	public final int getManaChoiceResultIndex() {
		return choice.getManaChoiceResultIndex();
	}

	public final Object[] getData() {
		return data;
	}
	
	public final String getDescription(final Object choiceResults[]) {
        return MagicMessage.replaceChoices(description,choiceResults);
	}

	public final String getChoiceDescription() {
		final String description=getDescription(MagicEvent.NO_CHOICE_RESULTS);
		if (description.length() > 0) {
			return description;
		}
		return hasChoice()?choice.getDescription():"";
	}
	
	private final MagicTarget getTarget(final MagicGame game,final Object choiceResults[],final int index) {
		final MagicTargetChoice targetChoice=getTargetChoice();
		final MagicTarget target=(MagicTarget)choiceResults[index];
		if (game.isLegalTarget(player,source,targetChoice,target)) {
			return target;
		} else {
    		return MagicTargetNone.getInstance();
        }
	}
    
    public final boolean processTarget(
            final MagicGame game,
            final Object choiceResults[],
            final int index,
            final MagicTargetAction effect
            ) {
        final MagicTarget target = getTarget(game, choiceResults, index);
        if (target != MagicTargetNone.getInstance()) {
            effect.doAction(target);
            return true;
        } else {
            return false;
        }
    }
	
    public final boolean processTargetPermanent(
            final MagicGame game,
            final Object choiceResults[],
            final int index,
            final MagicPermanentAction effect
            ) {
        final MagicTarget target = getTarget(game, choiceResults, index);
        if (target.isPermanent()) {
            effect.doAction((MagicPermanent)target);
            return true;
        } else {
            return false;
        }
    }

    public final boolean processTargetCardOnStack(
            final MagicGame game,
            final Object choiceResults[],
            final int index,
            final MagicCardOnStackAction effect
            ) {
        final MagicTarget target = getTarget(game, choiceResults, index);
        if (target.isSpell()) {
            effect.doAction((MagicCardOnStack)target);
            return true;
        } else {
            return false;
        }
    }
    
    public final boolean processTargetCard(
            final MagicGame game,
            final Object choiceResults[],
            final int index,
            final MagicCardAction effect
            ) {
        final MagicTarget target = getTarget(game, choiceResults, index);
        if (target.isSpell()) {
            effect.doAction((MagicCard)target);
            return true;
        } else {
            return false;
        }
    }

    public final boolean processTargetPlayer(
            final MagicGame game,
            final Object choiceResults[],
            final int index,
            final MagicPlayerAction effect
            ) {
        final MagicTarget target = getTarget(game, choiceResults, index);
        if (target.isPlayer()) {
            effect.doAction((MagicPlayer)target);
            return true;
        } else {
            return false;
        }
    }

	public final void payManaCost(
            final MagicGame game,
            final MagicPlayer player,
            final Object choiceResults[],
            final int index) {
		final MagicPayManaCostResult result=(MagicPayManaCostResult)choiceResults[index];
		// Result can be null when paying cost is optional.
		if (result!=null) {
			result.doAction(game,player);
			// Let each payed mana cost influence score.
			game.changeScore(ArtificialScoringSystem.getManaScore(result.getConverted()));
		}
	}
	
	public final void payManaCost(final MagicGame game,final MagicPlayer player,final Object choiceResults[]) {
		final int manaIndex=getManaChoiceResultIndex();
		if (manaIndex>=0) {
			payManaCost(game,player,choiceResults,manaIndex);
		}
	}
	
	public final void executeEvent(final MagicGame game,final Object choiceResults[]) {
		action.executeEvent(game,this,data,choiceResults);
	}

    public String toString() {
        return "EVENT: " + player.getIndex() + " " + description + " " + (hasChoice() ? choice.getDescription() : "");
    }

    public long getEventId() {
        long[] keys = {
            (source != null ? source.getId() : -1L),
            (player != null ? player.getIndex() : -1L),
	        (choice != null ? choice.hashCode() : -1L),
	        (targetPicker != null ? targetPicker.hashCode() : -1L),
            (action != null ? action.hashCode() : -1L),
            (data != null && data.length > 0 && data[0] != null) ?
                ((data[0] instanceof MagicMappable) ? ((MagicMappable)data[0]).getId() : data[0].hashCode()) : -1L,
            (data != null && data.length > 1 && data[1] != null) ?
                ((data[1] instanceof MagicMappable) ? ((MagicMappable)data[1]).getId() : data[1].hashCode()) : -1L,
            (data != null && data.length > 2 && data[2] != null) ?
                ((data[2] instanceof MagicMappable) ? ((MagicMappable)data[2]).getId() : data[2].hashCode()) : -1L,
        };
        return magic.MurmurHash3.hash(keys);
    }
}
