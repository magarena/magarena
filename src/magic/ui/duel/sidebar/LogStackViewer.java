package magic.ui.duel.sidebar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.data.MagicIcon;
import magic.translate.UiString;
import magic.ui.IconImages;
import magic.ui.ScreenController;
import magic.ui.message.MessageStyle;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.widget.ActionButtonTitleBar;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class LogStackViewer extends TexturedPanel {

    public static final Font MESSAGE_FONT = FontsAndBorders.FONT1.deriveFont(Font.PLAIN);
    public static final Color CHOICE_COLOR = Color.RED.darker();

    // translatable strings
    private static final String _S1 = "View log file";
    private static final String _S2 = "Shows the complete game log that is written to file.";
    private static final String _S4 = "Log";
    private static final String _S5 = "Stack";

    private final LogViewer logViewer;
    private final StackViewer stackViewer;
    private final ActionButtonTitleBar logTitleBar;
    private final ActionButtonTitleBar stackTitleBar;
    private MessageStyle messageStyle = MessageStyle.PLAIN;

    LogStackViewer(LogViewer aLogBookViewer, StackViewer aStackViewer) {
        
        this.logViewer = aLogBookViewer;
        this.stackViewer = aStackViewer;

        logTitleBar = new ActionButtonTitleBar(UiString.get(_S4), getLogActionButtons());
        stackTitleBar = new ActionButtonTitleBar(UiString.get(_S5), getStackActionButtons());

        setOpaque(false);
        setBorders();

        setLayout(new MigLayout("insets 0, gap 0, flowy", "[fill, grow]", "[][shrinkprio 200][][]"));
        setLogVisible(isLogVisible());
    }

    private JButton getLogFileActionButton() {
        return new ActionBarButton(
            IconImages.getIcon(MagicIcon.LOG_FILE),
            UiString.get(_S1),
            UiString.get(_S2),
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
            IconImages.getIcon(aIcon),
            null, null,
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switchLogVisibility();
                }
            }
        );
    }

    private JButton getMessageStyleActionButton() {
        return new ActionBarButton(
            IconImages.getIcon(MagicIcon.MARKER_ICON),
            "Cycle message style",
            "Click to cycle through various styles for the log/stack messages.",
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    messageStyle = messageStyle.getNext();
                    logViewer.setMessageStyle(messageStyle);
                }
            }
        );
    }

    private List<JButton> getLogActionButtons() {
        final List<JButton> btns = new ArrayList<>();
        btns.add(getMessageStyleActionButton());
        btns.add(getLogFileActionButton());
        btns.add(getLogViewActionButton(MagicIcon.DOWNARROW_ICON));
        return btns;
    }

    private List<JButton> getStackActionButtons() {
        final List<JButton> btns = new ArrayList<>();
        btns.add(getLogFileActionButton());
        btns.add(getLogViewActionButton(MagicIcon.UPARROW_ICON));
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

    private void switchLogVisibility() {
        final boolean isLogVisible = !isLogVisible();
        GeneralConfig.getInstance().setLogMessagesVisible(isLogVisible);
        setLogVisible(isLogVisible);
        GeneralConfig.getInstance().save();        
    }

}
