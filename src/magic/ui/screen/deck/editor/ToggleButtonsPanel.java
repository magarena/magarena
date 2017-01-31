package magic.ui.screen.deck.editor;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ToggleButtonsPanel extends TexturedPanel {

    private final ButtonGroup toggleGroup = new ButtonGroup();
    private final Map<String, JToggleButton> toggleButtons = new LinkedHashMap<>();

    ToggleButtonsPanel() {
        setBackground(DeckActionPanel.BACKGROUND_COLOR);
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
