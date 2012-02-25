package magic.model.choice;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.ui.GameController;
import magic.ui.choice.MayChoicePanel;

import java.util.concurrent.Callable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MagicMulliganChoice extends MagicChoice {

	private static final List<Object[]> YES_CHOICE_LIST =
			Collections.singletonList(new Object[]{YES_CHOICE});
	private static final List<Object[]> NO_CHOICE_LIST =
			Collections.singletonList(new Object[]{NO_CHOICE});
	
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
		int minLands = (game.getTurnPlayer() == player) ? 3 : 2;
		final int maxLands = 4;
		int lands = 0;
		int high = 0;
		int nonLandManaSources = 0;

		if (player.getHandSize() <= 5) {
			return NO_CHOICE_LIST;
		}
		
		for (final MagicCard card : player.getHand()) {
			final MagicCardDefinition cardDefinition = card.getCardDefinition();
			if (cardDefinition.isLand()) {
				lands++;
			} else {
				if (!cardDefinition.getManaActivations().isEmpty()) {
					nonLandManaSources++;
				}
				if (cardDefinition.getConvertedCost()>4) {
					high++;
				}
			}
		}

		if (nonLandManaSources >= 1) {
			minLands--;
		}
		if (lands >= minLands && lands <= maxLands && high <= 2) {
			return NO_CHOICE_LIST;
		}
		
    	return YES_CHOICE_LIST;
	}
	
	@Override
	public Object[] getPlayerChoiceResults(
			final GameController controller,
			final MagicGame game,
			final MagicPlayer player,
			final MagicSource source) {
		if (player.getHandSize() <= 1) {
			return new Object[]{NO_CHOICE};
		}
		controller.disableActionButton(false);
		final MayChoicePanel choicePanel =
				controller.showComponent(new Callable<MayChoicePanel>() {
            public MayChoicePanel call() {
                return new MayChoicePanel(controller,source,"You may take a mulligan.");
            }
        });
		if (controller.waitForInputOrUndo()) {
			return UNDO_CHOICE_RESULTS;
		}
		if (choicePanel.isYesClicked()) {
			return new Object[]{YES_CHOICE};
		}
		return new Object[]{NO_CHOICE};
	}
}
