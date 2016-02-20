package magic.ui.widget;

import magic.data.GeneralConfig;
import magic.ui.duel.resolution.ResolutionProfileResult;
import magic.ui.duel.resolution.ResolutionProfileType;
import magic.ui.theme.Theme;

import magic.ui.utility.GraphicsUtils;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import magic.ui.utility.MagicStyle;

@SuppressWarnings("serial")
public class ZoneBackgroundLabel extends JLabel {

    private boolean game;
    private boolean image=true;
    private int playerX;
    private int handY;
    private BufferedImage customImage;

    public void setGame(final boolean game) {
        this.game=game;
        if (isCustomBackgroundImage()) {
            customImage = GraphicsUtils.getCustomBackgroundImage();
        }
    }

    public void setImage(final boolean image) {
        this.image=image;
    }

    public void setZones(final ResolutionProfileResult result) {
        final Rectangle rect=result.getBoundary(ResolutionProfileType.GameZones);
        playerX=rect.width;
        handY=rect.height;
    }

    private void paintZoneTile(final Graphics g,final BufferedImage aImage,final Rectangle rect) {
        final int imageWidth=aImage.getWidth();
        final int imageHeight=aImage.getHeight();
        final int x2=rect.x+rect.width;
        final int y2=rect.y+rect.height;
        for (int y=rect.y;y<y2;y+=imageHeight) {
            for (int x=rect.x;x<x2;x+=imageWidth) {
                g.drawImage(aImage,x,y,this);
            }
        }
    }

    private void paintZoneStretch(final Graphics g,final BufferedImage aImage,final Rectangle rect) {
        final int iw=aImage.getWidth();
        final int ih=aImage.getHeight();
        final int iw2=ih*rect.width/rect.height;
        final Rectangle imageRect;
        if (iw2<=iw) {
            imageRect=new Rectangle((iw-iw2)/2,0,iw2,ih);
        } else {
            final int ih2=iw*rect.height/rect.width;
            imageRect=new Rectangle(0,(ih-ih2)/2,iw,ih2);
        }
        g.drawImage(aImage,rect.x,rect.y,rect.x+rect.width,rect.y+rect.height,
                imageRect.x,imageRect.y,imageRect.x+imageRect.width,imageRect.y+imageRect.height,this);
    }

    private void paintZone(final Graphics g,final BufferedImage aImage,final Rectangle rect,final boolean stretch) {
        if (stretch) {
            paintZoneStretch(g,aImage,rect);
        } else {
            paintZoneTile(g,aImage,rect);
        }
    }
 
    private void drawThemeBackground(final Graphics g) {

        final Theme theme = MagicStyle.getTheme();
        final Dimension size = getSize();

        final int stretch = theme.getValue(Theme.VALUE_GAME_STRETCH);
        final boolean battlefieldStretch = (stretch & 1) == 1;
        final boolean playerStretch = (stretch & 2) == 2;
        final boolean handStretch = (stretch & 4) == 4;

        switch (theme.getValue(Theme.VALUE_GAME_LAYOUT)) {
            case 1:
                paintZone(g, theme.getTexture(Theme.TEXTURE_BATTLEFIELD),
                    new Rectangle(0, 0, size.width, size.height), battlefieldStretch);
                break;
            case 2:
                if (image) {
                    paintZone(g, theme.getTexture(Theme.TEXTURE_PLAYER),
                        new Rectangle(0, 0, size.width, size.height), playerStretch);
                    paintZone(g, theme.getTexture(Theme.TEXTURE_BATTLEFIELD),
                        new Rectangle(playerX, 0, size.width - playerX, handY), battlefieldStretch);
                } else {
                    paintZone(g, theme.getTexture(Theme.TEXTURE_PLAYER),
                        new Rectangle(0, 0, playerX, size.height), playerStretch);
                    paintZone(g, theme.getTexture(Theme.TEXTURE_BATTLEFIELD),
                        new Rectangle(playerX, 0, size.width - playerX, size.height), battlefieldStretch);
                }
                break;
            case 3:
                paintZone(g, theme.getTexture(Theme.TEXTURE_PLAYER),
                    new Rectangle(0, 0, playerX, size.height), playerStretch);
                if (image) {
                    paintZone(g, theme.getTexture(Theme.TEXTURE_BATTLEFIELD),
                        new Rectangle(playerX, 0, size.width - playerX, handY), battlefieldStretch);
                    paintZone(g, theme.getTexture(Theme.TEXTURE_HAND),
                        new Rectangle(playerX, handY, size.width - playerX, handY), handStretch);
                } else {
                    paintZone(g, theme.getTexture(Theme.TEXTURE_BATTLEFIELD),
                        new Rectangle(playerX, 0, size.width - playerX, size.height), battlefieldStretch);
                }
                break;
        }
        final int border = theme.getValue(Theme.VALUE_GAME_BORDER);
        if (border > 0) {
            final Graphics2D g2d = (Graphics2D) g;
            g2d.setPaint(theme.getColor(Theme.COLOR_GAME_BORDER));
            if (image) {
                g2d.fillRect(playerX, 0, border, handY);
                g2d.fillRect(playerX, handY, size.width - playerX, border);
            } else {
                g2d.fillRect(playerX, 0, border, size.height);
            }
        }

    }

    private void drawCustomBackground(final Graphics g) {
        final Dimension size = getSize();
        final Theme theme = MagicStyle.getTheme();
        final BufferedImage background;
        if (isCustomBackgroundImage()) {
            background = customImage;
        } else {
            background = theme.getTexture(Theme.TEXTURE_BACKGROUND);
        }
        final boolean stretchTexture = theme.getValue(Theme.VALUE_BACKGROUND_STRETCH) == 1;
        paintZone(g, background, new Rectangle(0, 0, size.width, size.height), stretchTexture);
    }

    private boolean isCustomBackgroundImage() {
        return GeneralConfig.getInstance().isCustomBackground();
    }

    @Override
    public void paintComponent(final Graphics g) {
        if (game && !isCustomBackgroundImage()) {
            drawThemeBackground(g);
        } else {
            drawCustomBackground(g);
        }
    }

}
