package magic.cardBuilder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;

import magic.ui.helpers.ImageHelper;
import magic.data.LRUCache;

public class ResourceManager {

    private static final int MAX_IMAGES = 100;
    private static final Map<String, BufferedImage> cache = new LRUCache<>(MAX_IMAGES);

    // Used as reference class for accessing JAR resources.
    private static final ResourceManager instance = new ResourceManager();

    static final String FRAMES_FOLDER = "/cardbuilder/frames/";
    static final String IMAGES_FOLDER = "/cardbuilder/images/";

    private static InputStream getJarResourceStream(String filename) {
        return instance.getClass().getResourceAsStream(filename);
    }

    private static BufferedImage getComponent(String imageName) {
        String fName = FRAMES_FOLDER + imageName;
        try (final InputStream is = getJarResourceStream(fName)) {
            return ImageHelper.getOptimizedImage(ImageIO.read(is));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static BufferedImage getImage(String name) {
        String fName = "/cardbuilder/images/" + name;
        try (final InputStream is = getJarResourceStream(fName)) {
            return ImageHelper.getOptimizedImage(ImageIO.read(is));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static BufferedImage getImage(final CardResource cr) {
        String fName = cr.path;
        if (cache.containsKey(fName)) {
            return cache.get(fName);
        }
        try (final InputStream is = getJarResourceStream(fName)) {
            final BufferedImage bi = ImageHelper.getOptimizedImage(ImageIO.read(is));
            cache.put(fName, bi);
            return bi;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static BufferedImage newFrame(BufferedImage bi) {
        return ImageHelper.getOptimizedImage(bi);
    }

    public static BufferedImage newFrame(final CardResource cr) {
        return ImageHelper.getOptimizedImage(getImage(cr));
    }

}
