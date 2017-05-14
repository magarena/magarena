package magic.ui.widget.duel.sidebar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.ui.FontsAndBorders;
import magic.ui.IChoiceViewer;
import magic.ui.duel.viewerinfo.StackItemViewerInfo;
import magic.ui.screen.duel.game.SwingGameController;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.PanelButton;
import magic.ui.widget.message.TextLabel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class StackButton extends PanelButton implements IChoiceViewer {

    private final StackItemViewerInfo stackInfo;
    private final SwingGameController controller;

    StackButton(SwingGameController aController, StackItemViewerInfo stackItem, int maxWidth, int itemNum) {

        this.controller = aController;
        this.stackInfo = stackItem;

        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(FontsAndBorders.getPlayerBorder(stackItem.isControllerMainPlayer));
        panel.setLayout(new BorderLayout(0, 0));
        setComponent(panel);

        final JLabel sourceLabel = new JLabel(stackItem.name);
        sourceLabel.setIcon(stackItem.icon);
        sourceLabel.setFont(sourceLabel.getFont().deriveFont(Font.BOLD | Font.ITALIC));

        final JLabel itemLabel = new JLabel("#" + Integer.toString(itemNum));
        itemLabel.setFont(sourceLabel.getFont().deriveFont(Font.BOLD | Font.ITALIC));

        final JPanel headerPanel = new JPanel(new MigLayout("insets 0", "[fill, grow][]4"));
        headerPanel.setOpaque(false);
        headerPanel.add(sourceLabel);
        headerPanel.add(itemLabel);

        panel.add(headerPanel, BorderLayout.NORTH);

        final TextLabel textLabel = new TextLabel(
            stackItem.description,
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
