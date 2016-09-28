package magic.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.activation.MimetypesFileTypeMap;

public class FileDropTargetListener implements DropTargetListener {

    private static final String IMAGE_FORMATS = "bmp gif jpeg jpg png wbmp";

    private final IDragDropListener listener;

    public FileDropTargetListener(final IDragDropListener listener) {
        this.listener = listener;
    }

    /**
     * This method seems to work well on linux, not so Windows
     * where it always returns null.
     */
    private String tryProbeContentType(File aFile) {
        try {
            return Files.probeContentType(aFile.toPath());
        } catch (IOException ex) {
            System.err.println(ex);
            return null;
        }
    }

    private String tryGuessContentTypeFromStream(File aFile) {
        try (final InputStream is = new BufferedInputStream(new FileInputStream(aFile))) {
            return URLConnection.guessContentTypeFromStream(is);
        } catch (IOException ex) {
            System.err.println(ex);
            return null;
        }
    }

    private String tryMimetypesFileTypeMap(File aFile) {
        final MimetypesFileTypeMap mtftp = new MimetypesFileTypeMap();
        mtftp.addMimeTypes("image " + IMAGE_FORMATS);
        mtftp.addMimeTypes("application/zip zip");
        return mtftp.getContentType(aFile);
    }

    private String getMimeType(File aFile) {
        String mimeType = tryProbeContentType(aFile);
        if (mimeType == null) {
            mimeType = tryGuessContentTypeFromStream(aFile);
        }
        if (mimeType == null) {
            mimeType = tryMimetypesFileTypeMap(aFile);
        }
        return mimeType;
    }

    private void doFileAction(File aFile) {

        final String mimeType = getMimeType(aFile);
        System.out.println(mimeType);

        if (mimeType != null) {

            // zip file
            if ("application/zip".equals(mimeType)) {
                listener.onZipFileDropped(aFile);
                return;
            }

            // image file
            // on Windows, dragging an image from the browser directly
            // returns a mime type of "image" thus the need to check parts.
            final String[] parts = mimeType.split("/");
            if ("image".equals(parts[0].toLowerCase())) {
                if (parts.length == 1 || IMAGE_FORMATS.contains(parts[1].toLowerCase())) {
                    listener.onImageFileDropped(aFile);
                    return;
                }
            }
        }

        // unsupported
        MagicSound.ALERT.play();
    }

    @SuppressWarnings("unchecked")
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

