package magic.ui;

import magic.data.FileIO;
import magic.data.IconImages;
import magic.ui.widget.ZoneBackgroundLabel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ReadmePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final String README_FILENAME="README.txt";

	private static final int MAX_WIDTH=1000;
	private static final String HTML_OPEN="<html><body>";
	private static final String HTML_CLOSE="</body></html>";

	private static final Border KEYWORD_BORDER=BorderFactory.createCompoundBorder(
		BorderFactory.createEmptyBorder(3,0,3,6),
		BorderFactory.createLineBorder(Color.BLACK)
	);
		
	private final MagicFrame frame;
	private final ZoneBackgroundLabel backgroundLabel;
	private final JScrollPane keywordsPane;
	private final JButton closeButton;
	
	public ReadmePanel(final MagicFrame frame) {
		
		this.frame=frame;
		
		setLayout(null);
		
		closeButton=new JButton(IconImages.CLOSE);
		closeButton.setFocusable(false);
		closeButton.setSize(28,28);
		closeButton.addActionListener(this);
		add(closeButton);
		
		JTextArea readMeTextArea = new JTextArea();
		readMeTextArea.setEditable(false);
		
        String content = "";
        try { //load content from README.txt file
            content = FileIO.toStr(new java.io.File(README_FILENAME));
        } catch (final java.io.IOException ex) {
            System.err.println("WARNING! Unable to read from " + README_FILENAME);
			readMeTextArea.setText(README_FILENAME + " not found.");
		}
        readMeTextArea.setText(content);

		keywordsPane=new JScrollPane(readMeTextArea);
		keywordsPane.setBorder(null);
		keywordsPane.setOpaque(false);
		keywordsPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		keywordsPane.getVerticalScrollBar().setUnitIncrement(50);
		keywordsPane.getVerticalScrollBar().setBlockIncrement(50);
		keywordsPane.getViewport().setOpaque(false);
		add(keywordsPane);
		
		//final JPanel keywordsPanel=createKeywordsPanel();
		//keywordsPane.getViewport().add(keywordsPanel);
		
		backgroundLabel=new ZoneBackgroundLabel();
		backgroundLabel.setBounds(0,0,0,0);
		add(backgroundLabel);
		
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				resizeComponents();
			}
		});
                
        SwingUtilities.invokeLater(new Runnable() {
            public void run()
            {
                keywordsPane.getVerticalScrollBar().setValue(0);              
            }           
        }); 
	}
	
	private void resizeComponents() {
		final Dimension size=getSize();
		backgroundLabel.setSize(size);
		closeButton.setLocation(size.width-closeButton.getWidth(),0);
		final int width=Math.min(MAX_WIDTH,size.width-closeButton.getWidth()-40);
		keywordsPane.setBounds(20,20,width,size.height-40);
		revalidate();
		repaint();
	}

	@Override
	public void actionPerformed(final ActionEvent event) {
		final Object source=event.getSource();
		if (source==closeButton) {
			frame.closeReadme();
		}
	}
}
