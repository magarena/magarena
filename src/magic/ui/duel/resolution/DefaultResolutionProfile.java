package magic.ui.duel.resolution;

import java.awt.Dimension;
import java.awt.Rectangle;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;

public class DefaultResolutionProfile implements ResolutionProfile {

    public static final int PLAYER_ZONE_VIEWER_HEIGHT = 145;
    private static final int SIDEBAR_WIDTH = 300;

    // this is the gap between the edge of the RHS panel and its content.
    public static final int BATTLEFIELD_INSET =
        MagicStyle.getTheme().getValue(Theme.VALUE_GAME_OFFSET);

    public static int SPACING = MagicStyle.getTheme().getValue(Theme.VALUE_SPACING);

    @Override
    public ResolutionProfileResult calculate(final Dimension size) {

        ResolutionProfileResult result = new ResolutionProfileResult();

        if (SPACING <= 0) {
            if (size.width > 1250) {
                SPACING = 20;
            } else {
                SPACING = 10;
            }
        }

        int x = BATTLEFIELD_INSET;
        int y = size.height - SPACING - PLAYER_ZONE_VIEWER_HEIGHT;
        result.setBoundary(ResolutionProfileType.GameZones,
            new Rectangle(-1, -1, getPanelWidthLHS(), y - BATTLEFIELD_INSET)
        );

        final int width3 = size.width - SIDEBAR_WIDTH - SPACING * 3 - BATTLEFIELD_INSET;

        // size and position of {@code PlayerZoneViewer}.
        result.setBoundary(ResolutionProfileType.GameImageHandGraveyardViewer,
            new Rectangle(x, y, width3, PLAYER_ZONE_VIEWER_HEIGHT));

        final int height3 = size.height - SPACING * 5 - PLAYER_ZONE_VIEWER_HEIGHT - BATTLEFIELD_INSET;
        final int height4 = (height3 * 3) / 8;
        final int height5 = height3 - height4 * 2;

        y = SPACING;
        result.setBoundary(
            ResolutionProfileType.GameImageOpponentPermanentViewer,
            new Rectangle(x, y, width3, height4));

        y += height4 + SPACING;
        result.setBoundary(
            ResolutionProfileType.GameImageCombatViewer,
            new Rectangle(x, y, width3, height5));

        y += height5 + SPACING;
        result.setBoundary(
            ResolutionProfileType.GameImagePlayerPermanentViewer,
            new Rectangle(x, y, width3, height4));

        return result;
    }

    public static int getPanelWidthLHS() {
        return SIDEBAR_WIDTH + (SPACING * 2);
    }
}
