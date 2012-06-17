package magic.ui.viewer;

import magic.data.History;
import magic.model.MagicColor;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.TitleBar;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public class HistoryViewer extends JPanel{
    
    private static final long serialVersionUID = 1L;
    
    private static final Dimension PREFERRED_SIZE = new Dimension(270, 175);
    
    public HistoryViewer() {    
        setPreferredSize(PREFERRED_SIZE);
        setBorder(FontsAndBorders.UP_BORDER);    
        setLayout(new BorderLayout());
        final TitleBar titleBar = new TitleBar("History - " + History.getName());
        add(titleBar, BorderLayout.NORTH);
        
        final TexturedPanel mainPanel = new TexturedPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(FontsAndBorders.BLACK_BORDER_2);
        
        final int gamesPlayed = History.getGamesPlayed();
        final int gamesWon = History.getGamesWon();
        final int gamesLost = gamesPlayed - gamesWon;
        final int gamesWinPercentage = getPercentage(gamesWon, gamesPlayed);
        final int averageTurns = (gamesPlayed > 0) ? History.getTurnsPlayed() / gamesPlayed : 0;
        final int duelsPlayed = History.getDuelsPlayed();
        final int duelsWon = History.getDuelsWon();
        final int duelsLost = duelsPlayed - duelsWon;
        final int duelsWinPercentage = getPercentage(duelsWon, duelsPlayed);
        final int avgLifeLeftPlayer = (gamesWon > 0) ? History.getLifeLeftPlayer() / gamesWon : 0;
        final int avgLifeLeftAI = (gamesLost > 0) ? History.getLifeLeftAI() / gamesLost : 0;
        
        final int[] colorCount = new int[MagicColor.NR_COLORS];
        colorCount[0] = History.getColorBlack();
        colorCount[1] = History.getColorBlue();
        colorCount[2] = History.getColorGreen();
        colorCount[3] = History.getColorRed();
        colorCount[4] = History.getColorWhite();
        int mostCount = Integer.MIN_VALUE;
        MagicColor mostColor = null;
        for (final MagicColor color : MagicColor.values()) {    
            final int count = colorCount[color.ordinal()];
            if (count > mostCount) {
                mostCount = count;
                mostColor = color;
            }
        }

        final JTextArea textArea = new JTextArea(8, 170);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        // set background to get transparent effect
        // must be done this way because of use of TexturedPanel()
        textArea.setBackground(new Color(255, 255, 255, 0));
        textArea.setForeground(ThemeFactory.getInstance().getCurrentTheme().getTextColor());
        textArea.setTabSize(16);
        textArea.setText("Games played:\t" + gamesPlayed +
                "\nGames won / lost\t" + gamesWon + " / " + 
                gamesLost + " (" + gamesWinPercentage + "%)" +
                "\nAverage turns per game:\t" + averageTurns +
                "\nAverage remaining player life:\t" + avgLifeLeftPlayer +
                "\nAverage remaining AI life:\t" + avgLifeLeftAI +
                "\nDuels played:\t" + duelsPlayed +
                "\nDuels won / lost:\t" + duelsWon + " / " + 
                duelsLost + " (" + duelsWinPercentage + "%)" +
                "\nMost used color:\t" + mostColor.getName()
                );
        mainPanel.add(textArea);
            
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private static final int getPercentage(final int value, final int total) {
        return total>0 ? (value*100)/total : 0;
    }
}
