package magic.model;

import magic.ai.ArtificialScoringSystem;
import magic.data.CardDefinitions;
import magic.data.CardProperty;
import magic.model.event.MagicActivation;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicCardActivation;
import magic.model.event.MagicCardEvent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventSource;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPlayCardEvent;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicCDA;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenDrawnTrigger;
import magic.model.trigger.MagicWhenPutIntoGraveyardTrigger;
import magic.model.trigger.MagicWhenSpellIsCastTrigger;
import magic.model.trigger.MagicWhenCycleTrigger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Date;

public class MagicCardDefinition implements MagicAbilityStore {

    public static final MagicCardDefinition UNKNOWN = new MagicCardDefinition() {
        //definition for unknown cards
        @Override
        protected void initialize() {
            setName("Unknown");
            setDistinctName("Unknown");
            setToken();
            setValue(1);
            addType(MagicType.Creature);
            setCost(MagicManaCost.create("{15}"));
            setPowerToughness(1,1);
            addAbility(MagicAbility.Defender);
            addAbility(MagicAbility.CannotBeCountered);
            addAbility(MagicAbility.Shroud);
            setTiming(MagicTiming.Main);
            setIndex(1000000);
        }
    };

    public static final MagicCardDefinition MORPH = new MagicCardDefinition() {
        //definition for face down cards
        @Override
        protected void initialize() {
            setName("");
            setDistinctName("2/2 face-down creature");
            setValue(1);
            addType(MagicType.Creature);
            setCost(MagicManaCost.create("{0}"));
            setPowerToughness(2, 2);
            setTiming(MagicTiming.Main);
            setIndex(1000001);
        }
    };
    
    // name displayed in UI, may be repeated in tokens
    private String name;

    // name used for mapping and persistence, must be unique
    private String distinctName;
    private boolean isValid = true;
    private boolean isScriptFileMissing = false;

    private String imageURL;
    private String cardInfoUrl = "";
    private int imageCount = 1;
    private Date imageUpdated;
    private int index=-1;
    private double value;
    private int gathererRating;
    private int removal;
    private int score=-1; // not initialized
    private MagicRarity rarity;
    private boolean token = false;
    private boolean hidden = false;
    private int typeFlags;
    private EnumSet<MagicSubType> subTypeFlags = EnumSet.noneOf(MagicSubType.class);
    private EnumSet<MagicAbility> abilityFlags = EnumSet.noneOf(MagicAbility.class);
    private int colorFlags = -1;
    private MagicManaCost cost=MagicManaCost.ZERO;
    private String manaSourceText="";
    private final int[] manaSource=new int[MagicColor.NR_COLORS];
    private int power;
    private int toughness;
    private String text = "";
    private MagicStaticType staticType=MagicStaticType.None;
    private MagicTiming timing=MagicTiming.None;
    private MagicCardEvent cardEvent=MagicPlayCardEvent.create();
    private final Collection<MagicActivation<MagicPermanent>> permActivations=new ArrayList<MagicActivation<MagicPermanent>>();
    private final Collection<MagicActivation<MagicPermanent>> morphActivations=new ArrayList<MagicActivation<MagicPermanent>>();
    private final LinkedList<MagicActivation<MagicCard>> cardActivations = new LinkedList<MagicActivation<MagicCard>>();
    private final LinkedList<MagicActivation<MagicCard>> graveyardActivations = new LinkedList<MagicActivation<MagicCard>>();
    private final Collection<MagicCDA> CDAs = new ArrayList<MagicCDA>();
    private final Collection<MagicTrigger<?>> triggers = new ArrayList<MagicTrigger<?>>();
    private final Collection<MagicStatic> statics=new ArrayList<MagicStatic>();
    private final LinkedList<MagicWhenComesIntoPlayTrigger> comeIntoPlayTriggers = new LinkedList<MagicWhenComesIntoPlayTrigger>();
    private final Collection<MagicWhenSpellIsCastTrigger> spellIsCastTriggers = new ArrayList<MagicWhenSpellIsCastTrigger>();
    private final Collection<MagicWhenCycleTrigger> cycleTriggers = new ArrayList<MagicWhenCycleTrigger>();
    private final Collection<MagicWhenDrawnTrigger> drawnTriggers = new ArrayList<MagicWhenDrawnTrigger>();
    private final Collection<MagicWhenPutIntoGraveyardTrigger> putIntoGraveyardTriggers = new ArrayList<MagicWhenPutIntoGraveyardTrigger>();
    private final Collection<MagicManaActivation> manaActivations=new ArrayList<MagicManaActivation>();
    private final Collection<MagicEventSource> costEventSources=new ArrayList<MagicEventSource>();
    private boolean excludeManaOrCombat;
    private MagicCardDefinition flipCardDefinition;
    private MagicCardDefinition transformCardDefinition;

