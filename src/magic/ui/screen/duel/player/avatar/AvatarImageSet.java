package magic.ui.screen.duel.player.avatar;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

import magic.data.MagicIcon;
import magic.ui.ImageFileIO;
import magic.ui.MagicImages;
import magic.ui.helpers.ImageHelper;
import magic.ui.theme.PlayerAvatar;

class AvatarImageSet {

    private final static String IMAGES_FILTER = "*.{png,jpg}";

    private final Path path;
    private ImageIcon sampleImage = MagicImages.getIcon(MagicIcon.MISSING);

    AvatarImageSet(final Path path) {
        this.path = path;
        loadSampleImage();
    }

    String getName() {
        return path.getFileName().toString();
    }

    ImageIcon getSampleImage() {
        return sampleImage;
    }

    private void loadSampleImage() {
        // find first image file in directory using a try-with-resource block for safety.
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(this.path, IMAGES_FILTER)) {
            final Iterator<Path> itr = ds.iterator();
            if (itr.hasNext()) {
                final String filePath = itr.next().toAbsolutePath().toString();
                final InputStream ins = new FileInputStream(new File(filePath));
                final BufferedImage image = ImageFileIO.toImg(ins, MagicImages.MISSING_BIG);
                this.sampleImage = new ImageIcon(ImageHelper.scale(image, 
                        PlayerAvatar.MEDIUM_SIZE, PlayerAvatar.MEDIUM_SIZE)
                );
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    List<Path> getImagePaths() {
        final List<Path> paths = new ArrayList<>();
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(this.path, IMAGES_FILTER)) {
            for (Path p : ds) {
                paths.add(p);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return paths;
    }

}
