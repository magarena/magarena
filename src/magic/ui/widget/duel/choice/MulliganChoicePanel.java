package magic.ui.widget.duel.choice;

import java.util.Collections;

import javax.swing.SwingUtilities;

import magic.model.MagicCardList;
import magic.model.MagicSource;
import magic.ui.ScreenController;
import magic.ui.screen.duel.game.SwingGameController;

@SuppressWarnings("serial")
public class MulliganChoicePanel extends MayChoicePanel {

    public MulliganChoicePanel(final SwingGameController controller, final MagicSource source, final String message, final MagicCardList hand) {
        super(controller, source, message);
        SwingUtilities.invokeLater(() -> showMulliganScreen(hand));
    }

    private void showMulliganScreen(final MagicCardList hand) {
        Collections.sort(hand);
        ScreenController.showMulliganScreen(MulliganChoicePanel.this, hand);
    }

    public void doMulliganAction(final boolean takeMulligan) {
        setYesClicked(takeMulligan);
        getGameController().actionClicked();
    }

}