    private String abilityProperty;
    private String requiresGroovy;
    private String effectProperty;
    private String flipCardName;
    private String transformCardName;

    private boolean isMissing = false;

    private Set<MagicType> cardType = EnumSet.noneOf(MagicType.class);

    public MagicCardDefinition() {
        initialize();
    }
    
    public static MagicCardDefinition create(final MagicCardDefinitionInit init) {
        final MagicCardDefinition cdef = new MagicCardDefinition();
        init.initialize(cdef);
        cdef.validate();
        return cdef;
    }

    protected void initialize() {}

    public void setAbilityProperty(final String value) {
        abilityProperty = value;
    }

    public void setRequiresGroovy(final String value) {
        requiresGroovy = value;
    }
    
    public void setEffectProperty(final String value) {
        effectProperty = value;
    }
    
    public void setFlipCardName(final String value) {
        flipCardName = value;
    }
    
    public void setTransformCardName(final String value) {
        transformCardName = value;
    }
    
    public void setHidden() {
        hidden = true;
    }
    
    public boolean isHidden() {
        return hidden;
    }

    public void loadAbilities() {
        if (requiresGroovy != null) {
            CardProperty.LOAD_GROOVY_CODE.setProperty(this, requiresGroovy);
            requiresGroovy = null;
        }
        if (abilityProperty != null) {
            CardProperty.LOAD_ABILITY.setProperty(this, abilityProperty);
            abilityProperty = null;
        }
        if (effectProperty != null) {
            CardProperty.LOAD_EFFECT.setProperty(this, effectProperty);
            effectProperty = null;
        }
        if (getFlippedDefinition().isHidden()) {
            flipCardDefinition.loadAbilities();
        }
        if (getTransformedDefinition().isHidden()) {
            transformCardDefinition.loadAbilities();
        }
    }

    public boolean isValid() {
        return isValid;
    }
    public void setIsValid(boolean b) {
        this.isValid = b;
    }

    public void setImageUpdated(final Date d) {
        imageUpdated = d;
    }

