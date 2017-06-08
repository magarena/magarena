package magic.ui.widget.alerter;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class AlertPanel extends JPanel {

    private final MigLayout miglayout = new MigLayout();
    private final List<AlertButton> alertButtons = new ArrayList<>();

    public AlertPanel() {
        setOpaque(false);
        setAlertButtons();
        setLayout(miglayout);
        refreshLayout();
    }

    private void setAlertButtons() {
        alertButtons.clear();
        alertButtons.add(new UpgradeJavaAlertButton());
        alertButtons.add(new NewVersionAlertButton());
        alertButtons.add(new MissingImagesAlertButton());
    }

    private void refreshLayout() {
        miglayout.setLayoutConstraints("insets 1 2 0 4, hidemode 3, ay center, ax center");
        for (AlertButton btn : alertButtons) {
            add(btn);
        }
    }

    public void refreshAlerts() {
        for (AlertButton btn : alertButtons) {
            btn.doAlertCheck();
        }
    }

}
