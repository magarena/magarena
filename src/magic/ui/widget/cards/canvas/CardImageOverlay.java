package magic.ui.widget.cards.canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import magic.model.MagicCardDefinition;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.helpers.ImageHelper;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.TexturedPanel;

@SuppressWarnings("serial")
public class CardImageOverlay extends TexturedPanel {

    private BufferedImage cardImage = null;
    private boolean isSplitCard = false;

    public CardImageOverlay(final MagicCardDefinition aCard) {

        isSplitCard = aCard.isSplitCard();

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
                drawCardImage(aCard);
            }
        });

        addMouseListener(mouseClick);
        addMouseMotionListener(new MouseMotionAdapter() {});
        addKeyListener(new KeyAdapter() {});
        setFocusTraversalKeysEnabled(false);

        setBackground(MagicStyle.getTranslucentColor(Color.DARK_GRAY, 200));

        ScreenController.getFrame().setGlassPane(this);
        setVisible(true);

    }

    private void drawCardImage(final MagicCardDefinition aCard) {
        isSplitCard = aCard.isSplitCard();
        BufferedImage baseImage = MagicImages.getCardImage(aCard);
        int baseWidth = baseImage.getWidth();
        int baseHeight = baseImage.getHeight();
        double scale = Math.min(
            Math.min(getWidth(), baseWidth) / (double) baseWidth,
            Math.min(getHeight(), baseHeight) / (double) baseHeight
        );
        cardImage = ImageHelper.scale(baseImage, (int)(scale * baseWidth), (int)(scale * baseHeight));
        repaint();
    }

    private void drawSplitCard(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(this.getWidth() / 2, this.getHeight() / 2);
        g2d.rotate(Math.toRadians(90));
        g2d.translate(-cardImage.getWidth() / 2, -cardImage.getHeight() / 2);
        g2d.drawImage(cardImage, 0, 0, null);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cardImage != null) {
            if (isSplitCard) {
                drawSplitCard(g);
            } else {
                g.drawImage(
                    cardImage,
                    (getWidth() - cardImage.getWidth()) / 2,
                    (getHeight() - cardImage.getHeight()) / 2,
                    this
                );
            }
        }
    }

}
