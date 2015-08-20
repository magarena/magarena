package magic.ui.widget;

import java.awt.Dimension;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class MenuedTitleBar extends TitleBar {

    public MenuedTitleBar(String text, final JPopupMenu aMenu) {        
        super(text);
        setPreferredSize(new Dimension(getPreferredSize().width, 26));
        setPreferredSize(getPreferredSize());
        add(new MenuIconLabel(aMenu), "alignx right");
    }

}
