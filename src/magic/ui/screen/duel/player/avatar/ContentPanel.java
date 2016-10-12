package magic.ui.screen.duel.player.avatar;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import magic.ui.FontsAndBorders;
import magic.ui.ImageFileIO;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.helpers.ImageHelper;
import magic.ui.theme.PlayerAvatar;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.WrapLayout;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ContentPanel extends JPanel {

    private final AvatarImagesScreen screen;
    private JPanel viewer;
    private final Map<JLabel, Path> imagePathMap = new HashMap<>();
    private JLabel selectedImageLabel = null;

    ContentPanel(final AvatarImagesScreen screen) {
        super(new MigLayout("insets 0, gap 0"));
        this.screen = screen;
        setOpaque(false);
        add(new ImageSetsPanel(screen), "w 240!, h 100%");
        add(getAvatarImageSetViewer(screen), "w 100%, h 100%");
    }

    private JScrollPane getAvatarImageSetViewer(AvatarImagesScreen screen) {

        viewer = new TexturedPanel();
        viewer.setLayout(new WrapLayout());
        viewer.setBorder(FontsAndBorders.BLACK_BORDER);
        viewer.setBackground(FontsAndBorders.MAGSCREEN_FADE_COLOR);

        final JScrollPane scroller = new JScrollPane(viewer);
        scroller.getViewport().setOpaque(false);
        scroller.setBorder(BorderFactory.createEmptyBorder());
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.getVerticalScrollBar().setUnitIncrement(20);
        return scroller;
    }

    void displayImageSetIcons(final AvatarImageSet imageSet) {
        viewer.removeAll();
        imagePathMap.clear();
        for (Path imagePath : imageSet.getImagePaths()) {
            final String filePath = imagePath.toAbsolutePath().toString();
            try (final InputStream ins = new FileInputStream(new File(filePath))) {
                final BufferedImage image = ImageFileIO.toImg(ins, MagicImages.MISSING_BIG);
                final ImageIcon icon = new ImageIcon(ImageHelper.scale(image, PlayerAvatar.LARGE_SIZE, PlayerAvatar.LARGE_SIZE));
                final JLabel iconLabel = new JLabel(icon);
                imagePathMap.put(iconLabel, imagePath);
                iconLabel.setBorder(FontsAndBorders.EMPTY_BORDER);
                viewer.add(iconLabel);
                iconLabel.addMouseListener(new MouseAdapter() {
                    private final Border defaultBorder = iconLabel.getBorder();
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        iconLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                        iconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        iconLabel.setBorder(defaultBorder);
                    }
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (e.getButton() == 1) {
                            final JLabel imageLabel = (JLabel)e.getSource();
                            if (imageLabel != selectedImageLabel) {
                                screen.setSelectedAvatar((JLabel)e.getSource());
                                selectedImageLabel = imageLabel;
                            } else {
                                screen.notifyConsumer();
                                ScreenController.closeActiveScreen(false);
                            }
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        viewer.revalidate();
        viewer.repaint();
    }

    Path getImagePath() {
        return imagePathMap.get(selectedImageLabel);
    }

}
