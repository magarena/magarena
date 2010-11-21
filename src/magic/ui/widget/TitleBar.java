package magic.ui.widget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TitleBar extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final Color TITLE_COLOR=new Color(0x23,0x6B,0x8E);

	private final JLabel label;
	
	public TitleBar(final String title) {

		setLayout(new BorderLayout());
		label=new JLabel(title);
		label.setIconTextGap(4);
		label.setOpaque(true);
		label.setForeground(Color.WHITE);
		label.setBackground(TITLE_COLOR);
		label.setPreferredSize(new Dimension(0,20));
		label.setBorder(FontsAndBorders.BLACK_BORDER);
		add(label,BorderLayout.CENTER);		
	}
	
	public void setText(final String text) {
		
		label.setText(text);
	}

	public void setIcon(final ImageIcon icon) {
		
		label.setIcon(icon);
	}
	
	public void setHorizontalAlignment(final int alignment) {
		
		label.setHorizontalAlignment(alignment);
	}
}