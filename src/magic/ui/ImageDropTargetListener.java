package magic.ui;

import magic.ui.utility.GraphicsUtils;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.activation.MimetypesFileTypeMap;

/**
 * Sets the background to an image that has been dragged into the Magarena frame.
 * <p>
 * This might be an image file or an image dragged directly from the internet browser.
 */
public class ImageDropTargetListener implements DropTargetListener {

    private final IImageDragDropListener listener;

    public ImageDropTargetListener(final IImageDragDropListener listener) {
        this.listener = listener;
    }

    @Override
    public void drop(DropTargetDropEvent event) {
        processDroppedImageFile(event);
    }

    @Override
    public void dragEnter(DropTargetDragEvent event) {
    }

    @Override
    public void dragExit(DropTargetEvent event) {
    }

    @Override
    public void dragOver(DropTargetDragEvent event) {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent event) {
    }

    @SuppressWarnings("unchecked")
    private void processDroppedImageFile(final DropTargetDropEvent event) {

        event.acceptDrop(DnDConstants.ACTION_COPY);

        // Get the transfer which can provide the dropped item data
        Transferable transferable = event.getTransferable();

        // Get the data formats of the dropped item
        DataFlavor[] flavors = transferable.getTransferDataFlavors();

        for (DataFlavor flavor : flavors) {
            try {
                // If the drop items are files then process the first one only.
                if (flavor.isFlavorJavaFileListType()) {

                    final List<File> files = (List<File>) transferable.getTransferData(flavor);

                    // linux workaround - no need to crash out.
                    if (files == null || files.isEmpty()) {
                        ScreenController.showWarningMessage(
                                "Sorry, this did not work.\n"
                                + "Try downloading the image first and then dragging the file into Magarena.");
                        break;
                    }

                    final File imageFile = new File(files.get(0).getPath());

                    if (isValidImageFile(imageFile)) {
                        listener.setDroppedImageFile(imageFile);
                    } else {
                        ScreenController.showWarningMessage("Invalid image!");
                    }
                    break;
                }

            } catch (UnsupportedFlavorException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Inform that the drop is complete
        event.dropComplete(true);

    }

    private boolean isValidImageFile(final File imageFile) {
        return isValidMimeType(imageFile, "image")
                && GraphicsUtils.isValidImageFile(imageFile.toPath());
    }

    private boolean isValidMimeType(final File file, final String mimeType) {
        final MimetypesFileTypeMap mtftp = new MimetypesFileTypeMap();
        mtftp.addMimeTypes("image png tif jpg jpeg bmp");
        final String mimetype = mtftp.getContentType(file);
        final String fileType = mimetype.split("/")[0];
        return fileType.equalsIgnoreCase(mimeType);
    }

}

