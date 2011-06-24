package magic.model;

import magic.*;

public class MagicRandom {

	private static final MersenneTwisterFast RNG;
	
    static {
        String seedStr = System.getProperty("rndSeed");
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
