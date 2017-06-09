package magic.ui.screen.menu;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
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
class NewMenuButton extends JButton {

    // translatable strings
    private static final String _S1 = "Close";

    private final static Color COLOR_NORMAL = Color.WHITE;
    private final static Color COLOR_DISABLED = Color.DARK_GRAY;
    private static final Font CUSTOM_FONT = MagicFont.JaceBelerenBold.get().deriveFont(32f);
    private static final Font DEFAULT_FONT = FontsAndBorders.FONT_MENU_BUTTON.deriveFont(30.0f);

    private boolean isRunnable;
    private boolean hasSeparator;

    NewMenuButton(final String caption, final AbstractAction action, final String tooltip, final boolean showSeparator) {
        super(caption);
        this.isRunnable = (action != null);
        this.hasSeparator = showSeparator;
        setFont(getDisplayFont());
        setHorizontalAlignment(SwingConstants.CENTER);
        setForeground(COLOR_NORMAL);
        setButtonTransparent();
        setFocusable(true);
        setToolTipText(tooltip);
        if (isRunnable) {
            setMouseAdapter();
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addActionListener(action);
        }
    }
    NewMenuButton(final String caption, final AbstractAction action, final String tooltip) {
        this(caption, action, tooltip, true);
    }
    NewMenuButton(final String caption, final AbstractAction action) {
        this(caption, action, null);
    }
    NewMenuButton() {
        isRunnable = false;
        hasSeparator = false;
    }

    public boolean isRunnable() {
        return isRunnable;
    }

    private void setButtonTransparent() {
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        if (!isRunnable) {
            setBorder(null);
        }
    }

    private void setMouseAdapter() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    setForeground(MagicStyle.getRolloverColor());
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (isEnabled()) {
                    setForeground(Color.WHITE);
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled() && SwingUtilities.isLeftMouseButton(e)) {
                    setForeground(MagicStyle.getPressedColor());
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                }

            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (isEnabled()) {
                    setForeground(Color.WHITE);
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }
        });
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        isRunnable = b;
        setForeground(b ? COLOR_NORMAL : COLOR_DISABLED);
    }

    public boolean hasSeparator() {
        return hasSeparator;
    }

    public void setSeparator(boolean b) {
        hasSeparator = b;
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

    public static NewMenuButton getCloseScreenButton(final String caption) {
        return new NewMenuButton(caption, closeScreenAction);
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

    public static NewMenuButton getCloseScreenButton() {
        return getCloseScreenButton(MText.get(_S1));
    }

    public static NewMenuButton getTestButton() {
        return new NewMenuButton("Test", closeScreenAction);
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
    public static NewMenuButton build(Runnable action, ImageIcon image, String title, String description) {
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

    public static NewMenuButton build(Runnable action, MagicIcon icon, String title) {
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
    public static NewMenuButton build(Runnable action, MagicIcon icon, String title, String description) {
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
    public static NewMenuButton build(Runnable action, String title, String tooltip) {
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
    public static NewMenuButton build(Runnable action, String title) {
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
    public static NewMenuButton buildLayoutButton(final Runnable action) {
        return new NewLayoutButton(action);
    }

    public static Font getDisplayFont() {
        return MText.canUseCustomFonts() && GeneralConfig.getInstance().useCustomFonts()
            ? CUSTOM_FONT : DEFAULT_FONT;
    }

}
