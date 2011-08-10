package magic.ui.resolution;

import java.awt.*;

public class ResolutionProfiles {

	public static final ResolutionProfile DEFAULT_PROFILE=new DefaultResolutionProfile();
	
	public static ResolutionProfileResult calculate(final Dimension size) {
		return DEFAULT_PROFILE.calculate(size);
	}
}
