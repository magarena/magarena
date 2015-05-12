package magic.data;

public enum MagicIcon {

    HEADER_ICON("headerIcon.png"),
    OPTIONS_ICON("w_book.png"),
    OPTIONBAR_ICON("w_book24.png"),
    REFRESH_ICON("w_refresh.png"),
    MULLIGAN_ICON("w_mulligan.png"),
    HAND_ICON("w_hand.png"),
    TILED_ICON("w_tiled.png"),
    SAVE_ICON("w_save.png"),
    LOAD_ICON("w_load.png"),
    SWAP_ICON("w_swap.png"),
    DECK_ICON("w_deck.png"),
    NEXT_ICON("w_next.png"),
    BACK_ICON("w_back.png"),
    LIFE_ICON("w_life.png"),
    TARGET_ICON("w_target.png"),
    CUBE_ICON("w_cube.png"),
    LANDS_ICON("w_lands.png"),
    CREATURES_ICON("w_creatures.png"),
    SPELLS_ICON("w_spells.png"),
    EDIT_ICON("w_edit.png"),
    HELP_ICON("w_help.png"),
    OPEN_ICON("w_open.png"),
    RANDOM_ICON("w_random32.png"),
    CLEAR_ICON("w_clear28.png"),
    FILTER_ICON("w_filter24.png"),
    ARROWDOWN_ICON("w_arrowdown.png"),
    ARROWUP_ICON("w_arrowup.png"),
    PLUS_ICON("w_plus28.png"),
    MINUS_ICON("w_minus28.png"),
    
    MISSING_ICON("missing2.png"),
    ARENA("arena.png"),
    ANY("any.png"),
    FOLDER("folder.png"),
    LOG("log.png"),
    OK("ok.gif"),
    CANCEL("cancel.gif"),
    FORWARD("forward.png"),
    FORWARD2("forward2.png"),
    START("start.png"),
    STOP("stop.png"),
    UNDO("undo.png"),
    BUSY("busy.gif"),
    BUSY16("busy16.gif"),
    ALL("all.gif"),
    LEFT("left.gif"),
    RIGHT("right.gif"),
    CREATURE("creature.png"),
    ARTIFACT("artifact.png"),
    EQUIPMENT("equipment.gif"),
    ENCHANTMENT("enchantment.gif"),
    AURA("aura.png"),
    SPELL("spell.gif"),
    ABILITY("ability.png"),
    TRIGGER("trigger.png"),
    TOKEN("token.png"),
    LAND("land.gif"),
    LAND2("land2.gif"),
    LIFE("life.gif"),
    PREVENT("prevent.gif"),
    PREVENT2("prevent2.gif"),
    POISON("poison.png"),
    HAND("hand.gif"),
    HAND2("hand2.png"),
    HAND_ZONE("b_hand_zone.png"),
    LIBRARY2("library2.gif"),
    LIBRARY_ZONE("b_library_zone.png"),
    GRAVEYARD("graveyard.gif"),
    GRAVEYARD2("graveyard2.gif"),
    GRAVEYARD_ZONE("b_graveyard_zone.png"),
    EXILE("exile.png"),
    EXILE_ZONE("b_exile_zone.png"),
    DIFFICULTY("difficulty.png"),
    CANNOTTAP("cannottap.png"),
    SLEEP("sleep.gif"),
    REGENERATED("regenerated.gif"),
    DAMAGE("damage.gif"),
    COMBAT("combat.gif"),
    ATTACK("attack.gif"),
    BLOCK("block.gif"),
    BLOCKED("blocked.gif"),
    MESSAGE("message.png"),
    PROGRESS("progress.png"),
    TROPHY("trophy.gif"),
    GOLD("gold.png"),
    SILVER("silver.png"),
    BRONZE("bronze.png"),
    CLOVER("clover.gif"),
    LOSE("lose.png"),
    TARGET("target.gif"),
    VALID("valid.gif"),
    STRENGTH("strength.png"),
    DELAY("delay.png"),
    PICTURE("picture.png"),
    
    // ability icons
    FLYING("flying.png"),
    TRAMPLE("trample.png"),
    STRIKE("strike.png"),
    DEATHTOUCH("deathtouch.png"),
    PROTBLACK("protblack.png"),
    PROTBLUE("protblue.png"),
    PROTGREEN("protgreen.png"),
    PROTRED("protred.png"),
    PROTWHITE("protwhite.png"),
    PROTALLCOLORS("protallcolors.png"),
    DEFENDER("defender.png"),
    VIGILANCE("vigilance.png"),
    DOUBLESTRIKE("doublestrike.png"),
    INFECT("infect.png"),
    WITHER("wither.png"),
    LIFELINK("lifelink.png"),
    REACH("reach.png"),
    SHROUD("shroud.png"),
    HEXPROOF("hexproof.png"),
    FEAR("fear.png"),
    INTIMIDATE("intimidate.png"),
    INDESTRUCTIBLE("indestructible.png"),

