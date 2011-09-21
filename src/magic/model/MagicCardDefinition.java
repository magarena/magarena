package magic.model;

import magic.ai.ArtificialScoringSystem;
import magic.data.IconImages;
import magic.model.event.MagicActivation;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicCardActivation;
import magic.model.event.MagicCardEvent;
import magic.model.event.MagicEquipActivation;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPlayCardEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.event.MagicTapManaActivation;
import magic.model.event.MagicTiming;
import magic.model.trigger.MagicBattleCryTrigger;
import magic.model.trigger.MagicExaltedTrigger;
import magic.model.trigger.MagicLivingWeaponTrigger;
import magic.model.trigger.MagicTrigger;
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicCDA;
import magic.model.mstatic.MagicLayer;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.lang.StringBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
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
            setTiming(MagicTiming.Main);
		}
	};

    private static int numTriggers = 0;
    private static int numStatics = 0;
    private static int numPermanentActivations = 0;
    private static int numManaActivations = 0;
    private static int numSpellEvent = 0;
    private static int numModifications = 0;
    private static int numCDAs = 0;

	private final String name;
	private final String fullName;
	private String imageURL;
	private int imageCount = 1;
    private Collection<Long> ignore;
	private int index=-1;
	private int value=0;
	private int removal=0;
	private int score=-1; // not initialized
	private MagicRarity rarity;
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
	private long abilityFlags=0;
	private MagicStaticType staticType=MagicStaticType.None;
	private MagicTiming timing=MagicTiming.None;
	private MagicCardEvent cardEvent=MagicPlayCardEvent.getInstance();
    private MagicActivation cardActivation;
	private final Collection<MagicCDA> CDAs = new ArrayList<MagicCDA>();
	private final Collection<MagicTrigger> triggers=new ArrayList<MagicTrigger>();
	private final Collection<MagicStatic> statics=new ArrayList<MagicStatic>();
	private final Collection<MagicTrigger> comeIntoPlayTriggers=new ArrayList<MagicTrigger>();
	private final Collection<MagicTrigger> putIntoGraveyardTriggers=new ArrayList<MagicTrigger>();
	private final Collection<MagicActivation> activations=new ArrayList<MagicActivation>();
	private final Collection<MagicManaActivation> manaActivations=new ArrayList<MagicManaActivation>();
	private boolean excludeManaOrCombat=false;
	
	protected MagicCardDefinition(final String name,final String fullName) {
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
		System.err.println(numStatics + " statics");
		System.err.println(numPermanentActivations + " permanent activations");
		System.err.println(numManaActivations + " mana activations");
		System.err.println(numSpellEvent + " spell event");
		System.err.println(numModifications + " card modifications");
		System.err.println(numCDAs + " CDAs");
    }
    
    public boolean isValid() {
        return true;
    }

    public void addIgnore(final long size) {
        //lazy initialization of the ignore list
        if (ignore == null) {
            ignore = new ArrayList<Long>(2);
        }
        ignore.add(size);
    }

    public boolean isIgnored(final long size) {
        return ignore != null && ignore.contains(size);
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

    public void setImageCount(final int count) {
		this.imageCount = count;
	}
	
	public int getImageCount() {
		return imageCount;
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
	
	public int getFreeScore() {
		if (score<0) {
			score=ArtificialScoringSystem.getFreeCardDefinitionScore(this);
		}
		return score;
	}
					
	public void setRarity(final char c) {
		this.rarity = MagicRarity.getRarity(c);
	}
	
	public boolean isRarity(final MagicRarity r) {
		return this.rarity == r;
	}

	public int getRarity() {
		return rarity.ordinal();
	}
	
	public String getRarityString() {
		return rarity.getName();
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
				
	protected void setToken() {
		token=true;
	}
	
	public boolean isToken() {
		return token;
	}

    int getTypeFlags() {
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
        } else if (obj instanceof MagicStatic) {
            final MagicStatic mstatic = (MagicStatic)obj;
            addStatic(mstatic);
            mstatic.setCardIndex(index);
            System.err.println("Adding static to " + getFullName());
            numStatics++;
        } else if (obj instanceof MagicPermanentActivation) {
            final MagicPermanentActivation mact = (MagicPermanentActivation)obj;
            addActivation(mact);
            mact.setCardIndex(index);
            System.err.println("Adding permanent activation to " + getFullName());
            numPermanentActivations++;
        } else if (obj instanceof MagicChangeCardDefinition) {
            final MagicChangeCardDefinition chg = (MagicChangeCardDefinition)obj;
            chg.change(this);
            System.err.println("Adding modification to " + getFullName());
            numModifications++;
        } else if (obj instanceof MagicCDA) {
            final MagicCDA cda = (MagicCDA)obj;
            CDAs.add(cda);
            System.err.println("Adding CDA to " + getFullName());
            numCDAs++;
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
	
	public boolean isLegendary() {
		return hasType(MagicType.Legendary);
	}
	
	public boolean isAura() {
		return isEnchantment() && hasSubType(MagicSubType.Aura);
	}
	
	public boolean isInstant() {
		return hasType(MagicType.Instant);
	}
	
	public boolean isSorcery() {
		return hasType(MagicType.Sorcery);
	}
	
	public boolean isSpell() {
		return isInstant()||isSorcery();
	}
	
	public String getLongTypeString() {
		if (isBasic()) {
			return "Basic " + getTypeString();
		}
		if (isLegendary()) {
			return "Legendary " + getTypeString();
		}
		
		return getTypeString();	
	}
	
	public String getTypeString() {
		StringBuffer sb = new StringBuffer();
		if (isLand()) {
			sb.append(MagicType.Land.toString());
		} 
		if (isArtifact()) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(MagicType.Artifact.toString());
		} 
		if (isCreature()) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(MagicType.Creature.toString());
		} 
		if (isEnchantment()) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(MagicType.Enchantment.toString());
		} 
		if (isInstant()) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(MagicType.Instant.toString());
		} 
		if (isSorcery()) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(MagicType.Sorcery.toString());
		}
		
		return sb.toString();	
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
	
	EnumSet<MagicSubType> getSubTypeFlags() {
		return subTypeFlags;
	}
	
	public String getSubTypeString() {
		String brackets = getSubTypeFlags().toString(); // [...,...]
		if (brackets.length() <= 2) {
			return "";
		}
		return brackets.substring(1, brackets.length() - 1);
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
	
	protected void setColor(final MagicColor color) {
		
		colorFlags|=color.getMask();
	}
	
	public boolean hasColor(final MagicColor color) {
		return (colorFlags&color.getMask())!=0;
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
	
	public boolean hasConvertedCost(int c) {
		return convertedCost == c;
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

	private String getManaSourceText() {
		
		return manaSourceText;
	}
	
	public int getManaSource(final MagicColor color) {
		return manaSource[color.ordinal()];
	}
	
	public void setBasicManaActivations(final String basicText) {
		final int length=basicText.length();
		final List<MagicManaType> manaTypes=new ArrayList<MagicManaType>(length+1);
		for (int i=0;i<length;i++) {
			manaTypes.add(MagicColor.getColor(basicText.charAt(i)).getManaType());
		}
		manaTypes.add(MagicManaType.Colorless);
		addManaActivation(new MagicTapManaActivation(manaTypes,0));
	}
	
	public void setPower(final int power) {
		this.power = power;
	}

    public int getCardPower() {
        return power;
    }

	public int getPower(final MagicGame game, final MagicPlayer player) {
		return genPowerToughness(game,player).power();
	}
	
	public void setToughness(final int toughness) {
		this.toughness = toughness;
	}

    public int getCardToughness() {
        return toughness;
    }
	
	public int getToughness(final MagicGame game, final MagicPlayer player) {
		return genPowerToughness(game,player).toughness();
	}	

    public MagicPowerToughness genPowerToughness(final MagicGame game, final MagicPlayer player) {
        final MagicPowerToughness pt = new MagicPowerToughness(power, toughness);
        for (final MagicCDA lv : CDAs) {
            lv.getPowerToughness(game, player, pt);
        }
        return pt;
    }
	
	public void setAbility(final MagicAbility ability) {
		abilityFlags|=ability.getMask();
		if (ability==MagicAbility.Exalted) {
			addTrigger(MagicExaltedTrigger.getInstance());
		} else if (ability==MagicAbility.BattleCry) {
			addTrigger(MagicBattleCryTrigger.getInstance());
		} else if (ability==MagicAbility.LivingWeapon) {
            addTrigger(MagicLivingWeaponTrigger.getInstance());
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
	
	MagicStaticType getStaticType() {
		return staticType;
	}
	
	public void setTiming(final MagicTiming timing) {
		this.timing=timing;
	}
	
	public MagicTiming getTiming() {
		return timing;
	}
	
	private void setCardEvent(final MagicCardEvent cardEvent) {
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
		
	private void addTrigger(final MagicTrigger trigger) {
		switch (trigger.getType()) {
			case WhenComesIntoPlay:
				comeIntoPlayTriggers.add(trigger);
				break;
			case WhenPutIntoGraveyard:
				putIntoGraveyardTriggers.add(trigger);
				break;
			default:
				triggers.add(trigger);
				break;
		}
	}
	
    private void addStatic(final MagicStatic mstatic) {
        statics.add(mstatic);
    }
	
	public Collection<MagicTrigger> getTriggers() {
		return triggers;
	}
	
    public Collection<MagicStatic> getStatics() {
		return statics;
	}
	
	public Collection<MagicTrigger> getComeIntoPlayTriggers() {
		return comeIntoPlayTriggers;
	}
	
	public Collection<MagicTrigger> getPutIntoGraveyardTriggers() {
		return putIntoGraveyardTriggers;
	}
	
	private void addActivation(final MagicPermanentActivation activation) {
		activations.add(activation);
	}
	
	public Collection<MagicActivation> getActivations() {
		return activations;
	}
	
	protected void addManaActivation(final MagicManaActivation activation) {
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
	
	private boolean subTypeHasText(final String s) {
		final MagicSubType[] subTypeValues = MagicSubType.values();
		for (final MagicSubType subtype : subTypeValues) {
			if(subtype.hasSubType(subTypeFlags) && subtype.toString().toLowerCase().contains(s)) {
				return true;
			}
		}
		return false;
	}
		
	private boolean abilityHasText(final String s) {
		final MagicAbility[] abilityValues = MagicAbility.values();
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

	public static final Comparator<MagicCardDefinition> NAME_COMPARATOR_DESC=new Comparator<MagicCardDefinition>() {
		@Override
		public int compare(final MagicCardDefinition cardDefinition1,final MagicCardDefinition cardDefinition2) {
			return cardDefinition1.getName().compareTo(cardDefinition2.getName());
		}
	};

	public static final Comparator<MagicCardDefinition> NAME_COMPARATOR_ASC=new Comparator<MagicCardDefinition>() {
		@Override
		public int compare(final MagicCardDefinition cardDefinition1,final MagicCardDefinition cardDefinition2) {
			return NAME_COMPARATOR_DESC.compare(cardDefinition2, cardDefinition1);
		}
	};
	
	public static final Comparator<MagicCardDefinition> CONVERTED_COMPARATOR_DESC=new Comparator<MagicCardDefinition>() {
		@Override
		public int compare(final MagicCardDefinition cardDefinition1,final MagicCardDefinition cardDefinition2) {
			final int cdif=cardDefinition1.getConvertedCost()-cardDefinition2.getConvertedCost();
			if (cdif!=0) {
				return cdif;
			}
			return cardDefinition1.getName().compareTo(cardDefinition2.getName());
		}
	};
	
	public static final Comparator<MagicCardDefinition> CONVERTED_COMPARATOR_ASC=new Comparator<MagicCardDefinition>() {
		@Override
		public int compare(final MagicCardDefinition cardDefinition1,final MagicCardDefinition cardDefinition2) {
			return CONVERTED_COMPARATOR_DESC.compare(cardDefinition2, cardDefinition1);
		}
	};

	public static final Comparator<MagicCardDefinition> TYPE_COMPARATOR_DESC=new Comparator<MagicCardDefinition>() {
		@Override
		public int compare(final MagicCardDefinition cardDefinition1,final MagicCardDefinition cardDefinition2) {		
			int c = cardDefinition1.getTypeString().compareTo(cardDefinition2.getTypeString());
			if(c == 0) {
				return cardDefinition1.getLongTypeString().compareTo(cardDefinition2.getLongTypeString());
			}
			return c;
		}
	};

	public static final Comparator<MagicCardDefinition> TYPE_COMPARATOR_ASC=new Comparator<MagicCardDefinition>() {
		@Override
		public int compare(final MagicCardDefinition cardDefinition1,final MagicCardDefinition cardDefinition2) {
			return TYPE_COMPARATOR_DESC.compare(cardDefinition2, cardDefinition1);
		}
	};

	public static final Comparator<MagicCardDefinition> RARITY_COMPARATOR_DESC=new Comparator<MagicCardDefinition>() {
		@Override
		public int compare(final MagicCardDefinition cardDefinition1,final MagicCardDefinition cardDefinition2) {
			return cardDefinition1.getRarityString().compareTo(cardDefinition2.getRarityString());
		}
	};

	public static final Comparator<MagicCardDefinition> RARITY_COMPARATOR_ASC=new Comparator<MagicCardDefinition>() {
		@Override
		public int compare(final MagicCardDefinition cardDefinition1,final MagicCardDefinition cardDefinition2) {
			return RARITY_COMPARATOR_DESC.compare(cardDefinition2, cardDefinition1);
		}
	};
}
