package magic.ui.screen.widget;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import magic.data.MagicIcon;
import magic.translate.MText;
import magic.ui.FontsAndBorders;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.helpers.ImageHelper;
import magic.ui.screen.menu.MenuButton;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.button.LayoutButton;

@SuppressWarnings("serial")
public class PlainMenuButton extends MenuButton {

    // translatable strings
    private static final String _S1 = "Close";

    private final boolean hasSeparator;

    public PlainMenuButton(String caption, AbstractAction action, String tooltip, boolean showSeparator) {
        super(caption, action, tooltip);
        this.hasSeparator = showSeparator;
        setFont(FontsAndBorders.FONT_MENU_BUTTON);
    }

    public PlainMenuButton(String caption, AbstractAction action, String tooltip) {
        this(caption, action, tooltip, true);
    }

    public PlainMenuButton(String caption, AbstractAction action) {
        this(caption, action, null);
    }

    public PlainMenuButton() {
        hasSeparator = false;
    }

    public boolean isRunnable() {
        return isRunnable;
    }

    public boolean hasSeparator() {
        return hasSeparator;
    }

    //
    // Static convenience methods.
    //

    private final static AbstractAction closeScreenAction = new AbstractAction() {
        @Override
        public void actionPerformed(final ActionEvent e) {
            ScreenController.closeActiveScreen(false);
        }
    };

    public static PlainMenuButton getCloseScreenButton(final String caption) {
        return new PlainMenuButton(caption, closeScreenAction);
    }

    @Override
    public void setIcon(final Icon defaultIcon) {
        super.setIcon(defaultIcon);
        setRolloverIcon(ImageHelper.getRecoloredIcon(
                (ImageIcon) defaultIcon,
                MagicStyle.getRolloverColor())
        );
        setPressedIcon(ImageHelper.getRecoloredIcon(
                (ImageIcon) defaultIcon,
                MagicStyle.getPressedColor())
        );
        setDisabledIcon(ImageHelper.getRecoloredIcon(
                (ImageIcon) defaultIcon,
                COLOR_DISABLED)
        );
    }

    public static PlainMenuButton getCloseScreenButton() {
        return getCloseScreenButton(MText.get(_S1));
    }

    public static PlainMenuButton getTestButton() {
        return new PlainMenuButton("Test", closeScreenAction);
    }

    /**
     * Creates an icon-only button with tooltip.<br>
     *
     * @param action click action
     * @param image
     * @param title tooltip title text (<b>in English</b>).
     * @param description tooltip body text (<b>in English</b>).
     * @return
     */
    public static PlainMenuButton build(Runnable action, ImageIcon image, String title, String description) {
        return new ActionBarButton(
                image, title, description,
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        action.run();
                    }
                }
        );
    }

    public static PlainMenuButton build(Runnable action, MagicIcon icon, String title) {
        return build(action, MagicImages.getIcon(icon), title, null);
    }

    /**
     * Creates an icon-only button with tooltip.<br>
     *
     * @param action click action
     * @param icon
     * @param title tooltip title text (<b>in English</b>).
     * @param description tooltip body text (<b>in English</b>).
     * @return
     */
    public static PlainMenuButton build(Runnable action, MagicIcon icon, String title, String description) {
        return build(action, MagicImages.getIcon(icon), title, description);
    }

    /**
     * Creates a text-only button with tooltip.
     *
     * @param action click action.
     * @param title button caption and tooltip title (<b>in English</b>).
     * @param tooltip main tooltip text (<b>in English</b>).
     * @return
     */
    public static PlainMenuButton build(Runnable action, String title, String tooltip) {
        return new ActionBarButton(
                title, tooltip,
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        action.run();
                    }
                }
        );
    }

    /**
     * Creates a text-only button.
     *
     * @param action click action.
     * @param title button caption (<b>in English</b>).
     * @return
     */
    public static PlainMenuButton build(Runnable action, String title) {
        return new ActionBarButton(title, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    /**
    * Action bar button used to change screen layout.
    */
    public static PlainMenuButton buildLayoutButton(final Runnable action) {
        return new LayoutButton(action);
    }

}
