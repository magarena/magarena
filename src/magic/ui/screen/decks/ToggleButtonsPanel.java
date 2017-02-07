package magic.ui.screen.decks;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import magic.ui.screen.deck.editor.*;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ToggleButtonsPanel extends TexturedPanel {

    private static final Color BACKGROUND_COLOR = MagicStyle.getTranslucentColor(Color.DARK_GRAY, 230);

    private final ButtonGroup toggleGroup = new ButtonGroup();
    private final Map<String, JToggleButton> toggleButtons = new LinkedHashMap<>();

    ToggleButtonsPanel() {
        setBackground(BACKGROUND_COLOR);
    }

    void refreshLayout() {
        removeAll();
        setLayout(new MigLayout("insets 2 0 0 6, aligny top"));
        for (JToggleButton btn : toggleButtons.values()) {
            add(btn, "hidemode 3");
        }
        revalidate();
    }

    final JToggleButton addToggleButton(String text, AbstractAction action) {
        if (toggleButtons.containsKey(text) == false) {
            toggleButtons.put(text, getToggleButton(text, action));
        }
        return toggleButtons.get(text);
    }

    private JToggleButton getToggleButton(String text, AbstractAction action) {
        final JToggleButton btn = new ViewToggleButton(text);
        if (action != null) {
            btn.addActionListener(action);
        }
        toggleGroup.add(btn);
        return btn;
    }

    void setSelectedToggleButton(String aViewName) {
        toggleButtons.get(aViewName).setSelected(true);
    }

}
