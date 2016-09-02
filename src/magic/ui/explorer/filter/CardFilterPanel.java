package magic.ui.explorer.filter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private final MigLayout layout = new MigLayout();
    private final ICardFilterPanelListener listener;

    private final FilterButtonPanel formatsFilter;
    private final FilterButtonPanel cubeFilter;
    private final FilterButtonPanel setsFilter;
    private final FilterButtonPanel typeFilter;
    private final FilterButtonPanel subtypeFilter;
    private final FilterButtonPanel colorFilter;
    private final FilterButtonPanel costFilter;
    private final FilterButtonPanel rarityFilter;
    private final FilterButtonPanel statusFilter;
    private final FilterButtonPanel textFilter;
    private final FilterButtonPanel unsupportedFilter;

    private JButton resetButton;

    private int totalFilteredCards = 0;
    private int playableCards = 0;
    private int missingCards = 0;

    private static List<MagicCardDefinition> cardPool;

    private boolean disableUpdate; // so when we change several filters, it doesn't update until the end

    public CardFilterPanel(final ICardFilterPanelListener aListener) {

        listener = aListener;

        cardPool = listener.isDeckEditor()
            ? CardDefinitions.getDefaultPlayableCardDefs()
            : CardDefinitions.getAllCards();

        disableUpdate = false;

        // filter buttons
        cubeFilter = new CubeFBP(this);
        formatsFilter = new FormatFBP(this);
        setsFilter = new SetsFBP(this);
        typeFilter = new TypeFBP(this, aListener.isDeckEditor());
        subtypeFilter = new SubtypeFBP(this);
        colorFilter = new ColorFBP(this);
        costFilter = new ManaCostFBP(this);
        rarityFilter = new RarityFBP(this);
        statusFilter = new StatusFBP(this, aListener.isDeckEditor());
        textFilter = new TextSearchFBP(aListener);
        unsupportedFilter = showUnsupportedFilter() ? new UnsupportedFBP(this) : new EmptyFBP();

        // layout filter buttons.
        layout.setLayoutConstraints("flowy, wrap 2, gap 4");
        layout.setColumnConstraints("fill, 66:108");
        layout.setRowConstraints("fill, 36");
        setLayout(layout);
        add(cubeFilter);
        add(formatsFilter);
        add(setsFilter);
        add(typeFilter);
        add(subtypeFilter);
        add(colorFilter);
        add(costFilter);
        add(rarityFilter);
        add(statusFilter);
        add(textFilter);
        add(unsupportedFilter);

        addResetButton();
    }

    private boolean showUnsupportedFilter() {
        return !listener.isDeckEditor() && GeneralConfig.getInstance().showMissingCardData();
    }

    private boolean isCardfiltered(final MagicCardDefinition cardDefinition) {
        // never show overlay cards
        if (cardDefinition.isOverlay()) {
            return false;
        }

        if (textFilter.doesNotInclude(cardDefinition)) {
            return false;
        }

        if (cubeFilter.doesNotInclude(cardDefinition)) {
            return false;
        }

        if (formatsFilter.doesNotInclude(cardDefinition)) {
            return false;
        }

        if (setsFilter.doesNotInclude(cardDefinition)) {
            return false;
        }

        if (typeFilter.doesNotInclude(cardDefinition)) {
            return false;
        }

        if (subtypeFilter.doesNotInclude(cardDefinition)) {
            return false;
        }

        if (colorFilter.doesNotInclude(cardDefinition)) {
            return false;
        }

        if (costFilter.doesNotInclude(cardDefinition)) {
            return false;
        }

        if (rarityFilter.doesNotInclude(cardDefinition)) {
            return false;
        }

        if (statusFilter.doesNotInclude(cardDefinition)) {
            return false;
        }

        if (unsupportedFilter.doesNotInclude(cardDefinition)) {
            return false;
        }

        return true;
    }

    private Stream<MagicCardDefinition> getFilteredStream() {
        return cardPool.stream().filter(listener.isDeckEditor() ? card -> !card.isHidden() && isCardfiltered(card) : this::isCardfiltered);
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

    public void resetFilters() {
        disableUpdate = true; // ignore any events caused by resetting filters

        cubeFilter.reset();
        formatsFilter.reset();
        setsFilter.reset();
        typeFilter.reset();
        subtypeFilter.reset();
        colorFilter.reset();
        costFilter.reset();
        rarityFilter.reset();
        statusFilter.reset();
        textFilter.reset();
        unsupportedFilter.reset();

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

    private void addResetButton() {
        resetButton = new JButton(UiString.get(_S15));
        resetButton.setToolTipText(UiString.get(_S16));
        resetButton.setFont(new Font("dialog", Font.BOLD, 12));
        resetButton.setForeground(new Color(127, 23 ,23));
        resetButton.addActionListener(this);
        resetButton.setPreferredSize(FILTER_BUTTON_PREFERRED_SIZE);
        resetButton.setMinimumSize(FILTER_BUTTON_MINIMUM_SIZE);
        add(resetButton);
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
