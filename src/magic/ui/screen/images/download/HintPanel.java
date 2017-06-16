package magic.ui.screen.images.download;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.ui.MagicImages;
import magic.ui.mwidgets.MWidget;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class HintPanel extends JPanel {

    private final JLabel lbl = new JLabel();
    private final MouseAdapter tooltipMouseAdapter;

    public HintPanel(String aHint) {

        setOpaque(false);

        lbl.setVerticalAlignment(SwingConstants.TOP);
        lbl.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lbl.setVerticalTextPosition(SwingConstants.TOP);

        tooltipMouseAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                showTooltipHint(e.getSource());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                clearHint();
            }
        };

        setLayout(new MigLayout());
        add(lbl, "w 100%, h 100%");

        setToolTipText(aHint);
        showTooltipHint(this);
    }

    public HintPanel() {
        this(null);
    }

    /**
     *
     * @param aComponent
     * @param text : tooltip text (can use html).
     */
    public void addHintSource(JComponent aComponent, String text) {
        aComponent.setToolTipText(text);
        aComponent.addMouseListener(tooltipMouseAdapter);
    }

    public void addHintSource(MWidget aWidget) {
        aWidget.addMouseListener(tooltipMouseAdapter);
    }

    private void clearHint() {
        showTooltipHint(this);
    }

    private void showTooltipHint(Object source) {
        if (source instanceof JComponent) {
            final JComponent c = (JComponent) source;
            lbl.setText(String.format("<html>%s</html>",
                c.getToolTipText() == null ? getToolTipText() : c.getToolTipText()));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.04f);
        final Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(composite);
        g2d.drawImage(
            MagicImages.LOGO,
            (getWidth() - MagicImages.LOGO.getWidth()) / 2,
            (getHeight() - MagicImages.LOGO.getHeight()) / 2,
            this
        );
        g2d.dispose();
    }

}
