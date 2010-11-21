package magic.model;

import java.util.Random;

public class MagicRandom {

	private static final Random RANDOM=new Random();

	private MagicRandom() {
		
	}
	
	public static int nextInt(final int n) {
		
		return RANDOM.nextInt(n);
	}	
}