package magic.ui.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import magic.data.CardStatistics;
import magic.model.MagicColor;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicPlayerProfile;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.TitleBar;

public class DeckStatisticsViewer extends JPanel implements ChangeListener {

	private static final long serialVersionUID = 1L;

	private final TitleBar titleBar;
	private final JPanel topPanel;
	private final JPanel linesPanel;
	private final List<JLabel> lines;
	private final JLabel curveLabels[];
	private final Color textColor;
	
	public DeckStatisticsViewer() {
		
		textColor=ThemeFactory.getInstance().getCurrentTheme().getTextColor();
		
		setLayout(new BorderLayout());
				
		titleBar=new TitleBar("Deck Statistics");
		add(titleBar,BorderLayout.NORTH);
				
		final JPanel mainPanel=new TexturedPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(FontsAndBorders.BLACK_BORDER_2);
		add(mainPanel,BorderLayout.CENTER);

		topPanel=new JPanel(new FlowLayout(FlowLayout.LEFT,7,5));
		topPanel.setOpaque(false);
		mainPanel.add(topPanel,BorderLayout.NORTH);
		
		linesPanel=new JPanel();
		linesPanel.setOpaque(false);
		mainPanel.add(linesPanel,BorderLayout.CENTER);
				
		final JPanel bottomPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		bottomPanel.setOpaque(false);
		mainPanel.add(bottomPanel,BorderLayout.SOUTH);
		final JPanel curvePanel=new JPanel(new GridLayout(2,1));
		curvePanel.setOpaque(false);
		bottomPanel.add(curvePanel);
		curveLabels=new JLabel[CardStatistics.MANA_CURVE_SIZE];
		final JPanel curveTopPanel=new JPanel(new GridLayout(1,CardStatistics.MANA_CURVE_SIZE));
		curveTopPanel.setOpaque(false);
		curveTopPanel.setBorder(FontsAndBorders.TABLE_ROW_BORDER);
		curvePanel.add(curveTopPanel);
		final JPanel curveBottomPanel=new JPanel(new GridLayout(1,CardStatistics.MANA_CURVE_SIZE));
		curveBottomPanel.setOpaque(false);
		curvePanel.add(curveBottomPanel);
		curveBottomPanel.setBorder(FontsAndBorders.TABLE_BOTTOM_ROW_BORDER);
		
		final Dimension labelSize=new Dimension(25,20);
		for (int index=0;index<CardStatistics.MANA_CURVE_SIZE;index++) {
			
			final JLabel label=new JLabel(CardStatistics.MANA_CURVE_ICONS.get(index));
			label.setPreferredSize(labelSize);
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBorder(FontsAndBorders.TABLE_BORDER);
			curveTopPanel.add(label);
			curveLabels[index]=new JLabel("0");
			curveLabels[index].setPreferredSize(labelSize);
			curveLabels[index].setForeground(textColor);
			curveLabels[index].setHorizontalAlignment(JLabel.CENTER);
			curveLabels[index].setBorder(FontsAndBorders.TABLE_BORDER);
			curveBottomPanel.add(curveLabels[index]);
		}
		
		lines=new ArrayList<JLabel>();
	}
	
	public void setPlayer(final MagicPlayerDefinition player) {
		
		titleBar.setText("Deck Statistics : "+player.getName());
		
		final CardStatistics statistics=new CardStatistics(player.getDeck());

		topPanel.removeAll();
		for (int index=0;index<CardStatistics.NR_OF_TYPES;index++) {
			
			final int total=statistics.totalTypes[index];
			if (total>0) {
				final JLabel label=new JLabel(""+total);
				label.setForeground(textColor);
				label.setIcon(CardStatistics.TYPE_ICONS.get(index));
				label.setIconTextGap(4);
				topPanel.add(label);
			}
		}
		topPanel.revalidate();
		
		lines.clear();
		final JLabel allLabel=new JLabel(
				"Monocolor : "+statistics.monoColor+"  Multicolor : "+statistics.multiColor+"  Colorless : "+statistics.colorless);
		allLabel.setForeground(textColor);
		lines.add(allLabel);
							 
		final MagicPlayerProfile profile=player.getProfile();
		for (final MagicColor color : profile.getColors()) {
			
			final int index=color.ordinal();
			final JLabel label=new JLabel(color.getManaType().getIcon(true));
			label.setForeground(textColor);
			label.setHorizontalAlignment(JLabel.LEFT);
			label.setIconTextGap(5);
			label.setText("Cards : "+statistics.colorCount[index]+
						  "  Monocolor : "+statistics.colorMono[index]+
						  "  Lands : "+statistics.colorLands[index]);
			lines.add(label);
		}
		
		for (int index=0;index<CardStatistics.MANA_CURVE_SIZE;index++) {
			
			curveLabels[index].setText(""+statistics.manaCurve[index]);
		}
		
		linesPanel.removeAll();
		linesPanel.setLayout(new GridLayout(lines.size(),0));
		for (final JLabel line : lines) {
			
			line.setPreferredSize(new Dimension(0,25));
			linesPanel.add(line);
		}		
		revalidate();
		repaint();
	}
	
	@Override
	public void stateChanged(ChangeEvent event) {

		setPlayer((MagicPlayerDefinition)event.getSource());
	}
}
