package magic.ui.duel.viewer;

import magic.ui.SwingGameController;
import magic.ui.theme.Theme;
import magic.ui.widget.TabSelector;
import magic.ui.widget.TitleBar;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import magic.ui.utility.MagicStyle;

public class StackCombatViewer extends JPanel implements ChangeListener {

    private static final long serialVersionUID = 1L;

    private final CombatViewer combatViewer;
    private final JPanel cardPanel;
    private final CardLayout cardLayout;
    private final TitleBar titleBar;
    private final TabSelector tabSelector;

    public StackCombatViewer(final SwingGameController controller) {

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

    public void update() {
        combatViewer.update();
    }

    @Override
    public void stateChanged(final ChangeEvent event) {
        titleBar.setText(combatViewer.getTitle());
    }

}
