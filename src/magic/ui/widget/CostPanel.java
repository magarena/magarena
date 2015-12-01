package magic.ui.widget;

import magic.model.MagicManaCost;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import magic.data.MagicIcon;
import magic.ui.MagicImages;

public class CostPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Dimension MANA_ICON_SIZE = new Dimension(17,17);

    public CostPanel(final MagicManaCost cost) {

        setOpaque(false);
        setLayout(new BorderLayout());

        if (cost == null) {
            return;
        }

        final JPanel centerPanel=new JPanel();
        centerPanel.setOpaque(false);

        final SpringLayout springLayout = new SpringLayout();
        centerPanel.setLayout(springLayout);

        final List<MagicIcon> icons = cost.getIcons();

        final JLabel[] manaLabels=new JLabel[icons.size()];
        for (int i=0;i<manaLabels.length;i++) {

            manaLabels[i]=new JLabel();
            manaLabels[i].setPreferredSize(MANA_ICON_SIZE);
            manaLabels[i].setMaximumSize(MANA_ICON_SIZE);
            manaLabels[i].setHorizontalAlignment(JLabel.CENTER);

            if (i > 0) {
                // previous (left) mana touches current one
                springLayout.putConstraint(SpringLayout.EAST, manaLabels[i-1],
                             0, SpringLayout.WEST, manaLabels[i]);
            }
            if (i == manaLabels.length - 1) {
                // last mana touches right side of panel
                springLayout.putConstraint(SpringLayout.EAST, manaLabels[i],
                             0, SpringLayout.EAST, centerPanel);
            }

            centerPanel.add(manaLabels[i]);
        }
        int index=0;
        for (final MagicIcon icon : icons) {
            manaLabels[index++].setIcon(MagicImages.getIcon(icon));
            if (index==manaLabels.length) {
                break;
            }
        }
        add(centerPanel,BorderLayout.CENTER);
    }
}
