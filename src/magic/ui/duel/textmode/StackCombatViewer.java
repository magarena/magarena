package magic.ui.duel.textmode;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import magic.ui.duel.SwingGameController;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.TabSelector;
import magic.ui.widget.TitleBar;

class StackCombatViewer extends JPanel implements ChangeListener {

    private static final long serialVersionUID = 1L;

    private final CombatViewer combatViewer;
    private final JPanel cardPanel;
    private final CardLayout cardLayout;
    private final TitleBar titleBar;
    private final TabSelector tabSelector;

    StackCombatViewer(final SwingGameController controller) {

        combatViewer=new CombatViewer(controller);

        setOpaque(false);
        setLayout(new BorderLayout());

        cardLayout=new CardLayout();
        cardPanel=new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        cardPanel.add(combatViewer,"0");
        add(cardPanel,BorderLayout.CENTER);

        titleBar=new TitleBar("");
        add(titleBar,BorderLayout.NORTH);

        tabSelector=new TabSelector(this,false);
        tabSelector.addTab(MagicStyle.getTheme().getIcon(Theme.ICON_SMALL_COMBAT),"Combat");
        titleBar.add(tabSelector,BorderLayout.EAST);
    }

    void update() {
        combatViewer.update();
    }

    @Override
    public void stateChanged(final ChangeEvent event) {
        titleBar.setText(combatViewer.getTitle());
    }

}
