package magic.ui.viewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JLabel;
import javax.swing.JPanel;

import magic.data.IconImages;
import magic.ui.GameController;
import magic.ui.widget.FontsAndBorders;

public class ImageCombatViewer extends JPanel implements ChoiceViewer {

	private static final long serialVersionUID = 1L;

	private final ViewerInfo viewerInfo;
	private final ImagePermanentsViewer permanentsViewer;
	
	public ImageCombatViewer(final ViewerInfo viewerInfo,final GameController controller) {

		this.viewerInfo=viewerInfo;

		controller.registerChoiceViewer(this);
		
		setLayout(new BorderLayout(6,0));
		setOpaque(false);
		
		final JPanel leftPanel=new JPanel(new BorderLayout());
		leftPanel.setOpaque(false);
		add(leftPanel,BorderLayout.WEST);
		
		final JLabel combatLabel=new JLabel(IconImages.COMBAT);
		combatLabel.setOpaque(true);
		combatLabel.setPreferredSize(new Dimension(24,24));
		combatLabel.setBorder(FontsAndBorders.BLACK_BORDER);
		leftPanel.add(combatLabel,BorderLayout.NORTH);
		
		permanentsViewer=new ImagePermanentsViewer(controller);
		add(permanentsViewer,BorderLayout.CENTER);
	}
	
	public void update() {
		
		final PlayerViewerInfo attackingPlayerInfo=viewerInfo.getAttackingPlayerInfo();
		final PlayerViewerInfo defendingPlayerInfo=viewerInfo.getDefendingPlayerInfo();
		final SortedSet<PermanentViewerInfo> creatures=new TreeSet<PermanentViewerInfo>(PermanentViewerInfo.NAME_COMPARATOR);

		for (final PermanentViewerInfo permanentInfo : attackingPlayerInfo.permanents) {
			
			if (permanentInfo.attacking) {
				creatures.add(permanentInfo);
			}
		}
		
		for (final PermanentViewerInfo permanentInfo : defendingPlayerInfo.permanents) {
			
			if (permanentInfo.blocking&&permanentInfo.blockingInvalid) {
				creatures.add(permanentInfo);
			}
		}
		
		permanentsViewer.viewPermanents(creatures);
	}
	
	@Override
	public void showValidChoices(final Set<Object> validChoices) {

		permanentsViewer.showValidChoices(validChoices);
	}
}