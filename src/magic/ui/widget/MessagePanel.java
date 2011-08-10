package magic.ui.widget;

import magic.data.IconImages;
import magic.model.MagicMessage;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;

import javax.swing.*;
import java.awt.*;

public class MessagePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public MessagePanel(final MagicMessage message,final int maxWidth) {

		setBorder(FontsAndBorders.EMPTY_BORDER);
		setLayout(new BorderLayout(4,0));
		setOpaque(true);
		if (message.getPlayer().getIndex()==1) {
			setBackground(FontsAndBorders.GRAY2);
		}

		final JPanel leftPanel=new JPanel(new BorderLayout(2,0));
		leftPanel.setOpaque(false);
		add(leftPanel,BorderLayout.WEST);

		final Theme theme=ThemeFactory.getInstance().getCurrentTheme();
		final int face=message.getPlayer().getPlayerDefinition().getFace();		
		final JLabel playerLabel=new JLabel(theme.getAvatarIcon(face,1));
		leftPanel.add(playerLabel,BorderLayout.WEST);
		
		final int life=message.getLife();
		final JLabel lifeLabel=new JLabel(String.valueOf(Math.abs(life)));
		if (life<=0) {
			lifeLabel.setForeground(Color.RED);
		}
		lifeLabel.setPreferredSize(new Dimension(35,0));
		lifeLabel.setIcon(IconImages.REGENERATED);
		lifeLabel.setIconTextGap(2);
		leftPanel.add(lifeLabel,BorderLayout.EAST);
		
		final TextLabel textLabel=new TextLabel(message.getText(),maxWidth,false);
		textLabel.setColors(Color.BLACK,Color.BLUE);
		add(textLabel,BorderLayout.CENTER);
						
		final JPanel gamePanel=new JPanel(new BorderLayout());
		gamePanel.setOpaque(false);
		add(gamePanel,BorderLayout.EAST);

		final JLabel turnLabel=new JLabel("Turn "+message.getTurn());
		turnLabel.setFont(FontsAndBorders.FONT1);
		turnLabel.setHorizontalAlignment(JLabel.RIGHT);
		gamePanel.add(turnLabel,BorderLayout.NORTH);
		
		final JLabel phaseLabel=new JLabel(message.getPhaseType().getName());
		phaseLabel.setFont(FontsAndBorders.FONT0);
		phaseLabel.setHorizontalAlignment(JLabel.RIGHT);
		gamePanel.add(phaseLabel,BorderLayout.SOUTH);				
	}
}