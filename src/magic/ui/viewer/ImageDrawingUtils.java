package magic.ui.viewer;

import magic.data.IconImages;
import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicCounterType;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.ui.widget.FontsAndBorders;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

public class ImageDrawingUtils {
    public static void drawCostInfo(
            final Graphics g,
            final ImageObserver observer,
            final MagicManaCost cost,
            final int x1,
            final int x2,
            final int y) {
        final List<ImageIcon> icons=cost.getIcons();
        int x=x2-icons.size()*16;
        for (final ImageIcon icon : icons) {
            if (x>=x1) {
                g.drawImage(icon.getImage(),x,y,observer);
            }
            x+=16;
        }
    }

    public static int drawManaInfo(
            final Graphics g,
            final ImageObserver observer,
            final MagicCardDefinition cardDefinition,
            int ax,
            final int ay) {
        final List<ImageIcon> icons=new ArrayList<ImageIcon>();
        for (final MagicColor color : MagicColor.values()) {
            if (cardDefinition.getManaSource(color) > 0) {
                icons.add(color.getManaType().getIcon(true));
            }
        }    
        if (icons.size()==MagicColor.NR_COLORS) {
            icons.clear();
            icons.add(IconImages.ANY_MANA);
        }
        if (icons.isEmpty() && !cardDefinition.getManaActivations().isEmpty()) {
            icons.add(IconImages.COST_ONE);
        }
        if (!icons.isEmpty()) {
            for (final ImageIcon icon : icons) {
                g.drawImage(icon.getImage(),ax,ay,observer);
                ax+=16;
            }
        }
        return ax;
    }

    public static int drawAbilityInfo(
            final Graphics g,
            final ImageObserver observer,
            final long abilityFlags,
            int ax,
            final int ay) {
        if (MagicAbility.Flying.hasAbility(abilityFlags)) {                
            g.drawImage(IconImages.FLYING.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (MagicAbility.FirstStrike.hasAbility(abilityFlags)||
            MagicAbility.DoubleStrike.hasAbility(abilityFlags)) {                
            g.drawImage(IconImages.STRIKE.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (MagicAbility.Trample.hasAbility(abilityFlags)) {
            g.drawImage(IconImages.TRAMPLE.getImage(),ax,ay,observer);
            ax+=16;                
        }
        if (MagicAbility.Deathtouch.hasAbility(abilityFlags)||
            MagicAbility.Wither.hasAbility(abilityFlags)||
            MagicAbility.Infect.hasAbility(abilityFlags)) {
            g.drawImage(IconImages.DEATHTOUCH.getImage(),ax,ay,observer);
            ax+=16;                                
        }
        return ax;
    }
    
    public static int drawCountersInfo(
            final Graphics g,
            final ImageObserver observer,
            final MagicPermanent permanent,
            int ax,
            final int ay) {
        for (final MagicCounterType counterType : MagicCounterType.values()) {
            int amount = permanent.getCounters(counterType);
            if (amount > 0) {
                if (amount > 9) {
                    amount = 9;
                }
                if (counterType == MagicCounterType.Charge) {
                    g.drawImage(IconImages.CHARGE.getImage(),ax,ay,observer);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(Integer.toString(amount),ax+5,ay+14);
                    ax+=16;
                }
                if (counterType == MagicCounterType.Feather) {
                    g.drawImage(IconImages.FEATHER.getImage(),ax,ay,observer);
                    ax+=16;
                }
                if (counterType == MagicCounterType.Gold) {
                    g.drawImage(IconImages.GOLDCOUNTER.getImage(),ax,ay,observer);
                    ax+=16;
                }
                if (counterType == MagicCounterType.Bribery) {
                    g.drawImage(IconImages.BRIBECOUNTER.getImage(),ax,ay,observer);
                    ax+=16;
                }
                if (counterType == MagicCounterType.PlusOne) {
                    g.drawImage(IconImages.PLUS.getImage(),ax,ay,observer);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(Integer.toString(amount),ax+6,ay+14);
                    ax+=16;
                }
                if (counterType == MagicCounterType.MinusOne) {
                    g.drawImage(IconImages.MINUS.getImage(),ax,ay,observer);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(Integer.toString(amount),ax+6,ay+14);
                    ax+=16;
                }
            }
        }
        return ax;
    }
    
    public static void drawCreatureInfo(
            final Graphics g,
            final FontMetrics metrics,
            final String pt,
            final int ptWidth,
            final String damage,
            final int x,
            final int y,
            final boolean flip) {
        final boolean isDamage = damage.length() > 0;
        g.setColor(FontsAndBorders.GRAY2);
        g.fillRect(x,y,ptWidth+4,isDamage?32:18);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(x,y,ptWidth+4,isDamage?32:18);
        g.drawString(pt,x+3,isDamage&&flip?y+28:y+14);
        if (isDamage) {
            final int damageWidth=metrics.stringWidth(damage);
            final int dx=x+3+(ptWidth-damageWidth)/2;
            g.setColor(Color.RED);
            g.drawString(damage,dx,flip?y+14:y+28);
        }
    }        
}
