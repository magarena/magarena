package magic.ui.viewer;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;

import magic.model.MagicLogBook;
import magic.model.MagicMessage;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.MessagePanel;
import magic.ui.widget.TitleBar;

public class LogBookViewer extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private static final Border SEPARATOR_BORDER=BorderFactory.createCompoundBorder(
		BorderFactory.createMatteBorder(1,0,0,0,Color.BLACK),
		FontsAndBorders.EMPTY_BORDER
	);
	
	private static final int MAX_LINES=30;
	
	private final MagicLogBook logBook;
	private final JPanel messagePanel;
	
	public LogBookViewer(final MagicLogBook logBook) {
		
		this.logBook=logBook;
		
		setBorder(FontsAndBorders.UP_BORDER);
		setLayout(new BorderLayout());
		setOpaque(true);

		add(new TitleBar("Messages"),BorderLayout.NORTH);
		
		final JPanel centerPanel=new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(FontsAndBorders.BLACK_BORDER_2);
		add(centerPanel,BorderLayout.CENTER);
		
		messagePanel=new JPanel();
		messagePanel.setLayout(new BoxLayout(messagePanel,BoxLayout.Y_AXIS));
		centerPanel.add(messagePanel,BorderLayout.NORTH);
	}
	
	public MagicLogBook getLogBook() {
		
		return logBook;
	}
	
	public void update() {

		final int maxWidth=getWidth()-175;
		
		messagePanel.removeAll();
		int index=logBook.size()-1;
		boolean first=true;
		for (int lines=MAX_LINES;lines>0&&index>=0;lines--,index--) {
			
			final MagicMessage message=logBook.get(index);
			final MessagePanel panel=new MessagePanel(message,maxWidth);
			if (first) {
				first=false;
			} else {
				panel.setBorder(SEPARATOR_BORDER);
			}
			messagePanel.add(panel);
		}
		revalidate();
	}	
}