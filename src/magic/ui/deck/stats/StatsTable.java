package magic.ui.deck.stats;

import java.awt.Color;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.data.CardStatistics;
import magic.data.MagicIcon;
import magic.model.MagicColor;
import magic.model.MagicManaType;
import magic.ui.MagicImages;
import magic.ui.utility.GraphicsUtils;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class StatsTable extends JPanel {

    private static final ImageIcon[] manaIconOn = new ImageIcon[MagicColor.values().length];
    private static final ImageIcon[] manaIconOff = new ImageIcon[MagicColor.values().length];
    private static final Map<MagicIcon, ImageIcon> typeIconOff = new HashMap<>();
    static {
        setManaIcons();
        setTypeOffIcons();
    }

    StatsTable() {
        setOpaque(false);
        setLayout(new MigLayout("flowy, insets 0 2 2 2, gap 0"));
    }

    private static void setManaIcons() {
        for (MagicColor color : MagicColor.values()) {
            final ImageIcon colorIcon = MagicImages.getIcon(color.getManaType());
            manaIconOn[color.ordinal()] = colorIcon;
            final Image gsImage =  GraphicsUtils.getGreyScaleImage(colorIcon.getImage());
            manaIconOff[color.ordinal()] = new ImageIcon(GraphicsUtils.getTranslucentImage(gsImage, 0.3f));
        }
    }

    private static void setTypeOffIcons() {
        for (MagicIcon icon : MagicIcon.TYPE_ICONS) {
            final ImageIcon colorIcon = MagicImages.getIcon(icon);
            final Image fadedImage = GraphicsUtils.getTranslucentImage(colorIcon.getImage(), 0.2f);
            typeIconOff.put(icon, new ImageIcon(fadedImage));
        }
    }

    private JLabel getLabel(String text, boolean isEnabled) {
        JLabel lbl = new JLabel(isEnabled ? text : "—");
        lbl.setForeground(!isEnabled ? Color.LIGHT_GRAY : lbl.getForeground());
        return lbl;
    }

    private JLabel getValueLabel(int value, boolean isEnabled) {
        JLabel lbl = getLabel(Integer.toString(value), isEnabled);
        lbl.setForeground(value == 0 && isEnabled ? Color.GRAY : lbl.getForeground());
        return lbl;
    }

    private JLabel getValueLabel(int value) {
        return getValueLabel(value, value > 0);
    }

    private JLabel getPercentageLabel(CardStatistics stats, int total) {
        final int percentage = (int) Math.round(((double) total / stats.totalCards) * 100);
        JLabel lbl = getValueLabel(percentage, percentage > 0);
        lbl.setFont(lbl.getFont().deriveFont(10f));
        return lbl;
    }

    private JLabel getHeadingLabel(MagicIcon icon, String tooltip, int value) {
        JLabel lbl = new JLabel(value == 0 ? typeIconOff.get(icon) : MagicImages.getIcon(icon));
        lbl.setToolTipText(tooltip);
        return lbl;
    }

    private JLabel getHeadingLabel(MagicManaType mana, String tooltip) {
        JLabel lbl = new JLabel(MagicImages.getIcon(mana));
        lbl.setToolTipText(tooltip);
        return lbl;
    }

    private JLabel getHeadingLabel(String text, String tooltip) {
        JLabel lbl = new JLabel(text);
        lbl.setToolTipText(tooltip);
        return lbl;
    }

    void setStats(CardStatistics stats) {

        final MigLayout mig = new MigLayout("wrap 12, gapx 5");
        mig.setColumnConstraints("[center][20!, center]");
        mig.setRowConstraints("[][]0[]6[][]");

        final JPanel panel = new JPanel(mig);
        panel.setOpaque(false);

        // headings
        panel.add(new JLabel());
        panel.add(getHeadingLabel("Σ", "Total cards"));
        panel.add(getHeadingLabel(MagicIcon.LAND, "Land", stats.totalTypes[0]));
        panel.add(getHeadingLabel(MagicIcon.CREATURE, "Creature", stats.totalTypes[1]));
        panel.add(getHeadingLabel(MagicIcon.ARTIFACT, "Artifact", stats.totalTypes[2]));
        panel.add(getHeadingLabel(MagicIcon.ENCHANTMENT, "Enchantment", stats.totalTypes[3]));
        panel.add(getHeadingLabel(MagicIcon.INSTANT, "Instant", stats.totalTypes[4]));
        panel.add(getHeadingLabel(MagicIcon.SORCERY, "Sorcery", stats.totalTypes[5]));
        panel.add(getHeadingLabel(MagicIcon.PLANESWALKER, "Planeswalker", stats.totalTypes[6]));
        panel.add(getHeadingLabel(MagicManaType.Colorless, "Colorless"));
        panel.add(getHeadingLabel("=1", "Mono-color"));
        panel.add(getHeadingLabel(">1", "Multi-color"));

        // totals
        panel.add(new JLabel("Σ"));
        panel.add(getValueLabel(stats.totalCards));
        panel.add(getValueLabel(stats.totalTypes[0]));
        panel.add(getValueLabel(stats.totalTypes[1]));
        panel.add(getValueLabel(stats.totalTypes[2]));
        panel.add(getValueLabel(stats.totalTypes[3]));
        panel.add(getValueLabel(stats.totalTypes[4]));
        panel.add(getValueLabel(stats.totalTypes[5]));
        panel.add(getValueLabel(stats.totalTypes[6]));
        panel.add(getValueLabel(stats.colorless));
        panel.add(getValueLabel(stats.monoColor));
        panel.add(getValueLabel(stats.multiColor));

        // %
        panel.add(new JLabel("%"));
        panel.add(getPercentageLabel(stats, stats.totalCards));
        panel.add(getPercentageLabel(stats, stats.totalTypes[0]));
        panel.add(getPercentageLabel(stats, stats.totalTypes[1]));
        panel.add(getPercentageLabel(stats, stats.totalTypes[2]));
        panel.add(getPercentageLabel(stats, stats.totalTypes[3]));
        panel.add(getPercentageLabel(stats, stats.totalTypes[4]));
        panel.add(getPercentageLabel(stats, stats.totalTypes[5]));
        panel.add(getPercentageLabel(stats, stats.totalTypes[6]));
        panel.add(getPercentageLabel(stats, stats.colorless));
        panel.add(getPercentageLabel(stats, stats.monoColor));
        panel.add(getPercentageLabel(stats, stats.multiColor));

        for (int i = 0; i < stats.colorCount.length; i++) {
            final boolean hasColor = stats.colorCount[i] > 0;
            final JLabel label = new JLabel();
            if (hasColor) {
                label.setIcon(manaIconOn[i]);
            } else {
                label.setIcon(manaIconOff[i]);
                label.setForeground(Color.GRAY);
            }
            panel.add(label);
            panel.add(getValueLabel(stats.colorCount[i], hasColor));
            panel.add(getValueLabel(stats.colorLands[i], hasColor && stats.totalTypes[0] > 0));
            panel.add(getValueLabel(stats.colorCreatures[i], hasColor && stats.totalTypes[1] > 0));
            panel.add(getValueLabel(stats.colorArtifacts[i], hasColor && stats.totalTypes[2] > 0));
            panel.add(getValueLabel(stats.colorEnchantments[i], hasColor && stats.totalTypes[3] > 0));
            panel.add(getValueLabel(stats.colorInstants[i], hasColor && stats.totalTypes[4] > 0));
            panel.add(getValueLabel(stats.colorSorcery[i], hasColor && stats.totalTypes[5] > 0));
            panel.add(getValueLabel(stats.colorPlaneswalkers[i], hasColor && stats.totalTypes[6] > 0));
            panel.add(getLabel("---", false));
            panel.add(getValueLabel(stats.colorMono[i], hasColor));
            panel.add(getLabel("---", false));
        }

        removeAll();
        add(panel, "w 100%");
        revalidate();
    }

}