package magic.model.event;

import java.util.List;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCopyMap;
import magic.model.MagicCopyable;
import magic.model.MagicGame;
import magic.model.MagicMessage;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicPayManaCostResult;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicDefaultTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetPicker;

public class MagicEvent implements MagicCopyable {

	public static final MagicEvent NO_EVENTS[]=new MagicEvent[0];
	public static final MagicChoice NO_CHOICES=null;
	public static final Object NO_CHOICE_RESULTS[]=new Object[0];
	public static final Object NO_DATA[]=new Object[0];

	private MagicSource source;
	private MagicPlayer player;
	private MagicChoice choice;
	private MagicTargetPicker targetPicker;
	private Object data[];
	private MagicEventAction action;
	private String description;

	public MagicEvent(final MagicSource source,final MagicPlayer player,final MagicChoice choice,
			final MagicTargetPicker targetPicker,final Object data[],final MagicEventAction action,final String description) {

		this.source=source;
		this.player=player;
		this.choice=choice;
		this.targetPicker=targetPicker;
		this.data=data;
		this.action=action;
		this.description=description;
	}

	public MagicEvent(final MagicSource source,final MagicPlayer player,final MagicChoice choice,
			final Object data[],final MagicEventAction action,final String description) {
		
		this(source,player,choice,MagicDefaultTargetPicker.getInstance(),data,action,description);
	}
	
	public MagicEvent(final MagicSource source,final MagicPlayer player,final Object data[],final MagicEventAction action,final String description) {
		
		this(source,player,NO_CHOICES,null,data,action,description);
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
		
		return choice!=null;
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
            System.err.println("ACR:  " + choice.getDescription() + description + " time: " + time);
        }
        return choices;
	}
	
    public final Object[] getSimulationChoiceResult(final MagicGame game) {
        final long start = System.currentTimeMillis();
        final Object[] res = choice.getSimulationChoiceResult(game,this,player,source);
		final long time = System.currentTimeMillis() - start;
        if (time > 1000) {
            System.err.println("RCR:  " + choice.getDescription() + description + " time: " + time);
        }
        return res;
	}

	public final MagicTargetChoice getTargetChoice() {
		
		return choice!=null?choice.getTargetChoice():null;
	}
	
	public final int getManaChoiceResultIndex() {
		
		return choice!=null?choice.getManaChoiceResultIndex():-1;
	}

	public final Object[] getData() {
		
		return data;
	}
	
	public final String getDescription(final Object choiceResults[]) {

		if (description!=null) {
			return MagicMessage.replaceChoices(description,choiceResults);
		}
		return description;
	}

	public final String getChoiceDescription() {

		final String description=getDescription(MagicEvent.NO_CHOICE_RESULTS);
		if (description!=null) {
			return description;
		}
		return hasChoice()?choice.getDescription():"";
	}
	
	@SuppressWarnings("unchecked")
	public final <E extends MagicTarget> E getTarget(final MagicGame game,final Object choiceResults[],final int index) {

		final MagicTargetChoice targetChoice=getTargetChoice();
		final MagicTarget target=(MagicTarget)choiceResults[index];
		if (game.isLegalTarget(player,source,targetChoice,target)) {
			return (E)target;
		}
		return null;
	}

	public final void payManaCost(final MagicGame game,final MagicPlayer player,final Object choiceResults[],final int index) {
		
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
}
