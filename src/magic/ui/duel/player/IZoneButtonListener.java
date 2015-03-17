package magic.ui.duel.player;

import magic.model.MagicPlayerZone;
import magic.ui.duel.viewer.PlayerViewerInfo;

public interface IZoneButtonListener {
    void playerZoneSelected(PlayerViewerInfo playerInfo, MagicPlayerZone zone);
    void playerZoneSelectedClicked(PlayerViewerInfo playerInfo, MagicPlayerZone zone);
}
