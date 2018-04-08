package magic.ui.screen.images.download;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import magic.data.ImagesDownloadList;

class ScanWorker extends SwingWorker<ImagesDownloadList, Void> {

    private final IScanListener listener;
    private final CardImageDisplayMode displayMode;
    private ImagesDownloadList downloadList;

    ScanWorker(final IScanListener aListener, final CardImageDisplayMode mode) {
        this.listener = aListener;
        this.displayMode = mode;
    }

    static ImagesDownloadList getImagesDownloadList(IScanListener aListener, CardImageDisplayMode aMode) {
        // synchronized to speed up scanning by preventing
        // multiple scanners accessing filesystem at the same time.
        synchronized (ScanWorker.class) {
            return new ImagesDownloadList(aListener.getCards(aMode));
        }
    }

    @Override
    protected ImagesDownloadList doInBackground() {
        return getImagesDownloadList(listener, displayMode);
    }

    @Override
    protected void done() {
        try {
            downloadList = get();
        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException(ex);
        } catch (CancellationException ex) {
            System.out.println("ImagesScanner cancelled by user!");
        }
        if (!isCancelled()) {
            listener.doScannerFinished(downloadList);
        }
        listener.notifyStatusChanged(DownloadState.STOPPED);
    }
}
