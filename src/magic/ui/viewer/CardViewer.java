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
		
		setCard(MagicCardDefinition.EMPTY);
	}
	
	public void setCard(final MagicCardDefinition cardDefinition) {

		if (cardDefinition!=currentCardDefinition) {
			currentCardDefinition=cardDefinition;
			cardLabel.setIcon(new ImageIcon(CardImages.getInstance().getImage(cardDefinition)));
			repaint();
		}
	}
}