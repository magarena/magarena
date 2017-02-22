package magic.model.choice;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import magic.exception.UndoClickedException;
import magic.model.IUIGameController;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilterFactory;

/** Contains optimal decision logic for each choice type. */
public class MagicBasicLandChoice extends MagicChoice {

    // translatable UI text (prefix with _S).
    private static final String _S1 = "Choose yes or no.";

    private static final int ALL=0;
    private static final int MOST=1;
    private static final int UNSUMMON=2;

    private static final List<Object> LAND_OPTIONS=Arrays.<Object>asList(
        MagicSubType.Plains,
        MagicSubType.Island,
        MagicSubType.Swamp,
        MagicSubType.Forest,
        MagicSubType.Mountain
    );

    public static final MagicBasicLandChoice ALL_INSTANCE=new MagicBasicLandChoice(ALL);
    public static final MagicBasicLandChoice MOST_INSTANCE=new MagicBasicLandChoice(MOST);
    public static final MagicBasicLandChoice UNSUMMON_INSTANCE=new MagicBasicLandChoice(UNSUMMON);

    private final int type;

    private MagicBasicLandChoice(final int type) {
        super(_S1);
        this.type=type;
    }

    private static Collection<Object> getArtificialMostOptions(final MagicGame game,final MagicPlayer player) {
        final Collection<MagicPermanent> targets = MagicTargetFilterFactory.PERMANENT.filter(player);
        final int[] counts=new int[MagicSubType.ALL_BASIC_LANDS.size()];
        for (final MagicPermanent permanent : targets) {
            for (final MagicSubType subType : MagicSubType.ALL_BASIC_LANDS) {
                if (permanent.hasSubType(subType)) {
                    counts[subType.ordinal()]++;
                }
            }
        }

        int bestCount=Integer.MIN_VALUE;
        MagicSubType bestSubType=null;
        for (final MagicSubType subType : MagicSubType.ALL_BASIC_LANDS) {

            final int count=counts[subType.ordinal()];
            if (count>bestCount) {
                bestCount=count;
                bestSubType=subType;
            }
        }
        return Collections.<Object>singletonList(bestSubType);
    }

    private static Collection<Object> getArtificialUnsummonOptions(final MagicGame game,final MagicPlayer player) {

        final Collection<MagicPermanent> targets = MagicTargetFilterFactory.CREATURE.filter(player);
        final int[] scores=new int[MagicSubType.ALL_BASIC_LANDS.size()];
        for (final MagicPermanent permanent : targets) {
            int score=permanent.getScore();
            if (permanent.getController()==player) {
                score=-score;
            }
            for (final MagicSubType subType : MagicSubType.ALL_BASIC_LANDS) {
                if (permanent.hasSubType(subType)) {
                    scores[subType.ordinal()]+=score;
                }
            }
        }

        int bestScore=Integer.MIN_VALUE;
        MagicSubType bestSubType=null;
        for (final MagicSubType subType : MagicSubType.ALL_BASIC_LANDS) {

            final int score=scores[subType.ordinal()];
            if (score>bestScore) {
                bestScore=score;
                bestSubType=subType;
            }
        }
        return Collections.<Object>singletonList(bestSubType);
    }

    @Override
    Collection<Object> getArtificialOptions(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        switch (type) {
            case MOST: return getArtificialMostOptions(game,player);
            case UNSUMMON: return getArtificialUnsummonOptions(game,player);
            default: return LAND_OPTIONS;
        }
    }

    @Override
    public Object[] getPlayerChoiceResults(final IUIGameController controller, final MagicGame game, final MagicEvent event) throws UndoClickedException {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();
        controller.disableActionButton(false);
        return new Object[]{controller.getLandSubTypeChoice(source)};
    }

}
