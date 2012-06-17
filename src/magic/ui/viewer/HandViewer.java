package magic.ui.viewer;

import magic.model.MagicCardList;
import magic.ui.GameController;

public class HandViewer extends CardListViewer {

    private static final long serialVersionUID = 1L;

    private final ViewerInfo viewerInfo;
    
    public HandViewer(final ViewerInfo viewerinfo,final GameController controller) {
        
        super(controller,false);
        this.viewerInfo=viewerinfo;
        update();
    }
    
    @Override
    public String getTitle() {

        return "Hand : "+viewerInfo.getPlayerInfo(false).name;
    }

    @Override
    protected MagicCardList getCardList() {

        return viewerInfo.getPlayerInfo(false).hand;
    }
}