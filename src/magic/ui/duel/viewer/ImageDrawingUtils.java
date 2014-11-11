package magic.ui.duel.viewer;

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                    icons.add(IconImages.getIcon(color.getManaType(), true));
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
        if (abilityFlags.contains(MagicAbility.FirstStrike)) {
            g.drawImage(IconImages.STRIKE.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Trample)) {
            g.drawImage(IconImages.TRAMPLE.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Infect)) {
            g.drawImage(IconImages.INFECT.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Wither)) {
            g.drawImage(IconImages.WITHER.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.ProtectionFromBlack)) {
            g.drawImage(IconImages.PROTBLACK.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.ProtectionFromBlue)) {
            g.drawImage(IconImages.PROTBLUE.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.ProtectionFromGreen)) {
            g.drawImage(IconImages.PROTGREEN.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.ProtectionFromRed)) {
            g.drawImage(IconImages.PROTRED.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.ProtectionFromWhite)) {
            g.drawImage(IconImages.PROTWHITE.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.ProtectionFromAllColors)) {
            g.drawImage(IconImages.PROTALLCOLORS.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Defender)) {
            g.drawImage(IconImages.DEFENDER.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Vigilance)) {
            g.drawImage(IconImages.VIGILANCE.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.DoubleStrike)) {
            g.drawImage(IconImages.DOUBLESTRIKE.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Deathtouch)) {
            g.drawImage(IconImages.DEATHTOUCH.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Lifelink)) {
            g.drawImage(IconImages.LIFELINK.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Reach)) {
            g.drawImage(IconImages.REACH.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Shroud)) {
            g.drawImage(IconImages.SHROUD.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Hexproof)) {
            g.drawImage(IconImages.HEXPROOF.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Fear)) {
            g.drawImage(IconImages.FEAR.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Intimidate)) {
            g.drawImage(IconImages.INTIMIDATE.getImage(),ax,ay,observer);
            ax+=16;
        }
        if (abilityFlags.contains(MagicAbility.Indestructible)) {
            g.drawImage(IconImages.INDESTRUCTIBLE.getImage(),ax,ay,observer);
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
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.PlusOne) {
                    g.drawImage(IconImages.PLUS.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.MinusOne) {
                    g.drawImage(IconImages.MINUS.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Feather) {
                    g.drawImage(IconImages.FEATHER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Gold) {
                    g.drawImage(IconImages.GOLDCOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Bribery) {
                    g.drawImage(IconImages.BRIBECOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Fade) {
                    g.drawImage(IconImages.FADECOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Time) {
                    g.drawImage(IconImages.TIMECOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Quest) {
                    g.drawImage(IconImages.QUESTCOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Level) {
                    g.drawImage(IconImages.LEVELCOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Hoofprint) {
                    g.drawImage(IconImages.HOOFPRINTCOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Age) {
                    g.drawImage(IconImages.AGECOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Ice) {
                    g.drawImage(IconImages.ICECOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Spore) {
                    g.drawImage(IconImages.SPORECOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Arrowhead) {
                    g.drawImage(IconImages.ARROWHEADCOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Loyalty) {
                    g.drawImage(IconImages.LOYALTYCOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Ki) {
                    g.drawImage(IconImages.KICOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Depletion) {
                    g.drawImage(IconImages.DEPLETIONCOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Mining) {
                    g.drawImage(IconImages.MININGCOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Muster) {
                    g.drawImage(IconImages.MUSTERCOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Treasure) {
                    g.drawImage(IconImages.TREASURECOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Strife) {
                    g.drawImage(IconImages.STRIFECOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Study) {
                    g.drawImage(IconImages.STUDYCOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Trap) {
                    g.drawImage(IconImages.TRAPCOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Shield) {
                    g.drawImage(IconImages.SHIELDCOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Wish) {
                    g.drawImage(IconImages.WISHCOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Shell) {
                    g.drawImage(IconImages.SHELLCOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Hatchling) {
                    g.drawImage(IconImages.SHELLCOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Blaze) {
                    g.drawImage(IconImages.BLAZECOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Tide) {
                    g.drawImage(IconImages.TIDECOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Gem) {
                    g.drawImage(IconImages.GEMCOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Credit) {
                    g.drawImage(IconImages.BRIBECOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Pressure) {
                    g.drawImage(IconImages.PRESSURECOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Verse) {
                    g.drawImage(IconImages.VERSECOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Music) {
                    g.drawImage(IconImages.MUSICCOUNTER.getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
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
