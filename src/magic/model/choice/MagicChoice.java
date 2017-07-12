package magic.model.choice;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicRandom;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetNone;
import magic.exception.UndoClickedException;
import magic.exception.GameException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import magic.model.IUIGameController;

public abstract class MagicChoice {

    static final String YES_CHOICE="yes";
    static final String NO_CHOICE="no";

    protected static final List<Object[]> YES_CHOICE_LIST =
            Collections.singletonList(new Object[]{YES_CHOICE});
    protected static final List<Object[]> NO_CHOICE_LIST =
            Collections.singletonList(new Object[]{NO_CHOICE});

    protected static final List<Object[]> NO_OTHER_CHOICE_RESULTS = Arrays.asList(
        new Object[]{YES_CHOICE},
        new Object[]{NO_CHOICE}
    );

    public static final MagicChoice NONE = new MagicChoice("none") {
        @Override
        public Collection<Object> getArtificialOptions(final MagicGame game, final MagicEvent event) {
            return Collections.emptyList();
        }
        @Override
        public Object[] getPlayerChoiceResults(final IUIGameController controller, final MagicGame game, final MagicEvent event) {
            return new Object[0];
        }
        @Override
        public boolean isValid() {
            return false;
        }
    };

    private final String description;

    MagicChoice(final String description) {
        this.description=description;
    }

    @Override
    public String toString() {
        return getClass().toString();
    }

    public final String getDescription() {
        return description;
    }

    public MagicTargetChoice getTargetChoice() {
        return MagicTargetChoice.NONE;
    }

    public MagicTargetChoice getTargetChoice(final Object[] chosen) {
        return getTargetChoice();
    }

    public int getTargetChoiceResultIndex() {
        return -1;
    }

    public int getManaChoiceResultIndex() {
        return -1;
    }

    public long getStateId() {
        return description.hashCode();
    }

    public boolean isValid() {
        return true;
    }

    /** Checks if there are valid options for the choice. */
    public boolean hasOptions(final MagicGame game,final MagicPlayer player,final MagicSource source,final boolean hints) {
        return true;
    }

    /** Gets the available options for AI. */
    abstract Collection<?> getArtificialOptions(final MagicGame game,final MagicEvent event);

    /** Gets the choice results for AI. */
    public List<Object[]> getArtificialChoiceResults(final MagicGame game, final MagicEvent event) {
        final Collection<?> options=getArtificialOptions(game,event);
        final int size=options.size();
        if (size == 0) {
            return Collections.singletonList(new Object[]{MagicTargetNone.getInstance()});
        } else if (size == 1) {
            return Collections.singletonList(new Object[]{options.iterator().next()});
        } else {
            final List<Object[]> choiceResultsList=new ArrayList<Object[]>(size);
            for (final Object option : options) {
                choiceResultsList.add(new Object[]{option});
            }
            return choiceResultsList;
        }
    }

    /** Gets one choice results for simulation. */
    public Object[] getSimulationChoiceResult(final MagicGame game, final MagicEvent event) {
        final List<Object[]> choices = getArtificialChoiceResults(game, event);
        final int size = choices.size();
        if (size == 0) {
            throw new GameException("no simulation choice result", game);
        }
        return choices.get(MagicRandom.nextRNGInt(choices.size()));
    }

    /** Gets the choice results of the player. */
    public abstract Object[] getPlayerChoiceResults(final IUIGameController controller, final MagicGame game, final MagicEvent event) throws UndoClickedException;

    public static boolean isYesChoice(final Object choiceResult) {
        return choiceResult == YES_CHOICE;
    }

    public static boolean isNoChoice(final Object choiceResult) {
        return choiceResult == NO_CHOICE;
    }
}
