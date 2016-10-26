package magic.ui.widget.duel.choice;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import magic.model.MagicColor;
import magic.model.MagicSource;
import magic.ui.MagicImages;
import magic.ui.screen.duel.game.SwingGameController;
import magic.translate.MText;
import magic.ui.widget.duel.viewer.UserActionPanel;
import magic.ui.FontsAndBorders;
import magic.ui.widget.message.TextLabel;

@SuppressWarnings("serial")
public class ColorChoicePanel extends JPanel implements ActionListener {

    // translatable strings
    private static final String _S1 = "Choose a color.";

    private static final String MESSAGE = MText.get(_S1);
    private static final Dimension BUTTON_DIMENSION=new Dimension(35,35);

    private final SwingGameController controller;
    private MagicColor color;

    public ColorChoicePanel(final SwingGameController controller,final MagicSource source) {
        this.controller = controller;

        setLayout(new BorderLayout());
        setOpaque(false);

        final TextLabel textLabel=new TextLabel(SwingGameController.getMessageWithSource(source,MESSAGE),UserActionPanel.TEXT_WIDTH,true);
        add(textLabel,BorderLayout.CENTER);

        final JPanel buttonPanel=new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(FontsAndBorders.EMPTY_BORDER);
        add(buttonPanel,BorderLayout.SOUTH);

        for (final MagicColor color : MagicColor.values()) {

            final JButton button=new JButton(MagicImages.getIcon(color));
            button.setActionCommand(Character.toString(color.getSymbol()));
            button.setPreferredSize(BUTTON_DIMENSION);
            button.addActionListener(this);
            button.setFocusable(false);
            buttonPanel.add(button);
        }
    }

    public MagicColor getColor() {
        return color;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        color=MagicColor.getColor(event.getActionCommand().charAt(0));
        controller.actionClicked();
    }
}
