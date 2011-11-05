package magic.ui.viewer;

import magic.ai.MagicAI;
import magic.data.CardImagesProvider;
import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.model.MagicDuel;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.SliderPanel;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.TitleBar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

public class DuelDifficultyViewer extends JPanel implements ChangeListener {
	
	private static final long serialVersionUID = 186542L;
	
	public static final Dimension PREFERRED_SIZE = new Dimension(CardImagesProvider.CARD_WIDTH, 126);
	
	private static final String TITLE="Duel";
	
	private final TitleBar titleBar;
	private final MagicDuel duel;
	private final SliderPanel difficultySlider;
	private final SliderPanel extraLifeSlider;
	
	public DuelDifficultyViewer(final MagicDuel duel) {
		this.duel = duel;
		
		setPreferredSize(PREFERRED_SIZE);
		setBorder(FontsAndBorders.UP_BORDER);
		
		setLayout(new BorderLayout());
		titleBar = new TitleBar(TITLE);
		add(titleBar, BorderLayout.NORTH);
		
		final Theme theme = ThemeFactory.getInstance().getCurrentTheme();
		
		final TexturedPanel mainPanel = new TexturedPanel();
		mainPanel.setLayout(new GridLayout(3, 1));
		mainPanel.setOpaque(false);
		mainPanel.setBorder(FontsAndBorders.BLACK_BORDER_2);
		
		// progress
		final int gamesPlayed = getDuel().getGamesPlayed();
		final int gamesTotal = getDuel().getConfiguration().getNrOfGames();
		final int gamesWon = getDuel().getGamesWon();		
		final int winPercentage = getPercentage(gamesWon, gamesPlayed);
			
		final JLabel gameLabel = new JLabel("Played: " + gamesPlayed + " of " + gamesTotal + "      Won: " + gamesWon + " (" + winPercentage + "%)");
		gameLabel.setHorizontalAlignment(JLabel.CENTER);
		gameLabel.setFont(FontsAndBorders.FONT2);
		gameLabel.setForeground(theme.getTextColor());
		mainPanel.add(gameLabel);
		
		// sliders
		final GeneralConfig config = GeneralConfig.getInstance();
		
		difficultySlider = new SliderPanel("AI Level", IconImages.DIFFICULTY2, 1, MagicAI.MAX_LEVEL, 1, config.getDifficulty());
		difficultySlider.setTextColor(theme.getTextColor());
		difficultySlider.setOpaque(false);
		difficultySlider.setBounds(10,10,250,40);
		difficultySlider.addChangeListener(this);
		mainPanel.add(difficultySlider);
		
		extraLifeSlider = new SliderPanel("AI Life +", theme.getIcon(Theme.ICON_LIFE), 0, 10, 1, config.getExtraLife());
		extraLifeSlider.setTextColor(theme.getTextColor());
		extraLifeSlider.setOpaque(false);
		extraLifeSlider.setBounds(10,60,250,40);
		extraLifeSlider.addChangeListener(this);
		mainPanel.add(extraLifeSlider);
		
		add(mainPanel, BorderLayout.CENTER);
	}
	
	public static void setTitle(final TitleBar titleBar) {
		titleBar.setText(TITLE);
		titleBar.setIcon(null);
	}
	
	public MagicDuel getDuel() {
		return duel;
	}
	
	private static final int getPercentage(final int value,final int total) {
		return total>0 ? (value*100)/total : 0;
	}

	@Override
	public void stateChanged(final ChangeEvent event) {
		final GeneralConfig config = GeneralConfig.getInstance();
		
		config.setDifficulty(difficultySlider.getValue());
		config.setExtraLife(extraLifeSlider.getValue());
	}
}
