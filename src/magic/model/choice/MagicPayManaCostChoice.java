package magic.model.choice;

import magic.model.MagicCostManaType;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicRandom;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.ui.GameController;
import magic.ui.choice.ManaCostXChoicePanel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

/** X must be at least one in a mana cost. */
public class MagicPayManaCostChoice extends MagicChoice {
    
    private final MagicManaCost cost;
    
    public MagicPayManaCostChoice(final MagicManaCost cost) {
        super("Choose how to pay the mana cost.");
        this.cost=cost;
    }
    
    private MagicManaCost getCost() {
        return cost;
    }
    
    @Override
    public int getManaChoiceResultIndex() {
        return 0;
    }

    @Override
    boolean hasOptions(final MagicGame game,final MagicPlayer player,final MagicSource source,final boolean hints) {
        final Collection<Object> options = genOptions(game, player);
        return !options.isEmpty();
    }
            
    private Collection<Object> genOptions(final MagicGame game, final MagicPlayer player) {
        return game.getFastChoices() ?
            buildDelayedPayManaCostResults(game,player) :
            new MagicPayManaCostResultBuilder(game,player,cost.getBuilderCost()).getResults();
    }
    
    private Collection<Object> buildDelayedPayManaCostResults(final MagicGame game,final MagicPlayer player) {
        if (cost.hasX()) {
            final int maxX=player.getMaximumX(game,cost);
            if (maxX==1) {
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
    Collection<Object> getArtificialOptions(
            final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {
        
        final Collection<Object> options = genOptions(game, player); 

        assert !options.isEmpty() : 
            "No options to pay mana cost\n" + 
            "fastChoices = " + game.getFastChoices() + "\n" +
            "source = " + source + "\n" +
            "player = " + player + "\n" +
            "event = " + event + "\n"; 

        return options;
    }
    
    @Override
    public Object[] getSimulationChoiceResult(
            final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {
        //in simulation use delayed pay mana cost
        final List<Object> choices = (List<Object>)buildDelayedPayManaCostResults(game,player);
        return new Object[]{choices.get(MagicRandom.nextInt(choices.size()))};
    }
    
    @Override
    public Object[] getPlayerChoiceResults(final GameController controller,final MagicGame game,final MagicPlayer player,final MagicSource source) {
        controller.disableActionButton(false);
        
        final int x;
        if (cost.hasX()) {
            final int maximumX=player.getMaximumX(game,cost);
            final ManaCostXChoicePanel choicePanel = controller.showComponent(new Callable<ManaCostXChoicePanel>() {
                public ManaCostXChoicePanel call() {
                    return new ManaCostXChoicePanel(controller,source,maximumX);
                }
            });
            if (controller.waitForInputOrUndo()) {
                return UNDO_CHOICE_RESULTS;
            }
            x=choicePanel.getValueForX();
        } else {
            x=0;
        }

        final List<MagicCostManaType> costManaTypes=cost.getCostManaTypes(x * cost.getXCount());
        final MagicBuilderManaCost builderCost=new MagicBuilderManaCost();
        builderCost.addTypes(costManaTypes);
        final MagicPayManaCostResultBuilder builder=new MagicPayManaCostResultBuilder(game,player,builderCost);
        final boolean canSkip = MagicGame.canSkipSingleManaChoice();
        
        for (final MagicCostManaType costManaType : costManaTypes) {
            if (canSkip&&builder.useAllManaSources(costManaType)) {
                controller.update();
                break;
            }
            
            final int costMinAmount = builderCost.getMinimumAmount();
            final Set<Object> validSources=builder.getManaSources(costManaType,!canSkip);

            MagicPermanent sourcePermanent = MagicPermanent.NONE;
            if (validSources.size() == 1) {
                // only one valid choice
                sourcePermanent = (MagicPermanent)validSources.iterator().next();
            } 
            if (builder.getActivationsSize() == costMinAmount) {
                // more than one valid choice but number of activations = cost
                // find permanent with only one mana activation
                for (final Object obj : validSources) {
                    final MagicPermanent perm = (MagicPermanent)obj;
                    if (perm.countManaActivations() == 1) {
                        sourcePermanent = perm;
                        break;
                    }
                }
            }

            if (canSkip && sourcePermanent.isValid()) {
                // skip choice
            } else {
                controller.setValidChoices(validSources,false);
                controller.showMessage(source,"Choose a mana source to pay "+costManaType.getText()+".");
                if (controller.waitForInputOrUndo()) {
                    return UNDO_CHOICE_RESULTS;
                }
                controller.clearValidChoices();
                sourcePermanent=(MagicPermanent)controller.getChoiceClicked();                
            }

            builder.useManaSource(sourcePermanent,costManaType);
            controller.update();
        }
        return new Object[]{new MagicPlayerPayManaCostResult(x,costManaTypes.size())};
    }
}
