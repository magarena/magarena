package magic.model.choice;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.ui.GameController;
import magic.ui.choice.MayChoicePanel;
import magic.ui.choice.MultiKickerChoicePanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

// Kicker choice results : 0 = other choice, 1 = number of times kicked, 2 = kicker mana cost result
public class MagicKickerChoice extends MagicChoice {

	private static final List<Object> NO_OPTIONS_LIST = Collections.<Object>singletonList(null);
	
	private final MagicChoice otherChoice;
	private final MagicManaCost cost;
	private final boolean multi;
	
    public MagicKickerChoice(final MagicManaCost cost,final boolean multi) {
		super("Choose to many times to pay the kicker cost.");
		this.otherChoice=MagicChoice.NONE;
		this.cost=cost;
		this.multi=multi;
	}
	
	public MagicKickerChoice(final MagicChoice otherChoice,final MagicManaCost cost,final boolean multi) {
		super("Choose to many times to pay the kicker cost.");
		this.otherChoice=otherChoice;
		this.cost=cost;
		this.multi=multi;
	}

	@Override
	public MagicTargetChoice getTargetChoice() {
		return (otherChoice instanceof MagicTargetChoice) ? (MagicTargetChoice)otherChoice : MagicTargetChoice.NONE;
	}
	
	@Override
	public int getManaChoiceResultIndex() {
		return 2;
	}

	@Override
	public Collection<Object> getArtificialOptions(
            final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {
		throw new UnsupportedOperationException();
	}

	private int getMaximumCount(final MagicGame game,final MagicPlayer player) {
		final MagicBuilderManaCost builderCost=new MagicBuilderManaCost(player.getBuilderCost());
		for (int index=1;;index++) {
			cost.addTo(builderCost);
			if (!new MagicPayManaCostResultBuilder(game,player,builderCost).hasResults()) {
				return index-1;
			}
			if (multi==false) {
				return 1;
			}
		}
	}
	
	private MagicManaCost getCost(final int count) {
		if (count==1) {
			return cost;
		} else if (count==0) {
			return MagicManaCost.ZERO;			
		} else {
			final StringBuilder costText=new StringBuilder();
			final String text=cost.getText();
			for (int c=count;c>0;c--) {
				costText.append(text);
			}			
			return MagicManaCost.createCost(costText.toString());
		}
	}

	@Override
	public List<Object[]> getArtificialChoiceResults(
            final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {

		final Collection<Object> otherOptions;
		if (otherChoice == MagicChoice.NONE) {
			otherOptions=NO_OPTIONS_LIST;
		} else {
			otherOptions=otherChoice.getArtificialOptions(game,event,player,source);			
		}
		
		final List<Object[]> choiceResultsList=new ArrayList<Object[]>();
		final int maximumCount=getMaximumCount(game,player);
		for (int count=0;count<=maximumCount;count++) {
			final Object choiceResults[]=new Object[3];
			choiceResults[1]=count;
			
			final Collection<Object> manaOptions;
			if (count==0) {
				manaOptions=NO_OPTIONS_LIST;
			} else {
				final MagicPayManaCostChoice manaChoice=new MagicPayManaCostChoice(getCost(count));
				manaOptions=manaChoice.getArtificialOptions(game,event,player,source);
			}

			for (final Object manaOption : manaOptions) {
				choiceResults[2]=manaOption;
				for (final Object otherOption : otherOptions) {
					choiceResults[0]=otherOption;
					choiceResultsList.add(Arrays.copyOf(choiceResults,3));
				}				
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

		final int maximumCount=getMaximumCount(game,player);
		final int count;
		if (maximumCount>1) {
			// Multiple kickers.
			final MultiKickerChoicePanel kickerPanel=new MultiKickerChoicePanel(controller,source,cost,maximumCount);
			controller.showComponent(kickerPanel);
			if (controller.waitForInputOrUndo()) {
				return UNDO_CHOICE_RESULTS;
			}
			count=kickerPanel.getKickerCount();
		} else if (maximumCount==1) {
			// Single kicker.
			final MayChoicePanel kickerPanel=new MayChoicePanel(controller,source,"You may pay the kicker "+cost.getText()+'.');
			controller.showComponent(kickerPanel);
			if (controller.waitForInputOrUndo()) {
				return UNDO_CHOICE_RESULTS;
			}
			count=kickerPanel.isYesClicked()?1:0;				
		} else {
			count=0;
		}

		final Object choiceResults[]=new Object[3];
		choiceResults[1]=count;
		// Pay kicker.
		if (count>0) {
			final MagicPayManaCostChoice manaChoice=new MagicPayManaCostChoice(getCost(count));
			final Object manaChoiceResults[]=manaChoice.getPlayerChoiceResults(controller,game,player,source);
			if (manaChoiceResults==UNDO_CHOICE_RESULTS) {
				return UNDO_CHOICE_RESULTS;
			}			
			choiceResults[2]=manaChoiceResults[0];
		}

		// Pick other choice.
		if (otherChoice==MagicChoice.NONE) {
			choiceResults[0]=null;
		} else {
			final Object otherChoiceResults[]=otherChoice.getPlayerChoiceResults(controller,game,player,source);
			if (otherChoiceResults==UNDO_CHOICE_RESULTS) {
				return UNDO_CHOICE_RESULTS;
			}
			choiceResults[0]=otherChoiceResults[0];
		}
		return choiceResults;
	}
}
