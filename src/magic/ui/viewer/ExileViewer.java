package magic.ui.viewer;

import magic.model.MagicCardList;
import magic.ui.GameController;

public class ExileViewer extends CardListViewer {
    
    private static final long serialVersionUID = 1L;

    private final ViewerInfo viewerInfo;
    private final boolean opponent;
    
    public ExileViewer(final ViewerInfo viewerInfo,final GameController controller,final boolean opponent) {
        
        super(controller,true);
        this.viewerInfo=viewerInfo;
        this.opponent=opponent;
        update();
    }
    
    @Override
    public String getTitle() {

        return "Exile : "+viewerInfo.getPlayerInfo(opponent).name;
    }

    @Override
    protected MagicCardList getCardList() {

        return viewerInfo.getPlayerInfo(opponent).exile;
    }
}