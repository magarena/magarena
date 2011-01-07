package magic.ui.viewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;

import magic.ui.GameController;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;

public class ImageBattlefieldViewer extends JPanel implements ChoiceViewer {

	private static final long serialVersionUID = 1L;
	
	private final ViewerInfo viewerInfo;
	private final boolean opponent;
	private final ImagePermanentsViewer permanentsViewer;
	private final PermanentFilter permanentFilter;
	
	public ImageBattlefieldViewer(final ViewerInfo viewerInfo,final GameController controller,final boolean opponent) {
		
		this.viewerInfo=viewerInfo;
		this.opponent=opponent;

		final Theme theme=ThemeFactory.getInstance().getCurrentTheme();
		
		controller.registerChoiceViewer(this);
		
		setOpaque(false);
		
		setLayout(new BorderLayout(6,0));
		
		final JPanel leftPanel=new JPanel(new BorderLayout());
		leftPanel.setOpaque(false);
		add(leftPanel,BorderLayout.WEST);
		
		final JLabel iconLabel=new JLabel(theme.getIcon(Theme.ICON_SMALL_BATTLEFIELD));
		iconLabel.setOpaque(true);
		iconLabel.setBackground(theme.getColor(Theme.COLOR_ICON_BACKGROUND));
		iconLabel.setPreferredSize(new Dimension(24,24));
		iconLabel.setBorder(FontsAndBorders.BLACK_BORDER);
		leftPanel.add(iconLabel,BorderLayout.NORTH);
		
		permanentsViewer=new ImagePermanentsViewer(controller);
		add(permanentsViewer,BorderLayout.CENTER);
		
		permanentFilter=new PermanentFilter(this,controller);
	}
	
	public void update() {

		permanentsViewer.viewPermanents(permanentFilter.getPermanents(viewerInfo,opponent));
	}
	
	@Override
	public void showValidChoices(final Set<Object> validChoices) {

		permanentsViewer.showValidChoices(validChoices);
	}
}