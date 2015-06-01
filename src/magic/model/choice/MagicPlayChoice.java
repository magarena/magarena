package magic.model.choice;

import magic.data.GeneralConfig;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicActivation;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceActivation;
import magic.model.phase.MagicPhaseType;
import magic.exception.UndoClickedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import magic.model.IUIGameController;

public class MagicPlayChoice extends MagicChoice {

    private static final MagicChoice INSTANCE=new MagicPlayChoice();

    private static final String CONTINUE_MESSAGE="Click {f} or Space to pass.";
    private static final String MESSAGE="Play a card or ability.|" + CONTINUE_MESSAGE + "|[Right click {f} or Shift+Space to|skip till end of turn.]";

    private static final Collection<Object> PASS_OPTIONS=Collections.<Object>singleton(MagicPlayChoiceResult.SKIP);
    private static final Object[] PASS_CHOICE_RESULTS= {MagicPlayChoiceResult.SKIP};

    private MagicPlayChoice() {
        super("Play a card or ability.");
    }

    @Override
    Collection<Object> getArtificialOptions(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        // When something is already on top of stack for the player, always pass.
        if (game.getStack().hasItemOnTopOfPlayer(player)) {
            return PASS_OPTIONS;
        }

        //can't play spells or abilities during combat damage phase
        if (game.isPhase(MagicPhaseType.CombatDamage)) {
            return PASS_OPTIONS;
        }

        final ArrayList<Object> options=new ArrayList<>();

        // Pass is first choice when scores are equal.
        options.add(MagicPlayChoiceResult.PASS);

        // add rest of the options
        addValidChoices(game, player, true, options);

        // only one option, return SKIP instead of PASS
        return options.size() > 1 ? options : PASS_OPTIONS;
    }

    private static void addValidChoices(final MagicGame game, final MagicPlayer player, final boolean isAI, final Collection<Object> validChoices) {
        final Set<MagicSourceActivation<? extends MagicSource>> sourceActivations = player.getSourceActivations();
        MagicActivation<? extends MagicSource> skip = null;
        for (final MagicSourceActivation<? extends MagicSource> sourceActivation : sourceActivations) {
            if (sourceActivation.activation != skip && sourceActivation.canPlay(game, player, isAI)) {
                validChoices.add(isAI ?
                    new MagicPlayChoiceResult(sourceActivation) :
                    sourceActivation.source
                );
                if (isAI && sourceActivation.isIndependent()) {
                    skip = sourceActivation.activation;
                }
            }
        }
    }

    @Override
    public Object[] getPlayerChoiceResults(final IUIGameController controller, final MagicGame game, final MagicEvent event) throws UndoClickedException {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();
        
        controller.focusViewers(0);

        //always pass draw and begin combat if
        //  option is true and 
        //  stack is empty
        if (game.canAlwaysPass() && game.getStack().isEmpty()) {
            return PASS_CHOICE_RESULTS;
        }

        //skip all combat phases if
        //  nobody is attacking and
        //  stack is empty
        if ((game.isPhase(MagicPhaseType.DeclareAttackers) ||
             game.isPhase(MagicPhaseType.DeclareBlockers) ||
             game.isPhase(MagicPhaseType.CombatDamage) ||
             game.isPhase(MagicPhaseType.EndOfCombat)) &&
            player.getNrOfAttackers() == 0 &&
            player.getOpponent().getNrOfAttackers() == 0 &&
            game.getStack().isEmpty()) {
            return PASS_CHOICE_RESULTS;
        }

        //skip if phase is combat damage, not supposed to be able to do
        //anything but resolve triggers
        if (game.isPhase(MagicPhaseType.CombatDamage)) {
            if (!game.getStack().isEmpty()) {
                controller.pause(GeneralConfig.getInstance().getMessageDelay());
            }
            return PASS_CHOICE_RESULTS;
        }

        final Set<Object> validChoices = new HashSet<>();
        addValidChoices(game, player, false, validChoices);

        if (validChoices.isEmpty() && MagicGame.canSkipSingleChoice()) {
            boolean skip = true;

            //if AI blocks, don't skip priority so that user can observe how the AI is blocking
            if (game.isPhase(MagicPhaseType.DeclareBlockers) &&
                player.getOpponent().getNrOfBlockers() > 0 &&
                game.getStack().isEmpty()) {
                skip = false;
            }

            if (skip) {
                //pause if there is an item on the stack
                if (!game.getStack().isEmpty()) {
                    controller.pause(GeneralConfig.getInstance().getMessageDelay());
                }
                return PASS_CHOICE_RESULTS;
            }
        }

        if (game.shouldSkip()) {
            if (game.getStack().isEmpty() == false) {
                game.clearSkipTurnTill();
            } else if (game.isPhase(MagicPhaseType.DeclareAttackers) && player.getOpponent().getNrOfAttackers() > 0) {
                game.clearSkipTurnTill();
            } else {
                return PASS_CHOICE_RESULTS;
            }
        }

        if (validChoices.isEmpty()) {
            controller.showMessage(source,CONTINUE_MESSAGE);
        } else {
            controller.showMessage(source,MESSAGE);
            controller.setValidChoices(validChoices,false);
        }
        controller.enableForwardButton();
        controller.waitForInput();

        controller.clearValidChoices();
        controller.disableActionButton(false);
        game.snapshot();

        if (controller.isActionClicked()) {
            return PASS_CHOICE_RESULTS;
        }

        final MagicSource activationSource = controller.getChoiceClicked(); 
        final List<MagicPlayChoiceResult> results=new ArrayList<>();
        for (final MagicSourceActivation<? extends MagicSource> sourceActivation : activationSource.getSourceActivations()) {
            if (sourceActivation.canPlay(game,player,false)) {
                results.add(new MagicPlayChoiceResult(sourceActivation));
            }
        }

        assert results.size() > 0 : "ERROR! There should be at least one activation possible.";

        if (results.size() == 1) {
            return new Object[]{results.get(0)};
        } else {
            controller.setSourceCardDefinition(activationSource);
            return new Object[]{controller.getPlayChoice(activationSource, results)};
        }
    }

    public static MagicChoice getInstance() {
        return INSTANCE;
    }

}
