package magic.ui.viewer;

import magic.model.MagicCardList;
import magic.ui.GameController;

public class GraveyardViewer extends CardListViewer {

	private static final long serialVersionUID = 1L;

	private final ViewerInfo viewerInfo;
	private final boolean opponent;
	
	public GraveyardViewer(final ViewerInfo viewerInfo,final GameController controller,final boolean opponent) {
		
		super(controller,true);
		this.viewerInfo=viewerInfo;
		this.opponent=opponent;
		update();
	}
	
	public String getTitle() {

		return "Graveyard : "+viewerInfo.getPlayerInfo(opponent).name;
	}

	@Override
	protected MagicCardList getCardList() {

		return viewerInfo.getPlayerInfo(opponent).graveyard;
	}
}