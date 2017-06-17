package magic.model;

import java.util.Random;

@SuppressWarnings("serial")
public class MagicRandom extends Random {

    private long state;
    private static MagicRandom RNG = new MagicRandom();

    static {
        final String seedStr = System.getProperty("rndSeed");
        if (seedStr != null) {
            RNG.setState(Long.parseLong(seedStr));
        } else {
            RNG.setState(System.nanoTime());
        }
        System.err.println("Using random seed " + RNG.getState());
    }

    private MagicRandom() {
        this(System.nanoTime());
    }

    public MagicRandom(final long aState) {
        setState(aState);
    }

    public void setState(final long aState) {
        state = aState == 0 ? -1 : aState;
    }

    // from http://demesos.blogspot.com/2011/09/replacing-java-random-generator.html
    // parameters are from Numerical Recipes 3E, pg 347
    @Override
    protected int next(int nbits) {
        long x = state;
        x ^= (x << 21);
        x ^= (x >>> 35);
        x ^= (x << 4);
        state = x;
        x &= ((1L << nbits) - 1);
        return (int) x;
    }

    public long getState() {
        return state;
    }

    public static void setRNGState(final long state) {
        RNG.setState(state);
    }

    public static int nextRNGInt(final int n) {
        return RNG.nextInt(n);
    }

    public static int nextRNGInt() {
        return RNG.nextInt(Integer.MAX_VALUE);
    }
}
