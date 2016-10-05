package magic.ui.widget.card;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JWindow;
import magic.ui.utility.GraphicsUtils;
import net.miginfocom.swing.MigLayout;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.ease.Spline;

@SuppressWarnings("serial")
public class MagicInfoWindow extends JWindow {

    private final Color BACKGROUND_COLOR = new Color(252, 255, 179);
    private final JLabel title = new JLabel();
    private final JLabel body = new JLabel();

    private Timeline fadeInTimeline;

    public MagicInfoWindow() {

        setVisible(false);
        getContentPane().setBackground(BACKGROUND_COLOR);

        title.setFont(title.getFont().deriveFont(Font.BOLD).deriveFont(16f));
        title.setForeground(Color.BLACK);
        body.setFont(body.getFont().deriveFont(Font.PLAIN).deriveFont(14f));
        body.setForeground(Color.BLACK);

        title.setToolTipText(null);

        getContentPane().setLayout(new MigLayout("flowy, insets 2 3 2 4, gap 0"));
        refreshLayout();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setVisible(false);
            }
        });

    }

    private void refreshLayout() {
        final Container content = getContentPane();
        content.removeAll();
        final int titleHeight = title.getFontMetrics(body.getFont()).getMaxAscent();
        content.add(title, "h " + (titleHeight + 4) + "!");

        final String bodyText = body.getText();
        final String plainText = bodyText.replace("<html>", "").replace("</html>", "").replace("<br>", "");
        body.setText(plainText);
        final FontMetrics fontMetrics = body.getFontMetrics(body.getFont());
        body.setText(bodyText);

        final int maxWidth = 380;
        final String[] lineText = bodyText.replace("<html>", "").replace("</html>", "").trim().split("\r\n|\r|\n|<br>");
        int totalLines = 0;
        int totalWidth = 0;
        for (String text : lineText) {
            totalLines++;
            final int textWidth = fontMetrics.stringWidth(text.trim().isEmpty() ? "." : text);
            double calc1 = textWidth / ((double) maxWidth);
            final double calc2 = Math.ceil(calc1);
            long calc3 = Math.round(calc2);
            final int textLines = (int) (textWidth == 0 ? 1 : calc3 - 1);
            totalLines += textLines;
            final int W = textWidth > maxWidth ? maxWidth : textWidth;
            if (W > totalWidth) {
                totalWidth = W;
            }
        }

        final int lineHeight = fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent();
        final int totalHeight = (totalLines + 1) * lineHeight;

        body.setMinimumSize(new Dimension(0, 0));
        body.setPreferredSize(new Dimension(totalWidth, totalHeight));
        body.setMaximumSize(new Dimension(maxWidth, totalHeight));

        content.add(body, "w 0:" + totalWidth + ":" + maxWidth + ", h " + totalHeight + "!");
        revalidate();
    }

    public void setTitle(final String text) {
        title.setText(text);
        refreshLayout();
    }

    public void setDescription(final String text) {
        body.setText(text);
        refreshLayout();
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (GraphicsUtils.isWindowTranslucencySupported()) {
            if (aFlag == false) {
                setOpacity(0f);
            } else {
                fadeInTimeline = new Timeline();
                fadeInTimeline.setDuration(200);
                fadeInTimeline.setEase(new Spline(0.8f));
                fadeInTimeline.addPropertyToInterpolate(
                        Timeline.property("opacity")
                        .on(this)
                        .from(0.0f)
                        .to(1.0f));
                fadeInTimeline.play();
            }
        }
    }

}
