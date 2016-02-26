package magic.ui.canvas.cards;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.utility.GraphicsUtils;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.TexturedPanel;

@SuppressWarnings("serial")
public class CardImageOverlay extends TexturedPanel {

    private BufferedImage cardImage = null;

    public CardImageOverlay(final MagicCard aCard) {

        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "closeOverlay");
        getActionMap().put("closeOverlay", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                setVisible(false);
            }
        });

        final MouseAdapter mouseClick = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                setVisible(false);
            }
        };

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                drawCardImage(aCard.getCardDefinition());
            }
        });

        addMouseListener(mouseClick);
        addMouseMotionListener(new MouseMotionAdapter() {});
        addKeyListener(new KeyAdapter() {});
        setFocusTraversalKeysEnabled(false);

        setBackground(MagicStyle.getTranslucentColor(Color.DARK_GRAY, 200));

        ScreenController.getMainFrame().setGlassPane(this);
        setVisible(true);

    }

    private void drawCardImage(final MagicCardDefinition aCard) {
        final BufferedImage baseImage = MagicImages.geCardImageUseCache(aCard, this);
        final int baseWidth = baseImage.getWidth();
        final int baseHeight = baseImage.getHeight();
        final double scale = Math.min(
            Math.min(getWidth(), baseWidth) / (double) baseWidth,
            Math.min(getHeight(), baseHeight) / (double) baseHeight
        );
        cardImage = GraphicsUtils.scale(baseImage, (int)(scale * baseWidth), (int)(scale * baseHeight));
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cardImage != null) {
            g.drawImage(
                cardImage,
                (getWidth() - cardImage.getWidth()) / 2,
                (getHeight() - cardImage.getHeight()) / 2,
                this
            );
        }
    }

}
