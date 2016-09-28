package magic.ui;

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
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.activation.MimetypesFileTypeMap;

public class FileDropTargetListener implements DropTargetListener {

    private static final String IMAGE_FORMATS = "image bmp gif jpeg jpg png wbmp";

    private final IDragDropListener listener;

    public FileDropTargetListener(final IDragDropListener listener) {
        this.listener = listener;
    }


    private String getFileType(File aFile) {
        try {
            return Files.probeContentType(aFile.toPath());
        } catch (IOException ex) {
            return MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(aFile);
        }
    }

    private void doFileAction(File aFile) {

        final String fileType = getFileType(aFile);
        System.out.println(fileType);

        if (fileType != null) {

            // zip file
            if ("application/zip".equals(fileType)) {
                listener.onZipFileDropped(aFile);
                return;
            }

            // image file
            if ("image".equals(fileType.split("/")[0])) {
                final String format = fileType.split("/")[1].toLowerCase();
                if (IMAGE_FORMATS.contains(format)) {
                    listener.onImageFileDropped(aFile);
                    return;
                }
            }
        }

        // unsupported
        MagicSound.ALERT.play();
    }

    private Optional<File> getDroppedFile(Transferable trf) {

        // Get the data format of the first dropped item.
        final Optional<DataFlavor> fileFlavor = Stream.of(trf.getTransferDataFlavors())
                .filter(f -> f.isFlavorJavaFileListType())
                .findFirst();

        if (fileFlavor.isPresent()) {
            try {
                final Object data = trf.getTransferData(fileFlavor.get());
                return ((List<File>)data).stream().findFirst();
            } catch (UnsupportedFlavorException | IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        return Optional.empty();
    }

    private void doDropAction(final DropTargetDropEvent event) {

        event.acceptDrop(DnDConstants.ACTION_COPY);

        final Optional<File> file = getDroppedFile(event.getTransferable());
        if (file.isPresent()) {
            doFileAction(file.get());
        } else {
            MagicSound.ALERT.play();
        }

        // Inform that the drop is complete
        event.dropComplete(true);
    }

    @Override
    public void drop(DropTargetDropEvent event) {
        doDropAction(event);
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

}

