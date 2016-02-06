package magic.ui.image.download;

enum DownloadMode {

    CARDS("Card images"),
    CROPS("Cropped images");

    private final String desc;

    private DownloadMode(String aDesc) {
        this.desc = aDesc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
