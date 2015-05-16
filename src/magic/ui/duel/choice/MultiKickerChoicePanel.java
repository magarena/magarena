package magic.ui.duel.choice;

import magic.ui.IconImages;
import magic.model.MagicManaCost;
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
import magic.data.MagicIcon;
import magic.model.IGameController;

public class MultiKickerChoicePanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final Dimension BUTTON_DIMENSION=new Dimension(50,35);

    private final SwingGameController controller;
    private final JButton leftButton;
    private final JButton numberButton;
    private final JButton rightButton;
    private final int maximumCount;
    private int count;

    public MultiKickerChoicePanel(final IGameController controller, final MagicSource source, final MagicManaCost cost, final int maximumCount, final String name) {

        this.controller=(SwingGameController) controller;
        this.maximumCount=maximumCount;
        count=maximumCount;

        setLayout(new BorderLayout());
        setOpaque(false);

        final String message = "Choose how many times to pay the " + name + " cost of " + cost.getText() + ".";
        final TextLabel textLabel=new TextLabel(SwingGameController.getMessageWithSource(source,message),UserActionPanel.TEXT_WIDTH,true);
        add(textLabel,BorderLayout.CENTER);

        final JPanel buttonPanel=new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(FontsAndBorders.EMPTY_BORDER);
        add(buttonPanel,BorderLayout.SOUTH);

        leftButton=new JButton("",IconImages.getIcon(MagicIcon.LEFT));
        leftButton.setPreferredSize(BUTTON_DIMENSION);
        leftButton.addActionListener(this);
        leftButton.setFocusable(false);
        buttonPanel.add(leftButton);

        numberButton=new JButton(Integer.toString(count));
        numberButton.setPreferredSize(BUTTON_DIMENSION);
        numberButton.addActionListener(this);
        numberButton.setFocusable(false);
        buttonPanel.add(numberButton);

        rightButton=new JButton(IconImages.getIcon(MagicIcon.RIGHT));
        rightButton.setPreferredSize(BUTTON_DIMENSION);
        rightButton.addActionListener(this);
        rightButton.setFocusable(false);
        buttonPanel.add(rightButton);
    }

    public int getKicker() {
        return count;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final Object source=event.getSource();
        if (source==leftButton) {
            if (count>0) {
                count--;
                numberButton.setText(Integer.toString(count));
                numberButton.repaint();
            }
        } else if (source==rightButton) {
            if (count<maximumCount) {
                count++;
                numberButton.setText(Integer.toString(count));
                numberButton.repaint();
            }
        } else {
            controller.actionClicked();
        }
    }
}
