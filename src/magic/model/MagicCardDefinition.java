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
import magic.model.event.MagicTapManaActivation;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicCDA;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicWhenSpellIsCastTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenPutIntoGraveyardTrigger;
import magic.model.trigger.MagicWhenDrawnTrigger;
import magic.model.trigger.MagicGraveyardTriggerData ;
import magic.model.stack.MagicItemOnStack;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;

public class MagicCardDefinition {

    public static final MagicCardDefinition UNKNOWN = new MagicCardDefinition() {
        //definition for unknown cards
        @Override
        protected void initialize() {
            setName("Unknown");
            setFullName("Unknown");
            setToken();
            setValue(1);
            addType(MagicType.Creature);
            setCost(MagicManaCost.EIGHT);
            setPowerToughness(1,1);
            setAbility(MagicAbility.Defender);
            setAbility(MagicAbility.CannotBeCountered);
            setAbility(MagicAbility.Shroud);
            setTiming(MagicTiming.Main);
        }
    };

    private static int numTriggers;
    private static int numStatics;
    private static int numPermanentActivations;
    private static int numManaActivations;
    private static int numSpellEvent;
    private static int numCDAs;

    // name displayed in UI, may be repeated in tokens
    private String name;

    // name used for mapping and persistence, must be unique
    private String fullName;

    private String imageURL;
    private String cardInfoUrl = "";
    private int imageCount = 1;
    private Collection<Long> ignore;
    private int index=-1;
    private double value;
    private int removal;
    private int score=-1; // not initialized
    private MagicRarity rarity;
    private boolean token;
    private int typeFlags;
    private EnumSet<MagicSubType> subTypeFlags = EnumSet.noneOf(MagicSubType.class);
    private int colorFlags;
    private MagicManaCost cost=MagicManaCost.ZERO;
    private String manaSourceText="";
    private final int[] manaSource=new int[MagicColor.NR_COLORS];
    private int power;
    private int toughness;
    private long abilityFlags;
    private String text = "";
    private MagicStaticType staticType=MagicStaticType.None;
    private MagicTiming timing=MagicTiming.None;
    private MagicCardEvent cardEvent=MagicPlayCardEvent.create();
    private MagicCardActivation cardActivation;
    private final Collection<MagicCDA> CDAs = new ArrayList<MagicCDA>();
    private final Collection<MagicTrigger<?>> triggers = new ArrayList<MagicTrigger<?>>();
    private final Collection<MagicStatic> statics=new ArrayList<MagicStatic>();
    private final LinkedList<MagicWhenComesIntoPlayTrigger> comeIntoPlayTriggers = new LinkedList<MagicWhenComesIntoPlayTrigger>();
    private final Collection<MagicWhenSpellIsCastTrigger> spellIsCastTriggers = new ArrayList<MagicWhenSpellIsCastTrigger>();
    private final Collection<MagicWhenDrawnTrigger> drawnTriggers = new ArrayList<MagicWhenDrawnTrigger>();
    private final Collection<MagicWhenPutIntoGraveyardTrigger> putIntoGraveyardTriggers = new ArrayList<MagicWhenPutIntoGraveyardTrigger>();
    private final Collection<MagicActivation> activations=new ArrayList<MagicActivation>();
    private final Collection<MagicManaActivation> manaActivations=new ArrayList<MagicManaActivation>();
    private boolean excludeManaOrCombat;
    
    public MagicCardDefinition() {
        initialize();    
    }
    
    protected void initialize() {}
    
