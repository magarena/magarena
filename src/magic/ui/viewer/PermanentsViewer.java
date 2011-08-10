package magic.ui.viewer;

import magic.ui.GameController;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.TitleBar;
import magic.ui.widget.ViewerScrollPane;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.*;

public abstract class PermanentsViewer extends JPanel implements ChoiceViewer {

	private static final long serialVersionUID = 1L;
	
	private static final Dimension SEPARATOR_DIMENSION=new Dimension(0,10);

	protected final ViewerInfo viewerInfo;
	protected TitleBar titleBar=null;	
	protected final GameController controller;

	private final Collection<ChoiceViewer> targetViewers;
	private final ViewerScrollPane viewerPane;
	
	public PermanentsViewer(final ViewerInfo viewerInfo,final GameController controller) {
		
		this.viewerInfo=viewerInfo;
		this.controller=controller;
		setOpaque(false);

		targetViewers=new ArrayList<ChoiceViewer>();
		
		controller.registerChoiceViewer(this);
		
		setLayout(new BorderLayout());
		
		viewerPane=new ViewerScrollPane();
		add(viewerPane,BorderLayout.CENTER);
	}

	public final void update() {

		final int maxWidth=getWidth()-25;
		
		if (titleBar!=null) {
			titleBar.setText(getTitle());
		}

		targetViewers.clear();
		final JPanel contentPanel=viewerPane.getContent();		
		final JPanel basicLandsPanel=new JPanel(null);
		basicLandsPanel.setOpaque(false);
		contentPanel.add(basicLandsPanel);

		final Color separatorColor=ThemeFactory.getInstance().getCurrentTheme().getColor(Theme.COLOR_SEPARATOR_BACKGROUND);
		PermanentViewerInfo previousPermanentInfo=null;
		final SortedSet<PermanentViewerInfo> basicLands=new TreeSet<PermanentViewerInfo>(PermanentViewerInfo.NAME_COMPARATOR);
		for (final PermanentViewerInfo permanentInfo : getPermanents()) {

			if (permanentInfo.basic) {
				basicLands.add(permanentInfo);
				previousPermanentInfo=permanentInfo;
				continue;
			}			
			if (previousPermanentInfo!=null&&isSeparated(previousPermanentInfo,permanentInfo)) {
				final JPanel separatorPanel=new JPanel();
				separatorPanel.setPreferredSize(SEPARATOR_DIMENSION);
				separatorPanel.setBackground(separatorColor);
				separatorPanel.setOpaque(true);
				contentPanel.add(separatorPanel);
			}
			previousPermanentInfo=permanentInfo;
			final PermanentPanel panel=new PermanentPanel(permanentInfo,controller,getBorder(permanentInfo),maxWidth);
			targetViewers.add(panel);
			contentPanel.add(panel);
		}
		
		if (!basicLands.isEmpty()) {
			basicLandsPanel.setLayout(new GridLayout(1,8));
			for (final PermanentViewerInfo landPermanentInfo : basicLands) {
			
				final BasicLandPermanentButton button=new BasicLandPermanentButton(landPermanentInfo,controller);
				basicLandsPanel.add(button);
				targetViewers.add(button);
			}
		}

		viewerPane.switchContent();
		repaint();
	}
	
	public boolean isEmpty() {
		
		return targetViewers.isEmpty();
	}
	
	@Override
	public void showValidChoices(final Set<Object> validChoices) {

		for (final ChoiceViewer targetViewer : targetViewers) {
			
			targetViewer.showValidChoices(validChoices);
		}
	}
	
	public abstract String getTitle();
	
	public abstract Collection<PermanentViewerInfo> getPermanents();
	
	public abstract boolean isSeparated(final PermanentViewerInfo permanent1,final PermanentViewerInfo permanent2);
	
	public abstract Border getBorder(final PermanentViewerInfo permanentInfo);
}