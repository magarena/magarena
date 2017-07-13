package magic.model.choice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import magic.exception.GameException;
import magic.exception.UndoClickedException;
import magic.model.IUIGameController;
import magic.model.MagicCostManaType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicRandom;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.translate.MText;

/** X must be at least one in a mana cost. */
public class MagicPayManaCostChoice extends MagicChoice {

    private static final String _S_CHOOSE = "Choose a mana ability to pay %s."
    private static final String _S_NO_OPTIONS = "There is not enough mana to pay %s. Please undo.";

    private final MagicManaCost cost;

    public MagicPayManaCostChoice(final MagicManaCost cost) {
        super("Choose how to pay the mana cost.");
        this.cost=cost;
    }

    @Override
    public int getManaChoiceResultIndex() {
        return 0;
    }

    @Override
    public boolean hasOptions(final MagicGame game,final MagicPlayer player,final MagicSource source,final boolean hints) {
        final MagicBuilderManaCost builderCost=new MagicBuilderManaCost(player.getBuilderCost());
        cost.addTo(builderCost);
        return new MagicPayManaCostResultBuilder(game,player,builderCost).hasResults();
    }

    private Collection<Object> genOptions(final MagicGame game, final MagicPlayer player) {
        return game.getFastMana() ?
            buildDelayedPayManaCostResults(game,player) :
            new MagicPayManaCostResultBuilder(game,player,cost.getBuilderCost()).getResults();
    }

    private Collection<Object> buildDelayedPayManaCostResults(final MagicGame game,final MagicPlayer player) {
        if (cost.hasX()) {
            final int maxX=player.getMaximumX(game,cost);
            if (maxX <= 0) {
                throw new GameException("Unable to pay for {X} in " + cost + " as maxX = " + maxX, game);
            } else if (maxX == 1) {
                return Collections.<Object>singletonList(new MagicDelayedPayManaCostResult(cost,1));
            } else {
                final List<Object> choices=new ArrayList<Object>();
                for (int x=1;x<=maxX;x++) {
                    choices.add(new MagicDelayedPayManaCostResult(cost,x));
                }
                return choices;
            }
        } else {
            return Collections.<Object>singletonList(new MagicDelayedPayManaCostResult(cost,0));
        }
    }

    @Override
    Collection<Object> getArtificialOptions(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        final Collection<Object> options = genOptions(game, player);

        /*
        assert !options.isEmpty() :
            "No options to pay mana cost\n" +
            "fastMana = " + game.getFastMana() + "\n" +
            "source = " + source + "\n" +
            "player = " + player + "\n" +
            "event = " + event + "\n";
        */
        return options;
    }

    @Override
    public Object[] getSimulationChoiceResult(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        //in simulation use delayed pay mana cost
        final List<Object> choices = (List<Object>)buildDelayedPayManaCostResults(game,player);
        return new Object[]{choices.get(MagicRandom.nextRNGInt(choices.size()))};
    }

    @Override
    public Object[] getPlayerChoiceResults(final IUIGameController controller, final MagicGame game, final MagicEvent event) throws UndoClickedException {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        controller.disableActionButton(false);

        if (event.isSatisfied() == false) {
            controller.showMessage(source, MText.get(_S_NO_OPTIONS, cost.getText()));
            controller.waitForInput();
            return MagicEvent.NO_CHOICE_RESULTS;
        }

        final int x;
        if (cost.hasX()) {
            final int maximumX = player.getMaximumX(game, cost);
            x = controller.getPayManaCostXChoice(source, maximumX);
        } else {
            x = 0;
        }

        final List<MagicCostManaType> costManaTypes=cost.getCostManaTypes(x);
        final MagicBuilderManaCost builderCost=new MagicBuilderManaCost();
        builderCost.addTypes(costManaTypes);
        final MagicPayManaCostResultBuilder builder=new MagicPayManaCostResultBuilder(game,player,builderCost);
        final boolean canSkip = MagicGame.canSkipSingleManaChoice();
        final int costMinAmount = builderCost.getMinimumAmount();

        // if number of mana permanents equal to min amount &&
        //    number of mana abilities  equal to min amount
        // then the only way to pay is to use each mana ability
        boolean useAll = false;
        if (builder.getActivationsSize() == costMinAmount) {
            int totalManaActivations = 0;
            for (final MagicPermanent perm : player.getPermanents()) {
                totalManaActivations += perm.countManaActivations();
            }
            if (totalManaActivations == costMinAmount) {
                useAll = true;
            }
        }

        for (final MagicCostManaType costManaType : costManaTypes) {
            if (canSkip&&builder.useAllManaSources(costManaType)) {
                controller.updateGameView();
                break;
            }

            final Set<MagicPermanent> validSources=builder.getManaSources(costManaType,!canSkip);
            MagicPermanent sourcePermanent = MagicPermanent.NONE;

            if (canSkip && (validSources.size() == 1 || useAll)) {
                // only one valid choice or must use all
                sourcePermanent = validSources.iterator().next();
            } else {
                controller.setValidChoices(validSources,false);
                controller.showMessage(source, MText.get(_S_CHOOSE, costManaType.getText()));
                controller.waitForInput();
                controller.clearValidChoices();
                sourcePermanent = controller.getChoiceClicked();
            }

            builder.useManaSource(sourcePermanent,costManaType);
            controller.updateGameView();
        }
        return new Object[]{new MagicPlayerPayManaCostResult(x,costManaTypes.size())};
    }

}
