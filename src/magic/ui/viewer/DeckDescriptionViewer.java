package magic.ui.viewer;

import magic.model.MagicPlayerDefinition;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.TitleBar;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class DeckDescriptionViewer extends JPanel implements FocusListener {
	
	private static final long serialVersionUID = 1L;
	
	private static final Dimension PREFERRED_SIZE = new Dimension(270, 110);
	private final JTextArea textArea;
	private MagicPlayerDefinition player;
	
	public DeckDescriptionViewer() {
		setPreferredSize(PREFERRED_SIZE);
		setBorder(FontsAndBorders.UP_BORDER);	
		setLayout(new BorderLayout());
		
		final TitleBar titleBar = new TitleBar("Deck Description");
		add(titleBar, BorderLayout.NORTH);
		
		final TexturedPanel mainPanel = new TexturedPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setOpaque(false);
		mainPanel.setBorder(FontsAndBorders.BLACK_BORDER_2);
		add(mainPanel,BorderLayout.CENTER);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setForeground(ThemeFactory.getInstance().getCurrentTheme().getTextColor());
		textArea.addFocusListener(this);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.getVerticalScrollBar().setUnitIncrement(8);
		mainPanel.add(scrollPane,BorderLayout.CENTER);
	}

	public void setPlayer(MagicPlayerDefinition playerDef) {
		this.player = playerDef;
		textArea.setText(playerDef.getDeck().getDescription());
		textArea.setCaretPosition(0);
		
	}

	@Override
	public void focusGained(FocusEvent event) {}

	@Override
	public void focusLost(FocusEvent event) {
		player.getDeck().setDescription(textArea.getText());
	}
	
}
