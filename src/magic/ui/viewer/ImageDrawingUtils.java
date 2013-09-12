package magic.ui.viewer;

import magic.data.IconImages;
import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicCounterType;
import magic.model.MagicManaCost;
import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import magic.model.event.MagicManaActivation;
import magic.ui.widget.FontsAndBorders;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

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
        final Set<MagicManaType> types = new HashSet<MagicManaType>();
        for (final MagicManaActivation manaAct : cardDefinition.getManaActivations()) {
            for (final MagicManaType manaType : manaAct.getManaTypes()) {
                if (manaType != MagicManaType.Colorless) {
                    types.add(manaType);
                }
            }
        }
        final List<ImageIcon> icons = new ArrayList<ImageIcon>();
        if (types.size()==MagicColor.NR_COLORS) {
            icons.add(IconImages.ANY_MANA);
        } else if (types.isEmpty() && !cardDefinition.getManaActivations().isEmpty()) {
            icons.add(IconImages.COST_ONE);
        } else {
            for (final MagicColor color : MagicColor.values()) {
                if (types.contains(color.getManaType())) {
                    icons.add(color.getManaType().getIcon(true));
                }
            }
        }
        for (final ImageIcon icon : icons) {
            g.drawImage(icon.getImage(),ax,ay,observer);
            ax+=16;
        }
        return ax;
    }

    public static int drawAbilityInfo(
            final Graphics g,
            final ImageObserver observer,
            final Set<MagicAbility> abilityFlags,
            int ax,
            final int ay) {
        if (abilityFlags.contains(MagicAbility.Flying)) {
            g.drawImage(IconImages.FLYING.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.FirstStrike)||
            abilityFlags.contains(MagicAbility.DoubleStrike)) {
            g.drawImage(IconImages.STRIKE.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Trample)) {
            g.drawImage(IconImages.TRAMPLE.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Deathtouch)||
            abilityFlags.contains(MagicAbility.Wither)||
            abilityFlags.contains(MagicAbility.Infect)) {
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
                final String str = Integer.toString(amount);
                final int inc = 16 + 8 * (str.length() - 1);
                if (counterType == MagicCounterType.Charge) {
                    g.drawImage(IconImages.CHARGE.getImage(),ax,ay,observer);
                    drawStringWithOutline(g, str, ax+5, ay+14, observer);
                    ax+=inc;
                } else if (counterType == MagicCounterType.PlusOne) {
                    g.drawImage(IconImages.PLUS.getImage(),ax,ay,observer);
                    drawStringWithOutline(g, str, ax+6, ay+14, observer);
                    ax+=inc;
                } else if (counterType == MagicCounterType.MinusOne) {
                    g.drawImage(IconImages.MINUS.getImage(),ax,ay,observer);
                    drawStringWithOutline(g, str, ax+6, ay+14, observer);
                    ax+=inc;
                } else if (counterType == MagicCounterType.Feather) {
                    g.drawImage(IconImages.FEATHER.getImage(),ax,ay,observer);
                    ax+=16;
                } else if (counterType == MagicCounterType.Gold) {
                    g.drawImage(IconImages.GOLDCOUNTER.getImage(),ax,ay,observer);
                    ax+=16;
                } else if (counterType == MagicCounterType.Bribery) {
                    g.drawImage(IconImages.BRIBECOUNTER.getImage(),ax,ay,observer);
                    ax+=16;
                }
            }
        }
        return ax;
    }

    private static void drawStringWithOutline(final Graphics g, final String str, int x, int y, final ImageObserver observer) {
        g.setColor(FontsAndBorders.GRAY2);
        g.drawString(str,x+1,y);
        g.drawString(str,x-1,y);
        g.drawString(str,x,y+1);
        g.drawString(str,x,y-1);
        g.setColor(Color.DARK_GRAY);
        g.drawString(str,x,y);
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
