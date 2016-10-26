package magic.ui.widget.about;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import magic.translate.StringContext;
import magic.translate.MText;
import magic.ui.MagicImages;
import magic.ui.helpers.MouseHelper;
import magic.ui.ScreenController;
import magic.ui.helpers.WidgetHelper;

@SuppressWarnings("serial")
class AboutButton extends JButton {
    
    // translatable strings
    @StringContext(eg = "show Magarena About dialog.")
    private static final String _S1 = "About...";

    private final static ImageIcon M_ICON = new ImageIcon(MagicImages.MENU_LOGO);

    AboutButton() {
        super(M_ICON);
        setDefaultProperties();
        setDefaultAction();
    }

    private void setDefaultAction() {
        addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScreenController.showAboutScreen();
            }
        });
    }

    private void setDefaultProperties() {
        setToolTipText(MText.get(_S1));
        MouseHelper.showHandCursor(this);
        WidgetHelper.setTransparent(this);
    }
}
