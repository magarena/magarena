package magic.model.choice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicRandom;
import magic.model.event.MagicEvent;
import magic.ui.GameController;

public abstract class MagicChoice {
	
	public static final String YES_CHOICE="yes";
	public static final String NO_CHOICE="no";
	
	public static final Object[] UNDO_CHOICE_RESULTS=new Object[]{"Undo"};
	
	private final String description;
	
	public MagicChoice(final String description) {
		this.description=description;
	}
	
	public final String getDescription() {
		return description;
	}
	
	public MagicTargetChoice getTargetChoice() {
		return null;
	}
	
	public int getManaChoiceResultIndex() {
		return -1;
	}

	/** Checks if there are valid options for the choice. */
	public boolean hasOptions(final MagicGame game,final MagicPlayer player,final MagicSource source,final boolean hints) {
		return true;
	}
	
	/** Gets the available options for AI. */
	public abstract Collection<Object> getArtificialOptions(
			final MagicGame game,final MagicEvent event,final MagicPlayer player,final MagicSource source);
	
	/** Gets the choice results for AI. */
	public List<Object[]> getArtificialChoiceResults(
			final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {
		
		final Collection<Object> options=getArtificialOptions(game,event,player,source);
		final int size=options.size();
		if (size==1) {
			return Collections.singletonList(new Object[]{options.iterator().next()});
		} else if (size>1) {
			final List<Object[]> choiceResultsList=new ArrayList<Object[]>(size);
			for (final Object option : options) {
                choiceResultsList.add(new Object[]{option});
			}
			return choiceResultsList;
		} else {
			return Collections.emptyList();
		}
	}

	/** Gets one choice results for simulation. */
    public Object[] getSimulationChoiceResult(
			final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {
        final List<Object[]> choices = getArtificialChoiceResults(game, event, player, source);
        if (choices.size() == 0) {
            return null;
        } else {
            return choices.get(MagicRandom.nextInt(choices.size()));
        }
    }
	
	/** Gets the choice results of the player. */
	public abstract Object[] getPlayerChoiceResults(
            final GameController controller,
            final MagicGame game,
            final MagicPlayer player,
            final MagicSource source);

	public static boolean isYesChoice(final Object choiceResult) {
		return YES_CHOICE==choiceResult;
	}

	public static boolean isNoChoice(final Object choiceResult) {
		return NO_CHOICE==choiceResult;
	}
}
