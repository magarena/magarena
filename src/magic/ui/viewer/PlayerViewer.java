package magic.ui.viewer;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import magic.data.IconImages;
import magic.model.MagicPlayer;
import magic.ui.GameController;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.PanelButton;
import magic.ui.widget.PlayerAvatarPanel;
import magic.ui.widget.TexturedPanel;

public class PlayerViewer extends JPanel implements ChoiceViewer {

	private static final long serialVersionUID = 1L;

	private static final ImageIcon ICONS[]={
		IconImages.LIFE,IconImages.PREVENT2,IconImages.LAND2,
		IconImages.HAND2,IconImages.LIBRARY2,IconImages.GRAVEYARD2,
	};
	
	private final ViewerInfo viewerInfo;
	private final GameController controller;
	private final boolean opponent;
	private final PlayerAvatarPanel avatarPanel;
	private final JLabel labels[];
	private final JPanel labelsPanel;
	
	public PlayerViewer(final ViewerInfo viewerInfo,final GameController controller,final boolean opponent) {
		
		this.viewerInfo=viewerInfo;
		this.controller=controller;
		this.opponent=opponent;
		
		controller.registerChoiceViewer(this);
		
		setLayout(new BorderLayout());
		
		final PanelButton avatarButton=new PanelButton() {

			private static final long serialVersionUID = 1L;

			@Override
			public void mouseClicked() {

				final MagicPlayer player=viewerInfo.getPlayerInfo(opponent).player;
				PlayerViewer.this.controller.processClick(player);
			}
		};

		avatarPanel=new PlayerAvatarPanel(0);
		avatarButton.setComponent(avatarPanel);
		add(avatarButton,BorderLayout.WEST);
		
		labelsPanel=new TexturedPanel();
		labelsPanel.setBorder(FontsAndBorders.BLACK_BORDER);
		add(labelsPanel,BorderLayout.CENTER);

		labels=new JLabel[6];
		for (int index=0;index<labels.length;index++) {

			labels[index]=new JLabel("0");
			labels[index].setFont(FontsAndBorders.FONT2);
			labels[index].setIconTextGap(4);
			labels[index].setHorizontalAlignment(JLabel.CENTER);
			labels[index].setIcon(ICONS[index]);
		}
		
		setSmall(false);
		update();
	}
	
	public void setSmall(boolean small) {

		labelsPanel.removeAll();
		avatarPanel.setSmall(small);

		if (small) {
			labelsPanel.setLayout(new GridLayout(2,3));
		} else {
			labelsPanel.setLayout(new GridLayout(3,2));
		}
		
		for (int index=0;index<labels.length;index++) {
			
			labelsPanel.add(labels[index]);			
		}		
		
		showValidChoices(controller.getValidChoices());
		revalidate();
	}
	
	public void update() {
		
		final PlayerViewerInfo playerInfo=viewerInfo.getPlayerInfo(opponent);
		avatarPanel.setPlayerDefinition(playerInfo.player.getPlayerDefinition());
		
		labels[0].setText(""+playerInfo.life);
		labels[1].setText(""+playerInfo.preventDamage);
		labels[2].setText(""+playerInfo.lands);
		labels[3].setText(""+playerInfo.hand.size());
		labels[4].setText(""+playerInfo.library.size());
		labels[5].setText(""+playerInfo.graveyard.size());
		
		avatarPanel.setSelected(playerInfo.turn);
	}
	
	@Override
	public void showValidChoices(final Set<Object> validChoices) {

		final MagicPlayer player=viewerInfo.getPlayerInfo(opponent).player;		
		if (validChoices.contains(player)) {
			avatarPanel.setBackground(FontsAndBorders.TARGET_FOREGROUND);
			avatarPanel.setOpaque(true);
		} else {
			avatarPanel.setBackground(null);
			avatarPanel.setOpaque(false);
		}
		avatarPanel.repaint();
	}	
}