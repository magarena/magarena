package magic.ui.widget.duel.sidebar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.data.MagicIcon;
import magic.model.IUIGameController;
import magic.translate.MText;
import magic.ui.FontsAndBorders;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.helpers.ImageHelper;
import magic.ui.helpers.MouseHelper;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.DialButton;
import magic.ui.widget.ActionButtonTitleBar;
import magic.ui.widget.message.MessageStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class LogStackViewer extends JPanel {

    private static final Icon FFORWARD_ICON = ImageHelper.getRecoloredIcon(
        MagicIcon.FAST_FORWARD, Color.black, Color.white
    );

    public static final Font MESSAGE_FONT = FontsAndBorders.FONT1.deriveFont(Font.PLAIN);
    public static final Color CHOICE_COLOR = Color.RED.darker();

    // translatable strings
    private static final String _S1 = "View log file [L]";
    private static final String _S2 = "Shows the complete game log that is written to file.";
    private static final String _S4 = "Log";
    private static final String _S5 = "Stack";
    private static final String _S6 = "Cycle message style";
    private static final String _S7 = "Click to cycle through various styles for the log/stack messages.";
    private static final String _S8 = "Keywords glossary [K]";
    private static final String _S9 = "Quick reference...";
    private static final String _S10 = "Hide/show log [M]";
    private static final String _S11 = "Click to fast-forward stack.";

    private final LogViewer logViewer;
    private final StackViewer stackViewer;
    private final ActionButtonTitleBar logTitleBar;
    private final ActionButtonTitleBar stackTitleBar;
    private MessageStyle messageStyle = GeneralConfig.getInstance().getLogMessageStyle();
    private int stackCount = 0;
    private final IUIGameController controller;

    LogStackViewer(LogViewer aLogBookViewer, StackViewer aStackViewer, IUIGameController controller) {

        this.logViewer = aLogBookViewer;
        this.stackViewer = aStackViewer;
        this.controller = controller;

        logTitleBar = new ActionButtonTitleBar(MText.get(_S4), getLogActionButtons());
        stackTitleBar = new ActionButtonTitleBar(MText.get(_S5), getStackActionButtons());
        stackTitleBar.getLabel().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (stackCount > 0) {
                    controller.setStackFastForward(true);
                    MouseHelper.showDefaultCursor(stackTitleBar);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (stackCount > 0 && !controller.isStackFastForward()) {
                    MouseHelper.showHandCursor(stackTitleBar);
                } else {
                    MouseHelper.showDefaultCursor(stackTitleBar);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                MouseHelper.showDefaultCursor(stackTitleBar);
            }
        });

        setOpaque(false);
        setBorders();

        setLayout(new MigLayout("insets 0, gap 0, flowy", "[fill, grow]", "[][shrinkprio 200][][]"));
        setLogVisible(isLogVisible());
    }

    private JButton getLogFileActionButton() {
        return new ActionBarButton(
            MagicImages.getIcon(MagicIcon.LOG_FILE),
            MText.get(_S1),
            MText.get(_S2),
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ScreenController.showGameLogScreen();
                }
            }
        );
    }

    private JButton getLogViewActionButton(MagicIcon aIcon) {
        return new ActionBarButton(
            MagicImages.getIcon(aIcon),
            MText.get(_S10),
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switchLogVisibility();
                }
            }
        );
    }

    private JButton getMessageStyleActionButton() {
        return new DialButton(
            MessageStyle.values().length,
            messageStyle.ordinal(),
            MText.get(_S6),
            MText.get(_S7),
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    messageStyle = messageStyle.getNext();
                    logViewer.setMessageStyle(messageStyle);
                    stackViewer.update();
                }
            }
        );
    }

    private JButton getKeywordsActionButton() {
        return new ActionBarButton(
            MagicImages.getIcon(MagicIcon.KEY),
            MText.get(MText.get(_S8)),
            MText.get(MText.get(_S9)),
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ScreenController.showKeywordsScreen();
                }
            }
        );
    }


    private List<JButton> getLogActionButtons() {
        final List<JButton> btns = new ArrayList<>();
        btns.add(getKeywordsActionButton());
        btns.add(getLogFileActionButton());
        btns.add(getMessageStyleActionButton());
        btns.add(getLogViewActionButton(MagicIcon.ARROW_DOWN));
        for (JButton btn : btns) {
            btn.setFocusable(false);
        }
        return btns;
    }

    private List<JButton> getStackActionButtons() {
        final List<JButton> btns = new ArrayList<>();
        btns.add(getKeywordsActionButton());
        btns.add(getLogFileActionButton());
        btns.add(getMessageStyleActionButton());
        btns.add(getLogViewActionButton(MagicIcon.ARROW_UP));
        for (JButton btn : btns) {
            btn.setFocusable(false);
        }
        return btns;
    }

    private void doUpdateLayout() {
        removeAll();
        add(logTitleBar, "hidemode 1");
        add(logViewer, (isLogVisible() ? "h 60:100%" : "h 0:100%") + ", hidemode 0");
        add(stackTitleBar);
        add(stackViewer);
        revalidate();
    }

    Rectangle getStackViewerRectangle(Component canvas) {
        final Point pointOnCanvas = SwingUtilities.convertPoint(this, stackTitleBar.getLocation(), canvas);
        return new Rectangle(pointOnCanvas.x, pointOnCanvas.y, stackTitleBar.getWidth(), stackTitleBar.getHeight());
    }

    private void setBorders() {
        setBorder(null);
        logTitleBar.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        logViewer.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.BLACK));
        stackTitleBar.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        stackViewer.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.BLACK));
    }

    private void setLogVisible(boolean isLogVisible) {
        logTitleBar.setActionsVisible(isLogVisible);
        stackTitleBar.setActionsVisible(!isLogVisible);
        logTitleBar.setVisible(isLogVisible);
        logViewer.setVisible(isLogVisible);
        doUpdateLayout();
    }

    private boolean isLogVisible() {
        return GeneralConfig.getInstance().isLogMessagesVisible();
    }

    public void switchLogVisibility() {
        final boolean isLogVisible = !isLogVisible();
        GeneralConfig.getInstance().setLogMessagesVisible(isLogVisible);
        setLogVisible(isLogVisible);
        GeneralConfig.getInstance().save();
    }

    public void setStackCount(int newCount) {
        if (newCount == 0 || controller.isStackFastForward()) {
            final JLabel lbl = stackTitleBar.getLabel();
            lbl.setText(MText.get(_S5) + " : " + newCount);
            lbl.setIcon(null);
            lbl.setToolTipText(null);
        } else {
            final JLabel lbl = stackTitleBar.getLabel();
            lbl.setText(MText.get(_S5) + " : " + newCount + " ");
            lbl.setIcon(FFORWARD_ICON);
            lbl.setToolTipText(MText.get(_S11));
            lbl.setHorizontalTextPosition(SwingConstants.LEADING);
        }
        if (stackCount > 0 && newCount == 0) {
            controller.setStackFastForward(false);
        }
        stackCount = newCount;
    }
}
