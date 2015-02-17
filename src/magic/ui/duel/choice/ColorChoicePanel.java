package magic.ui.duel.choice;

import magic.ui.IconImages;
import magic.model.MagicColor;
import magic.model.MagicSource;
import magic.ui.SwingGameController;
import magic.ui.duel.viewer.UserActionPanel;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TextLabel;

import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import magic.model.IGameController;

public class ColorChoicePanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final String MESSAGE="Choose a color.";
    private static final Dimension BUTTON_DIMENSION=new Dimension(35,35);

    private final SwingGameController controller;
    private MagicColor color;

    public ColorChoicePanel(final IGameController controller,final MagicSource source) {
        this.controller = (SwingGameController)controller;

        setLayout(new BorderLayout());
        setOpaque(false);

        final TextLabel textLabel=new TextLabel(SwingGameController.getMessageWithSource(source,MESSAGE),UserActionPanel.TEXT_WIDTH,true);
        add(textLabel,BorderLayout.CENTER);

        final JPanel buttonPanel=new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(FontsAndBorders.EMPTY_BORDER);
        add(buttonPanel,BorderLayout.SOUTH);

        for (final MagicColor color : MagicColor.values()) {

            final JButton button=new JButton(IconImages.getIcon(color));
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
