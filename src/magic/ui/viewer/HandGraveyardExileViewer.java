package magic.ui.viewer;

import magic.model.MagicCardList;
import magic.ui.GameController;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.TabSelector;
import magic.ui.widget.TitleBar;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;
import java.awt.CardLayout;

public class HandGraveyardExileViewer extends JPanel implements ChangeListener {
    private static final long serialVersionUID = 1L;

    private final CardListViewer[] viewers;
    private final JPanel cardPanel;
    private final CardLayout cardLayout;
    private final TitleBar titleBar;
    private final TabSelector tabSelector;
    private final MagicCardList other = new MagicCardList();

    public HandGraveyardExileViewer(final GameController controller) {
        final Theme theme = ThemeFactory.getInstance().getCurrentTheme();

        viewers = new CardListViewer[]{
            new HandViewer(controller.getViewerInfo(), controller),
            new GraveyardViewer(controller.getViewerInfo(), controller, false),
            new GraveyardViewer(controller.getViewerInfo(), controller, true),
            new ExileViewer(controller.getViewerInfo(), controller, false),
            new ExileViewer(controller.getViewerInfo(), controller, true),
            new OtherViewer(controller.getViewerInfo(), other, controller)
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
            tabSelector.addTab(theme.getIcon(viewer.getIcon()), viewer.getTitle());
        }
        titleBar.add(tabSelector,BorderLayout.EAST);
    }

    public void update() {
        for (final CardListViewer viewer : viewers) {
            viewer.update();
        }
    }
    
    public void showCards(final MagicCardList cards) {
        other.clear();
        other.addAll(cards);
        viewers[5].update();
    }

    public void setSelectedTab(final int selectedTab) {
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
