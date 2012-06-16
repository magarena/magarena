package magic.model;

import magic.MersenneTwisterFast;

public class MagicRandom {

    private static final MersenneTwisterFast RNG;
    
    static {
        final String seedStr = System.getProperty("rndSeed");
        if (seedStr != null) {
            RNG = new MersenneTwisterFast(Long.parseLong(seedStr));
            System.err.println("Using random seed " + seedStr);
        } else {
            RNG = new MersenneTwisterFast();
        }
    }

    private MagicRandom() {}
    
    public static int nextInt(final int n) {
        return RNG.nextInt(n);
    }    
}
