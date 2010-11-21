package magic.ui.widget;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import magic.model.MagicManaCost;

public class CostPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public CostPanel(final MagicManaCost cost) {

		setOpaque(false);
		setLayout(new BorderLayout());
		
		final JPanel centerPanel=new JPanel();
		centerPanel.setOpaque(false);
		centerPanel.setLayout(new GridLayout(1,6));
		final JLabel manaLabels[]=new JLabel[5];
		for (int i=0;i<manaLabels.length;i++) {
			
			manaLabels[i]=new JLabel();
			manaLabels[i].setPreferredSize(new Dimension(17,0));
			manaLabels[i].setHorizontalAlignment(JLabel.CENTER);
			centerPanel.add(manaLabels[i]);
		}			
		if (cost!=null) {
			int index=0;
			for (final ImageIcon icon : cost.getIcons()) {
				
				manaLabels[index++].setIcon(icon);
				if (index==manaLabels.length) {
					break;
				}
			}
		}
		add(centerPanel,BorderLayout.CENTER);
	}
}