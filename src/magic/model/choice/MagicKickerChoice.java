package magic.model.choice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import magic.exception.UndoClickedException;
import magic.model.IUIGameController;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;

// Kicker choice results : 0 = other choice, 1 = number of times kicked, 2 = kicker mana cost result
public class MagicKickerChoice extends MagicChoice {

    private static final List<Object> NO_OPTIONS_LIST = Collections.singletonList(null);

    private final MagicChoice otherChoice;
    private final MagicManaCost cost;
    private final String name;

    public static MagicKickerChoice Replicate(final MagicChoice otherChoice, final MagicManaCost cost) {
        return new MagicKickerChoice(otherChoice, cost, "replicate");
    }

    public static MagicKickerChoice Replicate(final MagicManaCost cost) {
        return new MagicKickerChoice(cost, "replicate");
    }

    public MagicKickerChoice(final MagicChoice otherChoice, final MagicManaCost cost,final String name) {
        super("Choose how many times to pay the " + name + " cost.");
        this.otherChoice=otherChoice;
        this.cost=cost;
        this.name = name;
    }

    public MagicKickerChoice(final MagicChoice otherChoice,final MagicManaCost cost) {
        this(otherChoice, cost, "kicker");
    }

    public MagicKickerChoice(final MagicManaCost cost, final String name) {
        this(MagicChoice.NONE, cost, name);
    }

    public MagicKickerChoice(final MagicManaCost cost) {
        this(MagicChoice.NONE, cost, "kicker");
    }

    @Override
    public MagicTargetChoice getTargetChoice() {
        return (otherChoice instanceof MagicTargetChoice) ? (MagicTargetChoice)otherChoice : MagicTargetChoice.NONE;
    }

    @Override
    public int getTargetChoiceResultIndex() {
        return (otherChoice instanceof MagicTargetChoice) ? 0 : -1;
    }

    @Override
    public int getManaChoiceResultIndex() {
        return 2;
    }

    @Override
    Collection<Object> getArtificialOptions(final MagicGame game, final MagicEvent event) {
        throw new UnsupportedOperationException();
    }

    private int getMaximumCount(final MagicGame game,final MagicPlayer player) {
        final MagicBuilderManaCost builderCost=new MagicBuilderManaCost(player.getBuilderCost());
        for (int index=1;;index++) {
            cost.addTo(builderCost);
            if (!new MagicPayManaCostResultBuilder(game,player,builderCost).hasResults()) {
                return index-1;
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
            return MagicManaCost.create(costText.toString());
        }
    }

    @Override
    public List<Object[]> getArtificialChoiceResults(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();

        final Collection<?> otherOptions;
        if (otherChoice.isValid()) {
            otherOptions=otherChoice.getArtificialOptions(game,event);
        } else {
            otherOptions=NO_OPTIONS_LIST;
        }

        final List<Object[]> choiceResultsList=new ArrayList<>();
        final int maximumCount=getMaximumCount(game,player);
        for (int count=0;count<=maximumCount;count++) {
            final Object[] choiceResults=new Object[3];
            choiceResults[1]=count;

            final Collection<Object> manaOptions;
            if (count==0) {
                manaOptions=NO_OPTIONS_LIST;
            } else {
                final MagicPayManaCostChoice manaChoice=new MagicPayManaCostChoice(getCost(count));
                manaOptions=manaChoice.getArtificialOptions(game,event);
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
    public Object[] getPlayerChoiceResults(final IUIGameController controller, final MagicGame game, final MagicEvent event) throws UndoClickedException {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        final int maximumCount=getMaximumCount(game,player);
        final int count;
        if (maximumCount>1) {
            count = controller.getMultiKickerCountChoice(source, cost, maximumCount, name);
        } else if (maximumCount==1) {
            count = controller.getSingleKickerCountChoice(source, cost, name);
        } else {
            count=0;
        }

        final Object[] choiceResults=new Object[3];
        choiceResults[1]=count;
        // Pay kicker.
        if (count>0) {
            final MagicPayManaCostChoice manaChoice=new MagicPayManaCostChoice(getCost(count));
            final Object[] manaChoiceResults=manaChoice.getPlayerChoiceResults(controller,game,event);
            choiceResults[2]=manaChoiceResults[0];
        }

        // Pick other choice.
        if (otherChoice.isValid()) {
            final Object[] otherChoiceResults=otherChoice.getPlayerChoiceResults(controller,game,event);
            choiceResults[0]=otherChoiceResults[0];
        } else {
            choiceResults[0]=null;
        }
        return choiceResults;
    }

}
