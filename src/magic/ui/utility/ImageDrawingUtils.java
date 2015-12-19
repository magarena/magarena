package magic.ui.utility;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
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
        for (final AbilityIcon abi : AbilityIcon.values()) {
            if (abilityFlags.contains(abi.getAbility())) {
                g.drawImage(abi.getIcon().getImage(),ax,ay,observer);
                ax+=16;
            }
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
            if (amount > 0) {
                final String str = Integer.toString(amount);
                final int inc = 16 + 8 * (str.length() - 1);
                if (counterType == MagicCounterType.Charge) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.CHARGE).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.PlusOne) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.PLUS).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.MinusOne) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.MINUS).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.PlusTwo) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.PLUSTWO).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.MinusTwo) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.MINUSTWO).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.PlusZeroPlusOne) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.PLUSZEROPLUSONE).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.MinusZeroMinusOne) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.MINUSZEROMINUSONE).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.PlusZeroPlusTwo) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.PLUSZEROPLUSTWO).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.MinusZeroMinusTwo) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.MINUSZEROMINUSTWO).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.PlusOnePlusZero) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.PLUSONEPLUSZERO).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.MinusOneMinusZero) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.MINUSONEMINUSZERO).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.PlusOnePlusTwo) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.PLUSONEPLUSTWO).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.PlusTwoPlusZero) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.PLUSTWOPLUSZERO).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.MinusTwoMinusOne) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.MINUSTWOMINUSONE).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Feather) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.FEATHER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Gold) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.GOLDCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Bribery) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.BRIBECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Fade) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.FADECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Time) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.TIMECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Quest) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.QUESTCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Level) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.LEVELCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Hoofprint) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.HOOFPRINTCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Age) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.AGECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Ice) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.ICECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Spore) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.SPORECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Arrowhead) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.ARROWHEADCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Loyalty) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.LOYALTYCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Ki) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.KICOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Depletion) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.DEPLETIONCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Mining) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.MININGCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Muster) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.MUSTERCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Treasure) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.TREASURECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Strife) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.STRIFECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Study) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.STUDYCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Trap) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.TRAPCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Shield) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.SHIELDCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Wish) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.WISHCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Shell) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.SHELLCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Hatchling) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.SHELLCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Blaze) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.BLAZECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Tide) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.TIDECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Gem) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.GEMCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Credit) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.BRIBECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Pressure) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.PRESSURECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Verse) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.VERSECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Music) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.MUSICCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Rust) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.RUSTCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Lore) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.STUDYCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Blood) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.BLOODCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Growth) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.GROWTHCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Wage) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.BRIBECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Plague) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.PLAGUECOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Pin) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.PINCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Healing) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.HEALINGCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Scream) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.SCREAMCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Devotion) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.DEVOTIONCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Divinity) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.DIVINITYCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Death) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.DEATHCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Wind) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.WINDCOUNTER).getImage(),ax,ay,observer);
                    if (amount > 1){drawStringWithOutline(g, str, ax+6, ay+14, observer);}
                    ax+=inc;
                } else if (counterType == MagicCounterType.Tower) {
                    g.drawImage(MagicImages.getIcon(MagicIcon.TOWERCOUNTER).getImage(),ax,ay,observer);
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
        final boolean flip
    ) {
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

    public static void drawCardId(final Graphics g, long id, int x, int y) {
        if (MagicSystem.isDevMode()) {
            g.setFont(FontsAndBorders.FONT1.deriveFont(11f));
            GraphicsUtils.drawStringWithOutline(g, Long.toString(id), x + 6, y + 13);
        }
    }

}
