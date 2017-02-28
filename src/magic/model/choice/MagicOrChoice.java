package magic.model.choice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import magic.data.GeneralConfig;
import magic.exception.UndoClickedException;
import magic.model.IUIGameController;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;

public class MagicOrChoice extends MagicChoice {

    // translatable UI text (prefix with _S).
    private static final String _S1 = "Choose the mode.";

    private final MagicChoice[] choices;

    public MagicOrChoice(final String description,final MagicChoice... aChoices) {
        super(description);
        choices = aChoices;
    }

    public MagicOrChoice(final MagicChoice... choices) {
        this(_S1, choices);
    }

    @Override
    public MagicTargetChoice getTargetChoice(final Object[] chosen) {
        final int idx = (Integer)chosen[0] - 1;
        return idx >= 0 ? choices[idx].getTargetChoice() : MagicTargetChoice.NONE;
    }

    @Override
    public boolean hasOptions(final MagicGame game,final MagicPlayer player,final MagicSource source,final boolean hints) {
        for (final MagicChoice choice: choices) {
            if (choice.hasOptions(game, player, source, hints)) {
                return true;
            }
        }
        return false;
    }

    @Override
    Collection<Object> getArtificialOptions(final MagicGame game, final MagicEvent event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Object[]> getArtificialChoiceResults(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        final List<Object[]> choiceResultsList=new ArrayList<>();
        for (int i = 0; i < choices.length; i++) {
            if (choices[i].hasOptions(game,player,source,true)) {
                for (final Object obj : choices[i].getArtificialOptions(game,event)) {
                    choiceResultsList.add(new Object[] {
                        i + 1,
                        obj
                    });
                }
                if (choices[i].isValid() == false) {
                    choiceResultsList.add(new Object[] {
                        i + 1
                    });
                }
            }
        }

        if (choiceResultsList.isEmpty()) {
            choiceResultsList.add(new Object[]{0});
        }

        return choiceResultsList;
    }

    @Override
    public Object[] getPlayerChoiceResults(final IUIGameController controller, final MagicGame game, final MagicEvent event) throws UndoClickedException {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        final boolean hints = GeneralConfig.getInstance().getSmartTarget();
        final List<Integer> availableModes = new ArrayList<>();
        for (int i = 0; i < choices.length; i++) {
            if (choices[i].hasOptions(game,player,source,hints)) {
                availableModes.add(i + 1);
            }
        }

        if (availableModes.isEmpty()) {
            return new Object[]{0};
        }

        controller.disableActionButton(false);
        final int mode = controller.getModeChoice(source, availableModes);
        return choices[mode - 1].isValid() ?
            new Object[] {
                mode,
                choices[mode - 1].getPlayerChoiceResults(controller,game,event)[0]
            }:
            new Object[] {
                mode
            };
    }
}
