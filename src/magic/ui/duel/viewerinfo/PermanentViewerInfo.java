package magic.ui.duel.viewerinfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;

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

    public final long magicCardId;
    public final MagicPermanent permanent;
    public final MagicCardDefinition cardDefinition;
    public final String name;
    public final String blockedName;
    public final String powerToughness;
    public final Set<MagicAbility> abilityFlags;
    public final int damage;
    public final int shield;
    public final int position;
    public final boolean creature;
    public final boolean root;
    public final boolean tapped;
    public final boolean canNotTap;
    public final boolean attacking;
    public final boolean blocking;
    public final boolean blockingInvalid;
    public final boolean lowered;
    public final List<PermanentViewerInfo> blockers;
    public final SortedSet<PermanentViewerInfo> linked;

    PermanentViewerInfo(final MagicGame game,final MagicPermanent permanent) {
        this.permanent=permanent;
        cardDefinition=permanent.getCardDefinition();
        name=permanent.getName();
        powerToughness=getPowerToughness(permanent);
        abilityFlags=permanent.getAbilityFlags();
        damage=permanent.getDamage();
        shield=permanent.getPreventDamage();
        position=getPosition(permanent);
        creature=permanent.isCreature();
        attacking=permanent.isAttacking();
        blocking=permanent.isBlocking();
        blockingInvalid=permanent.getBlockedCreature().isInvalid();
        magicCardId = permanent.getCard().getId();

        root=permanent.getEnchantedPermanent().isInvalid() && permanent.getEquippedCreature().isInvalid();

        tapped=permanent.isTapped();

        canNotTap=!tapped && !permanent.canTap();

        lowered=isLowered(permanent);

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

    private static List<PermanentViewerInfo> getBlockers(final MagicGame game,final MagicPermanent permanent) {
        final List<PermanentViewerInfo> blockers= new ArrayList<>();
        for (final MagicPermanent blocker : permanent.getBlockingCreatures()) {
            blockers.add(new PermanentViewerInfo(game,blocker));
        }
        return blockers;
    }

    private static SortedSet<PermanentViewerInfo> getLinked(final MagicGame game,final MagicPermanent permanent) {
        final SortedSet<PermanentViewerInfo> linked= new TreeSet<>(NAME_COMPARATOR);
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

    public boolean isEqualTo(CardViewerInfo cardInfo) {
        return magicCardId == cardInfo.getId();
    }
}
