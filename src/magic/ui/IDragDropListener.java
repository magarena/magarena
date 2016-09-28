package magic.ui;

import java.io.File;

public interface IDragDropListener {

    public void onImageFileDropped(File imageFile);

    public void onZipFileDropped(File zipFile);

}
