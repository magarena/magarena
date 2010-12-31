package magic.ui.viewer;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.util.List;

import javax.swing.ImageIcon;

import magic.data.IconImages;
import magic.model.MagicAbility;
import magic.model.MagicManaCost;
import magic.ui.widget.FontsAndBorders;

public class ImageDrawingUtils {

	public static void drawCostInfo(final Graphics g,final ImageObserver observer,final MagicManaCost cost,final int x1,final int x2,final int y) {
		
		final List<ImageIcon> icons=cost.getIcons();
		int x=x2-icons.size()*16;
		for (final ImageIcon icon : icons) {

			if (x>=x1) {
				g.drawImage(icon.getImage(),x,y,observer);
			}
			x+=16;
		}
	}
	
	public static int drawAbilityInfo(final Graphics g,final ImageObserver observer,final long abilityFlags,int ax,int ay) {

		if (MagicAbility.Flying.hasAbility(abilityFlags)) {				
			g.drawImage(IconImages.FLYING.getImage(),ax,ay,observer);
			ax+=16;
		}
		if (MagicAbility.FirstStrike.hasAbility(abilityFlags)||MagicAbility.DoubleStrike.hasAbility(abilityFlags)) {				
			g.drawImage(IconImages.STRIKE.getImage(),ax,ay,observer);
			ax+=16;
		}
		if (MagicAbility.Trample.hasAbility(abilityFlags)) {
			g.drawImage(IconImages.TRAMPLE.getImage(),ax,ay,observer);
			ax+=16;				
		}
		if (MagicAbility.Deathtouch.hasAbility(abilityFlags)) {
			g.drawImage(IconImages.DEATHTOUCH.getImage(),ax,ay,observer);
			ax+=16;								
		}
		return ax;
	}
	
	public static void drawCreatureInfo(final Graphics g,final FontMetrics metrics,
			final String pt,final int ptWidth,final String damage,final int x,final int y,final boolean flip) {

		g.setColor(FontsAndBorders.GRAY2);
		g.fillRect(x,y,ptWidth+4,damage!=null?32:18);
		g.setColor(Color.DARK_GRAY);
		g.drawRect(x,y,ptWidth+4,damage!=null?32:18);
		g.drawString(pt,x+3,damage!=null&&flip?y+28:y+14);
		if (damage!=null) {
			final int damageWidth=metrics.stringWidth(damage);
			int dx=x+3+(ptWidth-damageWidth)/2;
			g.setColor(Color.RED);
			g.drawString(damage,dx,flip?y+14:y+28);
		}
	}	
}