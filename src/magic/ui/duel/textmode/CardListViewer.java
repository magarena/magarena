package magic.ui.duel.textmode;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.ui.SwingGameController;
import magic.ui.IChoiceViewer;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;

abstract class CardListViewer extends JPanel implements IChoiceViewer {
    private static final long serialVersionUID = 1L;
    private static final int  LINE_HEIGHT      = 26;

    private final SwingGameController controller;
    private final MagicCardList cardList;
    private final String title;
    private final String icon;
    private final boolean showCost;

    private final JScrollPane scrollPane;
    private final JPanel viewPanel;
    private final Collection<CardButton> buttons;

    CardListViewer(
        final SwingGameController controller,
        final MagicCardList cardList,
        final String title,
        final String icon
    ) {
        this(controller, cardList, title, icon, /* showCost */ true);
    }

    CardListViewer(
        final SwingGameController controller,
        final MagicCardList cardList,
        final String title,
        final String icon,
        final boolean showCost
    ) {
        this.controller = controller;
        this.cardList   = cardList;
        this.title      = title;
        this.icon       = icon;
        this.showCost   = showCost;

        controller.registerChoiceViewer(this);

        setOpaque(false);
        setLayout(new BorderLayout());

        scrollPane = new JScrollPane();
        scrollPane.setBorder(FontsAndBorders.NO_BORDER);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setBlockIncrement(LINE_HEIGHT * 2);
        scrollPane.getVerticalScrollBar().setUnitIncrement(LINE_HEIGHT * 2);
        add(scrollPane,BorderLayout.CENTER);

        viewPanel = new JPanel();
        viewPanel.setOpaque(false);
        viewPanel.setLayout(new BorderLayout());
        scrollPane.getViewport().add(viewPanel);

        buttons=new ArrayList<CardButton>();
    }

    void update() {
        final JPanel cardPanel = new JPanel();
        cardPanel.setBackground(ThemeFactory.getInstance().getCurrentTheme().getColor(Theme.COLOR_VIEWER_BACKGROUND));
        cardPanel.setBorder(FontsAndBorders.BLACK_BORDER);
        cardPanel.setLayout(new GridLayout(cardList.size(),1));

        buttons.clear();
        if (this.cardList.isEmpty()) {
            cardPanel.setPreferredSize(new Dimension(0,6));
        } else {
            for (final MagicCard card : this.cardList) {
                final CardButton button = new CardButton(this.controller, card, LINE_HEIGHT, this.showCost);
                buttons.add(button);
                cardPanel.add(button);
            }
        }
        viewPanel.removeAll();
        viewPanel.add(cardPanel,BorderLayout.NORTH);

        showValidChoices(controller.getValidChoices());
        revalidate();
        repaint();
    }

    @Override
    public void showValidChoices(final Set<?> validChoices) {
        for (final CardButton button : buttons) {
            button.showValidChoices(validChoices);
        }
    }

    String getIcon() {
        return this.icon;
    }

    String getTitle() {
        return this.title;
    }
}
