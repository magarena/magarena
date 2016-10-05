package magic.ui.screen.menu;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import magic.translate.UiString;
import magic.ui.screen.widget.MenuPanel;
import magic.ui.widget.KeysStripPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class MenuScreenContentPanel extends JPanel {

    private final MenuPanel mp;

    public MenuScreenContentPanel(String title, boolean showKeyStripPanel) {

        mp = new MenuPanel(UiString.get(title));
        
        final MigLayout layout = new MigLayout();
        layout.setLayoutConstraints("insets 0, gap 0, flowy");
        layout.setRowConstraints("[30!][100%, center][30!, bottom]");
        layout.setColumnConstraints("[center, fill, grow]");
        setLayout(layout);
        
        add(mp, "cell 0 1");
        if (showKeyStripPanel) {
            add(new KeysStripPanel());
        }

        setOpaque(false);
    }

    protected void addMenuItem(String name, Runnable action) {
        mp.addMenuItem(UiString.get(name), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    protected void addSpace() {
        mp.addBlankItem();
    }
    
    protected void refreshMenuLayout() {
        mp.refreshLayout();
    }


}
