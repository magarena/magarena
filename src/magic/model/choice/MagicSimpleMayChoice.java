package magic.model.choice;

import magic.data.GeneralConfig;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.ui.GameController;
import magic.ui.choice.MayChoicePanel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MagicSimpleMayChoice extends MagicChoice {

	public static final int DRAW_CARDS = 1;
	public static final int GAIN_LIFE = 2; // always returns YES_CHOICE_LIST
	public static final int LOSE_LIFE = 3;
	public static final int OPPONENT_LOSE_LIFE = 4; // always returns YES_CHOICE_LIST
	
	public static final int DEFAULT_NONE = 0;
	public static final int DEFAULT_NO = 1;
	public static final int DEFAULT_YES = 2;

	private static final List<Object[]> YES_CHOICE_LIST = Collections.singletonList(new Object[]{YES_CHOICE});
	private static final List<Object[]> NO_CHOICE_LIST = Collections.singletonList(new Object[]{NO_CHOICE});
	
	private final int action;
	private final int amount;
	private int defaultChoice;
	
	public MagicSimpleMayChoice(final String description,final int action,final int amount,final int defaultChoice) {
		super(description);
		this.action = action;
		this.amount = amount;
		this.defaultChoice = defaultChoice;
	}
	
	@Override
	Collection<Object> getArtificialOptions(final MagicGame game,final MagicEvent event,final MagicPlayer player,final MagicSource source) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Object[]> getArtificialChoiceResults(final MagicGame game,final MagicEvent event,final MagicPlayer player,final MagicSource source) {
		boolean yes = true;
		switch (action) {
			case DRAW_CARDS:
				yes = player.getLibrary().size() - amount >= 1;
				break;
			case LOSE_LIFE:
				yes = player.getLife() - amount >= 1;
				break;
		}
		return yes ? YES_CHOICE_LIST : NO_CHOICE_LIST;
	}
	
	@Override
	public Object[] getPlayerChoiceResults(final GameController controller,final MagicGame game,final MagicPlayer player,final MagicSource source) {
		final boolean hints = GeneralConfig.getInstance().getSmartTarget();
		if (hints && defaultChoice != DEFAULT_NONE) {
			return (defaultChoice == DEFAULT_NO) ?
					new Object[]{NO_CHOICE} :
					new Object[]{YES_CHOICE};
		}
		
		final MayChoicePanel choicePanel = new MayChoicePanel(controller,source,getDescription());
		controller.disableActionButton(false);
		controller.showComponent(choicePanel);
		if (controller.waitForInputOrUndo()) {
			return UNDO_CHOICE_RESULTS;
		}
		if (choicePanel.isYesClicked()) {
			return new Object[]{YES_CHOICE};
		}
		return new Object[]{NO_CHOICE};
	}
}