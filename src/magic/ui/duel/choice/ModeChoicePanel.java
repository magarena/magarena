package magic.ui.duel.choice;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import magic.model.IGameController;
import magic.model.MagicSource;
import magic.ui.screen.duel.game.SwingGameController;
import magic.translate.UiString;
import magic.ui.widget.duel.viewer.UserActionPanel;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.message.TextLabel;

@SuppressWarnings("serial")
public class ModeChoicePanel extends JPanel implements ActionListener {

    // translatable strings
    private static final String _S1 = "Choose the mode.";

    private static final String MESSAGE = UiString.get(_S1);
    private static final Dimension BUTTON_DIMENSION=new Dimension(70,25);

    private final SwingGameController controller;
    private final List<Integer> modes;
    private int result;

    public ModeChoicePanel(final IGameController aController, final MagicSource source, final List<Integer> aModes) {
        controller = (SwingGameController) aController;
        modes = aModes;

        setLayout(new BorderLayout());
        setOpaque(false);

        final TextLabel textLabel=new TextLabel(SwingGameController.getMessageWithSource(source,MESSAGE),UserActionPanel.TEXT_WIDTH,true);
        add(textLabel,BorderLayout.NORTH);

        final JPanel buttonPanel=new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        buttonPanel.setBorder(FontsAndBorders.EMPTY_BORDER);
        buttonPanel.setOpaque(false);
        add(buttonPanel,BorderLayout.CENTER);

        for (int index=0; index < modes.size(); index++) {
            final JButton button=new JButton(Integer.toString(modes.get(index)));
            button.setPreferredSize(BUTTON_DIMENSION);
            button.setBorder(BorderFactory.createLineBorder(FontsAndBorders.GRAY4));
            button.setActionCommand(Integer.toString(index));
            button.addActionListener(this);
            button.setFocusable(false);
            buttonPanel.add(button);
        }
    }

    public int getMode() {
        return result;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        result = modes.get(Integer.parseInt(event.getActionCommand()));
        controller.actionClicked();
    }
}
