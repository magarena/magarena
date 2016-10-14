package magic.ui.screen.widget;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import magic.data.MagicIcon;
import magic.translate.UiString;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.helpers.ImageHelper;
import magic.ui.utility.MagicStyle;
import magic.ui.FontsAndBorders;
import magic.ui.widget.button.LayoutButton;

@SuppressWarnings("serial")
public class MenuButton extends JButton {

    // translatable strings
    private static final String _S1 = "Close";

    private final static Color COLOR_NORMAL = Color.WHITE;
    private final static Color COLOR_DISABLED = Color.DARK_GRAY;

    private boolean isRunnable;
    private boolean hasSeparator;

    public MenuButton(final String caption, final AbstractAction action, final String tooltip, final boolean showSeparator) {
        super(caption);
        this.isRunnable = (action != null);
        this.hasSeparator = showSeparator;
        setFont(FontsAndBorders.FONT_MENU_BUTTON);
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
    public MenuButton(final String caption, final AbstractAction action, final String tooltip) {
        this(caption, action, tooltip, true);
    }
    public MenuButton(final String caption, final AbstractAction action) {
        this(caption, action, null);
    }
    protected MenuButton() {
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

    public static MenuButton getCloseScreenButton(final String caption) {
        return new MenuButton(caption, closeScreenAction);
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

    public static MenuButton getCloseScreenButton() {
        return getCloseScreenButton(UiString.get(_S1));
    }

    public static MenuButton getTestButton() {
        return new MenuButton("Test", closeScreenAction);
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
    public static MenuButton build(Runnable action, ImageIcon image, String title, String description) {
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

    public static MenuButton build(Runnable action, MagicIcon icon, String title) {
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
    public static MenuButton build(Runnable action, MagicIcon icon, String title, String description) {
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
    public static MenuButton build(Runnable action, String title, String tooltip) {
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
     * @param text button caption (<b>in English</b>).
     * @return
     */
    public static MenuButton build(Runnable action, String title) {
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
    public static MenuButton buildLayoutButton(final Runnable action) {
        return new LayoutButton(action);
    }

}
