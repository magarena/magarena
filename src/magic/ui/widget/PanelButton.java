package magic.ui.widget;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import magic.ui.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class PanelButton extends JPanel {

    protected abstract void onMouseClicked();

    public PanelButton() {

        setLayout(new MigLayout("insets 0, fill"));
        setBorder(FontsAndBorders.UP_BORDER);
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && isValidClick()) {
                    setBorder(FontsAndBorders.DOWN_BORDER);
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    setBorder(FontsAndBorders.UP_BORDER);
                    if (contains(e.getX(), e.getY())) {
                        onMouseClicked();
                    }
                }
            }
        });
    }

    protected boolean isValidClick() {
        return true;
    }

    public void setComponent(final JComponent component) {
        add(component, "grow");
    }
}
