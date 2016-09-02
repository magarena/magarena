package magic.ui.explorer.filter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JButton;
import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;
import magic.ui.ICardFilterPanelListener;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class CardFilterPanel extends TexturedPanel implements ActionListener {

    // translatable strings
    private static final String _S15 = "Reset";
    private static final String _S16 = "Clears all filters";

    private static final Dimension FILTER_BUTTON_PREFERRED_SIZE = new Dimension(108, 36);
    private static final Dimension FILTER_BUTTON_MINIMUM_SIZE = new Dimension(66, 36);

    private final ICardFilterPanelListener listener;

    private final List<FilterButtonPanel> filterButtons = new ArrayList<>();
    private JButton resetButton;

    private int totalFilteredCards = 0;
    private int playableCards = 0;
    private int missingCards = 0;

    private static List<MagicCardDefinition> cardPool;

    private boolean disableUpdate; // so when we change several filters, it doesn't update until the end

    public CardFilterPanel(final ICardFilterPanelListener aListener) {

        this.listener = aListener;

        cardPool = isDeckEditor()
            ? CardDefinitions.getDefaultPlayableCardDefs()
            : CardDefinitions.getAllCards();

        createFilterButtons(aListener);
        createResetButton();
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

    private void createFilterButtons(ICardFilterPanelListener aListener) {
        filterButtons.add(new CubeFBP(this));
        filterButtons.add(new FormatFBP(this));
        filterButtons.add(new SetsFBP(this));
        filterButtons.add(new TypeFBP(this, isDeckEditor()));
        filterButtons.add(new SubtypeFBP(this));
        filterButtons.add(new ColorFBP(this));
        filterButtons.add(new ManaCostFBP(this));
        filterButtons.add(new RarityFBP(this));
        filterButtons.add(new StatusFBP(this, isDeckEditor()));
        filterButtons.add(new TextSearchFBP(aListener));
        filterButtons.add(showUnsupportedFilter() ? new UnsupportedFBP(this) : new EmptyFBP());
    }

    private boolean showUnsupportedFilter() {
        return !isDeckEditor() && GeneralConfig.getInstance().showMissingCardData();
    }

    private boolean isCardfiltered(final MagicCardDefinition cardDefinition) {

        // never show overlay cards
        if (cardDefinition.isOverlay()) {
            return false;
        }

        for (FilterButtonPanel filter : filterButtons) {
            if (filter.doesNotInclude(cardDefinition)) {
                return false;
            }
        }

        return true;
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
        disableUpdate = true; // ignore any events caused by resetting filters
        for (FilterButtonPanel filter : filterButtons) {
            filter.reset();
        }
        disableUpdate = false;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final Component c = (Component)event.getSource();
        c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (event.getSource().equals(resetButton)) {
            resetFilters();
        }
        if (!disableUpdate) {
            listener.refreshTable();
        }
        c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void createResetButton() {
        resetButton = new JButton(UiString.get(_S15));
        resetButton.setToolTipText(UiString.get(_S16));
        resetButton.setFont(new Font("dialog", Font.BOLD, 12));
        resetButton.setForeground(new Color(127, 23 ,23));
        resetButton.addActionListener(this);
        resetButton.setPreferredSize(FILTER_BUTTON_PREFERRED_SIZE);
        resetButton.setMinimumSize(FILTER_BUTTON_MINIMUM_SIZE);
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

}
