package magic.ui.explorer.filter;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import magic.ui.MagicUI;

@SuppressWarnings("serial")
class FilterResetButton extends JButton {

    FilterResetButton(final FilterButtonPanel fbp) {
        super("Reset");
        setFont(getFont().deriveFont(Font.BOLD, 12));
        setForeground(new Color(127, 23, 23));
        addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MagicUI.showBusyCursorFor(FilterResetButton.this);
                fbp.resetStayOpen();
                MagicUI.showDefaultCursorFor(FilterResetButton.this);
            }
        });
    }
}
