package magic.ui.screen.duel.player.avatar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import magic.ui.FontsAndBorders;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class AvatarListCellRenderer extends JLabel implements ListCellRenderer<AvatarImageSet> {

    AvatarListCellRenderer() {
        setOpaque(false);
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends AvatarImageSet> list,
            AvatarImageSet value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        final Color foreColor = isSelected ? MagicStyle.getRolloverColor() : Color.WHITE;

        final JLabel setNameLabel = new JLabel(value.getName());
        setNameLabel.setFont(FontsAndBorders.FONT2);
        setNameLabel.setForeground(foreColor);
        setNameLabel.setVerticalAlignment(SwingConstants.TOP);

        final JPanel infoPanel = new JPanel(new MigLayout("insets 0, gap 0, flowy"));
        infoPanel.setOpaque(false);
        infoPanel.setForeground(foreColor);
        infoPanel.add(setNameLabel, "w 100%, gapbottom 4");

        final JPanel itemPanel = new JPanel(new MigLayout("insets 0 0 0 6, gap 0"));
        itemPanel.setPreferredSize(new Dimension(0, 70));
        itemPanel.setOpaque(false);
        itemPanel.setForeground(foreColor);
        itemPanel.setBorder(isSelected ? BorderFactory.createLineBorder(MagicStyle.getRolloverColor(), 1) : null);
        itemPanel.add(new JLabel(value.getSampleImage()), "w 70!, h 70!");
        itemPanel.add(infoPanel, "w 100%");
        return itemPanel;

    }
}