    public boolean isImageUpdatedAfter(final Date d) {
        return imageUpdated != null && imageUpdated.after(d);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDistinctName() {
        return distinctName;
    }

    public void setDistinctName(final String name) {
        distinctName = name;
    }

    /**
     * Returns the name of the card containing only ASCII characters.
     */
    public String getAsciiName() {
        return CardDefinitions.getASCII(distinctName);
    }
    
    /**
     * Returns the name of the script/groovy file without extension
     */
    public String getFilename() {
        return CardDefinitions.getCanonicalName(distinctName);
    }

    public void setIndex(final int index) {
        this.index=index;
    }

    public int getIndex() {
        return index;
    }

    public String getImageName() {
        return token ?
            CardDefinitions.getCanonicalName(distinctName):
            distinctName.replaceAll("[<>:\"/\\\\|?*\\x00-\\x1F]", "_");
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
        this.gathererRating = (int)(value * 1000);
    }

    public double getValue() {
        return value;
    }

    /**
     * Returns the "value" * 1000 which is used to sort cards in the card explorer.
     * <p>
     * The "value" property actually represents the members rating from the Gatherer
     * website but as a double value it cannot be used with a comparator.
     */
    public int getGathererRating() {
        return gathererRating;
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
        return (rarity == null ? "" : rarity.getName());
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
        typeFlags |= type.getMask();
        if (type == MagicType.Land) {
            if (colorFlags == -1) {
                // Lands default to colorless
                colorFlags = 0;
            } else {
                assert colorFlags != 0 : "redundant color declaration: " + colorFlags;
            }
        }
        cardType.add(type);
    }

    public Set<MagicType> getCardType() {
        return cardType;
    }

    public boolean hasType(final MagicType type) {
        return (typeFlags&type.getMask())!=0;
    }
    
    public MagicCardDefinition getFlippedDefinition() {
        if (flipCardDefinition == null) {
            flipCardDefinition = isFlipCard() ?
                CardDefinitions.getCard(flipCardName) :
                MagicCardDefinition.UNKNOWN;
        }
        return flipCardDefinition;
    }

    public MagicCardDefinition getTransformedDefinition() {
        if (transformCardDefinition == null) {
            transformCardDefinition = isDoubleFaced() ?
                CardDefinitions.getCard(transformCardName) :
                MagicCardDefinition.UNKNOWN;
        }
        return transformCardDefinition;
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
        return hasSubType(MagicSubType.Equipment);
    }

    public boolean isPlaneswalker() {
        return hasType(MagicType.Planeswalker);
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

    public boolean isPermanent() {
        return isSpell() == false;
    }
    
    public boolean isFlipCard() {
        return flipCardName != null;
    }
    
    public boolean isDoubleFaced() {
        return transformCardName != null;
    }

    public boolean hasMultipleAspects() {
        return isFlipCard() || isDoubleFaced();
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
        subTypeFlags = MagicSubType.getSubTypes(subTypeNames);
    }

    public void addSubType(final MagicSubType subType) {
        subTypeFlags.add(subType);
    }

    EnumSet<MagicSubType> genSubTypeFlags() {
        return subTypeFlags.clone();
    }

    public EnumSet<MagicSubType> getSubTypeFlags() {
        final EnumSet<MagicSubType> subTypes = genSubTypeFlags();
        applyCDASubType(null, null, subTypes);
        return subTypes;
    }

    public void applyCDASubType(final MagicGame game, final MagicPlayer player, final Set<MagicSubType> flags) {
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
        return getSubTypeFlags().contains(subType);
    }

    public void setColors(final String colors) {
        colorFlags = MagicColor.getFlags(colors);
        assert cost == MagicManaCost.ZERO || colorFlags != cost.getColorFlags() : "redundant color declaration: " + colorFlags;
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
        if (colorFlags == -1) {
            // color defaults to follow mana cost
            colorFlags = cost.getColorFlags();
        } else {
            assert colorFlags != cost.getColorFlags() : "redundant color declaration: " + colorFlags;
        }
    }

    public void validate() {
        //every card should have a timing hint
        if (!isToken() && getTiming() == MagicTiming.None) {
            throw new RuntimeException(
                getName() + " does not have a timing hint"
            );
        }

        //check colorFlags is set
        if (colorFlags == -1) {
            throw new RuntimeException(name + "'s color is not set");
        }
    }

    public MagicManaCost getCost() {
        return cost;
    }

    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        final List<MagicEvent> costEvent = new ArrayList<MagicEvent>();
        if (cost != MagicManaCost.ZERO) {
            costEvent.add(new MagicPayManaCostEvent(
                source,
                cost
            ));
        }
        for (final MagicEventSource eventSource : costEventSources) {
            costEvent.add(eventSource.getEvent(source));
        }
        return costEvent;
    }

    public boolean isPlayable(final MagicDeckProfile profile) {
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

    public MagicPowerToughness genPowerToughness() {
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

    public void addAbility(final MagicAbility ability) {
        abilityFlags.add(ability);
    }

    public Set<MagicAbility> genAbilityFlags() {
        return abilityFlags.clone();
    }

    public boolean hasAbility(final MagicAbility ability) {
        return abilityFlags.contains(ability);
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
    
    public String getFlattenedText() {
        return this.text.replace("\n", " ");
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

    public void setEvent(final MagicCardEvent aCardEvent) {
        assert cardEvent == MagicPlayCardEvent.create() : "Attempting to set two MagicCardEvents for " + this;
        cardEvent = aCardEvent;
    }

    public MagicCardEvent getCardEvent() {
        return cardEvent;
    }

    public MagicActivationHints getActivationHints() {
        return new MagicActivationHints(timing,true);
    }

    // cast card activation is the first element of cardActivations
    public MagicActivation<MagicCard> getCastActivation() {
        assert cardActivations.size() >= 1 : this + " has no card activations";
        return cardActivations.getFirst();
    }

    public Collection<MagicActivation<MagicCard>> getCardActivations() {
        return cardActivations;
    }

    public Collection<MagicActivation<MagicCard>> getGraveyardActivations() {
        return graveyardActivations;
    }

    public void addCDA(final MagicCDA cda) {
        CDAs.add(cda);
    }

    public void addCostEvent(final MagicEventSource eventSource) {
        costEventSources.add(eventSource);
    }

    public void addTrigger(final MagicWhenSpellIsCastTrigger trigger) {
        spellIsCastTriggers.add(trigger);
    }
    
    public void addTrigger(final MagicWhenCycleTrigger trigger) {
        cycleTriggers.add(trigger);
    }

    public void addTrigger(final MagicWhenComesIntoPlayTrigger trigger) {
        if (trigger.usesStack()) {
            comeIntoPlayTriggers.add(trigger);
        } else {
            comeIntoPlayTriggers.addFirst(trigger);
        }
    }

    public void addTrigger(final MagicWhenPutIntoGraveyardTrigger trigger) {
        putIntoGraveyardTriggers.add(trigger);
    }

    public void addTrigger(final MagicWhenDrawnTrigger trigger) {
        drawnTriggers.add(trigger);
    }

    public void addTrigger(final MagicTrigger<?> trigger) {
        triggers.add(trigger);
    }

    public void addStatic(final MagicStatic mstatic) {
        statics.add(mstatic);
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
    
    public Collection<MagicWhenCycleTrigger> getCycleTriggers() {
        return cycleTriggers;
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
        permActivations.add(activation);
    }
    
    public void addMorphAct(final MagicPermanentActivation activation) {
        morphActivations.add(activation);
    }

    public void addCardAct(final MagicCardActivation activation) {
        cardActivations.add(activation);
    }

    public void addGraveyardAct(final MagicCardActivation activation) {
        graveyardActivations.add(activation);
    }

    public void setCardAct(final MagicCardActivation activation) {
        assert cardActivations.size() == 1 : "removing multiple (" + cardActivations.size() + ") card activations";
        cardActivations.clear();
        cardActivations.add(activation);
    }

    public Collection<MagicActivation<MagicPermanent>> getActivations() {
        return permActivations;
    }
    
    public Collection<MagicActivation<MagicPermanent>> getMorphActivations() {
        return morphActivations;
    }

    public void addManaAct(final MagicManaActivation activation) {
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

    private boolean subTypeHasText(final String s) {
        final MagicSubType[] subTypeValues = MagicSubType.values();
        for (final MagicSubType subtype : subTypeValues) {
            if (subTypeFlags.contains(subtype) && subtype.toString().toLowerCase().contains(s)) {
                return true;
            }
        }
        return false;
    }

    private boolean abilityHasText(final String s) {
        for (final MagicAbility ability : MagicAbility.values()) {
            if (hasAbility(ability) && ability.getName().toLowerCase().contains(s)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasText(String s) {
        s = s.toLowerCase();
        return (
            CardDefinitions.getASCII(distinctName).toLowerCase().contains(s) ||
            CardDefinitions.getASCII(name).toLowerCase().contains(s) ||
            subTypeHasText(s) ||
            abilityHasText(s) ||
            CardDefinitions.getASCII(getText()).toLowerCase().contains(s)
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
            if (c == 0) {
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
            final int p1 = cardDefinition1.isCreature() ? cardDefinition1.getCardPower() : -100;
            final int p2 = cardDefinition2.isCreature() ? cardDefinition2.getCardPower() : -100;

            if (p1 != p2) {
                return p1 - p2;
            } else {
                return cardDefinition1.getName().compareTo(cardDefinition2.getName());
            }
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
            final int t1 = cardDefinition1.isCreature() ? cardDefinition1.getCardToughness() : -100;
            final int t2 = cardDefinition2.isCreature() ? cardDefinition2.getCardToughness() : -100;

            if (t1 != t2) {
                return t1 - t2;
            } else {
                return cardDefinition1.getName().compareTo(cardDefinition2.getName());
            }
        }
    };

    public static final Comparator<MagicCardDefinition> TOUGHNESS_COMPARATOR_ASC=new Comparator<MagicCardDefinition>() {
        @Override
        public int compare(final MagicCardDefinition cardDefinition1,final MagicCardDefinition cardDefinition2) {
            return TOUGHNESS_COMPARATOR_DESC.compare(cardDefinition2, cardDefinition1);
        }
    };

    public void setIsMissing(boolean b) {
        this.isMissing = b;
    }
    public boolean isMissing() {
        return isMissing;
    }

    public void setIsScriptFileMissing(boolean b) {
        isScriptFileMissing = b;
    }
    public boolean IsScriptFileMissing() {
        return isScriptFileMissing;
    }
}
