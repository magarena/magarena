package magic.ui.choice;

import magic.model.MagicSource;
import magic.model.choice.MagicPlayChoiceResult;
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

public class PlayChoicePanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final String MESSAGE="Choose which ability to play.";
    private static final Dimension BUTTON_DIMENSION=new Dimension(70,25);

    private final GameController controller;
    private final List<MagicPlayChoiceResult> results;
    private MagicPlayChoiceResult result;

    public PlayChoicePanel(
            final GameController controller,
            final MagicSource source,
            final List<MagicPlayChoiceResult> results) {

        this.controller=controller;
        this.results=results;

        setLayout(new BorderLayout());
        setOpaque(false);

        final TextLabel textLabel=new TextLabel(GameController.getMessageWithSource(source,MESSAGE),GameViewer.TEXT_WIDTH,true);
        add(textLabel,BorderLayout.NORTH);

        final JPanel buttonPanel=new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        buttonPanel.setBorder(FontsAndBorders.EMPTY_BORDER);
        buttonPanel.setOpaque(false);
        add(buttonPanel,BorderLayout.CENTER);

        for (int index=0;index<results.size();index++) {
            final JButton button=new JButton(results.get(index).getText());
            button.setPreferredSize(BUTTON_DIMENSION);
            button.setBorder(BorderFactory.createLineBorder(FontsAndBorders.GRAY4));
            button.setActionCommand(Integer.toString(index));
            button.addActionListener(this);
            button.setFocusable(false);
            buttonPanel.add(button);
        }
    }

    public MagicPlayChoiceResult getResult() {
        return result;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        result=results.get(Integer.parseInt(event.getActionCommand()));
        controller.actionClicked();
    }
}
