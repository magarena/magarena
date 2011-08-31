package magic.ui;

import magic.data.IconImages;
import magic.data.KeywordDefinitions;
import magic.data.KeywordDefinitions.KeywordDefinition;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.ZoneBackgroundLabel;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class KeywordsPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

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
	
	public KeywordsPanel(final MagicFrame frame) {
		
		this.frame=frame;
		
		setLayout(null);
		
		closeButton=new JButton(IconImages.CLOSE);
		closeButton.setFocusable(false);
		closeButton.setSize(28,28);
		closeButton.addActionListener(this);
		add(closeButton);
		
		keywordsPane=new JScrollPane();
		keywordsPane.setBorder(null);
		keywordsPane.setOpaque(false);
		keywordsPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		keywordsPane.getVerticalScrollBar().setUnitIncrement(50);
		keywordsPane.getVerticalScrollBar().setBlockIncrement(50);
		keywordsPane.getViewport().setOpaque(false);
		add(keywordsPane);
		
		final JPanel keywordsPanel=createKeywordsPanel();
		keywordsPane.getViewport().add(keywordsPanel);
		
		backgroundLabel=new ZoneBackgroundLabel();
		backgroundLabel.setBounds(0,0,0,0);
		add(backgroundLabel);
		
		this.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {

				resizeComponents();
			}
		});
	}

	private JPanel createKeywordsPanel() {

		final Theme theme=ThemeFactory.getInstance().getCurrentTheme();
		final Color nameColor=theme.getColor(Theme.COLOR_NAME_FOREGROUND);
		final Color textColor=theme.getTextColor();
		
		final JPanel keywordsPanel=new JPanel();
		keywordsPanel.setOpaque(false);
		keywordsPanel.setLayout(new BoxLayout(keywordsPanel,BoxLayout.Y_AXIS));

		for (final KeywordDefinition keywordDefinition : KeywordDefinitions.getInstance().getKeywordDefinitions()) {

			final JPanel keywordPanel=new JPanel();
			keywordPanel.setLayout(new BorderLayout());
			keywordPanel.setOpaque(false);
			keywordPanel.setBorder(KEYWORD_BORDER);
			keywordsPanel.add(keywordPanel);
			final JPanel innerPanel=new TexturedPanel();
			innerPanel.setLayout(new BorderLayout(0,2));
			innerPanel.setBorder(FontsAndBorders.EMPTY_BORDER);
			keywordPanel.add(innerPanel,BorderLayout.CENTER);
			final JLabel nameLabel=new JLabel(HTML_OPEN+keywordDefinition.name+HTML_CLOSE);
			nameLabel.setForeground(nameColor);
			nameLabel.setFont(FontsAndBorders.FONT2);
			innerPanel.add(nameLabel,BorderLayout.NORTH);
			final JLabel descriptionLabel=new JLabel(HTML_OPEN+keywordDefinition.description+HTML_CLOSE);
			descriptionLabel.setForeground(textColor);
			innerPanel.add(descriptionLabel,BorderLayout.CENTER);
		}
		
		return keywordsPanel;
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
			frame.closeKeywords();
		}
	}
}