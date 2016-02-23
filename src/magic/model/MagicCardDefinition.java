package magic.model;

import java.util.*;

import magic.ai.ArtificialScoringSystem;
import magic.data.CardDefinitions;
import magic.data.CardProperty;
import magic.model.event.*;
import magic.model.mstatic.MagicCDA;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.EntersBattlefieldTrigger;
import magic.model.trigger.EntersWithCounterTrigger;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.ThisCycleTrigger;
import magic.model.trigger.ThisDrawnTrigger;
import magic.model.trigger.ThisPutIntoGraveyardTrigger;
import magic.model.trigger.ThisSpellIsCastTrigger;
import magic.ui.cardBuilder.IRenderableCard;
import magic.utility.MagicFileSystem;

public class MagicCardDefinition implements MagicAbilityStore, IRenderableCard {

    public static final MagicCardDefinition UNKNOWN = new MagicCardDefinition() {
        //definition for unknown cards
        @Override
        protected void initialize() {
            setName("Unknown");
            setDistinctName("Unknown");
            setToken();
            setValue(1);
            addType(MagicType.Creature);
            setColors("");
            setPowerToughness(1,1);
            setTiming(MagicTiming.Main);
            setIndex(1000000);
        }
    };

    // name displayed in UI, may be repeated in tokens
    private String name;

    // name used for mapping and persistence, must be unique
    private String distinctName;

    private String imageURL;
    private Date imageUpdated;
    private int index=-1;
    private double value;
    private int removal;
    private int score=-1; // not initialized
    private MagicRarity rarity;
    private boolean valid = true;
    private boolean token = false;
    private boolean secondHalf = false;
    private boolean hidden = false;
    private boolean overlay = false;
    private boolean excludeManaOrCombat = false;
    private int typeFlags;
    private EnumSet<MagicType> cardType = EnumSet.noneOf(MagicType.class);
    private EnumSet<MagicSubType> subTypeFlags = EnumSet.noneOf(MagicSubType.class);
    private String subTypeText ="";
    private EnumSet<MagicAbility> abilityFlags = EnumSet.noneOf(MagicAbility.class);
    private int colorFlags = -1;
    private MagicManaCost cost = MagicManaCost.NONE;
    private String manaSourceText="";
    private final int[] manaSource=new int[MagicColor.NR_COLORS];
    private int power;
    private int toughness;
    private String powerToughnessText = "";
    private int startingLoyalty;
    private String text = "";
    private MagicStaticType staticType=MagicStaticType.None;
    private MagicTiming timing=MagicTiming.None;
    private MagicCardEvent cardEvent=MagicPlayCardEvent.create();
    private final Collection<MagicActivation<MagicPermanent>> permActivations=new ArrayList<MagicActivation<MagicPermanent>>();
    private final Collection<MagicActivation<MagicPermanent>> morphActivations=new ArrayList<MagicActivation<MagicPermanent>>();
    private final LinkedList<MagicActivation<MagicCard>> handActivations = new LinkedList<MagicActivation<MagicCard>>();
    private final LinkedList<MagicActivation<MagicCard>> graveyardActivations = new LinkedList<MagicActivation<MagicCard>>();
    private final Collection<MagicCDA> CDAs = new ArrayList<MagicCDA>();
    private final Collection<MagicTrigger<?>> triggers = new ArrayList<MagicTrigger<?>>();
    private final Collection<MagicStatic> statics=new ArrayList<MagicStatic>();
    private final LinkedList<EntersBattlefieldTrigger> etbTriggers = new LinkedList<EntersBattlefieldTrigger>();
    private final Collection<ThisSpellIsCastTrigger> spellIsCastTriggers = new ArrayList<ThisSpellIsCastTrigger>();
    private final Collection<ThisCycleTrigger> cycleTriggers = new ArrayList<ThisCycleTrigger>();
    private final Collection<ThisDrawnTrigger> drawnTriggers = new ArrayList<ThisDrawnTrigger>();
    private final Collection<ThisPutIntoGraveyardTrigger> putIntoGraveyardTriggers = new ArrayList<ThisPutIntoGraveyardTrigger>();
    private final Collection<MagicManaActivation> manaActivations=new ArrayList<MagicManaActivation>();
    private final Collection<MagicEventSource> costEventSources=new ArrayList<MagicEventSource>();

