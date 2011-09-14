package magic.model.choice;

import magic.data.GeneralConfig;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.ui.GameController;
import magic.ui.choice.MayChoicePanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MagicMayChoice extends MagicChoice {
	
	private static final List<Object[]> NO_OTHER_CHOICE_RESULTS=Arrays.asList(
            new Object[]{YES_CHOICE},
            new Object[]{NO_CHOICE});
	
	private final MagicChoice choices[];
	private MagicTargetChoice targetChoice = MagicTargetChoice.TARGET_NONE;
	private int manaChoiceResultIndex=-1;
	public static final int DEFAULT_NONE = 0;
	public static final int DEFAULT_NO = 1;
	public static final int DEFAULT_YES = 2;
	private int defaultChoice = DEFAULT_NONE;
	
	public MagicMayChoice(final String description,final MagicChoice... choices) {
		super(description);
		this.choices=choices;
		for (int index=0;index<choices.length;index++) {
			final MagicChoice choice=choices[index];
			if (choice instanceof MagicTargetChoice) {
				targetChoice=(MagicTargetChoice)choice;
			} else if (choice instanceof MagicPayManaCostChoice) {
				manaChoiceResultIndex=index+1;
			}
		}
	}
	
	public MagicMayChoice(final String description,final int defaultChoice,final MagicChoice... choices) {
		super(description);
		this.defaultChoice = defaultChoice;
		this.choices=choices;
		for (int index=0;index<choices.length;index++) {		
			final MagicChoice choice=choices[index];
			if (choice instanceof MagicTargetChoice) {
				targetChoice=(MagicTargetChoice)choice;
			} else if (choice instanceof MagicPayManaCostChoice) {
				manaChoiceResultIndex=index+1;
			}
		}
	}
			
	private MagicChoice[] getChoices() {
		return choices;
	}

	@Override
	public MagicTargetChoice getTargetChoice() {
		return targetChoice;
	}

	@Override
	public int getManaChoiceResultIndex() {
		return manaChoiceResultIndex;
	}
	
	@Override
	Collection<Object> getArtificialOptions(
            final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Object[]> getArtificialChoiceResults(
            final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {

		final int nrOfChoices = choices.length;
		if (nrOfChoices == 0) {
			if (defaultChoice == DEFAULT_NONE) {
				return NO_OTHER_CHOICE_RESULTS;
			}
			return (defaultChoice == DEFAULT_NO) ?
					Collections.singletonList(new Object[]{NO_CHOICE}) :
					Collections.singletonList(new Object[]{YES_CHOICE});
		}	
		final int nrOfChoiceResults=nrOfChoices+1;
		final Object noChoiceResults[]=new Object[nrOfChoiceResults];
		noChoiceResults[0]=NO_CHOICE;
		
		final List<Collection<Object>> optionsList=new ArrayList<Collection<Object>>(nrOfChoices);
		for (int index=0;index<nrOfChoices;index++) {
			if (!choices[index].hasOptions(game,player,source,true)) {
				return Collections.singletonList(noChoiceResults);				
			}			
			optionsList.add(choices[index].getArtificialOptions(game,event,player,source));
		}

		final List<Object[]> choiceResultsList=new ArrayList<Object[]>();
		final Object yesChoiceResults[]=new Object[nrOfChoiceResults];
		yesChoiceResults[0]=YES_CHOICE;		

		int index=0;
		final LinkedList<Iterator<Object>> iterators=new LinkedList<Iterator<Object>>();
		iterators.addLast(optionsList.get(0).iterator());
		while (index>=0) {
			final Iterator<Object> iterator=iterators.getLast();
			if (iterator.hasNext()) {
				index++;
				yesChoiceResults[index]=iterator.next(); // Starts from index 1.
				if (index<nrOfChoices) {
					iterators.addLast(optionsList.get(index).iterator());
				} else {
					choiceResultsList.add(Arrays.copyOf(yesChoiceResults,nrOfChoiceResults));
					index--;
				}
			} else {
				iterators.removeLast();
				index--;
			}
		}

		choiceResultsList.add(noChoiceResults);
		return choiceResultsList;
	}

	@Override
	public Object[] getPlayerChoiceResults(
            final GameController controller,
            final MagicGame game,
            final MagicPlayer player,
            final MagicSource source) {

		final int nrOfChoices = choices.length;
		final Object choiceResults[]=new Object[nrOfChoices+1];
		choiceResults[0]=NO_CHOICE;

		final boolean hints = GeneralConfig.getInstance().getSmartTarget();
		if (hints && nrOfChoices == 0 && defaultChoice != DEFAULT_NONE) {
			if (defaultChoice == DEFAULT_YES) {
				choiceResults[0]=YES_CHOICE;
			}
			return choiceResults;
		}

		for (final MagicChoice choice : choices) {
			if (!choice.hasOptions(game,player,source,hints)) {
				return choiceResults;
			}
		}
		
		final MayChoicePanel choicePanel=new MayChoicePanel(controller,source,getDescription());
		controller.disableActionButton(false);
		controller.showComponent(choicePanel);
		if (controller.waitForInputOrUndo()) {
			return UNDO_CHOICE_RESULTS;
		}
		if (!choicePanel.isYesClicked()) {
			return choiceResults;
		}
		
		// Yes is chosen.
		choiceResults[0]=YES_CHOICE;
		for (int index=0;index<nrOfChoices;index++) {
			
			final Object partialChoiceResults[]=choices[index].getPlayerChoiceResults(controller,game,player,source);
			if (partialChoiceResults==UNDO_CHOICE_RESULTS) {
				return UNDO_CHOICE_RESULTS;
			}
			choiceResults[index+1]=partialChoiceResults[0];
		}		
		return choiceResults;
	}
}
