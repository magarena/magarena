package magic.model.choice;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCostManaType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicPlayCardAction;
import magic.model.event.MagicEvent;
import magic.model.phase.MagicMainPhase;
import magic.exception.UndoClickedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import magic.model.IUIGameController;

public class MagicMulliganChoice extends MagicChoice {

    private static final List<Object[]> YES_CHOICE_LIST =
            Collections.singletonList(new Object[]{YES_CHOICE});
    private static final List<Object[]> NO_CHOICE_LIST =
            Collections.singletonList(new Object[]{NO_CHOICE});
    private static final List<Object[]> ACTUAL_CHOICE_LIST =
            Collections.singletonList(new Object[]{NO_CHOICE,YES_CHOICE});

    public MagicMulliganChoice() {
        super("");
    }

    @Override
    Collection<Object> getArtificialOptions(
            final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Object[]> getArtificialChoiceResults(
            final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {

    	int costSum = 0;
    	List<MagicCard> deck = new ArrayList<>();
    	deck.addAll(player.getLibrary());
    	deck.addAll(player.getHand());
    	for(MagicCard card: deck){
    		costSum += card.getConvertedCost();
    	}
    	
    	// There is more fine tuning to be done here
		int minLands = 2;
		int maxLands = 3;
    	if(costSum > 90){
    		minLands = 3;
    		maxLands = 4;
    	}else if(costSum > 70){
    		minLands = 2;
    		maxLands = 4;
    	}
    	
        if (player.getHandSize() <= 4) {
            return NO_CHOICE_LIST;
        }

		final MagicGame assumedGame = new MagicGame(game, player);
		MagicPlayer assumedPlayer = assumedGame.getPlayer(player.getIndex());
		assumedGame.setPhase(MagicMainPhase.getFirstInstance());
		List<MagicCard> lands = new ArrayList<>();
        for (final MagicCard card : assumedPlayer.getHand()) {
            final MagicCardDefinition cardDefinition = card.getCardDefinition();
            if (cardDefinition.isLand()) {
            	assumedGame.doAction(new MagicPlayCardAction(card));
                lands.add(card);
            }
        }

        int playableCards = 0;
        for (final MagicCard card : assumedPlayer.getHand()) {
            final MagicCardDefinition cardDefinition = card.getCardDefinition();
            if (!cardDefinition.isLand() ) {
            	if(playableWith(card, assumedGame, assumedPlayer)){
            		playableCards++;
            	}
            }
        }

        if(player.getHandSize() > 6){
	        if(playableCards > 1){
	            return NO_CHOICE_LIST;
	        }
        }else{
	        if(playableCards > 0){
	            return NO_CHOICE_LIST;
	        }
        }

        if(lands.size() < minLands || lands.size() > maxLands){
            return YES_CHOICE_LIST;
        }else{
	        return ACTUAL_CHOICE_LIST;
        }
    }
    
    private boolean playableWith(MagicCard toPlay, MagicGame game, MagicPlayer player){
    	MagicManaCost mmc = toPlay.getCardDefinition().getCost();
    	List<MagicCostManaType> mcmts =  mmc.getCostManaTypes(0);

        final MagicBuilderManaCost builderCost=new MagicBuilderManaCost();
        builderCost.addTypes(mcmts);
        final MagicPayManaCostResultBuilder builder=new MagicPayManaCostResultBuilder(game,player,builderCost);
        return builder.hasResults();
    }

    @Override
    public Object[] getPlayerChoiceResults(
            final IUIGameController controller,
            final MagicGame game,
            final MagicPlayer player,
            final MagicSource source) throws UndoClickedException {

        if (player.getHandSize() <= 1) {
            return new Object[]{NO_CHOICE};
        }
        controller.disableActionButton(false);
        if (controller.getTakeMulliganChoice(source, player)) {
            return new Object[]{YES_CHOICE};
        }
        return new Object[]{NO_CHOICE};
    }

}