    private MagicCardDefinition flipCardDefinition;
    private MagicCardDefinition transformCardDefinition;
    private MagicCardDefinition splitCardDefinition;

    private String abilityProperty;
    private String requiresGroovy;
    private String effectProperty;
    private String flipCardName;
    private String transformCardName;
    private String splitCardName;

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

    public void setSplitCardName(final String value) {
        splitCardName = value;
    }

    public void setSecondHalf() {
        secondHalf = true;
    }

    public boolean isSecondHalf() {
        return secondHalf;
    }

    public void setHidden() {
        hidden = true;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setOverlay() {
        overlay = true;
    }

    public boolean isOverlay() {
        return overlay;
    }

    public boolean isPlayable() {
        return overlay == false && token == false && hidden == false && secondHalf == false;
    }

    public boolean isNonPlayable() {
        return isPlayable() == false;
    }

    public synchronized void loadAbilities() {
        if (startingLoyalty > 0 && etbTriggers.isEmpty()) {
            add(new EntersWithCounterTrigger(
                MagicCounterType.Loyalty,
                startingLoyalty
            ));
        }
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
        if (getSplitDefinition().isSecondHalf()) {
            splitCardDefinition.loadAbilities();
        }
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isInvalid() {
        return valid == false;
    }

    public void setInvalid() {
        valid = false;
    }

    public void setImageUpdated(final Date d) {
        imageUpdated = d;
    }

    /**
     * Returns true if script file has a non-null {@code image_updated} property
     * whose value is a date that comes after the given date.
     */
    public boolean isImageUpdatedAfter(final Date d) {
        return imageUpdated != null && imageUpdated.after(d);
    }

    /**
     * Returns the name of the card exactly as it appears on the printed card.
     * <p>
     * Note that in the case of token cards this means it may return the
     * same name (eg. five different Wurm tokens would all return "Wurm").
     *
     * @see getDistinctName()
     */
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns a guaranteed distinct card name.
     * <p>
     * In most cases this will be the same as {@link getName()} but for tokens
     * of the same type (eg. Wurm) this will return a name that clearly identifies
     * the card (eg. 5/5 green Wurm creature token with trample).
     *
     */
    public String getDistinctName() {
        return distinctName;
    }

    public void setDistinctName(String aName) {
        assert (this.name.equals(aName) ? this.name == aName : true) : "Same name but using two separate strings. Should reference same string for efficiency.";
        distinctName = aName;
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

    public void setImageURL(final String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public boolean hasImageUrl() {
        return imageURL != null;
    }

    public String getCardTextName() {
        return getImageName();
    }

    public void setValue(final double aValue) {
        value = aValue;
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
        return rarity == null ? -1 : rarity.ordinal();
    }

    public String getRarityString() {
        return (rarity == null ? "" : rarity.getName());
    }

    public Character getRarityChar() {
        return rarity == null ? 'C' : rarity.getChar();//Return common for null rarity
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
                CardDefinitions.getMissingOrCard(flipCardName) :
                MagicCardDefinition.UNKNOWN;
        }
        return flipCardDefinition;
    }

    public MagicCardDefinition getTransformedDefinition() {
        if (transformCardDefinition == null) {
            transformCardDefinition = isDoubleFaced() ?
                CardDefinitions.getMissingOrCard(transformCardName) :
                MagicCardDefinition.UNKNOWN;
        }
        return transformCardDefinition;
    }

    public MagicCardDefinition getSplitDefinition() {
        if (splitCardDefinition == null) {
            splitCardDefinition = isSplitCard() ?
                CardDefinitions.getMissingOrCard(splitCardName) :
                UNKNOWN;
        }
        return splitCardDefinition;
    }

    public MagicCardDefinition getCardDefinition() {
        return this;
    }

    public boolean isBasic() {
        return hasType(MagicType.Basic);
    }

    public boolean isEquipment() {
        return hasSubType(MagicSubType.Equipment);
    }

    public boolean isPlaneswalker() {
        return hasType(MagicType.Planeswalker);
    }

    public boolean isLegendary() {
        return hasType(MagicType.Legendary);
    }

    public boolean isTribal() {
        return hasType(MagicType.Tribal);
    }

    public boolean isSnow() {
        return hasType(MagicType.Snow);
    }

    public boolean isWorld() {
        return hasType(MagicType.World);
    }

    public boolean isAura() {
        return isEnchantment() && hasSubType(MagicSubType.Aura);
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

    public boolean isSplitCard() {
        return splitCardName != null;
    }

    public boolean hasMultipleAspects() {
        return isFlipCard() || isDoubleFaced();
    }

    public String getLongTypeString() {
        if (isBasic()) {
            if (isSnow()) {
                return "Basic Snow " + getTypeString();
            } else {
                return "Basic " + getTypeString();
            }
        }
        if (isLegendary()) {
            if (isSnow()) {
                return "Legendary Snow " + getTypeString();
            } else {
                return "Legendary " + getTypeString();
            }
        }
        if (isTribal()) {
            return "Tribal " + getTypeString();
        }
        if (isSnow()) {
            return "Snow " + getTypeString();
        }
        if (isWorld()) {
            return "World " + getTypeString();
        }
        return getTypeString();
    }

    public String getTypeString() {
        final StringBuilder sb = new StringBuilder();
        MagicType.TYPE_ORDER.stream().filter(this::hasType).forEach(type -> sb.append(type).append(' '));
        return sb.toString().trim();

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

    EnumSet<MagicSubType> genSubTypes() {
        return subTypeFlags.clone();
    }

    public EnumSet<MagicSubType> getSubTypes() {
        final EnumSet<MagicSubType> subTypes = genSubTypes();
        applyCDASubType(null, null, subTypes);
        return subTypes;
    }

    public void applyCDASubType(final MagicGame game, final MagicPlayer player, final Set<MagicSubType> flags) {
        for (final MagicCDA lv : CDAs) {
            lv.getSubTypeFlags(game, player, flags);
        }
    }

    public String getSubTypeString() {
        final String brackets = getSubTypes().toString(); // [...,...]
        if (brackets.length() <= 2) {
            return "";
        }
        return brackets.substring(1, brackets.length() - 1);
    }

    public boolean hasSubType(final MagicSubType subType) {
        return getSubTypes().contains(subType);
    }

    public void setColors(final String colors) {
        colorFlags = MagicColor.getFlags(colors);
        assert hasCost() == false || colorFlags != cost.getColorFlags() : "redundant color declaration: " + colorFlags;
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

    public int applyCDAColor(final MagicGame game, final MagicPlayer player, final int initColor) {
        int color = initColor;
        for (final MagicCDA lv : CDAs) {
            color =lv.getColorFlags(game, player, color);
        }
        return color;
    }

    public int getConvertedCost() {
        return cost.getConvertedCost();
    }

    public int getConvertedCost(final int x) {
        return cost.getConvertedCost(x);
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

        //every Aura should have an MagicPlayAuraEvent
        if (isAura() && cardEvent == MagicPlayCardEvent.create()) {
            throw new RuntimeException(name + " does not have the enchant property");
        }
    }

    public MagicManaCost getCost() {
        return cost;
    }

    public boolean hasCost() {
        return cost != MagicManaCost.NONE;
    }

    public List<MagicEvent> getCostEvent(final MagicCard source) {
        final List<MagicEvent> costEvent = new ArrayList<MagicEvent>();
        if (hasCost()) {
            costEvent.add(MagicPayManaCostEvent.Cast(
                source,
                cost
            ));
        }
        costEvent.addAll(getAdditionalCostEvent(source));
        return costEvent;
    }

    public List<MagicEvent> getAdditionalCostEvent(final MagicCard source) {
        final List<MagicEvent> costEvent = new ArrayList<MagicEvent>();
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

    public void setStartingLoyalty(final int aLoyalty) {
        startingLoyalty = aLoyalty;
    }

    public int getStartingLoyalty() {
        return startingLoyalty;
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

    public void applyCDAPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPermanent perm, final MagicPowerToughness pt) {
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

    // cast card activation is the first element of handActivations
    public MagicActivation<MagicCard> getCastActivation() {
        assert handActivations.size() >= 1 : this + " has no card activations";
        return handActivations.getFirst();
    }

    public Collection<MagicActivation<MagicCard>> getHandActivations() {
        return handActivations;
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

    public void addTrigger(final ThisSpellIsCastTrigger trigger) {
        spellIsCastTriggers.add(trigger);
    }

    public void addTrigger(final ThisCycleTrigger trigger) {
        cycleTriggers.add(trigger);
    }

    public void addTrigger(final EntersBattlefieldTrigger trigger) {
        if (trigger.usesStack()) {
            etbTriggers.add(trigger);
        } else {
            etbTriggers.addFirst(trigger);
        }
    }

    public void addTrigger(final ThisPutIntoGraveyardTrigger trigger) {
        putIntoGraveyardTriggers.add(trigger);
    }

    public void addTrigger(final ThisDrawnTrigger trigger) {
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

    public Collection<ThisSpellIsCastTrigger> getSpellIsCastTriggers() {
        return spellIsCastTriggers;
    }

    public Collection<ThisCycleTrigger> getCycleTriggers() {
        return cycleTriggers;
    }

    public Collection<EntersBattlefieldTrigger> getETBTriggers() {
        return etbTriggers;
    }

    public Collection<ThisPutIntoGraveyardTrigger> getPutIntoGraveyardTriggers() {
        return putIntoGraveyardTriggers;
    }

    public Collection<ThisDrawnTrigger> getDrawnTriggers() {
        return drawnTriggers;
    }

    public void addAct(final MagicPermanentActivation activation) {
        permActivations.add(activation);
    }

    public void addMorphAct(final MagicPermanentActivation activation) {
        morphActivations.add(activation);
    }

    public void addHandAct(final MagicHandCastActivation activation) {
        handActivations.add(activation);
    }

    public void addGraveyardAct(final MagicHandCastActivation activation) {
        graveyardActivations.add(activation);
    }

    public void setHandAct(final MagicHandCastActivation activation) {
        assert handActivations.size() == 1 : "removing multiple (" + handActivations.size() + ") card activations";
        handActivations.clear();
        handActivations.add(activation);
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
    
    public static final Comparator<MagicCardDefinition> SUBTYPE_COMPARATOR_DESC=new Comparator<MagicCardDefinition>() {
        @Override
        public int compare(final MagicCardDefinition cardDefinition1,final MagicCardDefinition cardDefinition2) {
            final int c = cardDefinition1.getSubTypeString().compareTo(cardDefinition2.getSubTypeString());
            return c;
        }
    };
    
    public static final Comparator<MagicCardDefinition> SUBTYPE_COMPARATOR_ASC=new Comparator<MagicCardDefinition>() {
        @Override
        public int compare(final MagicCardDefinition cardDefinition1,final MagicCardDefinition cardDefinition2) {
            return SUBTYPE_COMPARATOR_DESC.compare(cardDefinition2, cardDefinition1);
        }
    };

    public static final Comparator<MagicCardDefinition> RARITY_COMPARATOR_DESC=new Comparator<MagicCardDefinition>() {
        @Override
        public int compare(final MagicCardDefinition cardDefinition1,final MagicCardDefinition cardDefinition2) {
            return cardDefinition1.getRarity() - cardDefinition2.getRarity();
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

    public boolean isImageFileMissing() {
        return MagicFileSystem.isCardImageMissing(this);
    }

    public void setPowerToughnessText(String string) {
        powerToughnessText = string;
    }

    public String getPowerToughnessText() {
        return powerToughnessText;
    }

    public void setSubtypeText(String string) {
        final String subTypeList = string.replaceAll("(\\w),(\\w)", "$1, $2");// Not automatically adding space unless space is there
        subTypeText = subTypeList;
    }

    public String getSubTypeText() {
        return subTypeText;
    }

    public String getPowerLabel() {
        if (isCreature() && "".equals(powerToughnessText) == false) {
            return powerToughnessText.split("/")[0];
        } else {
            return "";
        }
    }

    public String getToughnessLabel() {
        if (isCreature() && "".equals(powerToughnessText) == false) {
            return powerToughnessText.split("/")[1];
        } else {
            return "";
        }
    }

    @Override
    public boolean hasText() {
        if (getText().contains("NONE") || getText().length() <=1) {
            return false;
        } else {
            return true;
		}
	}
}
