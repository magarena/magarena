package magic.model.choice;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import magic.exception.UndoClickedException;
import magic.model.IUIGameController;
import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilterFactory;

/** Contains optimal decision logic for each choice type. */
public class MagicColorChoice extends MagicChoice {

    // translatable UI text (prefix with _S).
    private static final String _S1 = "Choose a color.";

    private static final int ALL=0;
    private static final int MOST=1;
    private static final int UNSUMMON=2;
    private static final int RED_WHITE_BLUE=3;

    private static final List<Object> COLOR_OPTIONS=Arrays.<Object>asList(
        MagicColor.White,
        MagicColor.Blue,
        MagicColor.Black,
        MagicColor.Green,
        MagicColor.Red
    );

    private static final List<Object> RED_WHITE_BLUE_OPTIONS=Arrays.<Object>asList(
        MagicColor.Red,
        MagicColor.White,
        MagicColor.Blue
    );

    public static final MagicColorChoice ALL_INSTANCE=new MagicColorChoice(ALL);
    public static final MagicColorChoice MOST_INSTANCE=new MagicColorChoice(MOST);
    public static final MagicColorChoice UNSUMMON_INSTANCE=new MagicColorChoice(UNSUMMON);
    public static final MagicColorChoice RED_WHITE_BLUE_INSTANCE=new MagicColorChoice(RED_WHITE_BLUE);

    private final int type;

    private MagicColorChoice(final int type) {
        super(_S1);
        this.type=type;
    }

    private static Collection<Object> getArtificialMostOptions(final MagicGame game,final MagicPlayer player) {
        final Collection<MagicPermanent> targets = MagicTargetFilterFactory.PERMANENT.filter(player);
        final int[] counts=new int[MagicColor.NR_COLORS];
        for (final MagicPermanent permanent : targets) {
            for (final MagicColor color : MagicColor.values()) {
                if (permanent.hasColor(color)) {
                    counts[color.ordinal()]++;
                }
            }
        }

        int bestCount=Integer.MIN_VALUE;
        MagicColor bestColor=null;
        for (final MagicColor color : MagicColor.values()) {

            final int count=counts[color.ordinal()];
            if (count>bestCount) {
                bestCount=count;
                bestColor=color;
            }
        }
        return Collections.<Object>singletonList(bestColor);
    }

    private static Collection<Object> getArtificialUnsummonOptions(final MagicGame game,final MagicPlayer player) {

        final Collection<MagicPermanent> targets = MagicTargetFilterFactory.CREATURE.filter(player);
        final int[] scores=new int[MagicColor.NR_COLORS];
        for (final MagicPermanent permanent : targets) {
            int score=permanent.getScore();
            if (permanent.getController()==player) {
                score=-score;
            }
            for (final MagicColor color : MagicColor.values()) {
                if (permanent.hasColor(color)) {
                    scores[color.ordinal()]+=score;
                }
            }
        }

        int bestScore=Integer.MIN_VALUE;
        MagicColor bestColor=null;
        for (final MagicColor color : MagicColor.values()) {

            final int score=scores[color.ordinal()];
            if (score>bestScore) {
                bestScore=score;
                bestColor=color;
            }
        }
        return Collections.<Object>singletonList(bestColor);
    }

    @Override
    Collection<Object> getArtificialOptions(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();

        switch (type) {
            case MOST: return getArtificialMostOptions(game,player);
            case UNSUMMON: return getArtificialUnsummonOptions(game,player);
            case RED_WHITE_BLUE: return RED_WHITE_BLUE_OPTIONS;
            default: return COLOR_OPTIONS;
        }
    }

    @Override
    public Object[] getPlayerChoiceResults(final IUIGameController controller, final MagicGame game, final MagicEvent event) throws UndoClickedException {
        final MagicSource source = event.getSource();

        controller.disableActionButton(false);
        return new Object[]{controller.getColorChoice(source)};
    }

}
