package magic.ui.utility;

import magic.ui.helpers.ImageHelper;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import java.text.AttributedString;
import java.awt.font.TextAttribute;
import magic.data.MagicIcon;
import magic.model.MagicAbility;
import magic.model.MagicColor;
import magic.model.MagicCounterType;
import magic.model.MagicManaCost;
import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import magic.model.event.MagicManaActivation;
import magic.ui.MagicImages;
import magic.ui.widget.FontsAndBorders;
import magic.ui.theme.AbilityIcon;
import magic.utility.MagicSystem;
import magic.data.TextImages;

public class ImageDrawingUtils {

    public static void drawCostInfo(
        final Graphics g,
        final ImageObserver observer,
        final MagicManaCost cost,
        final int x1,
        final int x2,
        final int y
    ) {
        final List<MagicIcon> icons = cost.getIcons();
        int x=x2-icons.size()*16;
        for (final MagicIcon icon : icons) {
            if (x>=x1) {
                g.drawImage(MagicImages.getIcon(icon).getImage(),x,y,observer);
            }
            x+=16;
        }
    }

    public static int drawManaInfo(
        final Graphics g,
        final ImageObserver observer,
        final Collection<MagicManaActivation> acts,
        final boolean isSnow,
        int ax,
        final int ay
    ) {
        final Set<MagicManaType> types = new HashSet<>();
        for (final MagicManaActivation manaAct : acts) {
            for (final MagicManaType manaType : manaAct.getManaTypes()) {
                if (manaType != MagicManaType.Colorless) {
                    types.add(manaType);
                }
            }
        }
        final List<ImageIcon> icons = new ArrayList<>();
        if (types.size()==MagicColor.NR_COLORS) {
            icons.add(MagicImages.getIcon(MagicIcon.MANA_ANY));
        } else if (types.isEmpty() && !acts.isEmpty()) {
            icons.add(MagicImages.getIcon(MagicIcon.MANA_COLORLESS));
        } else {
            for (final MagicColor color : MagicColor.values()) {
                if (types.contains(color.getManaType())) {
                    icons.add(MagicImages.getIcon(color.getManaType()));
                }
            }
        }
        if (isSnow && !icons.isEmpty()) {
            icons.add(MagicImages.getIcon(MagicIcon.MANA_SNOW));
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
        final int ay
    ) {
        for (final Image icon : AbilityIcon.getSmallAbilityIcons(abilityFlags)) {
            g.drawImage(icon, ax, ay, observer);
            ax += 16;
        }
        return ax;
    }

    public static int drawCountersInfo(
        final Graphics g,
        final ImageObserver observer,
        final MagicPermanent permanent,
        int ax,
        final int ay
    ) {
        for (final MagicCounterType counterType : MagicCounterType.values()) {
            int amount = permanent.getCounters(counterType);
            if (amount > 0 && TextImages.contains(counterType.getText())) {
                final String str = Integer.toString(amount);
                final int inc = 16 + 8 * (str.length() - 1);
                final MagicIcon icon = TextImages.getIcon(counterType.getText());
                g.drawImage(MagicImages.getIcon(icon).getImage(),ax,ay,observer);
                if (amount > 1) {
                    drawStringWithOutline(g, str, ax+6, ay+14, observer);
                }
                ax+=inc;
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
        final String shield,
        final String damage,
        final int x,
        final int y,
        final boolean flip
    ) {
        final boolean isShieldDamage = damage.length() + shield.length() > 0;
        g.setColor(FontsAndBorders.GRAY2);
        g.fillRect(x,y,ptWidth+4,isShieldDamage?32:18);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(x,y,ptWidth+4,isShieldDamage?32:18);
        g.drawString(pt,x+3,isShieldDamage&&flip?y+28:y+14);
        if (isShieldDamage) {
            final String separator = shield.length() > 0 && damage.length() > 0 ? " " : "";
            final String shieldDamage = shield + separator + damage;
            final int shieldDamageWidth=metrics.stringWidth(shieldDamage);
            final int dx=x+3+(ptWidth-shieldDamageWidth)/2;
            final AttributedString aString = new AttributedString(shieldDamage);
            aString.addAttribute(TextAttribute.FONT, FontsAndBorders.FONT1);
            if (shield.length() > 0) {
                aString.addAttribute(TextAttribute.FOREGROUND, Color.BLUE, 0, shield.length());
            }
            if (damage.length() > 0) {
                aString.addAttribute(TextAttribute.FOREGROUND, Color.RED, shieldDamage.length() - damage.length(), shieldDamage.length());
            }
            g.drawString(aString.getIterator(),dx,flip?y+14:y+28);
        }
    }

    public static void drawCardId(final Graphics g, long id, int x, int y) {
        if (MagicSystem.isDevMode()) {
            g.setFont(FontsAndBorders.FONT1.deriveFont(11f));
            ImageHelper.drawStringWithOutline(g, Long.toString(id), x + 6, y + 13);
        }
    }

}
