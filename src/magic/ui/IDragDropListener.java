package magic.ui;

import java.io.File;

public interface IDragDropListener {
    void onImageFileDropped(File imageFile);
    void onZipFileDropped(File zipFile);
    void onGameSnapshotDropped(File aFile);
}
