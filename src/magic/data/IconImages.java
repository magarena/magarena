package magic.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import magic.MagicMain;

public class IconImages {

    //use mana symbols by goblin hero if it exists
    static final BufferedImage MANA = loadImage("icons/Mana.png");

	public static final BufferedImage MISSING=loadImage("icons/missing.png");
	public static final BufferedImage MISSING_CARD=loadImage("icons/missing_card.png");
	public static final ImageIcon MISSING2=loadIcon("missing2.png");
	
	public static final BufferedImage LOGO=loadImage("textures/logo.jpg");
	public static final BufferedImage WOOD=loadImage("textures/wood.jpg");
	public static final BufferedImage MARBLE=loadImage("textures/marble.jpg");
	public static final BufferedImage GRANITE=loadImage("textures/granite.jpg");
	public static final BufferedImage GRANITE2=loadImage("textures/granite2.jpg");	
	public static final BufferedImage OPAL=loadImage("textures/opal.jpg");
	public static final BufferedImage OPAL2=loadImage("textures/opal2.jpg");

	public static final ImageIcon ARENA=loadIcon("arena.png");
	public static final ImageIcon CUBE=loadIcon("cube.png");
	public static final ImageIcon ANY=loadIcon("any.png");
	public static final ImageIcon FOLDER=loadIcon("folder.png");
	public static final ImageIcon LOG=loadIcon("log.png");
	public static final ImageIcon TEXT=loadIcon("text.png");
	public static final ImageIcon OK=loadIcon("ok.gif");
	public static final ImageIcon CANCEL=loadIcon("cancel.gif");
	public static final ImageIcon CLOSE=loadIcon("close.png");
	public static final ImageIcon FORWARD=loadIcon("forward.png");
	public static final ImageIcon FORWARD2=loadIcon("forward2.png");
	public static final ImageIcon START=loadIcon("start.png");
	public static final ImageIcon STOP=loadIcon("stop.png");
	public static final ImageIcon UNDO=loadIcon("undo.png");
	public static final ImageIcon BUSY=loadAnimatedIcon("busy.gif");
	public static final ImageIcon YOU=loadIcon("you.gif");
	public static final ImageIcon OPPONENT=loadIcon("opponent.gif");
	public static final ImageIcon DISABLED=loadIcon("disabled.gif");
	public static final ImageIcon ALL=loadIcon("all.gif");
	public static final ImageIcon LEFT=loadIcon("left.gif");
	public static final ImageIcon RIGHT=loadIcon("right.gif");
	public static final ImageIcon CREATURE=loadIcon("creature.png");
	public static final ImageIcon ARTIFACT=loadIcon("artifact.png");
	public static final ImageIcon EQUIPMENT=loadIcon("equipment.gif");
	public static final ImageIcon ENCHANTMENT=loadIcon("enchantment.gif");
	public static final ImageIcon AURA=loadIcon("aura.png");
	public static final ImageIcon SPELL=loadIcon("spell.gif");
	public static final ImageIcon ABILITY=loadIcon("ability.png");
	public static final ImageIcon TRIGGER=loadIcon("trigger.png");
	public static final ImageIcon TOKEN=loadIcon("token.png");
	public static final ImageIcon PLUS=loadIcon("plus.png");
	public static final ImageIcon MINUS=loadIcon("minus.png");
	public static final ImageIcon CHARGE=loadIcon("charge.png");
	public static final ImageIcon FEATHER=loadIcon("feather.gif");
	public static final ImageIcon LAND=loadIcon("land.gif");
	public static final ImageIcon LAND2=loadIcon("land2.gif");
	public static final ImageIcon LIFE=loadIcon("life.gif");
	public static final ImageIcon PREVENT=loadIcon("prevent.gif");
	public static final ImageIcon PREVENT2=loadIcon("prevent2.gif");
	public static final ImageIcon POISON=loadIcon("poison.png");
	public static final ImageIcon HAND=loadIcon("hand.gif");
	public static final ImageIcon HAND2=loadIcon("hand2.png");
	public static final ImageIcon LIBRARY=loadIcon("library.gif");
	public static final ImageIcon LIBRARY2=loadIcon("library2.gif");
	public static final ImageIcon GRAVEYARD=loadIcon("graveyard.gif");
	public static final ImageIcon GRAVEYARD2=loadIcon("graveyard2.gif");
	public static final ImageIcon EXILE=loadIcon("exile.png");
	public static final ImageIcon NUMBER=loadIcon("number.png");
	public static final ImageIcon DIFFICULTY=loadIcon("difficulty.png");
	public static final ImageIcon DIFFICULTY2=loadIcon("difficulty2.gif");
	public static final ImageIcon CANNOTTAP=loadIcon("cannottap.png");
	public static final ImageIcon SLEEP=loadIcon("sleep.gif");
	public static final ImageIcon REGENERATED=loadIcon("regenerated.gif");
	public static final ImageIcon DAMAGE=loadIcon("damage.gif");
	public static final ImageIcon COMBAT=loadIcon("combat.gif");
	public static final ImageIcon ATTACK=loadIcon("attack.gif");
	public static final ImageIcon BLOCK=loadIcon("block.gif");	
	public static final ImageIcon BLOCKED=loadIcon("blocked.gif");
	public static final ImageIcon MESSAGE=loadIcon("message.png");
	public static final ImageIcon PROGRESS=loadIcon("progress.png");
	public static final ImageIcon TROPHY=loadIcon("trophy.gif");
	public static final ImageIcon GOLD=loadIcon("gold.png");
	public static final ImageIcon SILVER=loadIcon("silver.png");
	public static final ImageIcon BRONZE=loadIcon("bronze.png");
	public static final ImageIcon CLOVER=loadIcon("clover.gif");
	public static final ImageIcon LOSE=loadIcon("lose.png");
	public static final ImageIcon TARGET=loadIcon("target.gif");
	public static final ImageIcon VALID=loadIcon("valid.gif");
	public static final ImageIcon STRENGTH=loadIcon("strength.png");
	public static final ImageIcon EDIT=loadIcon("edit.png");
	public static final ImageIcon DELAY=loadIcon("delay.png");
	public static final ImageIcon PICTURE=loadIcon("picture.png");
	public static final ImageIcon AVATAR=loadIcon("avatar.gif");
	
