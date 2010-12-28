package magic.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import magic.data.CardDefinitions;
import magic.data.IconImages;
import magic.model.MagicCardDefinition;
import magic.model.MagicRandom;
import magic.ui.resolution.DefaultResolutionProfile;
import magic.ui.viewer.CardViewer;
import magic.ui.widget.BackgroundLabel;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;

public class VersionPanel extends TexturedPanel {

	private static final long serialVersionUID = 1L;
		
	private static final String VERSION = "Magarena 1.5";
	private static final String AUTHOR = "by ubeefx";
	private static final String WEB = "http://magarena.dyndns.org";
	private static final String SPACING = "   ";
	private static final String VERSION_TEXT = VERSION + SPACING + AUTHOR + SPACING + WEB;

	private final JPanel centerPanel;
	private final BackgroundLabel backgroundLabel;
	private final CardViewer cardViewer;
	
	public VersionPanel() {

		setLayout(new BorderLayout());

		final JLabel versionLabel=new JLabel(VERSION_TEXT);
		versionLabel.setIcon(IconImages.CUBE);
		versionLabel.setFont(FontsAndBorders.FONT2);
		versionLabel.setBorder(FontsAndBorders.EMPTY_BORDER);
		add(versionLabel,BorderLayout.SOUTH);
				
		backgroundLabel=new BackgroundLabel();

		cardViewer=new CardViewer(false,"Random Card");
		final List<MagicCardDefinition> spellCards=CardDefinitions.getInstance().getSpellCards();
		final int index=MagicRandom.nextInt(spellCards.size());
		cardViewer.setCard(CardDefinitions.getInstance().getSpellCards().get(index),0);
		cardViewer.setSize(DefaultResolutionProfile.CARD_VIEWER_WIDTH,DefaultResolutionProfile.CARD_VIEWER_HEIGHT);
		
		centerPanel=new JPanel();
		centerPanel.setLayout(null);
		centerPanel.add(cardViewer);
		centerPanel.add(backgroundLabel);
		add(centerPanel,BorderLayout.CENTER);
		
		centerPanel.addComponentListener(new ComponentAdapter()  {
			
			@Override
			public void componentResized(final ComponentEvent event) {
				
				final Dimension size=centerPanel.getSize();
				backgroundLabel.setSize(size);
				cardViewer.setLocation(size.width-cardViewer.getWidth()-10,size.height-cardViewer.getHeight()-10);
			}
		});
	}
}