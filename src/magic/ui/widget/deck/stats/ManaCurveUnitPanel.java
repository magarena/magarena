package magic.ui.widget.deck.stats;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.data.MagicIcon;
import magic.ui.MagicImages;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ManaCurveUnitPanel extends JPanel {

    static int MAX_VALUE = 0;

    private static final Color COLOR1 = MagicStyle.bleach1(
        MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND),
        0.2f
    );

    private final JLabel iconLabel;
    private final JLabel valueLabel;
    private int value;

    ManaCurveUnitPanel(MagicIcon manaIcon) {

        iconLabel = new JLabel(MagicImages.getIcon(manaIcon));
        iconLabel.setHorizontalAlignment(JLabel.CENTER);

        valueLabel = new JLabel("0");
        valueLabel.setHorizontalAlignment(JLabel.CENTER);

        setLayout(new MigLayout("flowy, insets 4"));
        add(iconLabel, "w 100%");
        add(valueLabel, "w 100%");

        setOpaque(false);
    }

    void setValue(int aValue) {
        this.value = aValue;
        valueLabel.setText(Integer.toString(aValue));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        if (value > 0) {
            int h = getHeight();
            int y = getHeight() - h - (int) (h * (value / (double) MAX_VALUE));
            final GradientPaint PAINT_COLOR = new GradientPaint(0, 0, COLOR1, 0, h + 50, Color.WHITE);
            g2d.setPaint(PAINT_COLOR);
            g2d.fillRect(0, y + h, getWidth(), h - y);
        }
        super.paintComponent(g);
    }

}
