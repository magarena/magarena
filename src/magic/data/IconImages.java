package magic.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import magic.MagicMain;

public class IconImages {

	public static final BufferedImage MISSING=loadImage("icons/missing.png");
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
	public static final ImageIcon CREATURE=loadIcon("creature.gif");
	public static final ImageIcon ARTIFACT=loadIcon("artifact.gif");
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
	
	public static final ImageIcon FLYING=loadIcon("flying.png");
	public static final ImageIcon TRAMPLE=loadIcon("trample.png");
	public static final ImageIcon STRIKE=loadIcon("strike.png");
	public static final ImageIcon DEATHTOUCH=loadIcon("deathtouch.png");

	public static final ImageIcon ANY_MANA=loadIcon("anymana.gif");
	public static final ImageIcon TAPPED=loadSymbolIcon("tapped.gif");
	public static final ImageIcon ONE=loadSymbolIcon("big_one.gif");
	public static final ImageIcon BLACK=loadSymbolIcon("big_black.gif");
	public static final ImageIcon BLUE=loadSymbolIcon("big_blue.gif");
	public static final ImageIcon GREEN=loadSymbolIcon("big_green.gif");	
	public static final ImageIcon RED=loadSymbolIcon("big_red.gif");
	public static final ImageIcon WHITE=loadSymbolIcon("big_white.gif");
	public static final ImageIcon COST_BLACK=loadSymbolIcon("black.gif");
	public static final ImageIcon COST_BLUE=loadSymbolIcon("blue.gif");
	public static final ImageIcon COST_GREEN=loadSymbolIcon("green.gif");	
	public static final ImageIcon COST_RED=loadSymbolIcon("red.gif");
	public static final ImageIcon COST_WHITE=loadSymbolIcon("white.gif");
	public static final ImageIcon COST_BLACK_GREEN=loadSymbolIcon("black_green.gif");
	public static final ImageIcon COST_BLACK_RED=loadSymbolIcon("black_red.gif");
	public static final ImageIcon COST_BLUE_BLACK=loadSymbolIcon("blue_black.gif");
	public static final ImageIcon COST_BLUE_RED=loadSymbolIcon("blue_red.gif");
	public static final ImageIcon COST_GREEN_BLUE=loadSymbolIcon("green_blue.gif");
	public static final ImageIcon COST_GREEN_WHITE=loadSymbolIcon("green_white.gif");
	public static final ImageIcon COST_RED_GREEN=loadSymbolIcon("red_green.gif");
	public static final ImageIcon COST_RED_WHITE=loadSymbolIcon("red_white.gif");
	public static final ImageIcon COST_WHITE_BLACK=loadSymbolIcon("white_black.gif");
	public static final ImageIcon COST_WHITE_BLUE=loadSymbolIcon("white_blue.gif");
	public static final ImageIcon COST_ZERO=loadSymbolIcon("zero.gif");
	public static final ImageIcon COST_ONE=loadSymbolIcon("one.gif");
	public static final ImageIcon COST_TWO=loadSymbolIcon("two.gif");
	public static final ImageIcon COST_THREE=loadSymbolIcon("three.gif");
	public static final ImageIcon COST_FOUR=loadSymbolIcon("four.gif");
	public static final ImageIcon COST_FIVE=loadSymbolIcon("five.gif");
	public static final ImageIcon COST_SIX=loadSymbolIcon("six.gif");
	public static final ImageIcon COST_SEVEN=loadSymbolIcon("seven.gif");
	public static final ImageIcon COST_EIGHT=loadSymbolIcon("eight.gif");
	public static final ImageIcon COST_NINE=loadSymbolIcon("nine.gif");
	public static final ImageIcon COST_X=loadSymbolIcon("x.gif");
		
	private static BufferedImage loadImage(final String name) {
		
		try {
			final InputStream inputStream=IconImages.class.getResourceAsStream(name);
			final BufferedImage image=ImageIO.read(inputStream);
			inputStream.close();
			return image;
		} catch (final Exception ex) {
			return null;
		}
	}
	
	private static ImageIcon loadIcon(final String name) {

		final BufferedImage image=loadImage("icons/"+name);
		return image!=null?new ImageIcon(image):MISSING2;
	}

	private static ImageIcon loadSymbolIcon(final String name) {

		final File iconFile=new File(MagicMain.getGamePath()+File.separator+"symbols"+File.separator+name);
		if (iconFile.exists()) {
			return new ImageIcon(iconFile.getAbsolutePath());
		}
		return MISSING2;
	}
	
	private static ImageIcon loadAnimatedIcon(final String name) {
		
		try {
			final byte data[]=new byte[1<<16];
			int size=0;
			final InputStream inputStream=IconImages.class.getResourceAsStream("icons/"+name);
			while (true) {
				
				final int len=inputStream.read(data,size,data.length-size);
				if (len<0) {
					break;
				}
				size+=len;
			}			
			return new ImageIcon(Arrays.copyOf(data,size));
		} catch (final Exception ex) {
			return MISSING2;
		}
	}
}