package magic.ui.duel.viewer;

import javax.swing.ImageIcon;
import magic.ui.IconImages;

import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.stack.MagicItemOnStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class PermanentViewerInfo {

    public static final Comparator<PermanentViewerInfo> NAME_COMPARATOR=new Comparator<PermanentViewerInfo>() {
        @Override
        public int compare(final PermanentViewerInfo permanentInfo1,final PermanentViewerInfo permanentInfo2) {
            final int dif=permanentInfo1.name.compareTo(permanentInfo2.name);
            if (dif!=0) {
                return dif;
            }
            return permanentInfo1.permanent.compareTo(permanentInfo2.permanent);
        }
    };

    public static final Comparator<PermanentViewerInfo> BLOCKED_NAME_COMPARATOR=new Comparator<PermanentViewerInfo>() {
        @Override
        public int compare(final PermanentViewerInfo permanentInfo1,final PermanentViewerInfo permanentInfo2) {
            final int dif=permanentInfo1.blockedName.compareTo(permanentInfo2.blockedName);
            if (dif!=0) {
                return dif;
            }
            return permanentInfo1.permanent.compareTo(permanentInfo2.permanent);
        }
    };

    public final MagicPermanent permanent;
    public final MagicCardDefinition cardDefinition;
    public final String name;
    public final String blockedName;
    public final ImageIcon icon;
    public final int index;
    public final String powerToughness;
    public final String text;
    public final Set<MagicAbility> abilityFlags;
    public final int damage;
    public final int position;
    public final boolean visible;
    public final boolean basic;
    public final boolean mana;
    public final boolean creature;
    public final boolean artifact;
    public final boolean enchantment;
    public final boolean root;
    public final boolean tapped;
    public final boolean canNotTap;
    public final boolean attacking;
    public final boolean blocking;
    public final boolean blockingInvalid;
    public final boolean lowered;
    public final MagicColor manaColor;
    public final List<PermanentViewerInfo> blockers;
    public final SortedSet<PermanentViewerInfo> linked;

    public PermanentViewerInfo(final MagicGame game,final MagicPermanent permanent) {
        this.permanent=permanent;
        cardDefinition=permanent.getCardDefinition();
        name=permanent.getName();
        icon=IconImages.getIcon(permanent);
        index=permanent.getCard().getImageIndex();
        powerToughness=getPowerToughness(permanent);
        abilityFlags=permanent.getAbilityFlags();
        text=getText(game,permanent,abilityFlags);
        damage=permanent.getDamage();
        position=getPosition(permanent);
        visible=permanent.getController()==game.getVisiblePlayer();
        basic=permanent.hasType(MagicType.Basic);
        mana=permanent.producesMana();
        creature=permanent.isCreature();
        attacking=permanent.isAttacking();
        blocking=permanent.isBlocking();
        blockingInvalid=permanent.getBlockedCreature().isInvalid();

        artifact=permanent.isEquipped() ||
            (permanent.isArtifact() && permanent.getEquippedCreature().isInvalid());

        enchantment=permanent.isEnchanted() ||
            (permanent.isEnchantment() && permanent.getEnchantedPermanent().isInvalid());

        root=permanent.getEnchantedPermanent().isInvalid() && permanent.getEquippedCreature().isInvalid();

        tapped=permanent.isTapped();

        canNotTap=!tapped && !permanent.canTap();

        lowered=isLowered(permanent);

        manaColor=getManaColor(permanent);
        blockers=getBlockers(game,permanent);
        linked=getLinked(game,permanent);

        blockedName = (blocking) ? permanent.getBlockedName() : permanent.getName() + permanent.getId();
    }

    private static boolean isLowered(final MagicPermanent permanent) {
        if (permanent.isAttacking()) {
            return true;
        } else if (permanent.getEquippedCreature().isValid()) {
            return isLowered(permanent.getEquippedCreature());
        } else if (permanent.getEnchantedPermanent().isValid()) {
            return isLowered(permanent.getEnchantedPermanent());
        } else {
            return false;
        }
    }

    private static String getPowerToughness(final MagicPermanent permanent) {
        if (permanent.isCreature()) {
            return permanent.getPowerToughness().toString();
        } else {
            return "";
        }
    }

    private static String getText(final MagicGame game,final MagicPermanent permanent,final Set<MagicAbility> abilityFlags) {
        final StringBuilder textBuffer=new StringBuilder();

        // States
        if (isTargeted(game,permanent)) {
            textBuffer.append("{O}");
        }
        if (permanent.isTapped()) {
            textBuffer.append(MagicPermanentState.Tapped.getText());
        } else if (!permanent.canTap()) {
            textBuffer.append("{SS}");
        }
        if (permanent.hasState(MagicPermanentState.DoesNotUntapDuringNext)) {
            textBuffer.append(MagicPermanentState.DoesNotUntapDuringNext.getText());
        }
        if (permanent.isRegenerated()) {
            textBuffer.append(MagicPermanentState.Regenerated.getText());
        }
        if (permanent.isBlocked()) {
            textBuffer.append(MagicPermanentState.Blocked.getText());
        }
        if (permanent.isToken()) {
            textBuffer.append("{t}");
        }

        // Colors
        for (final MagicColor color : MagicColor.values()) {
            if (permanent.hasColor(color)) {
                textBuffer.append(color.getManaType().getText());
            }
        }
        if (textBuffer.length()>0) {
            textBuffer.append(' ');
        }

        // Counters
        for (final MagicCounterType counterType : MagicCounterType.values()) {

            final int amount=permanent.getCounters(counterType);
            if (amount>0) {
                textBuffer.append(counterType.getText()).append(amount).append(' ');
            }
        }

        if (permanent.isCreature()) {
            // Damage
            if (permanent.getDamage()>0) {
                textBuffer.append("{D}").append(permanent.getDamage()).append(' ');
            }
            // Prevent damage.
            if (permanent.getPreventDamage()>0) {
                textBuffer.append("{P}").append(permanent.getPreventDamage()).append(' ');
            }
        }

        boolean first=true;

        // Sub types.
        if (permanent.hasAllCreatureTypes()) {
            if (first) {
                first = false;
                if (textBuffer.length() > 0) {
                    textBuffer.append('|');
                }
            } else {
                textBuffer.append(", ");
            }
            textBuffer.append("All creature types");
        } else {
            for (final MagicSubType subType : MagicSubType.values()) {
                if (permanent.hasSubType(subType)) {
                    if (first) {
                        first=false;
                        if (textBuffer.length()>0) {
                            textBuffer.append('|');
                        }
                    } else {
                        textBuffer.append(", ");
                    }
                    textBuffer.append(subType.toString());
                }
            }
        }

        /* Disable inclusion of ability text in text info as original ability
         * text is not retrievable from MagicAbility
        for (final MagicAbility ability : MagicAbility.values()) {
            if (abilityFlags.contains(ability)) {
                if (first) {
                    first=false;
                    if (textBuffer.length()>0) {
                        textBuffer.append('|');
                    }
                } else {
                    textBuffer.append(", ");
                }
                textBuffer.append(ability);
            }
        }
        */

        return textBuffer.toString();
    }

    private static int getPosition(final MagicPermanent permanent) {
        if (permanent.isCreature()) {
            return 2;
        } else if (permanent.isLand()) {
            return 1;
        } else if (permanent.isArtifact()) {
            return 3;
        } else {
            return 4;
        }
    }

    private static boolean isTargeted(final MagicGame game,final MagicPermanent permanent) {
        for (final MagicItemOnStack itemOnStack : game.getStack()) {
            if (itemOnStack.isTarget(permanent)) {
                return true;
            }
        }
        return false;
    }

    private static MagicColor getManaColor(final MagicPermanent permanent) {
        for (final MagicColor color : MagicColor.values()) {
            if (permanent.hasSubType(color.getLandSubType())) {
                return color;
            }
        }
        return MagicColor.Black;
    }

    private static List<PermanentViewerInfo> getBlockers(final MagicGame game,final MagicPermanent permanent) {
        final List<PermanentViewerInfo> blockers=new ArrayList<PermanentViewerInfo>();
        for (final MagicPermanent blocker : permanent.getBlockingCreatures()) {
            blockers.add(new PermanentViewerInfo(game,blocker));
        }
        return blockers;
    }

    private static SortedSet<PermanentViewerInfo> getLinked(final MagicGame game,final MagicPermanent permanent) {
        final SortedSet<PermanentViewerInfo> linked=new TreeSet<PermanentViewerInfo>(NAME_COMPARATOR);
        for (final MagicPermanent equipment : permanent.getEquipmentPermanents()) {
            linked.add(new PermanentViewerInfo(game,equipment));
            linked.addAll(getLinked(game, equipment));
        }
        for (final MagicPermanent aura : permanent.getAuraPermanents()) {
            linked.add(new PermanentViewerInfo(game,aura));
            linked.addAll(getLinked(game, aura));
        }
        return linked;
    }
}
