package magic.ui;

import magic.model.MagicPlayerZone;
import magic.ui.duel.viewerinfo.PlayerViewerInfo;

public interface IPlayerZoneListener {

    public void setActivePlayerZone(PlayerViewerInfo playerInfo, MagicPlayerZone zone);

}
