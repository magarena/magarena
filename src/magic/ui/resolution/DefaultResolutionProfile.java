package magic.ui.resolution;

import magic.data.CardImagesProvider;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;

import java.awt.Dimension;
import java.awt.Rectangle;

public class DefaultResolutionProfile implements ResolutionProfile {

	private static final int PLAYERS_VIEWER_WIDTH=134;
	private static final int DECK_VIEWER_WIDTH=350;
	private static final int DUEL_VIEWER_WIDTH=270;
	private static final int DUEL_VIEWER_HEIGHT=130;
	private static final int DECK_STATISTICS_VIEWER_HEIGHT=200;
	private static final int DECK_STRENGTH_VIEWER_HEIGHT=160;
	private static final int PLAY_BUTTON_HEIGHT=50;
	public static final int CARD_VIEWER_WIDTH=CardImagesProvider.CARD_WIDTH;
	public static final int CARD_VIEWER_HEIGHT=CardImagesProvider.CARD_HEIGHT+20;
	private static final int PLAYER_VIEWER_WIDTH=300;
	private static final int PLAYER_VIEWER_HEIGHT=154;
	private static final int PLAYER_VIEWER_HEIGHT_SMALL=80;
	private static final int GAME_VIEWER_HEIGHT=125;
	private static final int MIN_HAND_VIEWER_WIDTH=250;
	private static final int IMAGE_HAND_VIEWER_HEIGHT=145;
	private static final int MAX_LOGBOOK_VIEWER_WIDTH=1000;
	private static final int IMAGE_VIEWER_WIDTH=PLAYER_VIEWER_WIDTH;
	private static final int IMAGE_VIEWER_HEIGHT=PLAYER_VIEWER_WIDTH;
	private static final int BUTTON_SIZE=30;
	private static final int BUTTON_Y_SPACING=10;
	
