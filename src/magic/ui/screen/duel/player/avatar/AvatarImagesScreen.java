package magic.ui.screen.duel.player.avatar;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import magic.ui.helpers.UrlHelper;
import magic.ui.ScreenController;
import magic.ui.screen.interfaces.IAvatarImageConsumer;
import magic.ui.screen.widget.MenuButton;
import magic.ui.helpers.ImageHelper;
import magic.translate.UiString;
import magic.ui.screen.HeaderFooterScreen;

@SuppressWarnings("serial")
public class AvatarImagesScreen extends HeaderFooterScreen {

    // translatable strings.
    private static final String _S1 = "Select Avatar";
    private static final String _S2 = "Click to select this avatar image.";
    private static final String _S3 = "Avatars";
    private static final String _S4 = "Cancel";
    private static final String _S5 = "More avatars...";
    private static final String _S6 = "Get more avatars from the Magarena forum.";

    private final IAvatarImageConsumer consumer;
    private final ContentPanel contentPanel;

    public AvatarImagesScreen(final IAvatarImageConsumer consumer) {
        super(UiString.get(_S3));
        this.consumer = consumer;
        this.contentPanel = new ContentPanel(this);
        setMainContent(contentPanel);
        setLeftFooter(MenuButton.getCloseScreenButton(UiString.get(_S4)));
        addToFooter(MenuButton.build(this::doOpenAvatarsWebPage, _S5, _S6));
    }
    
    private void doOpenAvatarsWebPage() {
        UrlHelper.openURL(UrlHelper.URL_AVATARS);        
    }

    void displayImageSetIcons(final AvatarImageSet imageSet) {
        contentPanel.displayImageSetIcons(imageSet);
    }

    private void doSaveAvatarAndClose() {
        notifyConsumer();
        ScreenController.closeActiveScreen(false);
    }

    void setSelectedAvatar(final JLabel iconLabel) {
        final Icon icon = iconLabel.getIcon();
        final BufferedImage bi = ImageHelper.getCompatibleBufferedImage(
                icon.getIconWidth(), icon.getIconHeight(),
                BufferedImage.TRANSLUCENT
        );
        final Graphics g = bi.createGraphics();
        // paint the Icon to the BufferedImage.
        icon.paintIcon(null, g, 0,0);
        g.dispose();
        setRightFooter(MenuButton.build(this::doSaveAvatarAndClose,
                new ImageIcon(ImageHelper.scale(bi, 46, 46)),
                UiString.get(_S1), UiString.get(_S2))
        );
    }

    void notifyConsumer() {
        if (consumer != null) {
            SwingUtilities.invokeLater(() -> {
                consumer.setSelectedAvatarPath(contentPanel.getImagePath());
            });
        }
    }
}
