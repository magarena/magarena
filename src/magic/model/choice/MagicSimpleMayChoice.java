package magic.model.choice;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.ui.GameController;
import magic.ui.choice.MayChoicePanel;

public class MagicSimpleMayChoice extends MagicChoice {

	public static final int DRAW_CARDS=1;

	private static final List<Object[]> YES_CHOICE_LIST=Collections.singletonList(new Object[]{YES_CHOICE});
	private static final List<Object[]> NO_CHOICE_LIST=Collections.singletonList(new Object[]{NO_CHOICE});
	
	private final int action;
	private final int amount;
	
	public MagicSimpleMayChoice(final String description,final int action,final int amount) {
		
		super(description);
		this.action=action;
		this.amount=amount;
	}
	
	@Override
	public Collection<Object> getArtificialOptions(final MagicGame game,final MagicEvent event,final MagicPlayer player,final MagicSource source) {

		throw new UnsupportedOperationException();
	}

	@Override
	public List<Object[]> getArtificialChoiceResults(final MagicGame game,final MagicEvent event,final MagicPlayer player,final MagicSource source) {
		
		boolean yes=true;
		switch (action) {
			case DRAW_CARDS:
				yes=player.getLibrary().size()-amount>=1;
				break;
		}
		return yes?YES_CHOICE_LIST:NO_CHOICE_LIST;
	}
	
	@Override
	public Object[] getPlayerChoiceResults(final GameController controller,final MagicGame game,final MagicPlayer player,final MagicSource source) {

		final MayChoicePanel choicePanel=new MayChoicePanel(controller,source,getDescription());
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