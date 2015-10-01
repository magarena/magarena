package magic.ui.duel.textmode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;
import magic.ui.SwingGameController;
import magic.ui.IChoiceViewer;
import magic.ui.duel.viewer.PermanentViewerInfo;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;

class PermanentPanel extends JPanel implements IChoiceViewer {

    private static final long serialVersionUID = 1L;

    private final PermanentButton button;
    private final List<PermanentButton> linkedButtons;

    PermanentPanel(final PermanentViewerInfo permanentInfo, final SwingGameController controller, final Border border, final int maxWidth) {

        setBorder(FontsAndBorders.SMALL_EMPTY_BORDER);
        setLayout(new BorderLayout());

        button=new PermanentButton(permanentInfo,controller,border,maxWidth);
        add(button,BorderLayout.NORTH);

        linkedButtons=new ArrayList<>();
        final SortedSet<PermanentViewerInfo> linked=permanentInfo.linked;
        if (!linked.isEmpty()) {
            final Color attachedColor=ThemeFactory.getInstance().getCurrentTheme().getColor(Theme.COLOR_SEPARATOR_BACKGROUND);
            final Border attachedBorder=BorderFactory.createMatteBorder(0,10,0,0,attachedColor);
            final JPanel attachedPanel=new JPanel();
            attachedPanel.setLayout(new BoxLayout(attachedPanel,BoxLayout.Y_AXIS));
            attachedPanel.setBorder(attachedBorder);
            for (final PermanentViewerInfo linkedPermanentInfo : linked) {

                final PermanentButton linkedButton=new PermanentButton(
                        linkedPermanentInfo,
                        controller,
                        BorderFactory.createEmptyBorder(),
                        maxWidth-10);
                linkedButtons.add(linkedButton);
                attachedPanel.add(linkedButton);
            }
            add(attachedPanel,BorderLayout.CENTER);
        }
    }

    @Override
    public void showValidChoices(final Set<?> validChoices) {
        button.showValidChoices(validChoices);
        for (final PermanentButton linkedButton : linkedButtons) {
            linkedButton.showValidChoices(validChoices);
        }
    }
}