	@Override
	public ResolutionProfileResult calculate(final Dimension size) {

		final Theme theme=ThemeFactory.getInstance().getCurrentTheme();
		final int offset=theme.getValue(Theme.VALUE_GAME_OFFSET);
		final ResolutionProfileResult result=new ResolutionProfileResult();
		int spacing=theme.getValue(Theme.VALUE_SPACING);

		if (spacing<=0) {
			if (size.width>1250) {
				spacing=20;
			} else {
				spacing=10;
			}
		}
		
		// Duel
		final int maxHeight=size.height-spacing*2;
		int x=spacing;
		result.setBoundary(ResolutionProfileType.DuelPlayersViewer,
                new Rectangle(x,spacing,PLAYERS_VIEWER_WIDTH,maxHeight - PLAY_BUTTON_HEIGHT - spacing));
		int y = size.height;
        y -= spacing + PLAY_BUTTON_HEIGHT;
        result.setBoundary(ResolutionProfileType.DuelNewButton,
                new Rectangle(x,y,PLAYERS_VIEWER_WIDTH,PLAY_BUTTON_HEIGHT));

        x+=PLAYERS_VIEWER_WIDTH+spacing;
		final int deckWidth=Math.min(
                DECK_VIEWER_WIDTH,
                size.width-spacing*5-PLAYERS_VIEWER_WIDTH-CARD_VIEWER_WIDTH-DUEL_VIEWER_WIDTH);
		result.setBoundary(ResolutionProfileType.DuelDeckViewers,
                new Rectangle(x,spacing,deckWidth,maxHeight - PLAY_BUTTON_HEIGHT - spacing));
		y = size.height;
        y -= spacing + PLAY_BUTTON_HEIGHT;
		result.setBoundary(ResolutionProfileType.DuelPlayButton,
                new Rectangle(x,y,deckWidth,PLAY_BUTTON_HEIGHT));

		x+=deckWidth+spacing;
		result.setBoundary(ResolutionProfileType.DuelCardViewer,
                new Rectangle(x,spacing,CARD_VIEWER_WIDTH,CARD_VIEWER_HEIGHT));

		x=size.width-spacing-DUEL_VIEWER_WIDTH;
		y=spacing;
		result.setBoundary(ResolutionProfileType.DuelDeckStatisticsViewer,
                new Rectangle(x,y,DUEL_VIEWER_WIDTH,DECK_STATISTICS_VIEWER_HEIGHT));
		y+=DECK_STATISTICS_VIEWER_HEIGHT+spacing;
		result.setBoundary(ResolutionProfileType.DuelDeckStrengthViewer,
                new Rectangle(x,y,DUEL_VIEWER_WIDTH,DECK_STRENGTH_VIEWER_HEIGHT));
		y+=DECK_STRENGTH_VIEWER_HEIGHT+spacing;
		result.setBoundary(ResolutionProfileType.DuelDifficultyViewer,
                new Rectangle(x,y,DUEL_VIEWER_WIDTH,DUEL_VIEWER_HEIGHT));
					
		// Game
		x=spacing;
		y=spacing;
		int playerHeight=PLAYER_VIEWER_HEIGHT;
		int cardHeight=size.height-GAME_VIEWER_HEIGHT-spacing*4-BUTTON_Y_SPACING;
		final boolean small=cardHeight-playerHeight*2<CARD_VIEWER_HEIGHT;
		if (small) {
			playerHeight=PLAYER_VIEWER_HEIGHT_SMALL;
		}
		cardHeight-=playerHeight*2;
       
		result.setFlag(ResolutionProfileType.GamePlayerViewerSmall,small);
		
        //opponent viewer
        result.setBoundary(ResolutionProfileType.GameOpponentViewer,
                new Rectangle(x,y,PLAYER_VIEWER_WIDTH,playerHeight));
		y+=playerHeight+spacing;
        
        //log book, text view button and log book viewer
		//y-=BUTTON_SIZE+BUTTON_Y_SPACING;
		result.setBoundary(ResolutionProfileType.GameLogBookButton,
                new Rectangle(x,y,BUTTON_SIZE,BUTTON_SIZE));
		result.setBoundary(ResolutionProfileType.TextViewButton,
                new Rectangle(x+PLAYER_VIEWER_WIDTH-BUTTON_SIZE,y,BUTTON_SIZE,BUTTON_SIZE));
        final int logWidth=Math.min(MAX_LOGBOOK_VIEWER_WIDTH,size.width-spacing*3-BUTTON_SIZE);
		result.setBoundary(ResolutionProfileType.GameLogBookViewer,
                new Rectangle(x+BUTTON_SIZE+6,spacing,logWidth,size.height-spacing*2));
        y += BUTTON_SIZE+BUTTON_Y_SPACING;
		
        //image viewer, image stack viewer
        result.setBoundary(ResolutionProfileType.GameImageViewer,
                new Rectangle(x,y,IMAGE_VIEWER_WIDTH,IMAGE_VIEWER_HEIGHT));
		result.setBoundary(ResolutionProfileType.GameImageStackViewer,
                new Rectangle(x,y,PLAYER_VIEWER_WIDTH,cardHeight-BUTTON_Y_SPACING-BUTTON_SIZE));
		//y+=(cardHeight-CARD_VIEWER_HEIGHT)/2;

        //card viewer
		result.setBoundary(ResolutionProfileType.GameCardViewer,
				new Rectangle(x+(PLAYER_VIEWER_WIDTH-CARD_VIEWER_WIDTH)/2,y,CARD_VIEWER_WIDTH,CARD_VIEWER_HEIGHT));
		y+=CARD_VIEWER_HEIGHT+spacing;

        //game player viewer
		y=size.height-spacing-playerHeight;
		result.setBoundary(ResolutionProfileType.GamePlayerViewer,
                new Rectangle(x,y,PLAYER_VIEWER_WIDTH,playerHeight));

        // duel viewer
        y-=GAME_VIEWER_HEIGHT+spacing;
		result.setBoundary(ResolutionProfileType.GameDuelViewer,
                new Rectangle(x,y,PLAYER_VIEWER_WIDTH,GAME_VIEWER_HEIGHT));

		x+=PLAYER_VIEWER_WIDTH+spacing+offset;
		
		int width2=(size.width-PLAYER_VIEWER_WIDTH-spacing*5-offset)/3;
		if (width2<MIN_HAND_VIEWER_WIDTH) {
			width2=(size.width-PLAYER_VIEWER_WIDTH-spacing*4-offset)/2;			
			final int height2=(size.height-spacing*3)/2;
			final int x2=x+width2+spacing;
			y=spacing;
			result.setBoundary(ResolutionProfileType.GameOpponentPermanentViewer,
                    new Rectangle(x2,y,width2,height2));
			y+=height2+spacing;
			result.setBoundary(ResolutionProfileType.GamePlayerPermanentViewer,
                    new Rectangle(x2,y,width2,height2));			
		} else {
			final int height3=size.height-spacing*2;
			int x2=x+width2+spacing;
			y=spacing;
			result.setBoundary(ResolutionProfileType.GamePlayerPermanentViewer,
                    new Rectangle(x2,y,width2,height3));
			x2+=width2+spacing;
			result.setBoundary(ResolutionProfileType.GameOpponentPermanentViewer,
                    new Rectangle(x2,y,width2,height3));
		}
		
		final int height2=(size.height-spacing*3)/3;
		y=spacing;
		result.setBoundary(ResolutionProfileType.GameStackCombatViewer,new Rectangle(x,y,width2,2*height2));
		y+=2*height2+spacing;
		result.setBoundary(ResolutionProfileType.GameHandGraveyardViewer,new Rectangle(x,y,width2,height2));

		final int width3=size.width-PLAYER_VIEWER_WIDTH-spacing*3-offset;
		y=size.height-spacing-IMAGE_HAND_VIEWER_HEIGHT;
		result.setBoundary(ResolutionProfileType.GameImageHandGraveyardViewer,new Rectangle(x,y,width3,IMAGE_HAND_VIEWER_HEIGHT));
		result.setBoundary(ResolutionProfileType.GameZones,new Rectangle(0,0,x-offset,y-offset));
		
		final int height3=size.height-spacing*5-IMAGE_HAND_VIEWER_HEIGHT-offset;
		final int height4=(height3*3)/8;
		final int height5=height3-height4*2;
		y=spacing;
		result.setBoundary(ResolutionProfileType.GameImageOpponentPermanentViewer,new Rectangle(x,y,width3,height4));
		y+=height4+spacing;
		result.setBoundary(ResolutionProfileType.GameImageCombatViewer,new Rectangle(x,y,width3,height5));
		y+=height5+spacing;
		result.setBoundary(ResolutionProfileType.GameImagePlayerPermanentViewer,new Rectangle(x,y,width3,height4));
		return result;
	}
}
