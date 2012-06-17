package magic.ui.viewer;

import magic.data.IconImages;
import magic.model.MagicDuel;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.TitleBar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public class DuelViewer extends TexturedPanel {

    private static final long serialVersionUID = 1L;
    
    private static final String TITLE="Duel Progress";

    private static final Color GOLD_COLOR=new Color(0xCF,0xB5,0x3B);
    private static final Color SILVER_COLOR=new Color(0xA8,0xA8,0xA8);
    private static final Color BRONZE_COLOR=new Color(0xA6,0x7D,0x3D);
    private static final Color CLOVER_COLOR=new Color(0x23,0x8E,0x23);
    
    private final MagicDuel duel;
    
    public DuelViewer(final MagicDuel duel) {
        this.duel=duel;
        setLayout(new BorderLayout());
        add(createProgressPanel(),BorderLayout.CENTER);
    }
        
    private JPanel createProgressPanel() {

        final Theme theme=ThemeFactory.getInstance().getCurrentTheme();
        
        final int gamesPlayed=duel.getGamesPlayed();
        final int gamesWon=duel.getGamesWon();        
        final int percentage=getPercentage(gamesWon,gamesPlayed);
        
        final JPanel mainPanel=new JPanel(new BorderLayout(0,5));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(FontsAndBorders.BLACK_BORDER_2);
        
        if (duel.isFinished()) {
            final JLabel finishedLabel=new JLabel("Finished!");
            finishedLabel.setIconTextGap(6);
            finishedLabel.setHorizontalAlignment(JLabel.CENTER);
            finishedLabel.setFont(FontsAndBorders.FONT4);
            if (percentage>=90) {
                finishedLabel.setIcon(IconImages.GOLD);
                finishedLabel.setForeground(GOLD_COLOR);
            } else if (percentage>=70) {
                finishedLabel.setIcon(IconImages.SILVER);
                finishedLabel.setForeground(SILVER_COLOR);
            } else if (percentage>=50) {
                finishedLabel.setIcon(IconImages.BRONZE);
                finishedLabel.setForeground(BRONZE_COLOR);
            } else {
                finishedLabel.setIcon(IconImages.CLOVER);
                finishedLabel.setForeground(CLOVER_COLOR);
                finishedLabel.setText("Finished.");
            }
            mainPanel.add(finishedLabel,BorderLayout.CENTER);            
        } else {        
            final JLabel gameLabel=new JLabel("Game "+duel.getGameNr()+" of "+duel.getConfiguration().getNrOfGames());
            gameLabel.setHorizontalAlignment(JLabel.CENTER);
            gameLabel.setFont(FontsAndBorders.FONT3);
            gameLabel.setForeground(theme.getTextColor());
            mainPanel.add(gameLabel,BorderLayout.CENTER);
        }
        
        final JLabel winLabel=new JLabel(gamesWon+" of "+gamesPlayed+" games won ("+percentage+" %)");
        winLabel.setPreferredSize(new Dimension(0,24));
        winLabel.setHorizontalAlignment(JLabel.CENTER);
        winLabel.setIcon(IconImages.TROPHY);
        winLabel.setForeground(theme.getColor(Theme.COLOR_TEXT_FOREGROUND));
        mainPanel.add(winLabel,BorderLayout.SOUTH);
                
        return mainPanel;
    }
    
    public static void setTitle(final TitleBar titleBar) {
        titleBar.setText(TITLE);
        titleBar.setIcon(null);
    }
    
    public MagicDuel getDuel() {
        
        return duel;
    }
    
    private static final int getPercentage(final int value,final int total) {
        
        return total>0?(value*100)/total:0;
    }
}
