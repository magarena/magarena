package magic.ui;

import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicColoredType;
import magic.model.MagicCubeDefinition;
import magic.model.MagicPlayerProfile;
import magic.model.MagicType;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.TitleBar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExplorerFilterPanel extends TexturedPanel implements ActionListener, DocumentListener {
	
	private static final long serialVersionUID = 1L;

	private static final Comparator<MagicCardDefinition> NAME_COMPARATOR=new Comparator<MagicCardDefinition>() {

		@Override
		public int compare(final MagicCardDefinition cardDefinition1,final MagicCardDefinition cardDefinition2) {

			return cardDefinition1.getName().compareTo(cardDefinition2.getName());
		}
	};
	
	private static final Comparator<MagicCardDefinition> CONVERTED_COMPARATOR=new Comparator<MagicCardDefinition>() {

		@Override
		public int compare(final MagicCardDefinition cardDefinition1,final MagicCardDefinition cardDefinition2) {

			final int cdif=cardDefinition1.getConvertedCost()-cardDefinition2.getConvertedCost();
			if (cdif!=0) {
				return cdif;
			}
			return cardDefinition1.getName().compareTo(cardDefinition2.getName());
		}
	};
		
	private static final String TYPES[]={"Land","Instant","Sorcery","Creature","Artifact","Enchantment"};
	
	private final ExplorerPanel explorerPanel;
	private final JCheckBox typeCheckBoxes[];
	private final JCheckBox colorCheckBoxes[];
	private final JCheckBox exactlyCheckBox;
	private final JCheckBox excludeCheckBox;
	private final JCheckBox multiCheckBox;
	private final JTextField textFilterField;
	private final JRadioButton nameRadioButton;
	private final JRadioButton convertedRadioButton;
	private final int mode;
	private final MagicPlayerProfile profile;
	private final MagicCubeDefinition cube;
	
	public ExplorerFilterPanel(final ExplorerPanel explorerPanel,final int mode,final MagicPlayerProfile profile,final MagicCubeDefinition cube) {
				
		this.explorerPanel=explorerPanel;
		this.mode=mode;
		this.profile=profile;
		this.cube=cube;

		final Color textColor=ThemeFactory.getInstance().getCurrentTheme().getTextColor();
		
		setLayout(new BorderLayout());

		final TitleBar titleBar=new TitleBar("Filter");
		add(titleBar,BorderLayout.NORTH);

		final JPanel mainPanel=new JPanel();
		mainPanel.setLayout(new BoxLayout( mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setOpaque(false);
		mainPanel.setBorder(FontsAndBorders.BLACK_BORDER);
		add(mainPanel,BorderLayout.CENTER);
		
		// Type
		final TitledBorder typeBorder=BorderFactory.createTitledBorder("Type");
		typeBorder.setTitleColor(textColor);
		final JPanel typeFilterPanel=new JPanel(new BorderLayout(8,0));
		typeFilterPanel.setOpaque(false);
		typeFilterPanel.setBorder(typeBorder);
		mainPanel.add(typeFilterPanel);

		final JPanel leftTypeFilterPanel=new JPanel(new GridLayout(3,1));
		leftTypeFilterPanel.setOpaque(false);
		typeFilterPanel.add(leftTypeFilterPanel,BorderLayout.WEST);
		final JPanel rightTypeFilterPanel=new JPanel(new GridLayout(3,1));
		rightTypeFilterPanel.setOpaque(false);
		typeFilterPanel.add(rightTypeFilterPanel,BorderLayout.CENTER);
		
		typeCheckBoxes=new JCheckBox[TYPES.length];
		for (int index=0;index<TYPES.length;index++) {
			
			typeCheckBoxes[index]=new JCheckBox(TYPES[index]);
			typeCheckBoxes[index].addActionListener(this);
			typeCheckBoxes[index].setOpaque(false);
			typeCheckBoxes[index].setForeground(textColor);
			typeCheckBoxes[index].setFocusPainted(false);
			if (index<3) {
				leftTypeFilterPanel.add(typeCheckBoxes[index]);
			} else {
				rightTypeFilterPanel.add(typeCheckBoxes[index]);
			}
		}

		// Color
		final TitledBorder colorBorder=BorderFactory.createTitledBorder("Color");
		colorBorder.setTitleColor(textColor);
		final JPanel colorFilterPanel=new JPanel(new BorderLayout());
		colorFilterPanel.setOpaque(false);
		colorFilterPanel.setBorder(colorBorder);
		mainPanel.add(colorFilterPanel);

		colorCheckBoxes=new JCheckBox[MagicColor.NR_COLORS];
		final JPanel colorsPanel=new JPanel(new GridLayout(1,MagicColor.NR_COLORS));
		colorsPanel.setOpaque(false);
		int index=0;
		for (final MagicColor color : MagicColor.values()) {

			final JPanel colorPanel=new JPanel(new BorderLayout());
			colorPanel.setOpaque(false);
			colorCheckBoxes[index]=new JCheckBox("",false);
			colorCheckBoxes[index].addActionListener(this);
			colorCheckBoxes[index].setOpaque(false);
			colorCheckBoxes[index].setFocusPainted(false);
			colorCheckBoxes[index].setActionCommand(Character.toString(color.getSymbol()));
			colorPanel.add(colorCheckBoxes[index],BorderLayout.WEST);
			colorPanel.add(new JLabel(color.getManaType().getIcon(true)),BorderLayout.CENTER);
			colorsPanel.add(colorPanel);
			index++;
		}
		colorFilterPanel.add(colorsPanel,BorderLayout.NORTH);
		
		final JPanel otherColorPanel=new JPanel(new GridLayout(3,1));
		otherColorPanel.setOpaque(false);
		exactlyCheckBox=new JCheckBox("Match colors exactly",false);
		exactlyCheckBox.addActionListener(this);
		exactlyCheckBox.setForeground(textColor);
		exactlyCheckBox.setOpaque(false);
		exactlyCheckBox.setFocusPainted(false);
		otherColorPanel.add(exactlyCheckBox);
		excludeCheckBox=new JCheckBox("Exclude unselected colors",false);
		excludeCheckBox.addActionListener(this);
		excludeCheckBox.setForeground(textColor);
		excludeCheckBox.setOpaque(false);
		excludeCheckBox.setFocusPainted(false);
		otherColorPanel.add(excludeCheckBox);
		multiCheckBox=new JCheckBox("Match multicolored only",false);
		multiCheckBox.addActionListener(this);
		multiCheckBox.setForeground(textColor);
		multiCheckBox.setOpaque(false);
		multiCheckBox.setFocusPainted(false);
		otherColorPanel.add(multiCheckBox);
		colorFilterPanel.add(otherColorPanel,BorderLayout.CENTER);

		// Text
		final TitledBorder textFilterBorder=BorderFactory.createTitledBorder("Text");
		textFilterBorder.setTitleColor(textColor);
		final JPanel textFilterPanel=new JPanel(new BorderLayout(8,0));
		textFilterPanel.setOpaque(false);
		textFilterPanel.setBorder(textFilterBorder);
		textFilterField = new JTextField();
		textFilterField.addActionListener(this);
		textFilterField.getDocument().addDocumentListener(this);
		textFilterPanel.add(textFilterField);
		mainPanel.add(textFilterPanel);
				
		// Sort
		final TitledBorder sortBorder=BorderFactory.createTitledBorder("Sort");
		sortBorder.setTitleColor(textColor);
		final JPanel sortFilterPanel=new JPanel(new BorderLayout(8,0));
		sortFilterPanel.setOpaque(false);
		sortFilterPanel.setBorder(sortBorder);
		mainPanel.add(sortFilterPanel);

		final ButtonGroup sortGroup=new ButtonGroup();
		nameRadioButton=new JRadioButton("Name",true);
		nameRadioButton.addActionListener(this);
		nameRadioButton.setForeground(textColor);
		nameRadioButton.setOpaque(false);
		nameRadioButton.setFocusPainted(false);
		sortGroup.add(nameRadioButton);
		sortFilterPanel.add(nameRadioButton,BorderLayout.WEST);
		convertedRadioButton=new JRadioButton("Converted cost",false);
		convertedRadioButton.addActionListener(this);
		convertedRadioButton.setForeground(textColor);
		convertedRadioButton.setOpaque(false);
		convertedRadioButton.setFocusable(false);
		sortGroup.add(convertedRadioButton);
		sortFilterPanel.add(convertedRadioButton,BorderLayout.CENTER);
	}
	
	private boolean filter(final MagicCardDefinition cardDefinition) {

		if (cardDefinition.isToken()) {
			return false;
		}
		if (cube!=null&&!cube.containsCard(cardDefinition)) {
			return false;
		}
				
		switch (mode) {
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
		}
		
		if (profile!=null&&!cardDefinition.isBasic()&&!cardDefinition.isPlayable(profile)) {
			return false;
		}
		
		if (multiCheckBox.isSelected()&&cardDefinition.getColoredType()!=MagicColoredType.MultiColored) {
			return false;
		}
		
		boolean useTypes=false;
		boolean validTypes=false;
		for (int typeIndex=0;typeIndex<typeCheckBoxes.length;typeIndex++) {
		
			if (typeCheckBoxes[typeIndex].isSelected()) {
				useTypes=true;
				switch (typeIndex) {
					case 0: validTypes|=cardDefinition.isLand(); break;
					case 1: validTypes|=cardDefinition.hasType(MagicType.Instant); break;
					case 2: validTypes|=cardDefinition.hasType(MagicType.Sorcery); break;
					case 3: validTypes|=cardDefinition.isCreature(); break;
					case 4: validTypes|=cardDefinition.hasType(MagicType.Artifact); break;
					case 5: validTypes|=cardDefinition.isEnchantment(); break;
				}
			}
		}
		if (useTypes&&!validTypes) {
			return false;
		}
		
		// text
		final String filterString = textFilterField.getText();
		final String[] filters = filterString.split(" ");
		for(int i=0; i<filters.length; i++) {
			if(!cardDefinition.hasText(filters[i])) {
				return false;
			}
		}
		
		// colors		
		boolean useColors=false;
		boolean validColors=false;
		for (int colorIndex=0;colorIndex<colorCheckBoxes.length;colorIndex++) {

			final MagicColor color=MagicColor.getColor(colorCheckBoxes[colorIndex].getActionCommand().charAt(0));
			final boolean hasColor=color.hasColor(cardDefinition.getColorFlags());
			if (colorCheckBoxes[colorIndex].isSelected()) {
				useColors=true;
				if (hasColor) {
					validColors=true;
				} else if (exactlyCheckBox.isSelected()) {
					return false;
				}
			} else if (excludeCheckBox.isSelected()&&hasColor) {
				return false;
			}
		}		
		return !useColors||validColors;
	}
	
	private Comparator<MagicCardDefinition> getComparator() {
		
		return nameRadioButton.isSelected()?NAME_COMPARATOR:CONVERTED_COMPARATOR;
	}
	
	public List<MagicCardDefinition> getCardDefinitions() {
		
		final List<MagicCardDefinition> cardDefinitions=new ArrayList<MagicCardDefinition>();
		for (final MagicCardDefinition cardDefinition : CardDefinitions.getInstance().getCards()) {
			
			if (filter(cardDefinition)) {
				cardDefinitions.add(cardDefinition);
			}
		}		
		Collections.sort(cardDefinitions,getComparator());
		return cardDefinitions;
	}
	
	@Override
	public void actionPerformed(final ActionEvent event) {
		
		explorerPanel.updateCards();
	}

	@Override
	public void insertUpdate(final DocumentEvent arg0) {
		explorerPanel.updateCards();
	}

	@Override
	public void removeUpdate(final DocumentEvent arg0) {
		explorerPanel.updateCards();
	}
	
	@Override
	public void changedUpdate(final DocumentEvent arg0) {
	}
}
