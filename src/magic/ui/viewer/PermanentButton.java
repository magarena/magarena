package magic.ui.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import magic.ui.GameController;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.PanelButton;
import magic.ui.widget.TextLabel;

public class PermanentButton extends PanelButton implements ChoiceViewer {

	private static final long serialVersionUID = 1L;

	private final PermanentViewerInfo permanentInfo;
	private final GameController controller;
	private final JLabel nameLabel;
	private final JLabel ptLabel;
	private final TextLabel textLabel;

	public PermanentButton(final PermanentViewerInfo permanentInfo,final GameController controller,final Border border,final int maxWidth) {
		
		this.permanentInfo=permanentInfo;
		this.controller=controller;
		
		final JPanel panel=new JPanel();
		panel.setLayout(new BorderLayout(0,2));
		panel.setBorder(border);
		panel.setOpaque(false);
		
		final JPanel topPanel=new JPanel();
		topPanel.setOpaque(false);
		topPanel.setLayout(new BorderLayout());
		panel.add(topPanel,BorderLayout.NORTH);

		nameLabel=new JLabel(permanentInfo.name);
		nameLabel.setIcon(permanentInfo.icon);
		topPanel.add(nameLabel,BorderLayout.CENTER);
		
		ptLabel=new JLabel("");
		if (!permanentInfo.powerToughness.isEmpty()) {
			ptLabel.setText(permanentInfo.powerToughness);
			topPanel.add(ptLabel,BorderLayout.EAST);
		}
		
		textLabel=new TextLabel(permanentInfo.text,maxWidth-6,false);
		panel.add(textLabel,BorderLayout.CENTER);
					
		setComponent(panel);
		showValidChoices(controller.getValidChoices());
	}

	@Override
	public void mouseClicked() {

		controller.processClick(permanentInfo.permanent);
	}

	@Override
	public void mouseEntered() {

		controller.viewCard(permanentInfo.cardDefinition,permanentInfo.index);
	}
	
	@Override
	public void showValidChoices(final Set<Object> validChoices) {
		
		if (validChoices.contains(permanentInfo.permanent)) {	
			final Color targetColor=controller.isCombatChoice()?FontsAndBorders.COMBAT_TARGET_FOREGROUND:FontsAndBorders.TARGET_FOREGROUND; 
			nameLabel.setForeground(targetColor);
			ptLabel.setForeground(targetColor);
			textLabel.setForeground(targetColor);
		} else {
			nameLabel.setForeground(Color.BLACK);
			ptLabel.setForeground(Color.BLACK);
			textLabel.setForeground(Color.BLACK);
		}
		repaint();
	}	
}