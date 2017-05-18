package magic.ui.screen.widget;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import magic.data.MagicIcon;
import magic.translate.MText;
import magic.ui.IDeckProvider;
import magic.ui.MagicImages;
import magic.ui.ScreenController;

@SuppressWarnings("serial")
public final class SampleHandActionButton extends ActionBarButton {

    // translatable strings
    private static final String _S1 = "Sample Hand";
    private static final String _S2 = "Generate sample Hands from this deck.";
    private static final String _S3 = "A deck with a minimum of 7 cards is required first.";

    private SampleHandActionButton() {}

    public static ActionBarButton createInstance(final IDeckProvider provider) {
        return new ActionBarButton(
            MagicImages.getIcon(MagicIcon.HAND_ICON),
            MText.get(_S1), MText.get(_S2),
            new SampleHandAction(provider));
    }

    private static class SampleHandAction extends AbstractAction {

        private final IDeckProvider provider;

        public SampleHandAction(final IDeckProvider provider) {
            this.provider = provider;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (provider.getDeck().size() >= 7) {
                ScreenController.showSampleHandScreen(provider.getDeck());
            } else {
                showInvalidActionMessage(MText.get(_S3));
            }
        }

        private void showInvalidActionMessage(final String message) {
            ScreenController.showWarningMessage(message);
        }

    }

}
