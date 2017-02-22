package magic.model.choice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import magic.data.GeneralConfig;
import magic.exception.UndoClickedException;
import magic.model.IUIGameController;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.model.event.MagicMatchedCostEvent;

public class MagicMayChoice extends MagicChoice {

    // translatable UI text (prefix with _S).
    private static final String _S1 = "Proceed with \"may\" action?";

    private final MagicChoice[] choices;
    private final MagicTargetChoice targetChoice;
    private final int manaChoiceResultIndex;
    private final int targetChoiceResultIndex;

    private static final MagicChoice satisfied(final MagicMatchedCostEvent cost) {
        return new MagicChoice("satisfied") {
            @Override
            public Collection<Object> getArtificialOptions(final MagicGame game, final MagicEvent event) {
                return Collections.singletonList(null);
            }
            @Override
            public Object[] getPlayerChoiceResults(final IUIGameController controller, final MagicGame game, final MagicEvent event) {
                return new Object[1];
            }
            @Override
            public boolean hasOptions(final MagicGame game,final MagicPlayer player,final MagicSource source,final boolean hints) {
                return cost.getEvent(source).isSatisfied();
            }
        };
    }

    public MagicMayChoice(final String description,final MagicMatchedCostEvent cost) {
        this(description, satisfied(cost));
    }

    public MagicMayChoice(final String description,final MagicChoice... aChoices) {
        super(description);

        int validChoices = 0;
        for (final MagicChoice choice : aChoices) {
            if (choice.isValid()) {
                validChoices++;
            }
        }

        choices = new MagicChoice[validChoices];
        int idx = 0;
        for (final MagicChoice choice : aChoices) {
            if (choice.isValid()) {
                choices[idx] = choice;
                idx++;
            }
        }

        MagicTargetChoice localTargetChoice = MagicTargetChoice.NONE;
        int localManaChoiceResultIndex = -1;
        int localTargetChoiceResultIndex = -1;
        for (int index=0;index<choices.length;index++) {
            final MagicChoice choice=choices[index];
            if (choice instanceof MagicTargetChoice) {
                localTargetChoice=(MagicTargetChoice)choice;
                localTargetChoiceResultIndex=index+1;
            } else if (choice instanceof MagicPayManaCostChoice) {
                localManaChoiceResultIndex=index+1;
            }
        }

        targetChoice = localTargetChoice;
        targetChoiceResultIndex = localTargetChoiceResultIndex;
        manaChoiceResultIndex = localManaChoiceResultIndex;
    }

    public MagicMayChoice(final MagicChoice... choices) {
        this(_S1, choices);
    }

    private MagicChoice[] getChoices() {
        return choices;
    }

    @Override
    public MagicTargetChoice getTargetChoice() {
        return targetChoice;
    }

    @Override
    public int getTargetChoiceResultIndex() {
        return targetChoiceResultIndex;
    }

    @Override
    public int getManaChoiceResultIndex() {
        return manaChoiceResultIndex;
    }

    @Override
    Collection<Object> getArtificialOptions(final MagicGame game, final MagicEvent event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Object[]> getArtificialChoiceResults(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        final int nrOfChoices = choices.length;
        if (nrOfChoices == 0) {
            return NO_OTHER_CHOICE_RESULTS;
        }

        final int nrOfChoiceResults=nrOfChoices+1;
        final Object[] noChoiceResults=new Object[nrOfChoiceResults];
        noChoiceResults[0]=NO_CHOICE;

        final List<Collection<?>> optionsList=new ArrayList<Collection<?>>(nrOfChoices);
        for (int index=0;index<nrOfChoices;index++) {
            if (!choices[index].hasOptions(game,player,source,true)) {
                return Collections.singletonList(noChoiceResults);
            }
            optionsList.add(choices[index].getArtificialOptions(game,event));
        }

        final List<Object[]> choiceResultsList=new ArrayList<Object[]>();
        final Object[] yesChoiceResults=new Object[nrOfChoiceResults];
        yesChoiceResults[0]=YES_CHOICE;

        int index=0;
        final LinkedList<Iterator<?>> iterators=new LinkedList<Iterator<?>>();
        iterators.addLast(optionsList.get(0).iterator());
        while (index>=0) {
            final Iterator<?> iterator=iterators.getLast();
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
    public Object[] getPlayerChoiceResults(final IUIGameController controller, final MagicGame game, final MagicEvent event) throws UndoClickedException {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        final Object[] choiceResults=new Object[choices.length+1];
        choiceResults[0]=NO_CHOICE;

        final boolean hints = GeneralConfig.getInstance().getSmartTarget();
        for (final MagicChoice choice : choices) {
            if (!choice.hasOptions(game,player,source,hints)) {
                return choiceResults;
            }
        }

        controller.disableActionButton(false);
        final boolean chosen = controller.getMayChoice(source, getDescription());
        if (chosen) {
            // Yes is chosen.
            choiceResults[0]=YES_CHOICE;
            for (int index=0;index<choices.length;index++) {
                final Object[] partialChoiceResults=choices[index].getPlayerChoiceResults(controller,game,event);
                choiceResults[index+1]=partialChoiceResults[0];
            }
        }

        game.snapshot();

        return choiceResults;
    }

}
