package magic.ui.widget;

import java.awt.Dimension;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class MenuedTitleBar extends TitleBar {

    private final MenuIconLabel iconLabel = new MenuIconLabel();

    public MenuedTitleBar(String text, final JPopupMenu aMenu) {
        super(text);
        setPreferredSize(new Dimension(getPreferredSize().width, 26));
        setPreferredSize(getPreferredSize());
        iconLabel.setPopupMenu(aMenu);
        add(iconLabel, "alignx right");
    }

    public void setPopupMenu(JPopupMenu aMenu) {
        iconLabel.setPopupMenu(aMenu);
    }

}
