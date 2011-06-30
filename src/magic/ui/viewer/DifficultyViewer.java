package magic.ui.viewer;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import magic.ai.MagicAI;
import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.SliderPanel;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.TitleBar;

public class DifficultyViewer extends TexturedPanel implements ChangeListener {

	private static final long serialVersionUID = 1L;

	private static final String TITLE="Difficulty Settings";
	
	private final SliderPanel difficultySlider;
	private final SliderPanel extraLifeSlider;
	
	public DifficultyViewer() {
		setLayout(null);
		setBorder(FontsAndBorders.BLACK_BORDER);
		
		final GeneralConfig config=GeneralConfig.getInstance();
		final Theme theme=ThemeFactory.getInstance().getCurrentTheme();
		
		difficultySlider=new SliderPanel("Level",IconImages.DIFFICULTY2,1,MagicAI.MAX_LEVEL,1,config.getDifficulty());
		difficultySlider.setTextColor(theme.getTextColor());
		difficultySlider.setOpaque(false);
		difficultySlider.setBounds(10,10,250,40);
		difficultySlider.addChangeListener(this);
		add(difficultySlider);
		
		extraLifeSlider=new SliderPanel("Life +",theme.getIcon(Theme.ICON_LIFE),0,10,1,config.getExtraLife());
		extraLifeSlider.setTextColor(theme.getTextColor());
		extraLifeSlider.setOpaque(false);
		extraLifeSlider.setBounds(10,60,250,40);
		extraLifeSlider.addChangeListener(this);
		add(extraLifeSlider);
	}
	
	public void setTitle(final TitleBar titleBar) {
		titleBar.setText(TITLE);
		titleBar.setIcon(null);
	}

	@Override
	public void stateChanged(final ChangeEvent event) {
		final GeneralConfig config=GeneralConfig.getInstance();
		config.setDifficulty(difficultySlider.getValue());
		config.setExtraLife(extraLifeSlider.getValue());
	}
}
