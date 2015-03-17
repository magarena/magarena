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
public class ImageHandGraveyardExileViewer extends JPanel implements ChangeListener {

    // fired when contents of player zone is changed
    public static final String CP_PLAYER_ZONE = "activeZoneName";

    private final SwingGameController controller;
    private final TabSelector tabSelector;
    private final ImageCardListViewer cardListViewer;
    private final MagicCardList other = new MagicCardList();
    private JToggleButton selectedTab = null;

    public ImageHandGraveyardExileViewer(final SwingGameController controller) {

        this.controller = controller;
 
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

        cardListViewer = new ImageCardListViewer(controller);
        add(cardListViewer, BorderLayout.CENTER);        
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
            final String cardZoneTitle,
            final boolean showCardIcons) {
        if (showFullScreen) {
            showFullScreenZone(cards, cardZoneTitle);
        } else {
            cardListViewer.setCardList(cards, showCardIcons);
            firePropertyChange(CP_PLAYER_ZONE, null, cardZoneTitle);
        }
    }

    private void update(final boolean showFullScreen) {
        if (cardListViewer != null) {
            switch (tabSelector.getSelectedTab()) {
                case 0:
                    showCards(
                            getUserPlayer().hand,
                            showFullScreen, getHandZoneName(getUserPlayer()), true);
                    controller.setPlayerZone(getUserPlayer(), MagicPlayerZone.HAND);
                    break;
                case 1:
                    showCards(
                            getUserPlayer().graveyard,
                            showFullScreen, getGraveyardZoneName(getUserPlayer()), false);
                    controller.setPlayerZone(getUserPlayer(), MagicPlayerZone.GRAVEYARD);
                    break;
                case 2:
                    showCards(
                            getAiPlayer().graveyard,
                            showFullScreen, getGraveyardZoneName(getAiPlayer()), false);
                    controller.setPlayerZone(getAiPlayer(), MagicPlayerZone.GRAVEYARD);
                    break;
                case 3:
                    showCards(
                            getUserPlayer().exile,
                            showFullScreen, getExileZoneName(getUserPlayer()), false);
                    controller.setPlayerZone(getUserPlayer(), MagicPlayerZone.EXILE);
                    break;
                case 4:
                    showCards(
                            getAiPlayer().exile,
                            showFullScreen, getExileZoneName(getAiPlayer()), false);
                    controller.setPlayerZone(getAiPlayer(), MagicPlayerZone.EXILE);
                    break;
                case 5:
                    showCards(
                            other,
                            showFullScreen, "Other", false);
                    break;
            }
            repaint();
        }
    }

    private String getHandZoneName(final PlayerViewerInfo player) {
        return player.isAi ? player.name + " Hand" : "";
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
        update(event.getSource() == this.selectedTab && tabSelector.isUserClick());
        this.selectedTab = (JToggleButton)event.getSource();
    }

    public ImageCardListViewer getCardListViewer() {
        return cardListViewer;
    }

    public void setActivePlayerZone(PlayerViewerInfo playerInfo, MagicPlayerZone zone) {
        tabSelector.setIsUserClick(false);
        if (playerInfo.isAi) {
            switch (zone) {
                case HAND: // TODO
                    break;
                case GRAVEYARD: 
                    setSelectedTab(2);
                    break;
                case EXILE: setSelectedTab(4);
                    break;
            }
        } else {
            switch (zone) {
                case HAND: 
                    setSelectedTab(0);
                    break;
                case GRAVEYARD: 
                    setSelectedTab(1);
                    break;
                case EXILE: 
                    setSelectedTab(3);
                    break;
            }
        }
    }

    public void setFullScreenActivePlayerZone(PlayerViewerInfo playerInfo, MagicPlayerZone zone) {
        tabSelector.setIsUserClick(true);
        if (playerInfo.isAi) {
            switch (zone) {
                case HAND: // TODO
                    break;
                case GRAVEYARD:
                    setSelectedTab(2);
                    break;
                case EXILE: setSelectedTab(4);
                    break;
            }
        } else {
            switch (zone) {
                case HAND:
                    setSelectedTab(0);
                    break;
                case GRAVEYARD:
                    setSelectedTab(1);
                    break;
                case EXILE:
                    setSelectedTab(3);
                    break;
            }
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

}
