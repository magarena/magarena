package magic.ui.choice;

import magic.model.MagicSource;
import magic.ui.GameController;
import magic.ui.duel.viewer.GameViewer;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TextLabel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ModeChoicePanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final String MESSAGE="Choose the mode.";
    private static final Dimension BUTTON_DIMENSION=new Dimension(70,25);

    private final GameController controller;
    private final List<Integer> modes;
    private int result;

    public ModeChoicePanel(final GameController aController, final MagicSource source, final List<Integer> aModes) {
        controller = aController;
        modes = aModes;

        setLayout(new BorderLayout());
        setOpaque(false);

        final TextLabel textLabel=new TextLabel(GameController.getMessageWithSource(source,MESSAGE),GameViewer.TEXT_WIDTH,true);
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
