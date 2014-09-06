package magic.ui.duel.resolution;

import java.awt.Dimension;

public class ResolutionProfiles {

    private static final ResolutionProfile DEFAULT_PROFILE=new DefaultResolutionProfile();

    public static ResolutionProfileResult calculate(final Dimension size) {
        return DEFAULT_PROFILE.calculate(size);
    }
}
