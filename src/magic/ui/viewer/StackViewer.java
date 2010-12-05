package magic.ui.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;

import magic.ui.GameController;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.PanelButton;
import magic.ui.widget.TextLabel;
import magic.ui.widget.ViewerScrollPane;

public class StackViewer extends JPanel implements ChoiceViewer {

	private static final long serialVersionUID = 1L;
	
	private final ViewerInfo viewerInfo;
	private final GameController controller;
	private final ViewerScrollPane viewerPane;
	private final boolean image;
	private Collection<StackButton> buttons;
	private int setHeight = 0;
	
	public StackViewer(final ViewerInfo viewerInfo,final GameController controller,final boolean image) {
	
		this.viewerInfo=viewerInfo;
		this.controller=controller;
		this.image=image;
		setOpaque(false);

		controller.registerChoiceViewer(this);
		
		setLayout(new BorderLayout());
						
		viewerPane=new ViewerScrollPane();
		add(viewerPane,BorderLayout.CENTER);
						
		buttons=new ArrayList<StackButton>();
		
		update();
	}
	
	@Override
	public void setBounds(final Rectangle r) {

		super.setBounds(r);
		setHeight=r.height;
	}

	public String getTitle() {
		
		return "Stack";
	}
	
	public void update() {
				
		final int maxWidth=getWidth()-40;
		
		buttons.clear();
		
		final JPanel contentPanel=viewerPane.getContent();
		for (final StackViewerInfo stackInfo : viewerInfo.getStack()) {
			
			final JPanel panel=new JPanel(new BorderLayout());
			panel.setBorder(FontsAndBorders.SMALL_EMPTY_BORDER);
			final StackButton button=new StackButton(stackInfo,maxWidth);
			buttons.add(button);
			panel.add(button,BorderLayout.CENTER);
			contentPanel.add(panel);
		}

		final int contentHeight=viewerPane.getContent().getPreferredSize().height+20;
		if (contentHeight<setHeight) {
			setSize(getWidth(),contentHeight);
		} else {
			setSize(getWidth(),setHeight);
		}
		
		showValidChoices(controller.getValidChoices());
		viewerPane.switchContent();
		repaint();
	}
	
	public boolean isEmpty() {
		
		return buttons.isEmpty();
	}
	
	@Override
	public void showValidChoices(final Set<Object> validChoices) {
		
		for (final StackButton button : buttons) {
			
			button.showValidChoices(validChoices);
		}
	}
	
	private final class StackButton extends PanelButton implements ChoiceViewer {

		private static final long serialVersionUID = 1L;
		
		private final StackViewerInfo stackInfo;
		private final JLabel sourceLabel;
		private final TextLabel textLabel;
		
		public StackButton(final StackViewerInfo stackInfo,final int maxWidth) {
			
			this.stackInfo=stackInfo;
			
			final JPanel panel=new JPanel();
			panel.setOpaque(false);
			panel.setBorder(FontsAndBorders.getPlayerBorder(stackInfo.visible));
			panel.setLayout(new BorderLayout(0,2));
			setComponent(panel);

			sourceLabel=new JLabel(stackInfo.name);
			sourceLabel.setIcon(stackInfo.icon);
			panel.add(sourceLabel,BorderLayout.NORTH);
			
			textLabel=new TextLabel(stackInfo.description,maxWidth,false);
			panel.add(textLabel,BorderLayout.CENTER);
		}

		@Override
		public void mouseClicked() {

			controller.processClick(stackInfo.itemOnStack);
		}

		@Override
		public void mouseEntered() {

			if (image) {
				final Rectangle rect=new Rectangle(StackViewer.this.getLocationOnScreen().x,getLocationOnScreen().y,StackViewer.this.getWidth(),getHeight());
				controller.viewInfoRight(stackInfo.cardDefinition,0,rect);				
			} else {
				controller.viewCard(stackInfo.cardDefinition,0);
			}
		}
		
		@Override
		public void mouseExited() {

			if (image) {
				controller.hideInfo();
			}
		}

		@Override
		public void showValidChoices(final Set<Object> validChoices) {

			if (validChoices.contains(stackInfo.itemOnStack)) {
				sourceLabel.setForeground(FontsAndBorders.TARGET_FOREGROUND);
				textLabel.setForeground(FontsAndBorders.TARGET_FOREGROUND);
			} else {
				sourceLabel.setForeground(Color.BLACK);
				textLabel.setForeground(Color.BLACK);
			}
			sourceLabel.repaint();
		}
	}
}