package magic.ui.widget;

import java.awt.BorderLayout;
import java.awt.Rectangle;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ViewerScrollPane extends JScrollPane {

	private static final long serialVersionUID = 1L;

	private JPanel contentPanel=null;
	
	public ViewerScrollPane() {
		
		setOpaque(false);
		getViewport().setOpaque(false);
		setBorder(null);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		getVerticalScrollBar().setUnitIncrement(80);
		getVerticalScrollBar().setBlockIncrement(80);
	}
	
	public synchronized JPanel getContent() {
		
		if (contentPanel==null) {
			contentPanel=new JPanel();
			contentPanel.setBorder(FontsAndBorders.BLACK_BORDER_2);
			contentPanel.setLayout(new BoxLayout(contentPanel,BoxLayout.Y_AXIS));
		}
		return contentPanel;
	}
	
	public synchronized void switchContent() {

		if (contentPanel!=null) {
			final Rectangle rect=getViewport().getViewRect();
			final JPanel mainPanel=new JPanel(new BorderLayout());
			mainPanel.setOpaque(false);
			mainPanel.add(contentPanel,BorderLayout.NORTH);
			getViewport().add(mainPanel);
			getViewport().scrollRectToVisible(rect);
			contentPanel=null;
		}
	}
}