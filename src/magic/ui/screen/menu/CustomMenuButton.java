package magic.ui.screen.menu;

import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import magic.awt.MagicFont;
import magic.data.GeneralConfig;
import magic.data.MagicIcon;
import magic.translate.MText;
import magic.ui.FontsAndBorders;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.helpers.ImageHelper;
import magic.ui.utility.MagicStyle;

@SuppressWarnings("serial")
class CustomMenuButton extends MenuButton {

    private static final Font CUSTOM_FONT = MagicFont.JaceBelerenBold.get().deriveFont(32f);
    private static final Font DEFAULT_FONT = FontsAndBorders.FONT_MENU_BUTTON.deriveFont(30.0f);

    CustomMenuButton(String caption, AbstractAction action, String tooltip, boolean showSeparator) {
        super(caption, action, tooltip);
        setFont(getDisplayFont());
    }

    CustomMenuButton(String caption, AbstractAction action, String tooltip) {
        this(caption, action, tooltip, true);
    }

    CustomMenuButton(String caption, AbstractAction action) {
        this(caption, action, null);
    }

    CustomMenuButton() {
        // default to super class.
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

    public static CustomMenuButton getCloseScreenButton(final String caption) {
        return new CustomMenuButton(caption, closeScreenAction);
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

    public static CustomMenuButton getCloseScreenButton() {
        return getCloseScreenButton(MText.get(_S1));
    }

    public static CustomMenuButton getTestButton() {
        return new CustomMenuButton("Test", closeScreenAction);
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
    public static CustomMenuButton build(Runnable action, ImageIcon image, String title, String description) {
        return new NewActionBarButton(
                image, title, description,
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        action.run();
                    }
                }
        );
    }

    public static CustomMenuButton build(Runnable action, MagicIcon icon, String title) {
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
    public static CustomMenuButton build(Runnable action, MagicIcon icon, String title, String description) {
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
    public static CustomMenuButton build(Runnable action, String title, String tooltip) {
        return new NewActionBarButton(
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
     * @param text button caption (<b>in English</b>).
     * @return
     */
    public static CustomMenuButton build(Runnable action, String title) {
        return new NewActionBarButton(title, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    /**
    * Action bar button used to change screen layout.
    */
    public static CustomMenuButton buildLayoutButton(final Runnable action) {
        return new NewLayoutButton(action);
    }

    public static Font getDisplayFont() {
        return MText.canUseCustomFonts() && GeneralConfig.getInstance().useCustomFonts()
            ? CUSTOM_FONT : DEFAULT_FONT;
    }

}
