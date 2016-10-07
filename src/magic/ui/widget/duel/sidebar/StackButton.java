package magic.ui.widget.duel.sidebar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.ui.screen.duel.game.SwingGameController;
import magic.ui.IChoiceViewer;
import magic.ui.duel.viewerinfo.StackViewerInfo;
import magic.ui.theme.ThemeFactory;
import magic.ui.FontsAndBorders;
import magic.ui.widget.PanelButton;
import magic.ui.widget.message.TextLabel;

@SuppressWarnings("serial")
class StackButton extends PanelButton implements IChoiceViewer {

    private final StackViewerInfo stackInfo;
    private final SwingGameController controller;

    StackButton(SwingGameController aController, final StackViewerInfo stackInfo, final int maxWidth) {

        this.controller = aController;
        this.stackInfo = stackInfo;

        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(FontsAndBorders.getPlayerBorder(stackInfo.visible));
        panel.setLayout(new BorderLayout(0, 0));
        setComponent(panel);

        final JLabel sourceLabel = new JLabel(stackInfo.name);
        sourceLabel.setIcon(stackInfo.icon);
        sourceLabel.setFont(sourceLabel.getFont().deriveFont(Font.BOLD | Font.ITALIC));
        panel.add(sourceLabel, BorderLayout.NORTH);

        final TextLabel textLabel = new TextLabel(
            stackInfo.description,
            LogStackViewer.MESSAGE_FONT,
            maxWidth,
            false,
            LogStackViewer.CHOICE_COLOR,
            aController
        );
        panel.add(textLabel, BorderLayout.CENTER);

        setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);
    }

    private void doShowCardImage() {
        final Rectangle rect = new Rectangle(
            getParent().getLocationOnScreen().x,
            getLocationOnScreen().y,
            getParent().getWidth(),
            getHeight());
        controller.viewInfoRight(stackInfo.cardDefinition, 0, rect);
    }

    @Override
    public void mouseClicked() {
        controller.processClick(stackInfo.itemOnStack);
    }

    @Override
    public void mouseEntered() {
        doShowCardImage();
    }

    @Override
    public void mouseExited() {
        controller.hideInfo();
    }

    @Override
    public void showValidChoices(final Set<?> validChoices) {
        setValid(validChoices.contains(stackInfo.itemOnStack));
    }

    @Override
    public Color getValidColor() {
        return ThemeFactory.getInstance().getCurrentTheme().getChoiceColor();
    }
}
