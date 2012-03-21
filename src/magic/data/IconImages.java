package magic.data;

import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class IconImages {
	public static final BufferedImage MISSING=loadImage("icons/missing.png");
	public static final BufferedImage MISSING2=loadImage("icons/missing2.png");
	public static final BufferedImage MISSING_CARD=loadImage("icons/missing_card.png");
	public static final ImageIcon MISSING_ICON=loadIcon("missing2.png");
   
    private static final BufferedImage MANA = loadImage("icons/Mana.png");
	public static final BufferedImage LOGO=loadImage("textures/logo.jpg");
	public static final BufferedImage WIZARD=loadImage("icons/wizard.png");
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
	public static final ImageIcon GOLDCOUNTER=loadIcon("goldcounter.png");
	public static final ImageIcon BRIBECOUNTER=loadIcon("bribecounter.png");
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
	public static final ImageIcon TAPPED=loadSymbolIcon(50, false);
	public static final ImageIcon ONE=loadSymbolIcon(1, true);
	public static final ImageIcon WHITE=loadSymbolIcon(24, true);
	public static final ImageIcon BLUE=loadSymbolIcon(25, true);
	public static final ImageIcon BLACK=loadSymbolIcon(26, true);
	public static final ImageIcon RED=loadSymbolIcon(27, true);
	public static final ImageIcon GREEN=loadSymbolIcon(28, true);	
	public static final ImageIcon COST_WHITE=loadSymbolIcon(24, false);
	public static final ImageIcon COST_BLUE=loadSymbolIcon(25, false);
	public static final ImageIcon COST_BLACK=loadSymbolIcon(26, false);
	public static final ImageIcon COST_RED=loadSymbolIcon(27, false);
	public static final ImageIcon COST_GREEN=loadSymbolIcon(28, false);	
	public static final ImageIcon COST_WHITE_BLUE=loadSymbolIcon(30, false);
	public static final ImageIcon COST_WHITE_BLACK=loadSymbolIcon(31, false);
	public static final ImageIcon COST_BLUE_BLACK=loadSymbolIcon(32, false);
	public static final ImageIcon COST_BLUE_RED=loadSymbolIcon(33, false);
	public static final ImageIcon COST_BLACK_RED=loadSymbolIcon(34, false);
	public static final ImageIcon COST_BLACK_GREEN=loadSymbolIcon(35, false);
	public static final ImageIcon COST_RED_WHITE=loadSymbolIcon(36, false);
	public static final ImageIcon COST_RED_GREEN=loadSymbolIcon(37, false);
	public static final ImageIcon COST_GREEN_WHITE=loadSymbolIcon(38, false);
	public static final ImageIcon COST_GREEN_BLUE=loadSymbolIcon(39, false);
	public static final ImageIcon COST_ZERO=loadSymbolIcon(0, false);
	public static final ImageIcon COST_ONE=loadSymbolIcon(1, false);
	public static final ImageIcon COST_TWO=loadSymbolIcon(2, false);
	public static final ImageIcon COST_THREE=loadSymbolIcon(3, false);
	public static final ImageIcon COST_FOUR=loadSymbolIcon(4, false);
	public static final ImageIcon COST_FIVE=loadSymbolIcon(5, false);
	public static final ImageIcon COST_SIX=loadSymbolIcon(6, false);
	public static final ImageIcon COST_SEVEN=loadSymbolIcon(7, false);
	public static final ImageIcon COST_EIGHT=loadSymbolIcon(8, false);
	public static final ImageIcon COST_NINE=loadSymbolIcon(9, false);
	public static final ImageIcon COST_TEN=loadSymbolIcon(10, false);
	public static final ImageIcon COST_ELEVEN=loadSymbolIcon(11, false);
	public static final ImageIcon COST_TWELVE=loadSymbolIcon(12, false);
	public static final ImageIcon COST_THIRTEEN=loadSymbolIcon(13, false);
	public static final ImageIcon COST_FOURTEEN=loadSymbolIcon(14, false);
	public static final ImageIcon COST_FIFTEEN=loadSymbolIcon(15, false);
	public static final ImageIcon COST_SIXTEEN=loadSymbolIcon(16, false);
//	public static final ImageIcon COST_NINE_OR_MORE=loadIcon("nineplus.gif");
	public static final ImageIcon COST_X=loadSymbolIcon(21, false);
		
	private static BufferedImage loadImage(final String name) {
        return FileIO.toImg(IconImages.class.getResource(name), MISSING2);
	}
	
	private static ImageIcon loadIcon(final String name) {
		final BufferedImage image=loadImage("icons/"+name);
		return new ImageIcon(image);
	}

	private static ImageIcon loadSymbolIcon(final int pos, final boolean big) {
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
	
	private static ImageIcon loadAnimatedIcon(final String name) {
        final byte data[] = new byte[1<<16];
        int size = 0;
        final InputStream inputStream = IconImages.class.getResourceAsStream("icons/"+name);
        try { //load animated icon
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
            FileIO.close(inputStream);
        }
        return new ImageIcon(Arrays.copyOf(data,size));
	}
}
