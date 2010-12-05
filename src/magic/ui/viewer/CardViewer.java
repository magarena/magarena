package magic.ui.viewer;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import magic.data.CardImages;
import magic.model.MagicCardDefinition;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TitleBar;

public class CardViewer extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JLabel cardLabel;
	private MagicCardDefinition currentCardDefinition=null;
	private int currentIndex=0;
	
	public CardViewer(final boolean image) {
		
		this.setLayout(new BorderLayout());

		if (image) {
			setBorder(FontsAndBorders.WHITE_BORDER);
		} else {
			final TitleBar titleBar=new TitleBar("Card");
			add(titleBar,BorderLayout.NORTH);
		}
		
		cardLabel=new JLabel();
		add(cardLabel,BorderLayout.CENTER);
		
		setCard(MagicCardDefinition.EMPTY,0);
	}
	
	public void setCard(final MagicCardDefinition cardDefinition,final int index) {

		if (cardDefinition!=currentCardDefinition||index!=currentIndex) {
			currentCardDefinition=cardDefinition;
			currentIndex=index;
			cardLabel.setIcon(new ImageIcon(CardImages.getInstance().getImage(cardDefinition,index)));
			repaint();
		}
	}
}