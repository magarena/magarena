package magic.ui.widget;

import magic.model.MagicPlayerDefinition;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class PlayerAvatarPanel extends TexturedPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static final Border NORMAL_BORDER=BorderFactory.createEmptyBorder(6,6,6,6);	
	
	private final int index;
	private final JLabel faceLabel;
	private final TitleBar titleBar;
	private MagicPlayerDefinition playerDefinition;
	private boolean small;
	private Border selectedBorder;
	
	public PlayerAvatarPanel(final int index) {

		this.index=index;		
		this.setLayout(new BorderLayout());
		faceLabel=new JLabel();
		add(faceLabel,BorderLayout.CENTER);
		titleBar=new TitleBar("");
		titleBar.setHorizontalAlignment(JLabel.CENTER);
		add(titleBar,BorderLayout.SOUTH);
		setSelected(false);
		small=false;

		final Theme theme=ThemeFactory.getInstance().getCurrentTheme();
		selectedBorder=BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(2,2,2,2),
				BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(theme.getColor(Theme.COLOR_SELECTED_PLAYER),2),
						BorderFactory.createEmptyBorder(2,2,2,2)
					)
				);
	}
	
	public void setPlayerDefinition(final MagicPlayerDefinition playerDefinition) {
		
		this.playerDefinition=playerDefinition;
		update();
	}
	
	public MagicPlayerDefinition getPlayerDefinition() {
		
		return playerDefinition;
	}
	
	public int getIndex() {
		
		return index;
	}
	
	public void setSmall(final boolean small) {

		if (this.small!=small) {
			this.small=small;
			update();
		}
	}
	
	public void setSelected(final boolean selected) {

		this.setBorder(selected?selectedBorder:NORMAL_BORDER);
	}	
	
	public void update() {

		final Theme theme=ThemeFactory.getInstance().getCurrentTheme();
		final ImageIcon faceIcon=theme.getAvatarIcon(playerDefinition.getFace(),small?2:3);
		faceLabel.setIcon(faceIcon);
		titleBar.setText(playerDefinition.getName());
		if (small) {
			titleBar.setVisible(false);
			setPreferredSize(new Dimension(72,80));
		} else {
			new JLabel(faceIcon);
			titleBar.setVisible(true);
			setPreferredSize(new Dimension(132,150));
		}
		revalidate();		
	}
}