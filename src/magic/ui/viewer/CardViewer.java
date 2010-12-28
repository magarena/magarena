package magic.ui.viewer;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import magic.data.DefaultCardImagesProvider;
import magic.data.GeneralConfig;
import magic.data.HighQualityCardImagesProvider;
import magic.model.MagicCardDefinition;
import magic.ui.DelayedViewer;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TitleBar;
import magic.ui.widget.TransparentImagePanel;

public class CardViewer extends JPanel implements DelayedViewer {

	private static final long serialVersionUID = 1L;

	private final TransparentImagePanel cardPanel;
	private MagicCardDefinition currentCardDefinition=null;
	private int currentIndex=0;
	private boolean image;
	
	public CardViewer(final boolean image,final String title) {

		this.image=image;
		
		this.setLayout(new BorderLayout());
		this.setOpaque(false);

		if (image) {
			setBorder(FontsAndBorders.WHITE_BORDER);
		} else {
			final TitleBar titleBar=new TitleBar(title);
			add(titleBar,BorderLayout.NORTH);
		}
		
		cardPanel=new TransparentImagePanel();
		add(cardPanel,BorderLayout.CENTER);
		
		setCard(MagicCardDefinition.EMPTY,0);
	}
	
	public CardViewer(final boolean image) {
		
		this(image,"Card");
	}
	
	public void setCard(final MagicCardDefinition cardDefinition,final int index) {

		if (cardDefinition!=currentCardDefinition||index!=currentIndex) {
			currentCardDefinition=cardDefinition;
			currentIndex=index;
			final BufferedImage cardImage;
			if (image&&GeneralConfig.getInstance().isHighQuality()) {
				final BufferedImage sourceImage=HighQualityCardImagesProvider.getInstance().getImage(cardDefinition,index);
				final int imageWidth=sourceImage.getWidth(this);
				final int imageHeight=sourceImage.getHeight(this);
				cardImage=new BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_INT_ARGB);
				cardImage.getGraphics().drawImage(sourceImage,0,0,null);
				cardPanel.setOpacity(0.8f);
				setSize(imageWidth,imageHeight);
				revalidate();
			} else {
				cardImage=DefaultCardImagesProvider.getInstance().getImage(cardDefinition,index);
				if (image) {
					cardPanel.setOpacity(1.0f);
					setSize(DefaultCardImagesProvider.CARD_WIDTH,DefaultCardImagesProvider.CARD_HEIGHT);
					revalidate();
				}
			}
			
			cardPanel.setImage(cardImage);
			repaint();
		}
	}

	@Override
	public void showDelayed() {
	
		setVisible(true);
	}
	
	@Override
	public void hideDelayed() {

		setVisible(false);
	}
}