    // counters
    PLUS("plus.png"),
    MINUS("minus.png"),
    CHARGE("charge.png"),
    FEATHER("feather.gif"),
    GOLDCOUNTER("goldcounter.png"),
    BRIBECOUNTER("bribecounter.png"),
    TIMECOUNTER("time-counter.png"),
    FADECOUNTER("fade-counter.png"),
    QUESTCOUNTER("quest-counter.png"),
    LEVELCOUNTER("level-counter.png"),
    HOOFPRINTCOUNTER("hoofprint-counter.png"),
    AGECOUNTER("age-counter.png"),
    ICECOUNTER("ice-counter.png"),
    SPORECOUNTER("spore-counter.png"),
    ARROWHEADCOUNTER("arrowhead-counter.png"),
    LOYALTYCOUNTER("loyalty-counter.png"),
    KICOUNTER("ki-counter.png"),
    DEPLETIONCOUNTER("depletion-counter.png"),
    MININGCOUNTER("mining-counter.png"),
    MUSTERCOUNTER("muster-counter.png"),
    TREASURECOUNTER("treasure-counter.png"),
    STRIFECOUNTER("strife-counter.png"),
    STUDYCOUNTER("study-counter.png"),
    TRAPCOUNTER("trap-counter.png"),
    SHIELDCOUNTER("shield-counter.png"),
    WISHCOUNTER("wish-counter.png"),
    SHELLCOUNTER("shell-counter.png"),
    BLAZECOUNTER("blaze-counter.png"),
    TIDECOUNTER("tide-counter.png"),
    GEMCOUNTER("gem-counter.png"),
    PRESSURECOUNTER("pressure-counter.png"),
    VERSECOUNTER("verse-counter.png"),
    MUSICCOUNTER("verse-counter.png"),
    RUSTCOUNTER("rust-counter.png"),
    
    MANA_ANY("anymana.gif"),

    // mana icons are stored in a sprite/icon sheet.
    MANA_TAPPED(50),
    MANA_WHITE(24),
    MANA_BLUE(25),
    MANA_BLACK(26),
    MANA_RED(27),
    MANA_GREEN(28),
    MANA_HYBRID_WHITE(40),
    MANA_HYBRID_BLUE(41),
    MANA_HYBRID_BLACK(42),
    MANA_HYBRID_RED(43),
    MANA_HYBRID_GREEN(44),
    MANA_PHYREXIAN_WHITE(45),
    MANA_PHYREXIAN_BLUE(46),
    MANA_PHYREXIAN_BLACK(47),
    MANA_PHYREXIAN_RED(48),
    MANA_PHYREXIAN_GREEN(49),
    MANA_WHITE_BLUE(30),
    MANA_WHITE_BLACK(31),
    MANA_BLUE_BLACK(32),
    MANA_BLUE_RED(33),
    MANA_BLACK_RED(34),
    MANA_BLACK_GREEN(35),
    MANA_RED_WHITE(36),
    MANA_RED_GREEN(37),
    MANA_GREEN_WHITE(38),
    MANA_GREEN_BLUE(39),
    MANA_0(0),
    MANA_1(1),
    MANA_2(2),
    MANA_3(3),
    MANA_4(4),
    MANA_5(5),
    MANA_6(6),
    MANA_7(7),
    MANA_8(8),
    MANA_9(9),
    MANA_10(10),
    MANA_11(11),
    MANA_12(12),
    MANA_13(13),
    MANA_14(14),
    MANA_15(15),
    MANA_16(16),
    MANA_X(21);

    public static final String MANA_ICON_SHEET = "Mana.png";

    private final String iconFilename;
    private final int iconIndex;

    private MagicIcon(final String iconFilename) {
        this.iconFilename = iconFilename;
        this.iconIndex = -1;
    }

    private MagicIcon(final int iconIndex) {
        this.iconFilename = null;
        this.iconIndex = iconIndex;
    }

    public String getFilename() {
        if (iconFilename != null) {
            return iconFilename;
        } else {
            throw new UnsupportedOperationException("Mana icons do not have an associated filename.");
        }
    }

    public int getIconIndex() {
        if (iconIndex >= 0) {
            return iconIndex;
        } else {
            throw new UnsupportedOperationException("Only mana icons have a (icon sheet) index.");
        }
    }

    public boolean isManaIcon() {
        return iconFilename == null && iconIndex >= 0;
    }

}
