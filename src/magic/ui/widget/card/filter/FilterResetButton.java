package magic.ui.widget.card.filter;

import magic.ui.widget.card.filter.button.FilterPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import magic.translate.UiString;
import magic.ui.MouseHelper;

@SuppressWarnings("serial")
class FilterResetButton extends JButton {

    // translatable strings
    private static final String _S1 = "Reset";

    FilterResetButton(final FilterPanel fbp) {
        super(UiString.get(_S1));
        setFont(getFont().deriveFont(Font.BOLD, 12));
        setForeground(new Color(127, 23, 23));
        addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MouseHelper.showBusyCursor(FilterResetButton.this);
                fbp.resetStayOpen();
                MouseHelper.showDefaultCursor(FilterResetButton.this);
            }
        });
    }
}
