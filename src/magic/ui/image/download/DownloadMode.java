package magic.ui.image.download;

enum DownloadMode {

    MISSING("Missing images"),
    ALL("All images");

    private final String desc;

    private DownloadMode(String aDesc) {
        this.desc = aDesc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
