package magic.model.choice;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.ui.GameController;
import magic.ui.choice.MayChoicePanel;
import java.util.concurrent.Callable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

// Buyback choice results : 0 = other choice, 1 = is buyback payed, 2 = buyback mana cost result
public class MagicBuybackChoice extends MagicChoice {

	private static final List<Object> NO_OPTIONS_LIST = Collections.<Object>singletonList(null);
	
	private final MagicChoice otherChoice;
	private final MagicManaCost cost;
	
    public MagicBuybackChoice(final MagicManaCost cost) {
		super("");
		this.otherChoice = MagicChoice.NONE;
		this.cost = cost;
	}
	
	public MagicBuybackChoice(final MagicChoice otherChoice,final MagicManaCost cost) {
		super("");
		this.otherChoice = otherChoice;
		this.cost = cost;
	}

	@Override
	public MagicTargetChoice getTargetChoice() {
		return (otherChoice instanceof MagicTargetChoice) ?
				(MagicTargetChoice)otherChoice :
				MagicTargetChoice.TARGET_NONE;
	}
	
	@Override
	public int getManaChoiceResultIndex() {
		return 2;
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

		final Collection<Object> otherOptions;
		if (otherChoice.isValid()) {
			otherOptions = otherChoice.getArtificialOptions(game,event,player,source);			
		} else {
			otherOptions = NO_OPTIONS_LIST;
		}
		
		final List<Object[]> choiceResultsList = new ArrayList<Object[]>();
		final Object choiceResults[] = new Object[3];
		final Collection<Object> manaOptions;
		if (!new MagicPayManaCostResultBuilder(game,player,cost.getBuilderCost()).hasResults()) {
			choiceResults[1] = NO_CHOICE;
			manaOptions = NO_OPTIONS_LIST;
		}
		else {
			choiceResults[1] = YES_CHOICE;
			final MagicPayManaCostChoice manaChoice = new MagicPayManaCostChoice(cost);
			manaOptions = manaChoice.getArtificialOptions(game,event,player,source);
		}

		for (final Object manaOption : manaOptions) {
			choiceResults[2] = manaOption;
			for (final Object otherOption : otherOptions) {
				choiceResults[0] = otherOption;
				choiceResultsList.add(Arrays.copyOf(choiceResults,3));
			}				
		}
		return choiceResultsList;
	}
	
	@Override
	public Object[] getPlayerChoiceResults(
            final GameController controller,
            final MagicGame game,
            final MagicPlayer player,
            final MagicSource source) {

		boolean isYesClicked = false;
		if (new MagicPayManaCostResultBuilder(game,player,cost.getBuilderCost()).hasResults()) {
			final MayChoicePanel kickerPanel = 
					controller.showComponent(new Callable<MayChoicePanel>() {
                public MayChoicePanel call() {
			        return new MayChoicePanel(
			        		controller,
			        		source,
			        		"You may pay the buyback " + cost.getText() + '.');
                }
            });
			if (controller.waitForInputOrUndo()) {
				return UNDO_CHOICE_RESULTS;
			}
			isYesClicked = kickerPanel.isYesClicked();				
		}

		final Object choiceResults[] = new Object[3];
		
		// Pay buyback cost.
		if (isYesClicked) {
			choiceResults[1] = YES_CHOICE;
			final MagicPayManaCostChoice manaChoice = new MagicPayManaCostChoice(cost);
			final Object manaChoiceResults[] = 
					manaChoice.getPlayerChoiceResults(controller,game,player,source);
			if (manaChoiceResults == UNDO_CHOICE_RESULTS) {
				return UNDO_CHOICE_RESULTS;
			}			
			choiceResults[2] = manaChoiceResults[0];
		} else {
			choiceResults[1]=NO_CHOICE;
		}

		// Pick other choice.
		if (otherChoice.isValid()) {
			final Object otherChoiceResults[] = 
					otherChoice.getPlayerChoiceResults(controller,game,player,source);
			if (otherChoiceResults == UNDO_CHOICE_RESULTS) {
				return UNDO_CHOICE_RESULTS;
			}
			choiceResults[0] = otherChoiceResults[0];
		} else {
			choiceResults[0] = null;
		}
		return choiceResults;
	}
}
