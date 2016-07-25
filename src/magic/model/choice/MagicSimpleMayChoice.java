package magic.model.choice;

import magic.data.GeneralConfig;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.exception.UndoClickedException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import magic.model.IUIGameController;

public class MagicSimpleMayChoice extends MagicChoice {

    public static final int DRAW_CARDS = 1;
    public static final int GAIN_LIFE = 2;             // always returns YES_CHOICE_LIST
    public static final int LOSE_LIFE = 3;
    public static final int OPPONENT_LOSE_LIFE = 4;    // always returns YES_CHOICE_LIST
    public static final int UNTAP = 5;                 // always returns YES_CHOICE_LIST
    public static final int BECOME_CREATURE = 6;       // always returns YES_CHOICE_LIST
    public static final int ADD_POS_COUNTER = 7;       // always returns YES_CHOICE_LIST
    public static final int ADD_PLUSONE_COUNTER = 8;   // always returns YES_CHOICE_LIST
    public static final int PLAY_TOKEN = 9;            // always returns YES_CHOICE_LIST
    public static final int PUMP = 10;                 // always returns YES_CHOICE_LIST
    public static final int COUNTER_SPELL = 11;        // always returns YES_CHOICE_LIST

    public static final int DEFAULT_NONE = 0;
    public static final int DEFAULT_NO   = 1;
    public static final int DEFAULT_YES  = 2;

    private final int action;
    private final int amount;
    private int defaultChoice;

    private MagicSimpleMayChoice(final String description,final int action,final int amount,final int defaultChoice) {
        super(description);
        this.action = action;
        this.amount = amount;
        this.defaultChoice = defaultChoice;
    }

    public MagicSimpleMayChoice(final String description) {
        this(description, 0, 0, DEFAULT_YES);
    }

    public MagicSimpleMayChoice() {
        this(0, 0, DEFAULT_YES);
    }

    public MagicSimpleMayChoice(final int action) {
        this(action, 0, DEFAULT_YES);
    }

    public MagicSimpleMayChoice(final int action, final int defaultChoice) {
        this(action, 0, defaultChoice);
    }

    public MagicSimpleMayChoice(final int action, final int amount,final int defaultChoice) {
        this("Proceed with \"may\" action?", action, amount, defaultChoice);
    }

    @Override
    Collection<Object> getArtificialOptions(final MagicGame game,final MagicEvent event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Object[]> getArtificialChoiceResults(final MagicGame game,final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();
        boolean yes = true;
        switch (action) {
            case DRAW_CARDS:
                yes = player.getLibrary().size() - amount >= 1;
                break;
            case LOSE_LIFE:
                yes = player.getLife() - amount >= 1;
                break;
        }
        return yes ? YES_CHOICE_LIST : NO_CHOICE_LIST;
    }

    @Override
    public Object[] getPlayerChoiceResults(final IUIGameController controller, final MagicGame game, final MagicEvent event) throws UndoClickedException {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        final boolean hints = GeneralConfig.getInstance().getSmartTarget();
        if (hints && defaultChoice != DEFAULT_NONE) {
            return (defaultChoice == DEFAULT_NO) ?
                    new Object[]{NO_CHOICE} :
                    new Object[]{YES_CHOICE};
        }
        controller.disableActionButton(false);
        if (controller.getMayChoice(source, getDescription())) {
            return new Object[]{YES_CHOICE};
        }
        return new Object[]{NO_CHOICE};
    }

}
