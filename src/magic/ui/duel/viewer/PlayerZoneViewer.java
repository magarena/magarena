package magic.ui.duel.viewer;

import magic.model.MagicCardList;
import magic.ui.SwingGameController;
import magic.ui.theme.Theme;
import magic.ui.widget.TabSelector;

import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;
import magic.model.MagicPlayerZone;
import magic.ui.ScreenController;
import magic.ui.MagicStyle;

@SuppressWarnings("serial")
public class PlayerZoneViewer extends JPanel implements ChangeListener {

    // fired when contents of player zone is changed
    public static final String CP_PLAYER_ZONE = "activeZoneName";

    private final SwingGameController controller;
    private final TabSelector tabSelector;
    private final MagicCardList other = new MagicCardList();
    private JToggleButton selectedTab = null;
    private final ImageCardListViewer imageCardsListViewer;

    public PlayerZoneViewer(final SwingGameController controller) {

        this.controller = controller;
        this.imageCardsListViewer = new ImageCardListViewer(controller);
 
        setOpaque(false);
        setLayout(new BorderLayout(6, 0));

        final Theme theme = MagicStyle.getTheme();
        tabSelector = new TabSelector(this, true);
        tabSelector.addTab(theme.getIcon(Theme.ICON_SMALL_HAND), getHandZoneName(getUserPlayer()));
        tabSelector.addTab(theme.getIcon(Theme.ICON_SMALL_GRAVEYARD), getGraveyardZoneName(getUserPlayer()));
        tabSelector.addTab(theme.getIcon(Theme.ICON_SMALL_GRAVEYARD), getGraveyardZoneName(getAiPlayer()));
        tabSelector.addTab(theme.getIcon(Theme.ICON_SMALL_EXILE), getExileZoneName(getUserPlayer()));
        tabSelector.addTab(theme.getIcon(Theme.ICON_SMALL_EXILE), getExileZoneName(getAiPlayer()));
        tabSelector.addTab(theme.getIcon(Theme.ICON_SMALL_HAND), "Other : " + getUserPlayer().name);
        add(tabSelector, BorderLayout.WEST);

        add(imageCardsListViewer, BorderLayout.CENTER);
    }

    public void setSelectedTab(final int selectedTab) {
        if (selectedTab>=0) {
            tabSelector.setSelectedTab(selectedTab);
        }
    }

    public void showCards(final MagicCardList cards) {
        other.clear();
        other.addAll(cards);
    }

    public void update() {
        update(false);
    }

    private void showCards(
            final MagicCardList cards,
            final boolean showFullScreen,
            String cardZoneTitle,
            final boolean showCardIcons) {
        if (showFullScreen) {
            showFullScreenZone(cards, cardZoneTitle);
        } else {
            imageCardsListViewer.setCardList(cards, showCardIcons);
            firePropertyChange(CP_PLAYER_ZONE, null, cardZoneTitle);
        }
    }

    private void update(final boolean showFullScreen) {
        switch (tabSelector.getSelectedTab()) {
            case 0:
                showCards(
                        getUserPlayer().hand,
                        showFullScreen, getHandZoneName(getUserPlayer(), !showFullScreen && !getUserPlayer().isAi), true);
                break;
            case 1:
                showCards(
                        getUserPlayer().graveyard,
                        showFullScreen, getGraveyardZoneName(getUserPlayer()), false);
                break;
            case 2:
                showCards(
                        getAiPlayer().graveyard,
                        showFullScreen, getGraveyardZoneName(getAiPlayer()), false);
                break;
            case 3:
                showCards(
                        getUserPlayer().exile,
                        showFullScreen, getExileZoneName(getUserPlayer()), false);
                break;
            case 4:
                showCards(
                        getAiPlayer().exile,
                        showFullScreen, getExileZoneName(getAiPlayer()), false);
                break;
            case 5:
                showCards(
                        other,
                        showFullScreen, "Other", false);
                break;
        }
        repaint();
    }

    private String getHandZoneName(final PlayerViewerInfo player, final boolean hideName) {
        return hideName ? "" : player.name + " Hand";
    }
    private String getHandZoneName(final PlayerViewerInfo player) {
        return getHandZoneName(player, false);
    }

    private String getGraveyardZoneName(final PlayerViewerInfo player) {
        return player.name + " Graveyard";
    }

    private String getExileZoneName(final PlayerViewerInfo player) {
        return player.name + " Exile";
    }

    private void showFullScreenZone(final MagicCardList aCardList, final String zoneName) {
        ScreenController.showCardZoneScreen(aCardList, zoneName, false);
    }

    @Override
    public void stateChanged(final ChangeEvent event) {

        final JToggleButton btn = (JToggleButton) event.getSource();
        final boolean zoneChanged = selectedTab == null || !selectedTab.getActionCommand().equals(btn.getActionCommand());
        final boolean showFullScreenZone = !zoneChanged && tabSelector.isUserClick();

        if (zoneChanged || showFullScreenZone) {
            update(showFullScreenZone);
            if (zoneChanged) {
                notifyPlayerZoneListeners(Integer.parseInt(btn.getActionCommand()));
            }
        }
        
        this.selectedTab = btn;

    }

    private void notifyPlayerZoneListeners(final int newPlayerZoneIndex) {
        if (newPlayerZoneIndex == 0) {
            controller.notifyPlayerZoneChanged(getUserPlayer(), MagicPlayerZone.HAND);
        } else if (newPlayerZoneIndex == 1) {
            controller.notifyPlayerZoneChanged(getUserPlayer(), MagicPlayerZone.GRAVEYARD);
        } else if (newPlayerZoneIndex == 2) {
            controller.notifyPlayerZoneChanged(getAiPlayer(), MagicPlayerZone.GRAVEYARD);
        } else if (newPlayerZoneIndex == 3) {
            controller.notifyPlayerZoneChanged(getUserPlayer(), MagicPlayerZone.EXILE);
        } else if (newPlayerZoneIndex == 4) {
            controller.notifyPlayerZoneChanged(getAiPlayer(), MagicPlayerZone.EXILE);
        } else if (newPlayerZoneIndex == 5) {
            controller.notifyPlayerZoneChanged(getUserPlayer(), MagicPlayerZone.LIBRARY);
        }
    }

    /**
     * Returns the latest instance of PlayerViewInfo.
     * <p>
     * <b>Do not retain a reference toPlayerViewerInfo since a new instance
     * is created whenever ViewerInfo is updated.</b>
     */
    private PlayerViewerInfo getUserPlayer() {
        return controller.getViewerInfo().getPlayerInfo(false);
    }

    /**
     * Returns the latest instance of PlayerViewInfo.
     * <p>
     * <b>Do not retain a reference toPlayerViewerInfo since a new instance
     * is created whenever ViewerInfo is updated.</b>
     */
    private PlayerViewerInfo getAiPlayer() {
        return controller.getViewerInfo().getPlayerInfo(true);
    }

    public ImageCardListViewer getImageCardsListViewer() {
        return imageCardsListViewer;
    }

}
