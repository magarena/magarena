package magic.ui.widget.message;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import magic.ui.FontsAndBorders;
import magic.ui.screen.duel.game.SwingGameController;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import magic.utility.MagicSystem;

@SuppressWarnings("serial")
public class TextLabel extends JPanel {

    private static final int SPACE_WIDTH = 3;
    private static final int LINE_HEIGHT = 16;

    private static final Map<?, ?> desktopHintsMap;
    static {
        final Toolkit tk = Toolkit.getDefaultToolkit();
        desktopHintsMap = (Map<?, ?>) (tk.getDesktopProperty("awt.font.desktophints"));
    }

    private final SwingGameController controller;
    private final int maxWidth;
    private final boolean center;
    private final List<TComponent> components = new ArrayList<>();
    private boolean isMouseMoveBusy = false;
    private TextComponent activeComponent = null;

    // CTR
    public TextLabel(String text, Font aFont, int maxWidth, boolean center, Color aColor, SwingGameController aController) {

        this.maxWidth = maxWidth;
        this.center = center;
        this.controller = aController;

        setOpaque(false);
        setLayout(null);

        final boolean isUserPromptText = controller == null;
        final Color interactiveColor = isUserPromptText ? Color.BLACK : Color.BLUE;

        TComponentBuilder.buildTComponents(components, text, this, aFont, aColor, interactiveColor);
        layoutTComponents();

        if (!isUserPromptText) {
            setMouseListeners();
        }
    }

    // CTR
    public TextLabel(String text, int maxWidth, boolean center) {
        this(text, FontsAndBorders.FONT1, maxWidth, center, MagicStyle.getTheme().getColor(Theme.COLOR_CHOICE_FOREGROUND), null);
    }

    private void setMouseListeners() {

        addMouseListener(new MouseAdapter() {

            private boolean dispatchMouseEvent() {
                return activeComponent == null || !activeComponent.isInteractive();
            }

            private boolean isLeftClick(MouseEvent e) {
                return SwingUtilities.isLeftMouseButton(e);
            }

            // so StackButton works when it is a selectable choice.
            private void doDispatchEvent(MouseEvent ev) {
                ev.getComponent().getParent().getParent().dispatchEvent(ev);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                doMouseExitAction();
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
            @Override
            public void mousePressed(MouseEvent ev) {
                if (isLeftClick(ev) && dispatchMouseEvent()) {
                    doDispatchEvent(ev);
                }
            }
            @Override
            public void mouseReleased(MouseEvent ev) {
                if (isLeftClick(ev)) {
                    if (dispatchMouseEvent()) {
                        doDispatchEvent(ev);
                    } else {
                        doMouseClickAction();
                    }
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                doMouseMoveAction(e.getPoint());
            }
        });

    }

    private void doMouseClickAction() {
        if (activeComponent != null && controller != null) {
            controller.showMagicCardImage(activeComponent.getCardId());
        }
    }

    private void doMouseExitAction() {
        clearActiveComponent();
        refreshTComponentHighlight();
    }

    private void clearActiveComponent() {
        if (controller != null) {
            controller.hideInfoNoDelay();
            if (activeComponent != null) {
                controller.highlightCard(activeComponent.getCardId(), false);
            }
        }
        activeComponent = null;
    }

    private void doMouseMoveAction(Point pt) {

        if (isMouseMoveBusy) {
            return;
        }

        if (activeComponent != null && activeComponent.getBounds().contains(pt)) {
            return;
        }

        isMouseMoveBusy = true;

        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        clearActiveComponent();

        for (final TComponent c : components) {
            if (c instanceof TextComponent && c.getBounds().contains(pt)) {
                activeComponent = (TextComponent) c;
                if (activeComponent.isInteractive()) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    controller.highlightCard(activeComponent.getCardId(), true);
                } else {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
                break;
            }
        }

        refreshTComponentHighlight();

        isMouseMoveBusy = false;
    }


    private void layoutTComponents() {

        int x = 0;
        int y = 0;

        for (final TComponent component : components) {

            if (component == TComponentBuilder.SPACE_COMPONENT) {
                x += SPACE_WIDTH;

            } else if (component == TComponentBuilder.BREAK_COMPONENT) {
                x = 0;
                y += LINE_HEIGHT;

            } else {
                final Dimension csize = component.getPreferredSize();
                if (component.requiresNewLine() && x + csize.width > maxWidth) {
                    x = 0;
                    y += LINE_HEIGHT;
                }
                component.setLocation(x, y + (LINE_HEIGHT - csize.height));
                x += csize.width;
            }
        }

        final Insets insets = getInsets();
        setPreferredSize(new Dimension(
            maxWidth - insets.left + insets.right,
            y + LINE_HEIGHT + insets.top + insets.bottom));
    }

    @Override
    public void paintComponent(final Graphics g) {

        if (desktopHintsMap != null) {
            ((Graphics2D) g).addRenderingHints(desktopHintsMap);
        }

        final Dimension size = getPreferredSize();
        final int cx = center ? (getWidth() - maxWidth) / 2 : 0;
        final int cy = center ? (getHeight() - size.height) / 2 : 0;

        for (final TComponent c : components) {
            if (c == activeComponent && MagicSystem.isDevMode()) {
                highlightTComponent(c, g, cx, cy);
            }
            c.paint(this, g, cx, cy);
        }
    }

    private void highlightTComponent(TComponent c, Graphics g, int cx, int cy) {
        final Rectangle r = c.getBounds();
        if (c.isInteractive()) {
            g.setColor(MagicStyle.getRolloverColor());
            g.fillRect(r.x + cx, r.y + cy, r.width, r.height);
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.drawRect(r.x + cx, r.y + cy, r.width, r.height);
        }
    }

    private void refreshTComponentHighlight() {
        if (MagicSystem.isDevMode()) {
            repaint();
        }
    }

}
