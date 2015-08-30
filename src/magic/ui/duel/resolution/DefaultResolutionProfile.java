package magic.ui.duel.resolution;

import magic.ui.theme.Theme;

import java.awt.Dimension;
import java.awt.Rectangle;
import magic.ui.utility.MagicStyle;

public class DefaultResolutionProfile implements ResolutionProfile {

    private static final int PLAYER_VIEWER_WIDTH=300;
    public static final int PLAYER_VIEWER_HEIGHT_SMALL=80;
    public static final int GAME_VIEWER_HEIGHT=187;
    private static final int MIN_HAND_VIEWER_WIDTH=250;
    private static final int IMAGE_HAND_VIEWER_HEIGHT=145;
    private static final int MAX_LOGBOOK_VIEWER_WIDTH=1000;
    private static final int BUTTON_SIZE=30;
    private static final int BUTTON_Y_SPACING=10;

    private static Dimension containerSize;

    private static final int LHS_CONTENT_WIDTH = 300;
    private static final int OUTER_MARGIN = MagicStyle.getTheme().getValue(Theme.VALUE_SPACING);

    @Override
    public ResolutionProfileResult calculate(final Dimension size) {

        containerSize = size;

        final ResolutionProfileResult result=new ResolutionProfileResult();
        int spacing=MagicStyle.getTheme().getValue(Theme.VALUE_SPACING);

        if (spacing<=0) {
            if (size.width>1250) {
                spacing=20;
            } else {
                spacing=10;
            }
        }

        //
        // GamePanel Layout
        //
        int x = spacing;
        int y = spacing;
        int playerHeight = PLAYER_VIEWER_HEIGHT_SMALL;
        int cardHeight = size.height - GAME_VIEWER_HEIGHT - (spacing * 4) - BUTTON_Y_SPACING;
        final boolean small = true;
        cardHeight -= playerHeight * 2;

        result.setFlag(ResolutionProfileType.GamePlayerViewerSmall, small);

        //
        // LHS - Uses MigLayout so do not need to track position.
        //
        setLhsBounds(result);

        result.setBoundary(
                ResolutionProfileType.GameOpponentViewer,
                new Rectangle(-1, -1, PLAYER_VIEWER_WIDTH, playerHeight));

        result.setBoundary(
                ResolutionProfileType.GameLogBookButton,
                new Rectangle(-1, -1, BUTTON_SIZE, BUTTON_SIZE));
        result.setBoundary(
                ResolutionProfileType.TextViewButton,
                new Rectangle(-1, -1, BUTTON_SIZE, BUTTON_SIZE));

        final int logWidth = Math.min(MAX_LOGBOOK_VIEWER_WIDTH, size.width - (spacing * 3) - BUTTON_SIZE);
        result.setBoundary(
                ResolutionProfileType.GameLogBookViewer,
                new Rectangle(x + BUTTON_SIZE + 6, spacing, logWidth, size.height - spacing * 2));

        result.setBoundary(
                ResolutionProfileType.GameImageStackViewer,
                new Rectangle(-1, -1, PLAYER_VIEWER_WIDTH, cardHeight - BUTTON_Y_SPACING - BUTTON_SIZE));

        result.setBoundary(
                ResolutionProfileType.GameStatusPanel,
                new Rectangle(-1, -1, PLAYER_VIEWER_WIDTH, GAME_VIEWER_HEIGHT));

        result.setBoundary(
                ResolutionProfileType.GamePlayerViewer,
                new Rectangle(-1 ,-1, PLAYER_VIEWER_WIDTH, playerHeight));


        //
        // RHS - this still uses absolute positioning (null layout).
        //

        // this is the gap between the edge of the RHS panel and its content.
        final int offset = MagicStyle.getTheme().getValue(Theme.VALUE_GAME_OFFSET);
        x = offset;

        int width2=(size.width-PLAYER_VIEWER_WIDTH-spacing*5-offset)/3;
        if (width2<MIN_HAND_VIEWER_WIDTH) {
            width2=(size.width-PLAYER_VIEWER_WIDTH-spacing*4-offset)/2;
            final int height2=(size.height-spacing*3)/2;
            final int x2=x+width2+spacing;
            y=spacing;
            result.setBoundary(
                    ResolutionProfileType.GameOpponentPermanentViewer,
                    new Rectangle(x2, y, width2, height2));
            y+=height2+spacing;
            result.setBoundary(
                    ResolutionProfileType.GamePlayerPermanentViewer,
                    new Rectangle(x2, y, width2, height2));

        } else {
            final int height3=size.height-spacing*2;
            int x2=x+width2+spacing;
            y = spacing;
            result.setBoundary(
                    ResolutionProfileType.GamePlayerPermanentViewer,
                    new Rectangle(x2,y,width2,height3));
            x2 += width2 + spacing;
            result.setBoundary(
                    ResolutionProfileType.GameOpponentPermanentViewer,
                    new Rectangle(x2,y,width2,height3));
        }

        final int height2=(size.height-spacing*3)/3;
        y=spacing;
        result.setBoundary(
                ResolutionProfileType.GameStackCombatViewer,
                new Rectangle(x, y, width2, 2*height2));

        y+=2*height2+spacing;
        result.setBoundary(
                ResolutionProfileType.GameHandGraveyardViewer,
                new Rectangle(x, y, width2, height2));

        y = size.height - spacing - IMAGE_HAND_VIEWER_HEIGHT;
        result.setBoundary(
                ResolutionProfileType.GameZones,
                new Rectangle(-1, -1, getPanelWidthLHS(), y - offset));

        final int width3=size.width-PLAYER_VIEWER_WIDTH-spacing*3-offset;

        result.setBoundary(
                ResolutionProfileType.GameImageHandGraveyardViewer,
                new Rectangle(x, y, width3, IMAGE_HAND_VIEWER_HEIGHT));


        final int height3=size.height-spacing*5-IMAGE_HAND_VIEWER_HEIGHT-offset;
        final int height4=(height3*3)/8;
        final int height5=height3-height4*2;

        y=spacing;
        result.setBoundary(
                ResolutionProfileType.GameImageOpponentPermanentViewer,
                new Rectangle(x, y, width3, height4));

        y+=height4+spacing;
        result.setBoundary(
                ResolutionProfileType.GameImageCombatViewer,
                new Rectangle(x, y, width3, height5));

        y+=height5+spacing;
        result.setBoundary(
                ResolutionProfileType.GameImagePlayerPermanentViewer,
                new Rectangle(x, y, width3, height4));

        return result;
    }

    private void setLhsBounds(final ResolutionProfileResult result) {
        Rectangle r = new Rectangle(
                0, 0,
                getPanelWidthLHS(),
                containerSize.height);
        result.setBoundary(ResolutionProfileType.GameLHS, r);
    }

    public static int getPanelWidthLHS() {
        return LHS_CONTENT_WIDTH + (OUTER_MARGIN * 2);
    }
}
