package magic.ui.screen.duel.mulligan;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import magic.data.MagicIcon;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.translate.UiString;
import magic.ui.widget.cards.canvas.CardsCanvas.LayoutMode;
import magic.ui.widget.cards.canvas.CardsCanvas;
import magic.ui.widget.duel.choice.MulliganChoicePanel;
import magic.ui.screen.AbstractScreen;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.interfaces.IWikiPage;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;
import magic.utility.WikiPage;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class MulliganScreen
    extends AbstractScreen
    implements IStatusBar, IActionBar, IWikiPage {

    // translatable string
    private static final String _S1 = "Vancouver Mulligan";
    private static final String _S2 = "Close";
    private static final String _S3 = "Play this hand";
    private static final String _S5 = "Returns this hand to your library then draws a new hand with one less card.<br>You may scry 1 if your opening hand has fewer cards than your starting hand size.";
    private static final String _S6 = "You play %s";
    private static final String _S7 = "first.";
    private static final String _S8 = "second.";

    private volatile static boolean isActive = false;

    private CardsCanvas cardsCanvas;
    private MulliganChoicePanel choicePanel;
    private final AbstractAction takeMulliganAction = new TakeMulliganAction();
    private final StatusPanel statusPanel;
    private final MagicCardList hand;

    public MulliganScreen(final MulliganChoicePanel choicePanel, final MagicCardList hand) {
        this.choicePanel = choicePanel;
        this.hand = hand;
        isActive = true;
        statusPanel = new StatusPanel(choicePanel.getGameController().getGame());
        setContent(getScreenContent(hand));
        setEscapeKeyInputMap();
    }

    public static boolean isActive() {
        return isActive;
    }

    /**
     * Override standard behaviour of the Escape key as defined by AbstractScreen.
     * In this case close screen and set flag so that it is not re-opened should the
     * user subsequently take a mulligan from the main game screen.
     */
    private void setEscapeKeyInputMap() {
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "EscapeKeyAction");
        getActionMap().put("EscapeKeyAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!cardsCanvas.isBusy()) {
                    isActive = false;
                    ScreenController.closeActiveScreen(false);
                }
            }
        });
    }

    private JPanel getScreenContent(final MagicCardList hand) {
        cardsCanvas = new CardsCanvas();
        cardsCanvas.setAnimationEnabled(true);
        cardsCanvas.setAnimationDelay(50, 20);
        cardsCanvas.setLayoutMode(LayoutMode.SCALE_TO_FIT);
        refreshCardsDisplay(hand);
        return cardsCanvas;
    }

    public void dealNewHand(final MulliganChoicePanel choicePanel, final MagicCardList hand) {
        this.choicePanel = choicePanel;
        refreshCardsDisplay(hand);
    }

    private void refreshCardsDisplay(final MagicCardList hand) {
        // Important: uses Runnable so painting works properly.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                cardsCanvas.refresh(hand);
            }
        });
    }

    @Override
    public String getScreenCaption() {
        return UiString.get(_S1);
    }

    @Override
    public MenuButton getLeftAction() {
        return new MenuButton(UiString.get(_S2), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (!cardsCanvas.isBusy()) {
                    isActive = false;
                    ScreenController.closeActiveScreen(false);
                }
            }
        });
    }

    @Override
    public MenuButton getRightAction() {
        return new MenuButton(UiString.get(_S3), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (!cardsCanvas.isBusy()) {
                    choicePanel.doMulliganAction(false);
                    isActive = false;
                    ScreenController.closeActiveScreen(false);
                }
            }
        });
    }

    @Override
    public List<MenuButton> getMiddleActions() {
        final List<MenuButton> buttons = new ArrayList<>();
        buttons.add(new ActionBarButton(
                        MagicImages.getIcon(MagicIcon.MULLIGAN_ICON),
                        UiString.get(_S1), UiString.get(_S5),
                        takeMulliganAction)
                );
        return buttons;
    }

    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

    @Override
    public String getWikiPageName() {
        return WikiPage.MULLIGAN;
    }

    private final class TakeMulliganAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!cardsCanvas.isBusy()) {
                choicePanel.doMulliganAction(true);
                if (hand.size() <= 2) {
                    isActive = false;
                    ScreenController.closeActiveScreen(false);
                }
            }
        }
    }

    @Override
    public JPanel getStatusPanel() {
        return statusPanel;
    }

    private class StatusPanel extends JPanel {

        // ui
        private final MigLayout migLayout = new MigLayout();
        private final JLabel playingFirstLabel = new JLabel();

        public StatusPanel(final MagicGame game) {
            setLookAndFeel();
            setContent(game);
        }

        private void setLookAndFeel() {
            setOpaque(false);
            setLayout(migLayout);
            // playing first label
            playingFirstLabel.setForeground(Color.WHITE);
            playingFirstLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
            playingFirstLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }

        private void refreshLayout() {
            removeAll();
            migLayout.setLayoutConstraints("insets 0, gap 2, flowy");
            add(playingFirstLabel, "w 100%");
        }

        private void setContent(final MagicGame game) {
            final MagicPlayer turnPlayer = game.getTurnPlayer();
            final MagicPlayer humanPlayer = game.getPlayer(0);
            playingFirstLabel.setText(UiString.get(_S6,
                    turnPlayer == humanPlayer
                            ? UiString.get(_S7)
                            : UiString.get(_S8)));
            refreshLayout();
        }

    }

}