	public static final ImageIcon FLYING=loadIcon("flying.png");
	public static final ImageIcon TRAMPLE=loadIcon("trample.png");
	public static final ImageIcon STRIKE=loadIcon("strike.png");
	public static final ImageIcon DEATHTOUCH=loadIcon("deathtouch.png");

	public static final ImageIcon ANY_MANA=loadIcon("anymana.gif");
	public static final ImageIcon TAPPED=loadSymbolIcon("tapped.gif", 50, false);
	public static final ImageIcon ONE=loadSymbolIcon("big_one.gif", 1, true);
	public static final ImageIcon WHITE=loadSymbolIcon("big_white.gif", 24, true);
	public static final ImageIcon BLUE=loadSymbolIcon("big_blue.gif", 25, true);
	public static final ImageIcon BLACK=loadSymbolIcon("big_black.gif", 26, true);
	public static final ImageIcon RED=loadSymbolIcon("big_red.gif", 27, true);
	public static final ImageIcon GREEN=loadSymbolIcon("big_green.gif", 28, true);	
	public static final ImageIcon COST_WHITE=loadSymbolIcon("white.gif", 24, false);
	public static final ImageIcon COST_BLUE=loadSymbolIcon("blue.gif", 25, false);
	public static final ImageIcon COST_BLACK=loadSymbolIcon("black.gif", 26, false);
	public static final ImageIcon COST_RED=loadSymbolIcon("red.gif", 27, false);
	public static final ImageIcon COST_GREEN=loadSymbolIcon("green.gif", 28, false);	
	public static final ImageIcon COST_WHITE_BLUE=loadSymbolIcon("white_blue.gif", 30, false);
	public static final ImageIcon COST_WHITE_BLACK=loadSymbolIcon("white_black.gif", 31, false);
	public static final ImageIcon COST_BLUE_BLACK=loadSymbolIcon("blue_black.gif", 32, false);
	public static final ImageIcon COST_BLUE_RED=loadSymbolIcon("blue_red.gif", 33, false);
	public static final ImageIcon COST_BLACK_RED=loadSymbolIcon("black_red.gif", 34, false);
	public static final ImageIcon COST_BLACK_GREEN=loadSymbolIcon("black_green.gif", 35, false);
	public static final ImageIcon COST_RED_WHITE=loadSymbolIcon("red_white.gif", 36, false);
	public static final ImageIcon COST_RED_GREEN=loadSymbolIcon("red_green.gif", 37, false);
	public static final ImageIcon COST_GREEN_WHITE=loadSymbolIcon("green_white.gif", 38, false);
	public static final ImageIcon COST_GREEN_BLUE=loadSymbolIcon("green_blue.gif", 39, false);
	public static final ImageIcon COST_ZERO=loadSymbolIcon("zero.gif", 0, false);
	public static final ImageIcon COST_ONE=loadSymbolIcon("one.gif", 1, false);
	public static final ImageIcon COST_TWO=loadSymbolIcon("two.gif", 2, false);
	public static final ImageIcon COST_THREE=loadSymbolIcon("three.gif", 3, false);
	public static final ImageIcon COST_FOUR=loadSymbolIcon("four.gif", 4, false);
	public static final ImageIcon COST_FIVE=loadSymbolIcon("five.gif", 5, false);
	public static final ImageIcon COST_SIX=loadSymbolIcon("six.gif", 6, false);
	public static final ImageIcon COST_SEVEN=loadSymbolIcon("seven.gif", 7, false);
	public static final ImageIcon COST_EIGHT=loadSymbolIcon("eight.gif", 8, false);
	public static final ImageIcon COST_NINE=loadSymbolIcon("nine.gif", 9, false);
	public static final ImageIcon COST_NINE_OR_MORE=loadIcon("nineplus.gif");
	public static final ImageIcon COST_X=loadSymbolIcon("x.gif", 21, false);
		
