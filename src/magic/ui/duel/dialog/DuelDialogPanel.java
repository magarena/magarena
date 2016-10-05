package magic.ui.duel.dialog;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import magic.ui.screen.duel.game.SwingGameController;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DuelDialogPanel extends TexturedPanel {

    private final static MigLayout miglayout = new MigLayout("center, center");

    public DuelDialogPanel() {

        setVisible(false);
        setBackground(FontsAndBorders.IMENUOVERLAY_BACKGROUND_COLOR);
        setLayout(miglayout);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    setVisible(false);
                }
            }
        });

    }

    public void showEndGameMessage(final SwingGameController controller) {
        removeAll();
        add(new EndGameMessagePanel(controller));
        setVisible(true);
    }

    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        if (!isVisible) {
            removeAll();
        }
    }

}
