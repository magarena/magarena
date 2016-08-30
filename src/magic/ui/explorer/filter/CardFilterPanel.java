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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.data.MagicFormat;
import magic.data.MagicPredefinedFormat;
import magic.data.MagicSetDefinitions;
import magic.data.MagicSets;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicManaCost;
import magic.model.MagicRarity;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.translate.StringContext;
import magic.translate.UiString;
import magic.ui.CardFilterTextField;
import magic.ui.ICardFilterPanelListener;
import magic.ui.MagicImages;
import magic.ui.MagicLogs;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class CardFilterPanel extends TexturedPanel implements ActionListener {

    // translatable strings
    private static final String _S1 = "Match any selected";
    private static final String _S2 = "Match all selected";
    private static final String _S3 = "Exclude selected";
    private static final String _S4 = "Status";
    @StringContext(eg = "Set filter in Cards Explorer")
    private static final String _S5 = "Set";
    private static final String _S6 = "Cube";
    private static final String _S7 = "Format";
    @StringContext(eg = "Type filter in cards explorer")
    private static final String _S8 = "Type";
    private static final String _S9 = "Search";
    private static final String _S10 = "Searches name, type, subtype and oracle text.";
    private static final String _S11 = "Color";
    private static final String _S12 = "Cost";
    private static final String _S13 = "Subtype";
    private static final String _S14 = "Rarity";
    private static final String _S15 = "Reset";
    private static final String _S16 = "Clears all filters";
    private static final String _S17 = "New cards";
    private static final String _S18 = "Playable";
    private static final String _S19 = "Unimplemented";
    private static final String _S20 = "Token";
    private static final String _S21 = "Transform";
    private static final String _S22 = "Flip";
    private static final String _S23 = "Hidden";
    private static final String _S24 = "Split";
    private static final String _S25 = "Potential";

    private static final String[] COST_VALUES = new String[MagicManaCost.MAXIMUM_MANA_COST + 1];
    static {
        for (int i = 0; i <= MagicManaCost.MAXIMUM_MANA_COST; i++) {
            COST_VALUES[i] = Integer.toString(i);
        }
    }

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

    // formats
    private ButtonControlledPopup formatsPopup;
    private JCheckBox[] formatsCheckBoxes;
    private JRadioButton[] formatsFilterChoices;
    // status
    private ButtonControlledPopup statusPopup;
    private JCheckBox[] statusCheckBoxes;
    private JRadioButton[] statusFilterChoices;
    // unsupported statuses
    private ButtonControlledPopup unsupportedPopup;
    private JCheckBox[] unsupportedCheckBoxes;
    private JRadioButton[] unsupportedFilterChoices;
    // sets
    private ButtonControlledPopup setsPopup;
    public JCheckBox[] setsCheckBoxes;
    private JRadioButton[] setsFilterChoices;
    // cube
    private ButtonControlledPopup cubePopup;
    private JCheckBox[] cubeCheckBoxes;
    private JRadioButton[] cubeFilterChoices;
    //type
    private ButtonControlledPopup typePopup;
    private JCheckBox[] typeCheckBoxes;
    private JRadioButton[] typeFilterChoices;
    // color
    private ButtonControlledPopup colorPopup;
    private JCheckBox[] colorCheckBoxes;
    private JRadioButton[] colorFilterChoices;
    // mana cost
    private ButtonControlledPopup costPopup;
    private JCheckBox[] costCheckBoxes;
    private JRadioButton[] costFilterChoices;
    // sub type
    private ButtonControlledPopup subtypePopup;
    private JCheckBox[] subtypeCheckBoxes;
    private JRadioButton[] subtypeFilterChoices;
    // rarity
    private ButtonControlledPopup rarityPopup;
    private JCheckBox[] rarityCheckBoxes;
    private JRadioButton[] rarityFilterChoices;
    // oracle text
    private ButtonControlledPopup oraclePopup;
    private CardFilterTextField nameTextField = null;
    // ...
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

        layout.setLayoutConstraints("flowy, wrap 2, gap 4");
        setLayout(layout);

        addCubeFilter();
        addFormatsFilter();
        addSetsFilter();
        addCardTypeFilter();
        addCardSubtypeFilter();
        addCardColorFilter();
        addManaCostFilter();
        addCardRarityFilter();
        addStatusFilter();
        addOracleFilter();
        if (!listener.isDeckEditor() && GeneralConfig.getInstance().showMissingCardData()) {
            addUnsupportedFilter();
        } else {
            addDummyFilterButton();
        }
        addResetButton();

    }

    private void addDummyFilterButton() {
        final JButton btn = new JButton();
        btn.setVisible(false);
        btn.setPreferredSize(FILTER_BUTTON_PREFERRED_SIZE);
        btn.setMinimumSize(FILTER_BUTTON_MINIMUM_SIZE);
        add(btn);
    }

    private void addStatusFilter() {
        final String[] filterValues = getStatusFilterValues();
        statusPopup = addFilterPopupPanel(UiString.get(_S4));
        statusCheckBoxes = new JCheckBox[filterValues.length];
        statusFilterChoices = new JRadioButton[FILTER_CHOICES.length];
        populateCheckboxPopup(statusPopup, filterValues, statusCheckBoxes, statusFilterChoices, false);
    }

    private void addUnsupportedFilter() {
        unsupportedPopup = addFilterPopupPanel(UiString.get("Unsupported"));
        unsupportedCheckBoxes = new JCheckBox[MagicCardDefinition.getUnsupportedStatuses().size()];
        unsupportedFilterChoices = new JRadioButton[FILTER_CHOICES.length];
        final String[] filterValues = MagicCardDefinition.getUnsupportedStatuses().toArray(new String[0]);
        populateCheckboxPopup(unsupportedPopup, filterValues, unsupportedCheckBoxes, unsupportedFilterChoices, false);
    }


    private String[] getStatusFilterValues() {
        return listener.isDeckEditor() ? new String[]{UiString.get(_S17)} : new String[]{UiString.get(_S17), UiString.get(_S18), UiString.get(_S19), UiString.get(_S25)};
    }

    private void addSetsFilter() {
        setsPopup = addFilterPopupPanel(UiString.get(_S5));
        setsCheckBoxes = new JCheckBox[MagicSets.values().length];
        setsFilterChoices = new JRadioButton[FILTER_CHOICES.length];
        final String[] filterValues = MagicSetDefinitions.getFilterValues();
        populateCheckboxPopup(setsPopup, filterValues, setsCheckBoxes, setsFilterChoices, false);
    }

    private void addCubeFilter() {
        cubePopup = addFilterPopupPanel(UiString.get(_S6));
        cubeCheckBoxes = new JCheckBox[MagicFormat.getCubeFilterFormats().size()];
        cubeFilterChoices = new JRadioButton[FILTER_CHOICES.length];
        final String[] filterValues = MagicFormat.getCubeFilterLabels();
        populateCheckboxPopup(cubePopup, filterValues, cubeCheckBoxes, cubeFilterChoices, false);
    }

    private void addFormatsFilter() {
        formatsPopup = addFilterPopupPanel(UiString.get(_S7));
        formatsCheckBoxes = new JCheckBox[MagicPredefinedFormat.values().size()];
        formatsFilterChoices = new JRadioButton[FILTER_CHOICES.length];
        final String[] filterValues = MagicPredefinedFormat.getFilterValues();
        populateCheckboxPopup(formatsPopup, filterValues, formatsCheckBoxes, formatsFilterChoices, false);
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
            if (!p.equals(cubePopup)) {
                cubePopup.hidePopup();
            }
            if (!p.equals(typePopup)) {
                typePopup.hidePopup();
            }
            if (!p.equals(colorPopup)) {
                colorPopup.hidePopup();
            }
            if (!p.equals(costPopup)) {
                costPopup.hidePopup();
            }
            if (!p.equals(subtypePopup)) {
                subtypePopup.hidePopup();
            }
            if (!p.equals(rarityPopup)) {
                rarityPopup.hidePopup();
            }
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

        // search text in name, abilities, type, text, etc.
        if (nameTextField != null) {
            if (!nameTextField.getSearchTerms().isEmpty()) {
                for (String searchTerm : nameTextField.getSearchTerms()) {
                    if (!cardDefinition.hasText(searchTerm)) {
                        return false;
                    }
                }
            }
        }

        // cube
        if (!filterCheckboxes(cardDefinition, cubeCheckBoxes, cubeFilterChoices,
            new CardChecker() {
                @Override
                public boolean checkCard(final MagicCardDefinition card, final int i) {
                    final MagicFormat fmt = MagicFormat.getCubeFilterFormats().get(i);
                    return fmt.isCardLegal(card);
                }
            })) {
            return false;
        }

        // format
        if (!filterCheckboxes(cardDefinition, formatsCheckBoxes, formatsFilterChoices,
            new CardChecker() {
                @Override
                public boolean checkCard(final MagicCardDefinition card, final int i) {
                    final MagicFormat fmt = MagicPredefinedFormat.values().get(i);
                    return fmt.isCardLegal(card);
                }
            })) {
            return false;
        }

        // sets
        if (!filterCheckboxes(cardDefinition, setsCheckBoxes, setsFilterChoices,
            new CardChecker() {
                @Override
                public boolean checkCard(final MagicCardDefinition card, final int i) {
                    final MagicSets magicSet  = MagicSets.values()[i];
                    return  MagicSetDefinitions.isCardInSet(card, magicSet);
                }
            })) {
            return false;
        }

        // unsupported statuses
        if (unsupportedPopup != null && !filterCheckboxes(cardDefinition, unsupportedCheckBoxes, unsupportedFilterChoices,
            new CardChecker() {
            @Override
            public boolean checkCard(MagicCardDefinition card, int i) {
                return card.hasStatus(unsupportedCheckBoxes[i].getText());
            }
        })) {
            return false;
        }

        // type
        if (!filterCheckboxes(cardDefinition, typeCheckBoxes, typeFilterChoices,
            new CardChecker() {
                @Override
                public boolean checkCard(final MagicCardDefinition card, final int i) {
                    if (typeCheckBoxes[i].getText().equals(UiString.get(_S20))) {
                        return card.isToken();
                    } else if (typeCheckBoxes[i].getText().equals(UiString.get(_S21))) {
                        return card.isDoubleFaced();
                    } else if (typeCheckBoxes[i].getText().equals(UiString.get(_S22))) {
                        return card.isFlipCard();
                    } else if (typeCheckBoxes[i].getText().equals(UiString.get(_S23))) {
                        return card.isHidden();
                    } else if (typeCheckBoxes[i].getText().equals(UiString.get(_S24))) {
                        return card.isSplitCard();
                    } else {
                        return card.hasType(MagicType.FILTER_TYPES.toArray(new MagicType[0])[i]);
                    }
                }
            })) {
            return false;
        }

        // color
        if (!filterCheckboxes(cardDefinition, colorCheckBoxes, colorFilterChoices,
            new CardChecker() {
                @Override
                public boolean checkCard(final MagicCardDefinition card, final int i) {
                    return card.hasColor(MagicColor.values()[i]);
                }
            })) {
            return false;
        }

        // cost
        if (!filterCheckboxes(cardDefinition, costCheckBoxes, costFilterChoices,
            new CardChecker() {
                @Override
                public boolean checkCard(final MagicCardDefinition card, final int i) {
                    return card.hasConvertedCost(Integer.parseInt(COST_VALUES[i]));
                }
            })) {
            return false;
        }

        // subtype
        if (!filterCheckboxes(cardDefinition, subtypeCheckBoxes, subtypeFilterChoices,
            new CardChecker() {
                @Override
                public boolean checkCard(final MagicCardDefinition card, final int i) {
                    return card.hasSubType(MagicSubType.values()[i]);
                }
            })) {
            return false;
        }

        // rarity
        if (!filterCheckboxes(cardDefinition, rarityCheckBoxes, rarityFilterChoices,
            new CardChecker() {
                @Override
                public boolean checkCard(final MagicCardDefinition card, final int i) {
                    return card.isRarity(MagicRarity.values()[i]);
                }
            })) {
            return false;
        }

        // status
        if (!filterCheckboxes(cardDefinition, statusCheckBoxes, statusFilterChoices,
            new CardChecker() {
                @Override
                public boolean checkCard(final MagicCardDefinition card, final int i) {
                    final String status = statusCheckBoxes[i].getText();
                    if (status.equals(UiString.get(_S17))) {
                        return MagicLogs.isCardInDownloadsLog(card);
                    } else if (status.equals(UiString.get(_S18))) {
                         return CardDefinitions.isCardPlayable(card);
                    } else if (status.equals(UiString.get(_S19))) {
                        return CardDefinitions.isCardMissing(card);
                    } else if (status.equals(UiString.get(_S25))) {
                        return CardDefinitions.isPotential(card);
                    } else {
                        return true;
                    }
                }
            })) {
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

        unselectFilterSet(cubeCheckBoxes, cubeFilterChoices);
        unselectFilterSet(formatsCheckBoxes, formatsFilterChoices);
        unselectFilterSet(setsCheckBoxes, setsFilterChoices);
        unselectFilterSet(typeCheckBoxes, typeFilterChoices);
        unselectFilterSet(colorCheckBoxes, colorFilterChoices);
        unselectFilterSet(costCheckBoxes, costFilterChoices);
        unselectFilterSet(subtypeCheckBoxes, subtypeFilterChoices);
        unselectFilterSet(rarityCheckBoxes, rarityFilterChoices);
        unselectFilterSet(statusCheckBoxes, statusFilterChoices);
        if (unsupportedPopup != null) {
            unselectFilterSet(unsupportedCheckBoxes, unsupportedFilterChoices);
        }

        if (nameTextField != null) {
            nameTextField.setText("");
        }

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
        cubePopup.hidePopup();
        formatsPopup.hidePopup();
        setsPopup.hidePopup();
        typePopup.hidePopup();
        colorPopup.hidePopup();
        costPopup.hidePopup();
        subtypePopup.hidePopup();
        rarityPopup.hidePopup();
        oraclePopup.hidePopup();
        statusPopup.hidePopup();
        if (unsupportedPopup != null) {
            unsupportedPopup.hidePopup();
        }
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

    private void addCardTypeFilter() {
        typePopup = addFilterPopupPanel(UiString.get(_S8));
        final Object[] types = getTypeFilterValues();
        typeCheckBoxes = new JCheckBox[types.length];
        typeFilterChoices = new JRadioButton[FILTER_CHOICES.length];
        populateCheckboxPopup("flowy, wrap 9, insets 2, gapy 6, gapx 20", typePopup, types, typeCheckBoxes, typeFilterChoices, false);
    }

    private Object[] getTypeFilterValues() {
        final List<Object> types = MagicType.FILTER_TYPES.stream().map(MagicType::getDisplayName).collect(Collectors.toList());
        if (!listener.isDeckEditor()) {
            types.add(UiString.get(_S20));
            types.add(UiString.get(_S21));
            types.add(UiString.get(_S22));
            if (MagicSystem.isDevMode()) {
                types.add(UiString.get(_S23));
            }
            types.add(UiString.get(_S24));
        }
        return types.toArray();
    }

    private void addOracleFilter() {
        oraclePopup = addFilterPopupPanel(UiString.get(_S9), UiString.get(_S10));
        oraclePopup.setPopupSize(260, 38);
        nameTextField = new CardFilterTextField(listener);
        oraclePopup.add(nameTextField);
    }

    private void addCardColorFilter() {
        colorPopup = addFilterPopupPanel(UiString.get(_S11));
        colorCheckBoxes=new JCheckBox[MagicColor.NR_COLORS];
        final JPanel colorsPanel=new JPanel();
        colorsPanel.setLayout(new BoxLayout(colorsPanel, BoxLayout.X_AXIS));
        colorsPanel.setBorder(FontsAndBorders.DOWN_BORDER);
        colorsPanel.setOpaque(false);
        colorPopup.setPopupSize(280, 90);
        for (int i = 0; i < MagicColor.NR_COLORS; i++) {
            final MagicColor color = MagicColor.values()[i];
            final JPanel colorPanel=new JPanel();
            colorPanel.setOpaque(false);
            colorCheckBoxes[i]=new JCheckBox("",false);
            colorCheckBoxes[i].addActionListener(this);
            colorCheckBoxes[i].setOpaque(false);
            colorCheckBoxes[i].setFocusPainted(true);
            colorCheckBoxes[i].setAlignmentY(Component.CENTER_ALIGNMENT);
            colorCheckBoxes[i].setActionCommand(Character.toString(color.getSymbol()));
            colorPanel.add(colorCheckBoxes[i]);
            colorPanel.add(new JLabel(MagicImages.getIcon(color.getManaType())));
            colorsPanel.add(colorPanel);
        }
        colorsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        colorPopup.add(colorsPanel);

        final ButtonGroup colorFilterBg = new ButtonGroup();
        colorFilterChoices = new JRadioButton[FILTER_CHOICES.length];
        for (int i = 0; i < FILTER_CHOICES.length; i++) {
            colorFilterChoices[i] = new JRadioButton(FILTER_CHOICES[i]);
            colorFilterChoices[i].addActionListener(this);
            colorFilterChoices[i].setOpaque(false);
            colorFilterChoices[i].setForeground(TEXT_COLOR);
            colorFilterChoices[i].setFocusPainted(true);
            colorFilterChoices[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            if (i == 0) {
                colorFilterChoices[i].setSelected(true);
            }
            colorFilterBg.add(colorFilterChoices[i]);
            colorPopup.add(colorFilterChoices[i]);
        }
    }

    private void addManaCostFilter() {
        costPopup = addFilterPopupPanel(UiString.get(_S12));
        costCheckBoxes = new JCheckBox[COST_VALUES.length];
        costFilterChoices = new JRadioButton[FILTER_CHOICES.length];
        populateCheckboxPopup("flowx, wrap 5, insets 2, gap 8", costPopup, COST_VALUES, costCheckBoxes, costFilterChoices, true);
    }

    private void addCardSubtypeFilter() {
        subtypePopup = addFilterPopupPanel(UiString.get(_S13));
        subtypeCheckBoxes = new JCheckBox[MagicSubType.values().length];
        subtypeFilterChoices = new JRadioButton[FILTER_CHOICES.length];
        populateCheckboxPopup(subtypePopup, MagicSubType.values(), subtypeCheckBoxes, subtypeFilterChoices, false);
    }

    private void addCardRarityFilter() {
        rarityPopup = addFilterPopupPanel(UiString.get(_S14));
        rarityCheckBoxes = new JCheckBox[MagicRarity.values().length];
        rarityFilterChoices = new JRadioButton[FILTER_CHOICES.length];
        populateCheckboxPopup(rarityPopup, MagicRarity.getDisplayNames(), rarityCheckBoxes, rarityFilterChoices, true);
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
