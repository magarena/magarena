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
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;
import magic.ui.ICardFilterPanelListener;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class CardFilterPanel extends TexturedPanel implements ActionListener {

    // translatable strings
    private static final String _S1 = "Match any selected";
    private static final String _S2 = "Match all selected";
    private static final String _S3 = "Exclude selected";
    private static final String _S15 = "Reset";
    private static final String _S16 = "Clears all filters";
    private static final String _S17 = "New cards";
    private static final String _S18 = "Playable";
    private static final String _S19 = "Unimplemented";
    private static final String _S25 = "Potential";

    private static final Color TEXT_COLOR = ThemeFactory.getInstance().getCurrentTheme().getTextColor();
    private static final Dimension POPUP_CHECKBOXES_SIZE = new Dimension(200, 150);
    private static final Dimension FILTER_BUTTON_PREFERRED_SIZE = new Dimension(108, 36);
    private static final Dimension FILTER_BUTTON_MINIMUM_SIZE = new Dimension(66, 36);

    private final String[] FILTER_CHOICES = {
        UiString.get(_S1),
        UiString.get(_S2),
        UiString.get(_S3)
    };

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


    private String[] getStatusFilterValues() {
        return listener.isDeckEditor() ? new String[]{UiString.get(_S17)} : new String[]{UiString.get(_S17), UiString.get(_S18), UiString.get(_S19), UiString.get(_S25)};
    }

    private ButtonControlledPopup addFilterPopupPanel(final String title, final String tooltip) {
        final JButton selectButton = new JButton(title);
        selectButton.setToolTipText(tooltip);
        selectButton.setFont(FontsAndBorders.FONT1);
        selectButton.setPreferredSize(FILTER_BUTTON_PREFERRED_SIZE);
        selectButton.setMinimumSize(FILTER_BUTTON_MINIMUM_SIZE);
        add(selectButton);

        final ButtonControlledPopup pop = new ButtonControlledPopup(selectButton, title);
        pop.setLayout(new BoxLayout(pop, BoxLayout.Y_AXIS));
        selectButton.addActionListener(new PopupCloser(pop));
        return pop;
    }

    private ButtonControlledPopup addFilterPopupPanel(final String title) {
        return addFilterPopupPanel(title, null);
    }

    private class PopupCloser implements ActionListener {
        private final ButtonControlledPopup p;

        public PopupCloser(final ButtonControlledPopup p) {
            this.p = p;
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            // close all other popups except for our own button's
//            if (!p.equals(cubePopup)) {
//                cubePopup.hidePopup();
//            }
//            if (!p.equals(typePopup)) {
//                typePopup.hidePopup();
//            }
//            if (!p.equals(colorPopup)) {
//                colorPopup.hidePopup();
//            }
//            if (!p.equals(costPopup)) {
//                costPopup.hidePopup();
//            }
//            if (!p.equals(subtypePopup)) {
//                subtypePopup.hidePopup();
//            }
//            if (!p.equals(rarityPopup)) {
//                rarityPopup.hidePopup();
//            }
        }
    }

    private void populateCheckboxPopup(
        final String migLayout,
        final ButtonControlledPopup popup,
        final Object[] checkboxValues,
        final JCheckBox[] newCheckboxes,
        final JRadioButton[] newFilterButtons,
        final boolean hideAND
    ) {

        final JPanel checkboxesPanel = new JPanel(new MigLayout(migLayout));
        checkboxesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkboxesPanel.setOpaque(false);

        for (int i=0;i<checkboxValues.length;i++) {
            newCheckboxes[i]=new JCheckBox(checkboxValues[i].toString().replace('_', ' '));
            newCheckboxes[i].addActionListener(this);
            newCheckboxes[i].setOpaque(false);
            newCheckboxes[i].setForeground(TEXT_COLOR);
            newCheckboxes[i].setFocusPainted(true);
            newCheckboxes[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            checkboxesPanel.add(newCheckboxes[i]);
        }

        final JScrollPane scrollPane = new JScrollPane(checkboxesPanel);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setBorder(FontsAndBorders.DOWN_BORDER);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setPreferredSize(POPUP_CHECKBOXES_SIZE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        popup.add(scrollPane);

        final ButtonGroup bg = new ButtonGroup();
        for (int i = 0; i < FILTER_CHOICES.length; i++) {
            newFilterButtons[i] = new JRadioButton(FILTER_CHOICES[i]);
            newFilterButtons[i].addActionListener(this);
            newFilterButtons[i].setOpaque(false);
            newFilterButtons[i].setForeground(TEXT_COLOR);
            newFilterButtons[i].setFocusPainted(true);
            newFilterButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            if (i == 0) {
                newFilterButtons[0].setSelected(true);
            } else if (i == 1) {
                newFilterButtons[1].setVisible(!hideAND);
            }
            bg.add(newFilterButtons[i]);
            popup.add(newFilterButtons[i]);
        }

    }

    private void populateCheckboxPopup(final ButtonControlledPopup popup, final Object[] checkboxValues, final JCheckBox[] newCheckboxes, final JRadioButton[] newFilterButtons, final boolean hideAND) {
        populateCheckboxPopup("flowy, insets 2", popup, checkboxValues, newCheckboxes, newFilterButtons, hideAND);
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

    private boolean filterCheckboxes(final MagicCardDefinition cardDefinition, final JCheckBox[] checkboxes, final JRadioButton[] filterButtons, final CardChecker func) {
        boolean somethingSelected = false;
        boolean resultOR = false;
        boolean resultAND = true;

        for (int i=0; i < checkboxes.length; i++) {
            if (checkboxes[i].isSelected()) {
                somethingSelected = true;
                if (func.checkCard(cardDefinition, i)) {
                    resultOR = true;
                } else {
                    resultAND = false;
                }
            }
        }
        if (filterButtons[2].isSelected()) {
            // exclude selected
            return !resultOR;
        }
        // otherwise return OR or AND result
        return !somethingSelected || ((filterButtons[0].isSelected() && resultOR) || (filterButtons[1].isSelected() && resultAND));
    }

    private interface CardChecker {
        public boolean checkCard(MagicCardDefinition card, int i);
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

        closePopups();

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

    private void unselectFilterSet(final JCheckBox[] boxes, final JRadioButton[] filterButtons) {
        // uncheck all checkboxes
        for (JCheckBox checkbox : boxes) {
            checkbox.setSelected(false);
        }
        // reset to first option
        filterButtons[0].setSelected(true);
    }

    public void closePopups() {
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
