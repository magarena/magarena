package magic.ui.widget.duel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import magic.ui.FontsAndBorders;
import magic.ui.duel.viewerinfo.GameViewerInfo;
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

    public void showEndGameMessage(GameViewerInfo game) {
        removeAll();
        add(new EndGameMessagePanel(game));
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
