package magic.model;

import java.util.Random;

public class MagicRandom {

	private static final Random RNG;
	
    static {
        String seedStr = System.getProperty("rndSeed");
        if (seedStr != null) {
            RNG = new Random(Long.parseLong(seedStr));
            System.err.println("Using random seed " + seedStr);
        } else {
            RNG = new Random();
        }
    }

	private MagicRandom() {
		
	}
	
	public static int nextInt(final int n) {
		
		return RNG.nextInt(n);
	}	
}
