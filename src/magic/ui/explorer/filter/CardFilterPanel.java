package magic.ui.explorer.filter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;
import magic.ui.ICardFilterPanelListener;
import magic.ui.MagicUI;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class CardFilterPanel extends TexturedPanel 
        implements IFilterListener {

    // translatable strings
    private static final String _S15 = "Reset";
    private static final String _S16 = "Clears all filters";

    private static final Dimension FILTER_BUTTON_PREFERRED_SIZE = new Dimension(108, 36);
    private static final Dimension FILTER_BUTTON_MINIMUM_SIZE = new Dimension(66, 36);

    private final ICardFilterPanelListener listener;

    private final List<FilterButtonPanel> filterButtons = new ArrayList<>();
    private final JButton resetButton;

    private int totalFilteredCards = 0;
    private int playableCards = 0;
    private int missingCards = 0;

    private static List<MagicCardDefinition> cardPool;

    private class ResetButton extends JButton {
        ResetButton() {
            super(UiString.get(_S15));
            setToolTipText(UiString.get(_S16));
            setFont(new Font("dialog", Font.BOLD, 12));
            setForeground(new Color(127, 23, 23));
            setPreferredSize(FILTER_BUTTON_PREFERRED_SIZE);
            setMinimumSize(FILTER_BUTTON_MINIMUM_SIZE);
            addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MagicUI.showBusyCursorFor(resetButton);
                    resetFilters();
                    MagicUI.showDefaultCursorFor(resetButton);
                }
            });
        }
    }

    public CardFilterPanel(final ICardFilterPanelListener aListener) {

        this.listener = aListener;
        this.resetButton = new ResetButton();

        cardPool = isDeckEditor()
            ? CardDefinitions.getDefaultPlayableCardDefs()
            : CardDefinitions.getAllCards();

        createFilterButtons();
        refreshLayout();
    }

    private boolean isDeckEditor() {
        return this.listener.isDeckEditor();
    }

    private void refreshLayout() {
        MigLayout layout = new MigLayout();
        layout.setLayoutConstraints("flowy, wrap 2, gap 4");
        layout.setColumnConstraints("fill, 66:108");
        layout.setRowConstraints("fill, 36");
        setLayout(layout);
        // layout buttons in columns over two rows.
        for (FilterButtonPanel fb : filterButtons) {
            add(fb);
        }
        add(resetButton);
    }

    private void createFilterButtons() {
        filterButtons.add(new CubeFBP(this));
        filterButtons.add(new FormatFBP(this));
        filterButtons.add(new SetsFBP(this));
        filterButtons.add(new TypeFBP(this, isDeckEditor()));
        filterButtons.add(new SubtypeFBP(this));
        filterButtons.add(new ColorFBP(this));
        filterButtons.add(new ManaCostFBP(this));
        filterButtons.add(new RarityFBP(this));
        filterButtons.add(new StatusFBP(this, isDeckEditor()));
        filterButtons.add(new TextSearchFBP(this));
        filterButtons.add(showUnsupportedFilter() ? new UnsupportedFBP(this) : new EmptyFBP());
    }

    private boolean showUnsupportedFilter() {
        return !isDeckEditor() && GeneralConfig.getInstance().showMissingCardData();
    }

    private boolean isCardfiltered(final MagicCardDefinition aCard) {
        if (aCard.isOverlay()) {
            return false;
        } else {
            return filterButtons.stream().allMatch((filter) -> filter.matches(aCard));
        }
    }

    private Stream<MagicCardDefinition> getFilteredStream() {
        return cardPool.stream()
            .filter(isDeckEditor()
                ? card -> !card.isHidden() && isCardfiltered(card)
                : this::isCardfiltered
            );
    }

    public List<MagicCardDefinition> getFilteredCards() {
        final List<MagicCardDefinition> filteredList = getFilteredStream().collect(Collectors.toList());
        updateCardTotals(filteredList);
        return filteredList;
    }

    private void updateCardTotals(final List<MagicCardDefinition> cards) {

        totalFilteredCards = cards.size();

        missingCards = (int) cards.stream()
            .filter(card -> card.isPlayable() && card.isInvalid())
            .count();

        playableCards = (int) cards.stream()
            .filter(card -> card.isPlayable() && !card.isInvalid())
            .count();
    }

    private void resetFilters() {
        for (FilterButtonPanel filter : filterButtons) {
            filter.reset();
        }
        filterChanged();
    }

    public int getPlayableCardCount() {
        return playableCards;
    }

    public int getMissingCardCount() {
        return missingCards;
    }

    public int getTotalCardCount() {
        final int total = playableCards + missingCards;
        return total == 0 ? totalFilteredCards : total;
    }

    @Override
    public void filterChanged() {
        listener.refreshTable();
    }
}
