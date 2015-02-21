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
import magic.ui.ScreenController;
import magic.ui.MagicStyle;

public class ImageHandGraveyardExileViewer extends JPanel implements ChangeListener {

    private static final long serialVersionUID = 1L;

    private final ViewerInfo viewerInfo;
    private final TabSelector tabSelector;
    private final ImageCardListViewer cardListViewer;
    private final MagicCardList other = new MagicCardList();
    private JToggleButton selectedTab = null;

    public ImageHandGraveyardExileViewer(final SwingGameController controller) {
        
        viewerInfo = controller.getViewerInfo();

        setOpaque(false);
        setLayout(new BorderLayout(6,0));

        final String playerName=viewerInfo.getPlayerInfo(false).name;
        final String opponentName=viewerInfo.getPlayerInfo(true).name;

        tabSelector=new TabSelector(this,true);
        tabSelector.addTab(MagicStyle.getTheme().getIcon(Theme.ICON_SMALL_HAND),"Hand : "+playerName);
        tabSelector.addTab(MagicStyle.getTheme().getIcon(Theme.ICON_SMALL_GRAVEYARD),"Graveyard : "+playerName);
        tabSelector.addTab(MagicStyle.getTheme().getIcon(Theme.ICON_SMALL_GRAVEYARD),"Graveyard : "+opponentName);
        tabSelector.addTab(MagicStyle.getTheme().getIcon(Theme.ICON_SMALL_EXILE),"Exile : "+playerName);
        tabSelector.addTab(MagicStyle.getTheme().getIcon(Theme.ICON_SMALL_EXILE),"Exile : "+opponentName);
        tabSelector.addTab(MagicStyle.getTheme().getIcon(Theme.ICON_SMALL_HAND),"Other : "+playerName);
        add(tabSelector,BorderLayout.WEST);

        cardListViewer=new ImageCardListViewer(controller);
        add(cardListViewer,BorderLayout.CENTER);
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
            final boolean useCardZoneScreen,
            final String cardZoneTitle,
            final boolean aShowInfo) {
        if (useCardZoneScreen) {
            useCardZoneScreen(cards, cardZoneTitle);
        } else {
            cardListViewer.setCardList(cards, aShowInfo);
        }
    }

    private void update(final boolean useCardZoneScreen) {
        if (cardListViewer!=null) {
            switch (tabSelector.getSelectedTab()) {
                case 0:
                    showCards(
                        viewerInfo.getPlayerInfo(false).hand,
                        useCardZoneScreen, "Player Hand", true);
                    break;
                case 1:
                    showCards(
                            viewerInfo.getPlayerInfo(false).graveyard,
                            useCardZoneScreen, "Player Graveyard", false);
                    break;
                case 2:
                    showCards(
                            viewerInfo.getPlayerInfo(true).graveyard,
                            useCardZoneScreen, "Computer Graveyard", false);
                    break;
                case 3:
                    showCards(
                            viewerInfo.getPlayerInfo(false).exile,
                            useCardZoneScreen, "Player Exile", false);
                    break;
                case 4:
                    showCards(
                            viewerInfo.getPlayerInfo(true).exile,
                            useCardZoneScreen, "Computer Exile", false);
                    break;
                case 5:
                    showCards(
                            other,
                            useCardZoneScreen, "Other", false);
                    break;
            }
            repaint();
        }
    }

    private void useCardZoneScreen(final MagicCardList aCardList, final String zoneName) {
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
}
