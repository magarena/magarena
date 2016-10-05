package magic.ui.screen.images.download;

interface IDownloadListener {
    void setMessage(final String message);
    void setButtonState(final boolean isDownloading);
    void doCustomActionAfterDownload();
    void buildDownloadImagesList();
    void showProgress(int count, int total);
    void resetProgress();
}
