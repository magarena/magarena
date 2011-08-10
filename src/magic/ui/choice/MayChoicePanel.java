package magic.ui.choice;

import magic.data.IconImages;
import magic.model.MagicSource;
import magic.ui.GameController;
import magic.ui.viewer.GameViewer;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TextLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MayChoicePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final Dimension BUTTON_DIMENSION=new Dimension(100,35);
	
	private final GameController controller;
	private final JButton yesButton;
	private boolean yes=false;
	
	public MayChoicePanel(final GameController controller,final MagicSource source,final String message) {
		
		this.controller=controller;
		
		setLayout(new BorderLayout());
		setOpaque(false);
		
		final TextLabel textLabel=new TextLabel(controller.getMessageWithSource(source,message),GameViewer.TEXT_WIDTH,true);
		add(textLabel,BorderLayout.CENTER);

		final JPanel buttonPanel=new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
		buttonPanel.setOpaque(false);
		buttonPanel.setBorder(FontsAndBorders.EMPTY_BORDER);
		add(buttonPanel,BorderLayout.SOUTH);
		
		yesButton=new JButton("Yes",IconImages.OK);
		yesButton.setPreferredSize(BUTTON_DIMENSION);
		yesButton.addActionListener(this);
		yesButton.setFocusable(false);
		buttonPanel.add(yesButton);
		
		final JButton noButton=new JButton("No",IconImages.CANCEL);
		noButton.setPreferredSize(BUTTON_DIMENSION);
		noButton.addActionListener(this);
		noButton.setFocusable(false);
		buttonPanel.add(noButton);
	}
	
	public boolean isYesClicked() {
		
		return yes;
	}

	@Override
	public void actionPerformed(final ActionEvent event) {
		
		yes=event.getSource()==yesButton;
		controller.actionClicked();
	}
}