package magic.ui.duel.resolution;

import java.awt.Dimension;

public interface ResolutionProfile {

    ResolutionProfileResult calculate(final Dimension size);
}
