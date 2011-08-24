package magic.model;

import magic.ai.ArtificialScoringSystem;
import magic.data.IconImages;
import magic.model.event.*;
import magic.model.trigger.MagicBattleCryTrigger;
import magic.model.trigger.MagicExaltedTrigger;
import magic.model.trigger.MagicTrigger;
import magic.model.variable.MagicAttachmentLocalVariable;
import magic.model.variable.MagicLocalVariable;
import magic.model.variable.MagicLocalVariableList;
import magic.model.variable.MagicStaticLocalVariable;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MagicCardDefinition {

	public static final MagicCardDefinition UNKNOWN = new MagicCardDefinition("Unknown") {
        //definition for unknown cards
		@Override
		protected void initialize() {
			setToken();
			setValue(1);
			addType(MagicType.Creature);
			setConvertedCost(8);
			setCost(MagicManaCost.EIGHT);
			setPower(1);
			setToughness(1);
			setAbility(MagicAbility.Defender);
			setAbility(MagicAbility.CannotBeCountered);
			setAbility(MagicAbility.Shroud);
		}
	};

    private static int numTriggers = 0;
    private static int numPermanentActivations = 0;
    private static int numManaActivations = 0;
    private static int numSpellEvent = 0;
    private static int numModifications = 0;
    private static int numLocalVariables = 0;

	public static final List<MagicLocalVariable> DEFAULT_LOCAL_VARIABLES = 
        Arrays.<MagicLocalVariable>asList(MagicStaticLocalVariable.getInstance());

	private final String name;
	private final String fullName;
	private String imageURL;
	private int index=-1;
	private int value=0;
	private int removal=0;
	private int score=-1; // not initialized
	private MagicRarity rarity = null;
	private boolean token=false;
	private int typeFlags=0;
	private EnumSet<MagicSubType> subTypeFlags = EnumSet.noneOf(MagicSubType.class);
	private int colorFlags=0;
	private int convertedCost=0;
	private MagicColoredType coloredType=MagicColoredType.Colorless;
	private MagicManaCost cost=MagicManaCost.ZERO;
	private String manaSourceText="";
	private final int manaSource[]=new int[MagicColor.NR_COLORS];
	private int power=0;
	private int toughness=0;
	private int turnLocalVariableIndex=0;
	private long abilityFlags=0;
	private MagicStaticType staticType=MagicStaticType.None;
	private MagicTiming timing=MagicTiming.None;
	private MagicCardEvent cardEvent=MagicPlayCardEvent.getInstance();
    private MagicActivation cardActivation = null;
    private MagicLocalVariable attachmentLocalVariable = null;
	private final MagicLocalVariableList localVariables=new MagicLocalVariableList();
	private final Collection<MagicTrigger> triggers=new ArrayList<MagicTrigger>();
	private final Collection<MagicTrigger> comeIntoPlayTriggers=new ArrayList<MagicTrigger>();
	private final Collection<MagicTrigger> putIntograveyardTriggers=new ArrayList<MagicTrigger>();
	private final Collection<MagicActivation> activations=new ArrayList<MagicActivation>();
	private final Collection<MagicManaActivation> manaActivations=new ArrayList<MagicManaActivation>();
	private boolean excludeManaOrCombat=false;
	
	public MagicCardDefinition(final String name,final String fullName) {
		this.name=name;
		this.fullName=fullName;
		initialize();
	}
	
	public MagicCardDefinition(final String name) {
		this(name,name);
	}
	
	protected void initialize() {}
	
    public static void printStatistics() {
		System.err.println(numTriggers + " triggers");
		System.err.println(numPermanentActivations + " permanent activations");
		System.err.println(numManaActivations + " mana activations");
		System.err.println(numSpellEvent + " spell event");
		System.err.println(numModifications + " card modifications");
		System.err.println(numLocalVariables + " local variables");
    }

	public String getName() {
		return name;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setIndex(final int index) {
		this.index=index;
	}
		
	public int getIndex() {
		return index;
	}
		
	public String getImageName() {
		return fullName;
	}

    //two images for basic lands and 
    //one image for the other cards
	public int getImageCount() {
		return isBasic()?2:1;
	}

    public void setImageURL(final String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }
		
	public void setValue(final int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	public void setRemoval(final int removal) {
		this.removal=removal;
	}
	
	public int getRemoval() {
		return removal;
	}
	
	public int getScore() {
		if (score<0) {
			score=ArtificialScoringSystem.getCardDefinitionScore(this);
		}
		return score;
	}
					
	public void setRarity(final char c) {
		this.rarity = MagicRarity.getRarity(c);
	}

	public int getRarity() {
		return rarity.ordinal();
	}
	
	public Color getRarityColor() {
		final Theme theme=ThemeFactory.getInstance().getCurrentTheme();
		switch (getRarity()) {
			case 2: return theme.getColor(Theme.COLOR_UNCOMMON_FOREGROUND);
			case 3: return theme.getColor(Theme.COLOR_RARE_FOREGROUND);
			case 4: return theme.getColor(Theme.COLOR_RARE_FOREGROUND);
			default: return theme.getColor(Theme.COLOR_COMMON_FOREGROUND);
		}
	}
				
	public void setToken() {
		token=true;
	}
	
	public boolean isToken() {
		return token;
	}

    public int getTypeFlags() {
        return typeFlags;
    }
    
    public void add(final Object obj) {
        if (obj instanceof MagicSpellCardEvent) {
            final MagicSpellCardEvent cevent = (MagicSpellCardEvent)obj;
            setCardEvent(cevent);
            cevent.setCardIndex(index);
            System.err.println("Adding spell card event to " + getFullName());
            numSpellEvent++;
        } else if (obj instanceof MagicManaActivation) {
            final MagicManaActivation mact = (MagicManaActivation)obj;
            addManaActivation(mact);
            System.err.println("Adding mana activation to " + getFullName());
            numManaActivations++;
        } else if (obj instanceof MagicTrigger) {
            final MagicTrigger mtrig = (MagicTrigger)obj;
            addTrigger(mtrig);
            mtrig.setCardIndex(index);
            System.err.println("Adding trigger to " + getFullName());
            numTriggers++;
        } else if (obj instanceof MagicPermanentActivation) {
            final MagicPermanentActivation mact = (MagicPermanentActivation)obj;
            addActivation(mact);
            mact.setCardIndex(index);
            System.err.println("Adding permanent activation to " + getFullName());
            numPermanentActivations++;
        } else if (obj instanceof MagicChangeCardDefinition) {
            final MagicChangeCardDefinition chg = (MagicChangeCardDefinition)obj;
            chg.change(this);
            System.err.println("Modifying card definition " + getFullName());
            numModifications++;
        } else if (obj instanceof MagicLocalVariable) {
            final MagicLocalVariable lvar = (MagicLocalVariable)obj;
            addLocalVariable(lvar);
            System.err.println("Adding local variable to " + getFullName());
            numLocalVariables++;
        } else {
            System.err.println("ERROR! Unable to add object to MagicCardDefinition");
            throw new RuntimeException("Unknown field in companion object");
        }
    }
	
	public void addType(final MagicType type) {
		typeFlags|=type.getMask();
	}
	
	public boolean hasType(final MagicType type) {
		
		return (typeFlags&type.getMask())!=0;
	}
	
	public boolean isBasic() {
		
		return hasType(MagicType.Basic);
	}
	
	public boolean isLand() {
		
		return hasType(MagicType.Land);
	}
	
	public boolean isCreature() {
		
		return hasType(MagicType.Creature);
	}
	
	public boolean isArtifact() {

		return hasType(MagicType.Artifact);
	}
		
	public boolean isEquipment() {
		
		return isArtifact()&&hasSubType(MagicSubType.Equipment);
	}
	
	public boolean isEnchantment() {
		return hasType(MagicType.Enchantment);
	}
	
	public boolean isAura() {
		return isEnchantment() && hasSubType(MagicSubType.Aura);
	}
	
	public boolean isSpell() {
		
		return hasType(MagicType.Instant)||hasType(MagicType.Sorcery);
	}

	public boolean usesStack() {
		
		return !isLand();
	}
	
	public void setSubTypes(final String[] subTypeNames) {
		subTypeFlags = EnumSet.noneOf(MagicSubType.class);
		for (final String subTypeName : subTypeNames) {
			final MagicSubType subType=MagicSubType.getSubType(subTypeName); 
            subTypeFlags.add(subType);
		}
	}
	
	public EnumSet<MagicSubType> getSubTypeFlags() {
		return subTypeFlags;
	}
	
	public boolean hasSubType(final MagicSubType subType) {
		if (subType.isCreatureType() && hasAbility(MagicAbility.Changeling)) {
			return true;
		}
		return subType.hasSubType(subTypeFlags);		
	}
	
	public void setColors(final String colors) {
		
		colorFlags=MagicColor.getFlags(colors);		
	}
	
	public void setColor(final MagicColor color) {
		
		colorFlags|=color.getMask();
	}
		
	public int getColorFlags() {
		
		return colorFlags;
	}
	
	public void setColoredType() {
		
		int count=0;
		for (final MagicColor color : MagicColor.values()) {
			
			if (color.hasColor(colorFlags)) {
				count++;
			}
		}
		if (count>1) {
			coloredType=MagicColoredType.MultiColored;
		} else if (count==1) {
			coloredType=MagicColoredType.MonoColored;
		} else {
			coloredType=MagicColoredType.Colorless;
		}
	}
	
	public MagicColoredType getColoredType() {
		return coloredType;
	}
		
	public void setConvertedCost(final int convertedCost) {
		this.convertedCost = convertedCost;
	}
	
	public int getConvertedCost() {
		return convertedCost;
	}
	
	public int getCostBucket() {
		switch (convertedCost) {
			case 0:
			case 1:
			case 2:
				return 0;
			case 3:
			case 4:
				return 1;
			default: 
				return 2;
		}
	}
	
	public boolean hasX() {
		return cost.hasX();
	}

	public void setCost(final MagicManaCost cost) {
		this.cost=cost;
	}

	public MagicManaCost getCost() {
		return cost;
	}
	
	public boolean isPlayable(final MagicPlayerProfile profile) {
		if (isLand()) {
			int source=0;
			for (final MagicColor color : profile.getColors()) {
				source+=getManaSource(color);
			}
			return source>4;
		} else {
			return cost.getCostScore(profile)>0;
		}
	}
		
	public void setEquipCost(final MagicManaCost equipCost) {
		add(new MagicEquipActivation(equipCost));
	}
		
	public void setManaSourceText(final String sourceText) {
		
		manaSourceText=sourceText;
		for (int index=0;index<sourceText.length();index+=2) {
			
			final char symbol=sourceText.charAt(index);
			final int source=sourceText.charAt(index+1)-'0';
			final MagicColor color=MagicColor.getColor(symbol);
			manaSource[color.ordinal()]=source;
		}
	}

	public String getManaSourceText() {
		
		return manaSourceText;
	}
	
	public int getManaSource(final MagicColor color) {

		return manaSource[color.ordinal()];
	}
	
	public void setBasicManaActivations(final String basicText) {

		final int length=basicText.length();
		final List<MagicManaType> manaTypes=new ArrayList<MagicManaType>(length+1);
		for (int index=0;index<length;index++) {
			manaTypes.add(MagicColor.getColor(basicText.charAt(index)).getManaType());
		}
		manaTypes.add(MagicManaType.Colorless);
		addManaActivation(new MagicTapManaActivation(manaTypes,0));
	}

	public void setPower(final int power) {
		this.power = power;
	}

	public int getPower() {
		return power;
	}
	
	public void setToughness(final int toughness) {
		this.toughness = toughness;
	}
	
	public int getToughness() {
		return toughness;
	}		
	
	public void setVariablePT() {
		turnLocalVariableIndex=1;
	}
	
	public int getTurnLocalVariableIndex() {
		return turnLocalVariableIndex;
	}
	
	public void setAbility(final MagicAbility ability) {
		abilityFlags|=ability.getMask();
		if (ability==MagicAbility.Exalted) {
			addTrigger(MagicExaltedTrigger.getInstance());
		} else if (ability==MagicAbility.BattleCry) {
			addTrigger(MagicBattleCryTrigger.getInstance());
		}
	}

	public long getAbilityFlags() {
		return abilityFlags;
	}
	
	public boolean hasAbility(final MagicAbility ability) {
		return ability.hasAbility(abilityFlags);
	}
	
	public void setStaticType(final MagicStaticType staticType) {
		this.staticType=staticType;
	}
	
	public MagicStaticType getStaticType() {
		return staticType;
	}
	
	public void setTiming(final MagicTiming timing) {
		this.timing=timing;
	}
	
	public MagicTiming getTiming() {
		return timing;
	}
				
	public MagicLocalVariable getAttachmentLocalVariable() {
        if (attachmentLocalVariable == null) {
            attachmentLocalVariable = new MagicAttachmentLocalVariable(this);
        }
		return attachmentLocalVariable;
	}
	
	public void addLocalVariable(final MagicLocalVariable localVariable) {
		localVariables.add(localVariable);
	}
	
	public List<MagicLocalVariable> getLocalVariables() {
		if (localVariables.isEmpty()) {
			return DEFAULT_LOCAL_VARIABLES;
		} else {
			return localVariables;
		}
	}
	
	public void setCardEvent(final MagicCardEvent cardEvent) {
		this.cardEvent=cardEvent;
	}
	
	public MagicCardEvent getCardEvent() {
		return cardEvent;
	}

	public MagicActivationHints getActivationHints() {
		return new MagicActivationHints(timing,true,0);
	}
	
	public MagicActivation getCardActivation() {
		if (cardActivation==null) {
			cardActivation=new MagicCardActivation(index);
		}
		return cardActivation;
	}
		
	public void addTrigger(final MagicTrigger trigger) {
		switch (trigger.getType()) {
			case WhenComesIntoPlay:
				comeIntoPlayTriggers.add(trigger);
				break;
			case WhenPutIntoGraveyard:
				putIntograveyardTriggers.add(trigger);
				break;
			default:
				triggers.add(trigger);
				break;
		}
	}
	
	public Collection<MagicTrigger> getTriggers() {
		return triggers;
	}
	
	public Collection<MagicTrigger> getComeIntoPlayTriggers() {
		return comeIntoPlayTriggers;
	}
	
	public Collection<MagicTrigger> getPutIntoGraveyardTriggers() {
		return putIntograveyardTriggers;
	}
	
	public void addActivation(final MagicPermanentActivation activation) {
		activations.add(activation);
	}
	
	public Collection<MagicActivation> getActivations() {
		return activations;
	}
	
	public void addManaActivation(final MagicManaActivation activation) {
		manaActivations.add(activation);
	}
	
	public Collection<MagicManaActivation> getManaActivations() {
		return manaActivations;
	}
	
	public void setExcludeManaOrCombat() {
		excludeManaOrCombat=true;
	}
	
	public boolean hasExcludeManaOrCombat() {
		return excludeManaOrCombat;
	}
	
	public ImageIcon getIcon() {
		if (isLand()) {
			return IconImages.LAND; 
		} else if (isCreature()) {
			return IconImages.CREATURE;
		} else if (isEquipment()) {
			return IconImages.EQUIPMENT; 
		} else if (isAura()) {
			return IconImages.AURA;
		} else if (isEnchantment()) {
			return IconImages.ENCHANTMENT;
		} else if (isArtifact()) {
			return IconImages.ARTIFACT;
		} else {
			return IconImages.SPELL;
		}
	}
	
	public boolean subTypeHasText(String s) {
		MagicSubType[] subTypeValues = MagicSubType.values();
		for (final MagicSubType subtype : subTypeValues) {
			if(subtype.hasSubType(subTypeFlags) && subtype.toString().toLowerCase().contains(s)) {
				return true;
			}
		}
		return false;
	}
		
	public boolean abilityHasText(String s) {
		MagicAbility[] abilityValues = MagicAbility.values();
		for (final MagicAbility ability : abilityValues) {
			if(hasAbility(ability) && ability.getName().toLowerCase().contains(s)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasText(String s) {
		s = s.toLowerCase();
		return (
			fullName.toLowerCase().contains(s) ||
			name.toLowerCase().contains(s) ||
			subTypeHasText(s) ||
			abilityHasText(s)
		);
	}
}
