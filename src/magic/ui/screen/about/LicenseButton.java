package magic.ui.screen.about;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import magic.data.MagicIcon;
import magic.translate.MText;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.screen.widget.ActionBarButton;

@SuppressWarnings("serial")
class LicenseButton extends ActionBarButton {

    private static final String _S9 = "Magarena License";
    private static final String _S10 = "This program is free software : you can redistribute it and/or<br>modify it under the terms of the GNU General Public License<br>as published by the Free Software Foundation.";
    private static final String _S11 = "Magarena License";
    private static final String _S12 = "Displays the license details.";

    LicenseButton() {
        super(MagicImages.getIcon(MagicIcon.SCROLL),
                MText.get(_S11),
                MText.get(_S12),
                new AbstractAction() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        ScreenController.showInfoMessage(String.format("<html><b>%s</b><br>%s<html>",
                            MText.get(_S9),
                            MText.get(_S10))
                        );
                    }
                }
        );
    }
}
