package magic.ui.widget.duel.player;

import magic.ui.duel.viewerinfo.PlayerViewerInfo;
import magic.ui.screen.duel.game.SwingGameController;
import magic.ui.widget.ChoiceBorderPanelButton;

@SuppressWarnings("serial")
class PlayerAvatarButton extends ChoiceBorderPanelButton {

    private final PlayerImagePanel avatarPanel;
    private final PlayerViewerInfo playerInfo;
    private final SwingGameController controller;

    PlayerAvatarButton(final PlayerViewerInfo playerInfo, final SwingGameController controller) {
        this.playerInfo = playerInfo;
        this.controller = controller;
        avatarPanel = new PlayerImagePanel(playerInfo);
        avatarPanel.setOpaque(false);
        setComponent(avatarPanel);
    }

    void showAsValidChoice(boolean isValid) {
        setIsValidChoice(isValid);
    }

    void updateDisplay(PlayerViewerInfo playerInfo) {
        avatarPanel.updateDisplay(playerInfo);
    }

    @Override
    public void onMouseClicked() {
        super.onMouseClicked();
        if (isValidChoice) {
            controller.processClick(playerInfo.player);
        }
    }

}
