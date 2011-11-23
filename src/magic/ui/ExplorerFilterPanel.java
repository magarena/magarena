package magic.ui;

import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicCubeDefinition;
import magic.model.MagicPlayerProfile;
import magic.model.MagicRarity;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.ButtonControlledPopup;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ExplorerFilterPanel extends TexturedPanel implements ActionListener, DocumentListener {
	
	private static final long serialVersionUID = 1L;
	
	private static final String COST_VALUES[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
	private static final String FILTER_CHOICES[] = {"Match any selected", "Match all selected", "Exclude selected"};
	private static final String FILTER_BUTTON_TEXT = "Filter";
	private static final String HIDE_BUTTON_TEXT = "Hide";
	private static final String RESET_BUTTON_TEXT = "Reset";
	private static final Dimension BUTTON_SIZE = new Dimension(70, 25);
	private static final int SEARCH_FIELD_WIDTH = 12;
	private static final Color TEXT_COLOR = ThemeFactory.getInstance().getCurrentTheme().getTextColor();
	private static final Dimension POPUP_CHECKBOXES_SIZE = new Dimension(200, 150);
	private static final Dimension BUTTON_HOLDER_PANEL_SIZE = new Dimension(100, 72);
	private static final Dimension SEARCH_HOLDER_PANEL_SIZE = new Dimension(150, 72);
	
	private final JFrame frame;
	private final ExplorerPanel explorerPanel;
	private final ButtonControlledPopup typePopup;
	private final JCheckBox typeCheckBoxes[];
	private final JRadioButton typeFilterChoices[];
	private final ButtonControlledPopup colorPopup;
	private final JCheckBox colorCheckBoxes[];
	private final JRadioButton colorFilterChoices[];
	private final ButtonControlledPopup costPopup;
	private final JCheckBox costCheckBoxes[];
	private final JRadioButton costFilterChoices[];
	private final ButtonControlledPopup subtypePopup;
	private final JCheckBox subtypeCheckBoxes[];
	private final JRadioButton subtypeFilterChoices[];
	private final ButtonControlledPopup rarityPopup;
	private final JCheckBox rarityCheckBoxes[];
	private final JRadioButton rarityFilterChoices[];
	private final JTextField nameTextField;
	private final JButton resetButton;
	private final int mode;
	private final MagicPlayerProfile profile;
	private final MagicCubeDefinition cube;
	
	private boolean disableUpdate; // so when we change several filters, it doesn't update until the end
	
	public ExplorerFilterPanel(JFrame frame, final ExplorerPanel explorerPanel,final int mode,final MagicPlayerProfile profile,final MagicCubeDefinition cube) {
		this.frame = frame;
		this.explorerPanel=explorerPanel;
		this.mode=mode;
		this.profile=profile;
		this.cube=cube;
		
		disableUpdate = false;
		
		setBorder(FontsAndBorders.UP_BORDER);
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		// Type
		typePopup = addFilterPopupPanel("Card Type");
		typeCheckBoxes = new JCheckBox[MagicType.values().length];
		typeFilterChoices = new JRadioButton[FILTER_CHOICES.length];
		populateCheckboxPopup(typePopup, MagicType.values(), typeCheckBoxes, typeFilterChoices, false);
		
		// Color
		colorPopup = addFilterPopupPanel("Color");
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
			colorCheckBoxes[i].setFocusPainted(false);
			colorCheckBoxes[i].setAlignmentY(Component.CENTER_ALIGNMENT);
			colorCheckBoxes[i].setActionCommand(Character.toString(color.getSymbol()));
			colorPanel.add(colorCheckBoxes[i]);
			colorPanel.add(new JLabel(color.getManaType().getIcon(true)));
			colorsPanel.add(colorPanel);
		}
		colorsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		colorPopup.add(colorsPanel);
		
		ButtonGroup colorFilterBg = new ButtonGroup();
		colorFilterChoices = new JRadioButton[FILTER_CHOICES.length];
		for (int i = 0; i < FILTER_CHOICES.length; i++) {
			colorFilterChoices[i] = new JRadioButton(FILTER_CHOICES[i]);
			colorFilterChoices[i].addActionListener(this);
			colorFilterChoices[i].setOpaque(false);
			colorFilterChoices[i].setForeground(TEXT_COLOR);
			colorFilterChoices[i].setFocusPainted(false);
			colorFilterChoices[i].setAlignmentX(Component.LEFT_ALIGNMENT);
			if(i == 0) {
				colorFilterChoices[i].setSelected(true);
			}
			colorFilterBg.add(colorFilterChoices[i]);
			colorPopup.add(colorFilterChoices[i]);
		}
		
		// Mana Cost
		costPopup = addFilterPopupPanel("Mana Cost");
		costCheckBoxes = new JCheckBox[COST_VALUES.length];
		costFilterChoices = new JRadioButton[FILTER_CHOICES.length];
		populateCheckboxPopup(costPopup, COST_VALUES, costCheckBoxes, costFilterChoices, true);	

		// Subtype
		subtypePopup = addFilterPopupPanel("Subtype");
		subtypeCheckBoxes = new JCheckBox[MagicSubType.values().length];
		subtypeFilterChoices = new JRadioButton[FILTER_CHOICES.length];
		populateCheckboxPopup(subtypePopup, MagicSubType.values(), subtypeCheckBoxes, subtypeFilterChoices, false);	
		
		// Rarity
		rarityPopup = addFilterPopupPanel("Rarity");
		rarityCheckBoxes = new JCheckBox[MagicRarity.values().length];
		rarityFilterChoices = new JRadioButton[FILTER_CHOICES.length];
		populateCheckboxPopup(rarityPopup, MagicRarity.values(), rarityCheckBoxes, rarityFilterChoices, true);	

		// Search Name
		final TitledBorder textFilterBorder = BorderFactory.createTitledBorder("Search Text");
		textFilterBorder.setTitleColor(TEXT_COLOR);
		final JPanel textFilterPanel = new JPanel(new BorderLayout());
		textFilterPanel.setOpaque(false);
		textFilterPanel.setBorder(textFilterBorder);
		textFilterPanel.setPreferredSize(SEARCH_HOLDER_PANEL_SIZE);
		nameTextField = new JTextField(SEARCH_FIELD_WIDTH);
		nameTextField.addActionListener(this);
		nameTextField.getDocument().addDocumentListener(this);
		textFilterPanel.add(nameTextField, BorderLayout.CENTER);
		add(textFilterPanel);

		// Reset Button
		final TitledBorder resetBorder = BorderFactory.createTitledBorder("Reset All");
		resetBorder.setTitleColor(TEXT_COLOR);
		final JPanel resetFilterPanel = new JPanel(new BorderLayout());
		resetFilterPanel.setPreferredSize(BUTTON_HOLDER_PANEL_SIZE);
		resetFilterPanel.setOpaque(false);
		resetFilterPanel.setBorder(resetBorder);
		resetButton = new JButton(RESET_BUTTON_TEXT);
		resetButton.setFont(FontsAndBorders.FONT1);
		resetButton.addActionListener(this);
		resetButton.setPreferredSize(BUTTON_SIZE);
		resetFilterPanel.add(resetButton, BorderLayout.CENTER);
		add(resetFilterPanel);
	}
	
	private ButtonControlledPopup addFilterPopupPanel(final String title) {
		final TitledBorder border = BorderFactory.createTitledBorder(title);
		border.setTitleColor(TEXT_COLOR);
		final JPanel filterPanel = new JPanel(new BorderLayout());
		filterPanel.setOpaque(false);
		filterPanel.setBorder(border);
		filterPanel.setPreferredSize(BUTTON_HOLDER_PANEL_SIZE);
		add(filterPanel);
		
		JButton selectButton = new JButton(FILTER_BUTTON_TEXT);
		selectButton.setFont(FontsAndBorders.FONT1);
		selectButton.setPreferredSize(BUTTON_SIZE);
		filterPanel.add(selectButton, BorderLayout.CENTER);
		
		ButtonControlledPopup pop = new ButtonControlledPopup(frame, selectButton, HIDE_BUTTON_TEXT, FILTER_BUTTON_TEXT);
		pop.setLayout(new BoxLayout(pop, BoxLayout.Y_AXIS));
		selectButton.addActionListener(new PopupCloser(pop));
		return pop;
	}
	
	private class PopupCloser implements ActionListener {
		private final ButtonControlledPopup p;
		
		public PopupCloser(ButtonControlledPopup p) {
			this.p = p;
		}
	
		@Override
		public void actionPerformed(final ActionEvent event) {
			// close all other popups except for our own button's			
			if (p != typePopup) {
				typePopup.hidePopup();
			}
			if (p != colorPopup) {
				colorPopup.hidePopup();
			}
			if (p != costPopup) {
				costPopup.hidePopup();
			}
			if (p != subtypePopup) {
				subtypePopup.hidePopup();
			}
			if (p != rarityPopup) {
				rarityPopup.hidePopup();
			}
		}
	}
	
	private void populateCheckboxPopup(final ButtonControlledPopup popup, final Object[] checkboxValues, final JCheckBox[] newCheckboxes, final JRadioButton[] newFilterButtons, final boolean hideAND) {
		JPanel checkboxesPanel = new JPanel(new GridLayout(newCheckboxes.length, 1));
		checkboxesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		checkboxesPanel.setOpaque(false);		
		for (int i=0;i<checkboxValues.length;i++) {
			newCheckboxes[i]=new JCheckBox(checkboxValues[i].toString().replace('_', ' '));
			newCheckboxes[i].addActionListener(this);
			newCheckboxes[i].setOpaque(false);
			newCheckboxes[i].setForeground(TEXT_COLOR);
			newCheckboxes[i].setFocusPainted(false);
			newCheckboxes[i].setAlignmentX(Component.LEFT_ALIGNMENT);
			checkboxesPanel.add(newCheckboxes[i]);
		}
		
		JScrollPane scrollPane = new JScrollPane(checkboxesPanel);
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollPane.setBorder(FontsAndBorders.DOWN_BORDER);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setPreferredSize(POPUP_CHECKBOXES_SIZE);
		popup.add(scrollPane);
		
		ButtonGroup bg = new ButtonGroup();
		for (int i = 0; i < FILTER_CHOICES.length; i++) {
			newFilterButtons[i] = new JRadioButton(FILTER_CHOICES[i]);
			newFilterButtons[i].addActionListener(this);
			newFilterButtons[i].setOpaque(false);
			newFilterButtons[i].setForeground(TEXT_COLOR);
			newFilterButtons[i].setFocusPainted(false);
			newFilterButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
			if (i == 0) {
				newFilterButtons[i].setSelected(true);
			} else if (i == 1) {
				newFilterButtons[i].setVisible(!hideAND);
			}
			bg.add(newFilterButtons[i]);
			popup.add(newFilterButtons[i]);
		}
	}
	
	private boolean filter(final MagicCardDefinition cardDefinition) {
		
		if (cardDefinition.isToken()) {
			return false;
		}
		
		/* if (cube!=null&&!cube.containsCard(cardDefinition)) {
			return false;
		} */
				
		/* switch (mode) {
			case ExplorerPanel.LAND: 
				if (!cardDefinition.isLand()) {
					return false;
				}
				break;
			case ExplorerPanel.SPELL:
				if (cardDefinition.isLand()) {
					return false;
				}
				break;
		} */
		
		/* if (profile!=null&&!cardDefinition.isBasic()&&!cardDefinition.isPlayable(profile)) {
			return false;
		} */
		
		// search text in name, abilities, type, text, etc.
		final String filterString = nameTextField.getText();
		if (filterString.length() > 0) {
			final String[] filters = filterString.split(" ");
			for(int i=0; i<filters.length; i++) {
				if(!cardDefinition.hasText(filters[i])) {
					return false;
				}
			}
		}
		
		// type
		if (!filterCheckboxes(cardDefinition, typeCheckBoxes, typeFilterChoices, 
			new CardChecker() {
				public boolean checkCard(MagicCardDefinition card, int i) {
					return card.hasType(MagicType.values()[i]);
				}
			})) {
			return false;
		}
		
		// color
		if (!filterCheckboxes(cardDefinition, colorCheckBoxes, colorFilterChoices, 
			new CardChecker() {
				public boolean checkCard(MagicCardDefinition card, int i) {
					return card.hasColor(MagicColor.values()[i]);
				}
			})) {
			return false;
		}
		
		// cost
		if (!filterCheckboxes(cardDefinition, costCheckBoxes, costFilterChoices, 
			new CardChecker() {
				public boolean checkCard(MagicCardDefinition card, int i) {
					return card.hasConvertedCost(Integer.parseInt(COST_VALUES[i]));
				}
			})) {
			return false;
		}
		
		// subtype
		if (!filterCheckboxes(cardDefinition, subtypeCheckBoxes, subtypeFilterChoices, 
			new CardChecker() {
				public boolean checkCard(MagicCardDefinition card, int i) {
					return card.hasSubType(MagicSubType.values()[i]);
				}
			})) {
			return false;
		}
		
		// rarity
		if (!filterCheckboxes(cardDefinition, rarityCheckBoxes, rarityFilterChoices, 
			new CardChecker() {
				public boolean checkCard(MagicCardDefinition card, int i) {
					return card.isRarity(MagicRarity.values()[i]);
				}
			})) {
			return false;
		}
		
		return true;
	}
	
	private boolean filterCheckboxes(final MagicCardDefinition cardDefinition, final JCheckBox[] checkboxes, final JRadioButton[] filterButtons, CardChecker func) {
		boolean somethingSelected = false;
		boolean resultOR = false;
		boolean resultAND = true;
		
		for (int i=0; i < checkboxes.length; i++) {			
			if(checkboxes[i].isSelected()) {
				somethingSelected = true;
				if(!func.checkCard(cardDefinition, i)) {
					resultAND = false;
				} else {
					resultOR = true;
				}
			}
		}
		if (filterButtons[2].isSelected()) {
			// exclude selected
			return !resultOR;
		}
		if (!somethingSelected) {
			// didn't choose to exclude and nothing selected, so don't filter
			return true;
		} else {
			// otherwise return OR or AND result
			return (filterButtons[0].isSelected() && resultOR) || (filterButtons[1].isSelected() && resultAND);
		}
	}
	
	private interface CardChecker {
		public boolean checkCard(MagicCardDefinition card, int i);
	}
		
	public List<MagicCardDefinition> getCardDefinitions() {
		
		final List<MagicCardDefinition> cardDefinitions=new ArrayList<MagicCardDefinition>();
		for (final MagicCardDefinition cardDefinition : CardDefinitions.getCards()) {
			
			if (filter(cardDefinition)) {
				cardDefinitions.add(cardDefinition);
			}
		}
		return cardDefinitions;
	}
		
	public void resetFilters() {
		disableUpdate = true; // ignore any events caused by resetting filters
		
		closePopups();
		
		unselectFilterSet(typeCheckBoxes, typeFilterChoices);
		unselectFilterSet(colorCheckBoxes, colorFilterChoices);
		unselectFilterSet(costCheckBoxes, costFilterChoices);
		unselectFilterSet(subtypeCheckBoxes, subtypeFilterChoices);
		unselectFilterSet(rarityCheckBoxes, rarityFilterChoices);
		
		nameTextField.setText("");	
		
		disableUpdate = false;
	}
	
	private void unselectFilterSet(JCheckBox boxes[], JRadioButton filterButtons[]) {
		// uncheck all checkboxes
		for (int i = 0; i < boxes.length; i++){
			boxes[i].setSelected(false);
		}
		
		// reset to first option
		filterButtons[0].setSelected(true);
	}
	
	public void closePopups() {
		typePopup.hidePopup();
		colorPopup.hidePopup();
		costPopup.hidePopup();
		subtypePopup.hidePopup();
		rarityPopup.hidePopup();
	}
	
	@Override
	public void actionPerformed(final ActionEvent event) {
		final Object source=event.getSource();
		
		if(source == resetButton) {
			resetFilters();
		}
		
		if(!disableUpdate) {
			explorerPanel.updateCardPool();
		}
	}

	@Override
	public void insertUpdate(final DocumentEvent arg0) {
		if(!disableUpdate) {
			explorerPanel.updateCardPool();
		}
	}

	@Override
	public void removeUpdate(final DocumentEvent arg0) {
		if(!disableUpdate) {
			explorerPanel.updateCardPool();
		}
	}
	
	@Override
	public void changedUpdate(final DocumentEvent arg0) {
	}

}
