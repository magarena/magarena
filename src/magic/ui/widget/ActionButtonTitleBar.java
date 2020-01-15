package magic.ui.widget;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ActionButtonTitleBar extends TitleBar {

    private final JPanel actionsPanel = new JPanel();

    public ActionButtonTitleBar(String caption, List<JButton> actionButtons) {

        super(caption);

        setPreferredSize(new Dimension(getPreferredSize().width, 26));
        setPreferredSize(getPreferredSize());

        actionsPanel.setOpaque(false);

        actionsPanel.setLayout(new MigLayout("insets 0, gap 12", "", "grow, fill"));
        for (JButton btn : actionButtons) {
            actionsPanel.add(btn, "w 16!, h 16!");
        }

        add(actionsPanel, "alignx right, hidemode 3");

    }

    public void setActionsVisible(boolean b) {
        actionsPanel.setVisible(b);
    }

}