	private static BufferedImage loadImage(final String name) {
        try {
            return ImageIO.read(IconImages.class.getResource(name));
        } catch (final IOException ex) {
            System.err.println("WARNING. Unable to load image " + name);
            return null;
        }
	}
	
	private static ImageIcon loadIcon(final String name) {
		final BufferedImage image=loadImage("icons/"+name);
		ImageIcon icon = null;
        icon = new ImageIcon(image);
        return image != null ? icon : MISSING2;
	}

	private static ImageIcon loadSymbolIcon(final String name, final int pos, final boolean big) {
        if (MANA != null) {
            final int imgW = 75;
            final int imgH = 75;
            final int icoW = 15;
            final int icoH = 15;
            final int bigW = 25;
            final int bigH = 25;
            final int row = pos / 10;
            final int col = pos % 10;
            final BufferedImage subimage = MANA.getSubimage(col * imgW, row * imgH, imgW, imgH);
            if (big) {
                return new ImageIcon(magic.GraphicsUtilities.scale(subimage,bigW,bigH));
            } else {
                return new ImageIcon(magic.GraphicsUtilities.scale(subimage,icoW,icoH));
            }
        }
		final File iconFile=new File(MagicMain.getGamePath()+File.separator+"symbols"+File.separator+name);
		if (iconFile.exists()) {
			return new ImageIcon(iconFile.getAbsolutePath());
		}
		return loadIcon("missing2.png");
	}
	
    private static void reloadSymbolIcon(final ImageIcon imgico, final String name) {
		final File iconFile = new File(
                MagicMain.getGamePath()+File.separator+"symbols"+File.separator+name);
		if (iconFile.exists()) {
            try {
                final BufferedImage img = ImageIO.read(iconFile);
                imgico.setImage(img);
            } catch (final IOException ex) {
                System.err.println("WARNING. Unable to load image " + iconFile.getName());
            }
		}
	}

    public static void reloadSymbols() {
        reloadSymbolIcon(TAPPED, "tapped.gif");
	    reloadSymbolIcon(ONE, "big_one.gif");
        reloadSymbolIcon(BLACK, "big_black.gif");
        reloadSymbolIcon(BLUE, "big_blue.gif");
        reloadSymbolIcon(GREEN, "big_green.gif");	
        reloadSymbolIcon(RED, "big_red.gif");
        reloadSymbolIcon(WHITE, "big_white.gif");
        reloadSymbolIcon(COST_BLACK, "black.gif");
        reloadSymbolIcon(COST_BLUE, "blue.gif");
        reloadSymbolIcon(COST_GREEN, "green.gif");	
        reloadSymbolIcon(COST_RED, "red.gif");
        reloadSymbolIcon(COST_WHITE, "white.gif");
        reloadSymbolIcon(COST_BLACK_GREEN, "black_green.gif");
        reloadSymbolIcon(COST_BLACK_RED, "black_red.gif");
        reloadSymbolIcon(COST_BLUE_BLACK, "blue_black.gif");
        reloadSymbolIcon(COST_BLUE_RED, "blue_red.gif");
        reloadSymbolIcon(COST_GREEN_BLUE, "green_blue.gif");
        reloadSymbolIcon(COST_GREEN_WHITE, "green_white.gif");
        reloadSymbolIcon(COST_RED_GREEN, "red_green.gif");
        reloadSymbolIcon(COST_RED_WHITE, "red_white.gif");
        reloadSymbolIcon(COST_WHITE_BLACK, "white_black.gif");
        reloadSymbolIcon(COST_WHITE_BLUE, "white_blue.gif");
        reloadSymbolIcon(COST_ZERO, "zero.gif");
        reloadSymbolIcon(COST_ONE, "one.gif");
        reloadSymbolIcon(COST_TWO, "two.gif");
        reloadSymbolIcon(COST_THREE, "three.gif");
        reloadSymbolIcon(COST_FOUR, "four.gif");
        reloadSymbolIcon(COST_FIVE, "five.gif");
        reloadSymbolIcon(COST_SIX, "six.gif");
        reloadSymbolIcon(COST_SEVEN, "seven.gif");
        reloadSymbolIcon(COST_EIGHT, "eight.gif");
        reloadSymbolIcon(COST_NINE, "nine.gif");
        reloadSymbolIcon(COST_X, "x.gif");
    }
	
	private static ImageIcon loadAnimatedIcon(final String name) {
        final byte data[] = new byte[1<<16];
        int size = 0;
        final InputStream inputStream = IconImages.class.getResourceAsStream("icons/"+name);
        try {
            while (true) {
                final int len = inputStream.read(data,size,data.length-size);
                if (len < 0) {
                    break;
                }
                size += len;
            }
        } catch (final IOException ex) {
            System.err.println("WARNING. Unable to load animated icon " + name);
        } finally {
            try {
                inputStream.close();
            } catch (final IOException ex) {
                System.err.println("WARNING. Unable to close input stream");
            }
        }
        return new ImageIcon(Arrays.copyOf(data,size));
	}
}
