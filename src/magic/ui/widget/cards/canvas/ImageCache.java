package magic.ui.widget.cards.canvas;

import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

@SuppressWarnings("serial")
public class ImageCache extends LinkedHashMap<String, BufferedImage> {

    private final int capacity;
    private long accessCount;
    private long hitCount;

    public ImageCache(final int capacity) {
        super(capacity+1, 1.1f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<String, BufferedImage> eldest) {
        return size() > capacity;
    }

    @Override
    public BufferedImage get(Object key) {
        accessCount++;
        if (containsKey(key)) {
            hitCount++;
        }
        return super.get(key);
    }

    public long getAccessCount() {
        return accessCount;
    }

    public long getHitCount() {
        return hitCount;
    }

    /**
     * Measures how well a cache is performing.
     * <p>
     * Tells us how many cache accesses are "hits" - that is, how many times the required data
     * was found in the cache for a given number of accesses. Hit rate is usually expressed as
     * a percentage - a hit rate above 80% is usually pretty good.
     */
    public int getHitRatePercent() {
        final double ratio = (double)getHitCount() / getAccessCount();
        return (int)Math.round(ratio * 100);
    }

    @Override
    public BufferedImage put(String key, BufferedImage value) {
        return super.put(key, value);
    }

}
