package magic.ui.duel.choice;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import magic.data.MagicIcon;
import magic.model.IGameController;
import magic.model.MagicSource;
import magic.ui.MagicImages;
import magic.ui.screen.duel.game.SwingGameController;
import magic.translate.UiString;
import magic.ui.widget.duel.viewer.UserActionPanel;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.message.TextLabel;

@SuppressWarnings("serial")
public class ManaCostXChoicePanel extends JPanel implements ActionListener {

    // translatable strings
    private static final String _S1 = "Choose a value for X.";

    private static final String MESSAGE = UiString.get(_S1);
    private static final Dimension BUTTON_DIMENSION=new Dimension(50,35);

    private final SwingGameController controller;
    private final JButton leftButton;
    private final JButton numberButton;
    private final JButton rightButton;
    private final int maximumX;
    private int x;

    public ManaCostXChoicePanel(final IGameController controller,final MagicSource source,final int maximumX) {
        this.controller = (SwingGameController)controller;
        this.maximumX=maximumX;
        x=maximumX;

        setLayout(new BorderLayout());
        setOpaque(false);

        final TextLabel textLabel=new TextLabel(SwingGameController.getMessageWithSource(source,MESSAGE),UserActionPanel.TEXT_WIDTH,true);
        add(textLabel,BorderLayout.CENTER);

        final JPanel buttonPanel=new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(FontsAndBorders.EMPTY_BORDER);
        add(buttonPanel,BorderLayout.SOUTH);

        leftButton=new JButton("",MagicImages.getIcon(MagicIcon.LEFT));
        leftButton.setPreferredSize(BUTTON_DIMENSION);
        leftButton.addActionListener(this);
        leftButton.setFocusable(false);
        buttonPanel.add(leftButton);

        numberButton=new JButton(Integer.toString(x));
        numberButton.setPreferredSize(BUTTON_DIMENSION);
        numberButton.addActionListener(this);
        numberButton.setFocusable(false);
        buttonPanel.add(numberButton);

        rightButton=new JButton(MagicImages.getIcon(MagicIcon.RIGHT));
        rightButton.setPreferredSize(BUTTON_DIMENSION);
        rightButton.addActionListener(this);
        rightButton.setFocusable(false);
        buttonPanel.add(rightButton);
    }

    public int getValueForX() {
        return x;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final Object source=event.getSource();
        if (source==leftButton) {
            if (x>1) {
                x--;
                numberButton.setText(Integer.toString(x));
                numberButton.repaint();
            }
        } else if (source==rightButton) {
            if (x<maximumX) {
                x++;
                numberButton.setText(Integer.toString(x));
                numberButton.repaint();
            }
        } else {
            controller.actionClicked();
        }
    }
}
