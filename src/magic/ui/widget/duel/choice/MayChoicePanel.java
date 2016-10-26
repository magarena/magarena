package magic.ui.widget.duel.choice;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import magic.data.MagicIcon;
import magic.model.MagicSource;
import magic.ui.MagicImages;
import magic.ui.screen.duel.game.SwingGameController;
import magic.translate.MText;
import magic.ui.widget.duel.viewer.UserActionPanel;
import magic.ui.FontsAndBorders;
import magic.ui.widget.message.TextLabel;

@SuppressWarnings("serial")
public class MayChoicePanel extends JPanel implements ActionListener {

    // translatable strings
    private static final String _S1 = "Yes";
    private static final String _S2 = "No";

    private static final Dimension BUTTON_DIMENSION=new Dimension(90,35);

    private final SwingGameController controller;
    private final JButton yesButton;
    private boolean yes;

    public MayChoicePanel(final SwingGameController controllerObj,final MagicSource source,final String message) {

        this.controller = controllerObj;

        setLayout(new BorderLayout());
        setOpaque(false);

        final TextLabel textLabel=new TextLabel(SwingGameController.getMessageWithSource(source,message),UserActionPanel.TEXT_WIDTH,true);
        add(textLabel,BorderLayout.CENTER);

        final JPanel buttonPanel=new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(FontsAndBorders.EMPTY_BORDER);
        add(buttonPanel,BorderLayout.SOUTH);

        yesButton=new JButton(MText.get(_S1), MagicImages.getIcon(MagicIcon.OK));
        yesButton.setPreferredSize(BUTTON_DIMENSION);
        yesButton.addActionListener(this);
        yesButton.setFocusable(false);
        buttonPanel.add(yesButton);

        yesButton.getInputMap(2).put(KeyStroke.getKeyStroke('y'),"yes");

        @SuppressWarnings("serial")
        final AbstractAction yesAction = new AbstractAction() {
            public void actionPerformed(final ActionEvent evt) {
                yes = true;
                controller.actionClicked();
            }
        };
        yesButton.getActionMap().put("yes", yesAction);


        final JButton noButton=new JButton(MText.get(_S2), MagicImages.getIcon(MagicIcon.CANCEL));
        noButton.setPreferredSize(BUTTON_DIMENSION);
        noButton.addActionListener(this);
        noButton.setFocusable(false);
        buttonPanel.add(noButton);

        noButton.getInputMap(2).put(KeyStroke.getKeyStroke('n'),"no");

        @SuppressWarnings("serial")
        final AbstractAction noAction = new AbstractAction() {
            public void actionPerformed(final ActionEvent evt) {
                yes = false;
                controller.actionClicked();
            }
        };
        noButton.getActionMap().put("no", noAction);
    }

    public boolean isYesClicked() {
        return yes;
    }
    protected void setYesClicked(final boolean b) {
        yes = b;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        yes = event.getSource() == yesButton;
        controller.actionClicked();
    }

    public SwingGameController getGameController() {
        return controller;
    }

}
