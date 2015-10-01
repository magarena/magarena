package magic.ui.duel.textmode;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import magic.model.MagicCardList;
import magic.ui.SwingGameController;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.TabSelector;
import magic.ui.widget.TitleBar;

class HandGraveyardExileViewer extends JPanel implements ChangeListener {
    private static final long serialVersionUID = 1L;

    private final CardListViewer[] viewers;
    private final JPanel cardPanel;
    private final CardLayout cardLayout;
    private final TitleBar titleBar;
    private final TabSelector tabSelector;
    private final MagicCardList other = new MagicCardList();

    HandGraveyardExileViewer(final SwingGameController controller) {

        viewers = new CardListViewer[]{
            new HandViewer(controller),
            new GraveyardViewer(controller, false),
            new GraveyardViewer(controller, true),
            new ExileViewer(controller, false),
            new ExileViewer(controller, true),
            new OtherViewer(other, controller)
        };

        setOpaque(false);
        setLayout(new BorderLayout());

        titleBar = new TitleBar("");
        add(titleBar,BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        for (int index = 0; index < viewers.length; index++) {
            cardPanel.add(viewers[index], String.valueOf(index));
        }
        add(cardPanel,BorderLayout.CENTER);

        tabSelector = new TabSelector(this, false);
        for (final CardListViewer viewer : viewers) {
            tabSelector.addTab(MagicStyle.getTheme().getIcon(viewer.getIcon()), viewer.getTitle());
        }
        titleBar.add(tabSelector,BorderLayout.EAST);
    }

    void update() {
        for (final CardListViewer viewer : viewers) {
            viewer.update();
        }
    }
    
    void showCards(final MagicCardList cards) {
        other.clear();
        other.addAll(cards);
        viewers[5].update();
    }

    void setSelectedTab(final int selectedTab) {
        if (selectedTab>=0) {
            tabSelector.setSelectedTab(selectedTab);
        }
    }

    @Override
    public void stateChanged(final ChangeEvent e) {
        final int selectedTab=tabSelector.getSelectedTab();
        cardLayout.show(cardPanel,Integer.toString(selectedTab));
        titleBar.setText(viewers[selectedTab].getTitle());
    }
}
