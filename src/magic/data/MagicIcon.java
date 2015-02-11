package magic.data;

public enum MagicIcon {

    HEADER_ICON("headerIcon.png");

    private final String iconFilename;

    private MagicIcon(final String iconFilename) {
        this.iconFilename = iconFilename;
    }

    public String getFilename() {
        return iconFilename;
    }

}