    public static void printStatistics() {
        System.err.println(numTriggers + " triggers");
        System.err.println(numStatics + " statics");
        System.err.println(numPermanentActivations + " permanent activations");
        System.err.println(numManaActivations + " mana activations");
        System.err.println(numSpellEvent + " spell event");
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
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getFullName() {
        return fullName;
    }

    public void setFullName(final String name) {
        fullName = name;
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
    
    public void setCardInfoURL(final String url) {
        this.cardInfoUrl = url;
    }
    
    public String getCardInfoURL() {
        return this.cardInfoUrl;
    }
    
    public String getCardTextName() {
        return getImageName();
    }
        
    public void setValue(final double value) {
        this.value = value;
    }

    public double getValue() {
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
                
    public void setToken() {
        token=true;
    }
    
    public boolean isToken() {
        return token;
    }

    int getTypeFlags() {
        return typeFlags;
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
        return isArtifact() && hasSubType(MagicSubType.Equipment);
    }
    
    public boolean isEnchantment() {
        return hasType(MagicType.Enchantment);
    }
    
    public boolean isLegendary() {
        return hasType(MagicType.Legendary);
    }
    
    public boolean isTribal() {
        return hasType(MagicType.Tribal);
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
        
        if (isTribal()) {
            return "Tribal " + getTypeString();
        }
        return getTypeString();    
    }
    
    public String getTypeString() {
        final StringBuilder sb = new StringBuilder();
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

    EnumSet<MagicSubType> genCardSubTypeFlags() {
        return subTypeFlags.clone();
    }
    
    EnumSet<MagicSubType> getSubTypeFlags() {
        final EnumSet<MagicSubType> subTypes = genCardSubTypeFlags();
        applyCDASubType(null, null, subTypes);
        return subTypes;
    }
    
    public void applyCDASubType(
            final MagicGame game, 
            final MagicPlayer player, 
            final EnumSet<MagicSubType> flags) {
        for (final MagicCDA lv : CDAs) {
            lv.getSubTypeFlags(game, player, flags);
        }
    }
    
    public String getSubTypeString() {
        final String brackets = getSubTypeFlags().toString(); // [...,...]
        if (brackets.length() <= 2) {
            return "";
        }
        return brackets.substring(1, brackets.length() - 1);
    }
    
    public boolean hasSubType(final MagicSubType subType) {
        return subType.hasSubType(getSubTypeFlags());        
    }
    
    public void setColors(final String colors) {
        colorFlags=MagicColor.getFlags(colors);        
    }
    
    public boolean hasColor(final MagicColor color) {
        return (colorFlags&color.getMask())!=0;
    }

    public boolean isColorless() {
        return colorFlags == 0;
    }
        
    public int getColorFlags() {
        return colorFlags;
    }
    
    public int getConvertedCost() {
        return cost.getConvertedCost();
    }
    
    public boolean hasConvertedCost(final int c) {
        return getConvertedCost() == c;
    }
    
    public int getCostBucket() {
        switch (getConvertedCost()) {
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

    public void setCost(final MagicManaCost aCost) {
        cost = aCost;
        colorFlags |= cost.getColorFlags();
    }

    public void validate() {                                                           
        //every card should have a timing hint                                                                                        
        if (!isToken() && getTiming() == MagicTiming.None) {
            throw new RuntimeException(
                getName() + " does not have a timing hint"
            );
        }

        //check colorFlags and color from mana cost matches
        if (!isToken() && cost.getColorFlags() != colorFlags) {
            throw new RuntimeException(
                "name=" + name + " costColorFlags: " + cost.getColorFlags() + " != colorFlags: " + colorFlags
            );
        }
    }

    public MagicManaCost getCost() {
        return cost;
    }
    
    public boolean isPlayable(final MagicPlayerProfile profile) {
        if (isLand()) {
            int source = 0;
            for (final MagicColor color : profile.getColors()) {
                source += getManaSource(color);
            }
            return source > 4;
        } else {
            return cost.getCostScore(profile) > 0;
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
        add(new MagicTapManaActivation(manaTypes,0));
    }
    
    
    public void setPowerToughness(final int aPower, final int aToughness) {
        power = aPower;
        toughness = aToughness;
    }
    
    public int getCardPower() {
        return power;
    }
    
    public int getCardToughness() {
        return toughness;
    }

    public MagicPowerToughness genCardPowerToughness() {
        return new MagicPowerToughness(power, toughness);
    }
        
    public void applyCDAPowerToughness(
            final MagicGame game, 
            final MagicPlayer player, 
            final MagicPermanent perm,
            final MagicPowerToughness pt) {
        for (final MagicCDA lv : CDAs) {
            lv.modPowerToughness(game, player, perm, pt);
        }
    }
    
    public void setAbility(final MagicAbility ability) {
        setAbility(ability, "");
    }
    
    public void setAbility(final MagicAbility ability, final String arg) {
        ability.addAbilityImpl(this, arg);
    }

    void setAbilityFlags(final long flags) {
        abilityFlags = flags;
    }

    public long getAbilityFlags() {
        return abilityFlags;
    }
    
    public boolean hasAbility(final MagicAbility ability) {
        return ability.hasAbility(abilityFlags);
    }
    
    public void setText(final String text) {
        this.text = text;
    }
    
    public String getText() {
        return this.text;
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

    public void add(final MagicChangeCardDefinition mod) {
        mod.change(this);
    }
    
    public void setEvent(final MagicCardEvent cardEvent) {
        this.cardEvent=cardEvent;
        numSpellEvent++;
    }
    
    public MagicCardEvent getCardEvent() {
        return cardEvent;
    }

    public MagicActivationHints getActivationHints() {
        return new MagicActivationHints(timing,true);
    }
    
    public MagicCardActivation getCardActivation() {
        if (cardActivation==null) {
            cardActivation=new MagicCardActivation(this);
        }
        return cardActivation;
    }
    
    public void addCDA(final MagicCDA cda) {
        CDAs.add(cda);
        numCDAs++;
    }

    public void addTrigger(final MagicWhenSpellIsCastTrigger trigger) {
        spellIsCastTriggers.add(trigger);
        numTriggers++;
    }

    public void addTrigger(final MagicWhenComesIntoPlayTrigger trigger) {
        if (trigger.usesStack()) {
            comeIntoPlayTriggers.add(trigger);
        } else {
            comeIntoPlayTriggers.addFirst(trigger);
        }
        numTriggers++;
    }

    public void addTrigger(final MagicWhenPutIntoGraveyardTrigger trigger) {
        putIntoGraveyardTriggers.add(trigger);
        numTriggers++;
    }

    public void addTrigger(final MagicWhenDrawnTrigger trigger) {
        drawnTriggers.add(trigger);
        numTriggers++;
    }
        
    public void addTrigger(final MagicTrigger<?> trigger) {
        triggers.add(trigger);
        numTriggers++;
    }
    
    public void addStatic(final MagicStatic mstatic) {
        statics.add(mstatic);
        numStatics++;
    }
    
    public Collection<MagicTrigger<?>> getTriggers() {
        return triggers;
    }
    
    public Collection<MagicStatic> getStatics() {
        return statics;
    }
    
    public Collection<MagicWhenSpellIsCastTrigger> getSpellIsCastTriggers() {
        return spellIsCastTriggers;
    }
    
    public Collection<MagicWhenComesIntoPlayTrigger> getComeIntoPlayTriggers() {
        return comeIntoPlayTriggers;
    }
    
    public Collection<MagicWhenPutIntoGraveyardTrigger> getPutIntoGraveyardTriggers() {
        return putIntoGraveyardTriggers;
    }
    
    public Collection<MagicWhenDrawnTrigger> getDrawnTriggers() {
        return drawnTriggers;
    }
    
    public void addAct(final MagicPermanentActivation activation) {
        activations.add(activation);
        numPermanentActivations++;
    }
    
    public Collection<MagicActivation> getActivations() {
        return activations;
    }
    
    public void addManaAct(final MagicManaActivation activation) {
        manaActivations.add(activation);
        numManaActivations++;
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
        for (final MagicAbility ability : MagicAbility.CORE) {
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
            abilityHasText(s) ||
            getText().toLowerCase().contains(s)
        );
    }
    
    @Override
    public String toString() {
        return getName();
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
            final int c = cardDefinition1.getTypeString().compareTo(cardDefinition2.getTypeString());
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

    public static final Comparator<MagicCardDefinition> POWER_COMPARATOR_DESC=new Comparator<MagicCardDefinition>() {
        @Override
        public int compare(final MagicCardDefinition cardDefinition1,final MagicCardDefinition cardDefinition2) {
            if(!cardDefinition1.isCreature()) {
                return 1;
            } else if (!cardDefinition2.isCreature()) {
                return -1;
            }
            return cardDefinition1.getCardPower() - cardDefinition2.getCardPower();
        }
    };

    public static final Comparator<MagicCardDefinition> POWER_COMPARATOR_ASC=new Comparator<MagicCardDefinition>() {
        @Override
        public int compare(final MagicCardDefinition cardDefinition1,final MagicCardDefinition cardDefinition2) {
            return POWER_COMPARATOR_DESC.compare(cardDefinition2, cardDefinition1);
        }
    };

    public static final Comparator<MagicCardDefinition> TOUGHNESS_COMPARATOR_DESC=new Comparator<MagicCardDefinition>() {
        @Override
        public int compare(final MagicCardDefinition cardDefinition1,final MagicCardDefinition cardDefinition2) {
            if(!cardDefinition1.isCreature()) {
                return 1;
            } else if (!cardDefinition2.isCreature()) {
                return -1;
            }
            return cardDefinition1.getCardToughness() - cardDefinition2.getCardToughness();
        }
    };

    public static final Comparator<MagicCardDefinition> TOUGHNESS_COMPARATOR_ASC=new Comparator<MagicCardDefinition>() {
        @Override
        public int compare(final MagicCardDefinition cardDefinition1,final MagicCardDefinition cardDefinition2) {
            return TOUGHNESS_COMPARATOR_DESC.compare(cardDefinition2, cardDefinition1);
        }
    };
}
