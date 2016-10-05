package magic.ui.screen.images.download;

import java.util.stream.Stream;
import magic.data.ImagesDownloadList;
import magic.model.MagicCardDefinition;

interface IScanListener {
    Stream<MagicCardDefinition> getCards(final DownloadMode aType);
    void doScannerFinished(final ImagesDownloadList aList);
    void notifyStatusChanged(final DownloadState newState);
}
