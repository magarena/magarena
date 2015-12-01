package magic.ui.duel.viewer;

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
import magic.data.MagicIcon;
import magic.model.DuelPlayerConfig;
import magic.model.MagicColor;
import magic.model.MagicDeck;
import magic.ui.MagicImages;
import magic.translate.UiString;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.TitleBar;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckStatisticsViewer extends TexturedPanel implements ChangeListener {

    // translatable strings
    private static final String _S1 = "Deck Statistics";
    private static final String _S2 = "Deck Statistics : %d cards";
    private static final String _S3 = "Mono : %d  Multi : %d  Colorless : %d";
    private static final String _S4 = "Cards : %d  Monocolor : %d  Lands : %d";

    public static final Dimension PREFERRED_SIZE = new Dimension(300, 190);

    private final TitleBar titleBar;
    private final JPanel topPanel;
    private final JPanel linesPanel;
    private final List<JLabel> lines;
    private final JLabel[] curveLabels;
    private final Color textColor;

    public DeckStatisticsViewer() {

        textColor=ThemeFactory.getInstance().getCurrentTheme().getTextColor();

        setPreferredSize(PREFERRED_SIZE);
        setBorder(FontsAndBorders.UP_BORDER);
        setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);

        setLayout(new BorderLayout());

        titleBar=new TitleBar(UiString.get(_S1));
        add(titleBar,BorderLayout.NORTH);

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(FontsAndBorders.BLACK_BORDER_2);
        mainPanel.setOpaque(false);
        add(mainPanel,BorderLayout.CENTER);

        topPanel=new JPanel();
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
        for (int index=0; index < CardStatistics.MANA_CURVE_SIZE; index++) {
            final MagicIcon manaSymbol = CardStatistics.MANA_CURVE_ICONS.get(index);
            final JLabel label = new JLabel(MagicImages.getIcon(manaSymbol));
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

        lines=new ArrayList<>();
    }

    private void refreshCardTypeTotals(final CardStatistics statistics) {
        topPanel.removeAll();
        topPanel.setLayout(new MigLayout("insets 2, gap 6 0, wrap 2, flowy, center"));
        for (int index = 0; index < CardStatistics.NR_OF_TYPES; index++) {
            final int total = statistics.totalTypes[index];
            // card count
            final JLabel totalLabel = new JLabel(Integer.toString(total));
            totalLabel.setIcon(MagicImages.getIcon(CardStatistics.TYPE_ICONS.get(index)));
            totalLabel.setToolTipText(CardStatistics.TYPE_NAMES.get(index));
            totalLabel.setIconTextGap(4);
            topPanel.add(totalLabel, "w 35!");
            // card percentage
            final int percentage = (int)Math.round(((double)total / statistics.totalCards) * 100);
            final JLabel percentLabel = new JLabel(Integer.toString(percentage) + "%");
            percentLabel.setFont(FontsAndBorders.FONT0);
            topPanel.add(percentLabel, "h 12!, center, top");
        }
        topPanel.revalidate();
    }

    public void setDeck(final MagicDeck deck) {

        final CardStatistics statistics = new CardStatistics(deck);
        titleBar.setText(UiString.get(_S2, statistics.totalCards));

        refreshCardTypeTotals(statistics);

        lines.clear();
        final JLabel allLabel = new JLabel(UiString.get(_S3,
                statistics.monoColor,
                statistics.multiColor,
                statistics.colorless)
        );

        allLabel.setForeground(textColor);
        lines.add(allLabel);

        for (int i = 0; i < statistics.colorCount.length; i++) {
            if (statistics.colorCount[i] > 0) {
                final MagicColor color = MagicColor.values()[i];
                final JLabel label=new JLabel(MagicImages.getIcon(color.getManaType()));
                label.setForeground(textColor);
                label.setHorizontalAlignment(JLabel.LEFT);
                label.setIconTextGap(5);
                label.setText(UiString.get(_S4,
                        statistics.colorCount[i],
                        statistics.colorMono[i],
                        statistics.colorLands[i])
                );
                lines.add(label);
            }
        }

        for (int index=0;index<CardStatistics.MANA_CURVE_SIZE;index++) {
            curveLabels[index].setText(Integer.toString(statistics.manaCurve[index]));
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
    public void stateChanged(final ChangeEvent event) {
        setDeck(((DuelPlayerConfig)event.getSource()).getDeck());
    }
}
