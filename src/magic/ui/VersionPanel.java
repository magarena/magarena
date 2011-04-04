package magic.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import magic.data.CardDefinitions;
import magic.data.IconImages;
import magic.model.MagicCardDefinition;
import magic.model.MagicRandom;
import magic.ui.viewer.CardViewer;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.ZoneBackgroundLabel;

public class VersionPanel extends JPanel {

	private static final long serialVersionUID = 1L;
		
	private static final String VERSION = "Magarena 1.11";
	private static final String AUTHOR = "by ubeefx";
	private static final String WEB = "http://magarena.dyndns.org";
	private static final String SPACING = "   ";
	private static final String VERSION_TEXT = VERSION + SPACING + AUTHOR + SPACING + WEB;

	private static final Border LOGO_BORDER=BorderFactory.createMatteBorder(2,2,2,2,new Color(0x8C,0x78,0x53));
	
	private final ZoneBackgroundLabel backgroundLabel;
	private final JLabel versionLabel;
	private final JLabel logoLabel;
	private final CardViewer cardViewer;
	
	public VersionPanel() {

		setLayout(null);

		versionLabel=new JLabel(VERSION_TEXT);
		versionLabel.setForeground(Color.WHITE);
		versionLabel.setIcon(IconImages.CUBE);
		versionLabel.setFont(FontsAndBorders.FONT2);
		versionLabel.setBorder(FontsAndBorders.EMPTY_BORDER);
		add(versionLabel,BorderLayout.SOUTH);
				
		backgroundLabel=new ZoneBackgroundLabel();

		final ImageIcon logoIcon=new ImageIcon(IconImages.LOGO);
		logoLabel=new JLabel(logoIcon);
		logoLabel.setBorder(LOGO_BORDER);
		logoLabel.setSize(logoIcon.getIconWidth(),logoIcon.getIconHeight());
		versionLabel.setSize(logoIcon.getIconWidth(),20);
				
		cardViewer=new CardViewer(null,true,true);
		final List<MagicCardDefinition> spellCards=CardDefinitions.getInstance().getSpellCards();
		final int index=MagicRandom.nextInt(spellCards.size());
		cardViewer.setCard(spellCards.get(index),0);
		
		add(cardViewer);
		add(versionLabel);
		add(logoLabel);
		add(backgroundLabel);
		
		addComponentListener(new ComponentAdapter()  {
			
			@Override
			public void componentResized(final ComponentEvent event) {
				
				final Dimension size=getSize();
				backgroundLabel.setSize(size);
				final int lx=(size.width-logoLabel.getWidth()-cardViewer.getWidth()-10)/2;
				final int ly=(size.height-logoLabel.getHeight())/2;
				logoLabel.setLocation(lx>0?lx:0,ly>0?ly:0);
				versionLabel.setLocation(lx+10,ly+10);
				cardViewer.setLocation(size.width-cardViewer.getWidth()-10,size.height-cardViewer.getHeight()-10);
			}
		});
	}
}