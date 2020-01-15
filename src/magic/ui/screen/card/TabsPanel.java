package magic.ui.screen.card;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;

import magic.ui.screen.deck.editor.ViewToggleButton;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class TabsPanel extends TexturedPanel {

    private static final Color BACKGROUND_COLOR =
        MagicStyle.getTranslucentColor(Color.DARK_GRAY, 230);

    private final ButtonGroup toggleGroup = new ButtonGroup();
    private final Map<String, JToggleButton> toggleButtons = new LinkedHashMap<>();

    public TabsPanel() {
        setBackground(BACKGROUND_COLOR);
    }

    public void refreshLayout() {
        removeAll();
        setLayout(new MigLayout("insets 2 0 0 6, aligny top"));
        for (JToggleButton btn : toggleButtons.values()) {
            add(btn, "hidemode 3");
        }
        revalidate();
    }

    public JToggleButton addToggleButton(String text, AbstractAction action) {
        if (!toggleButtons.containsKey(text)) {
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

    public void setSelectedToggleButton(String aViewName) {
        toggleButtons.get(aViewName).setSelected(true);
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        if (toggleButtons != null) {
            toggleButtons.forEach((k, v) -> v.setFont(font));
        }
    }

    int getTabsCount() {
        return toggleButtons.size();
    }

}
