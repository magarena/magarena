package magic.ui.avatar;

import magic.ui.IconImages;
import magic.ui.theme.PlayerAvatar;

import javax.swing.ImageIcon;

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
import magic.data.MagicIcon;
import magic.ui.ImageFileIO;

public class AvatarImageSet {

    private final static String IMAGES_FILTER = "*.{png,jpg}";

    private final Path path;
    private ImageIcon sampleImage = IconImages.getIcon(MagicIcon.MISSING_ICON);

    public AvatarImageSet(final Path path) {
        this.path = path;
        loadSampleImage();
    }

    public String getName() {
        return path.getFileName().toString();
    }

    public ImageIcon getSampleImage() {
        return sampleImage;
    }

    private void loadSampleImage() {
        // find first image file in directory using a try-with-resource block for safety.
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(this.path, IMAGES_FILTER)) {
            final Iterator<Path> itr = ds.iterator();
            if (itr.hasNext()) {
                final String filePath = itr.next().toAbsolutePath().toString();
                final InputStream ins = new FileInputStream(new File(filePath));
                final BufferedImage image = ImageFileIO.toImg(ins, IconImages.MISSING);
                this.sampleImage = new ImageIcon(magic.ui.utility.GraphicsUtils.scale(image, PlayerAvatar.MEDIUM_SIZE, PlayerAvatar.MEDIUM_SIZE));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Path> getImagePaths() {
        final List<Path> paths = new ArrayList<Path>();
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(this.path, IMAGES_FILTER)) {
            for (Path path : ds) {
                paths.add(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paths;
    }

}
