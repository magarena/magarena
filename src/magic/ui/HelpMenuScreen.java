package magic.ui;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class HelpMenuScreen extends MagScreen {

    public HelpMenuScreen(final MagicFrame frame0) {
        super(getScreenContent(frame0), frame0);
    }

    private static JPanel getScreenContent(final MagicFrame frame0) {
        final JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new MigLayout("insets 0, gap 0, center, center"));
        content.add(new MagicHelpMenu(frame0));
        return content;
    }

    /* (non-Javadoc)
     * @see magic.ui.MagScreen#isScreenReadyToClose(magic.ui.MagScreen)
     */
    @Override
    public boolean isScreenReadyToClose(MagScreen nextScreen) {
        return true;
    }

}
