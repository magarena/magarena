package magic.ui.screen.duel.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import magic.model.MagicCardList;
import magic.ui.duel.resolution.ResolutionProfileResult;
import magic.ui.duel.resolution.ResolutionProfileType;
import magic.ui.duel.viewerinfo.CardViewerInfo;
import magic.ui.widget.duel.viewer.ImageBattlefieldViewer;
import magic.ui.widget.duel.viewer.ImageCombatViewer;
import magic.ui.widget.duel.viewer.PlayerZoneViewer;

@SuppressWarnings("serial")
public class BattlefieldPanel extends JPanel {

    private final BattlefieldTextOverlay textOverlay = new BattlefieldTextOverlay();

    private final PlayerZoneViewer playerZoneViewer;
    public final ImageBattlefieldViewer imagePlayerPermanentViewer;
    public final ImageBattlefieldViewer imageOpponentPermanentViewer;
    private final ImageCombatViewer imageCombatViewer;

    public BattlefieldPanel(final SwingGameController controller) {

        playerZoneViewer = controller.getPlayerZoneViewer();
        imagePlayerPermanentViewer = new ImageBattlefieldViewer(controller, false);
        imageOpponentPermanentViewer = new ImageBattlefieldViewer(controller, true);
        imageCombatViewer = new ImageCombatViewer(controller);

        playerZoneViewer.addPropertyChangeListener(PlayerZoneViewer.CP_PLAYER_ZONE,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        textOverlay.setPlayerZoneName((String) evt.getNewValue());
                        repaint();
                    }
                });

        setLayout(null);
        add(playerZoneViewer);
        add(imagePlayerPermanentViewer);
        add(imageOpponentPermanentViewer);
        add(imageCombatViewer);

        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller.waitingForUser() && e.getClickCount() == 2) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        controller.actionClicked();
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        controller.passKeyPressed();
                    }
                }
            }
        });
    }

    public void doUpdate() {
        playerZoneViewer.update();
        imagePlayerPermanentViewer.update();
        imageOpponentPermanentViewer.update();
        imageCombatViewer.update();
    }

    public void showCardsToChoose(final MagicCardList cards) {
        playerZoneViewer.showCardsToChoose(cards);
        if (cards != MagicCardList.NONE) {
            playerZoneViewer.setSelectedTab(5);
        }
    }

    public void focusViewers(int handGraveyard) {
        playerZoneViewer.setSelectedTab(handGraveyard);
    }

    public void resizeComponents(ResolutionProfileResult result) {
        playerZoneViewer.setBounds(result.getBoundary(ResolutionProfileType.GameImageHandGraveyardViewer));
        imagePlayerPermanentViewer.setBounds(result.getBoundary(ResolutionProfileType.GameImagePlayerPermanentViewer));
        imageOpponentPermanentViewer.setBounds(result.getBoundary(ResolutionProfileType.GameImageOpponentPermanentViewer));
        imageCombatViewer.setBounds(result.getBoundary(ResolutionProfileType.GameImageCombatViewer));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Image overlayImage = textOverlay.getOverlayImage(getWidth(), getHeight());
        g.drawImage(overlayImage, 0, 0, this);
    }

    public void highlightCard(CardViewerInfo cardInfo, boolean b) {
        if (!imagePlayerPermanentViewer.highlightCard(cardInfo, b))
            if (!imageOpponentPermanentViewer.highlightCard(cardInfo, b))
                imageCombatViewer.highlightCard(cardInfo, b);
    }

}

