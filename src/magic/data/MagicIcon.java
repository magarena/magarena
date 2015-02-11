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
    MINUS_ICON("w_minus28.png")
    ;

    private final String iconFilename;

    private MagicIcon(final String iconFilename) {
        this.iconFilename = iconFilename;
    }

    public String getFilename() {
        return iconFilename;
    }

}
