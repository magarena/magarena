package magic.model.choice;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import magic.ai.ArtificialScoringSystem;
import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;

public class MagicCombatCreature {

    public final MagicPermanent permanent;
    public final int score;
    public final int power;
    public final int lethalDamage;
    public final boolean normalDamage;
    public MagicCombatCreature[] candidateBlockers = new MagicCombatCreature[0];
    int attackerScore;
    private final Set<MagicAbility> flags;

    MagicCombatCreature(final MagicPermanent permanent) {
        this.permanent = permanent;
        this.score = permanent.getScore();
        final MagicPowerToughness pt = permanent.getPowerToughness();
        lethalDamage = permanent.getLethalDamage(pt.toughness());
        flags = permanent.getAbilityFlags();
        power = flags.contains(MagicAbility.DoubleStrike) ?
            pt.getPositivePower() * 2 :
            pt.getPositivePower();
        normalDamage =
            !flags.contains(MagicAbility.Deathtouch) &&
            !flags.contains(MagicAbility.Wither) &&
            !flags.contains(MagicAbility.Infect);
    }

    MagicCombatCreature(final MagicGame game,final MagicCombatCreature creature) {
        permanent=creature.permanent.map(game);
        score=creature.score;
        power=creature.power;
        lethalDamage=creature.lethalDamage;
        flags=creature.flags;
        normalDamage=creature.normalDamage;
    }

    public boolean hasAbility(final MagicAbility ability) {
        return flags.contains(ability);
    }

    void setAttacker(final MagicGame game,final Set<MagicCombatCreature> blockers) {
        final SortedSet<MagicCombatCreature> candidateBlockersSet =
                new TreeSet<>(new BlockerComparator(this));
        for (final MagicCombatCreature blocker : blockers) {
            if (blocker.permanent.canBlock(permanent)) {
                candidateBlockersSet.add(blocker);
            }
        }
        candidateBlockers=new MagicCombatCreature[candidateBlockersSet.size()];
        candidateBlockersSet.toArray(candidateBlockers);

        attackerScore=ArtificialScoringSystem.getAttackerScore(this);
    }

    public String getName() {
        return permanent.getName();
    }

    @Override
    public String toString() {
        final StringBuilder builder=new StringBuilder();
        builder.append(permanent.getName());
        builder.append('(')
               .append(power).append(',')
               .append(lethalDamage).append(',')
               .append(attackerScore).append(',')
               .append(score).append(')');
        if (candidateBlockers.length > 0) {
            builder.append(" = ").append(Arrays.toString(candidateBlockers));
        }
        return builder.toString();
    }

    private static final class BlockerComparator implements Comparator<MagicCombatCreature> {

        private final MagicCombatCreature attacker;

        public BlockerComparator(final MagicCombatCreature attacker) {
            this.attacker=attacker;
        }

        @Override
        public int compare(final MagicCombatCreature blocker1,final MagicCombatCreature blocker2) {
            int ldif=blocker1.lethalDamage-blocker2.lethalDamage;
            if (attacker.normalDamage) {
                final boolean front=blocker1.lethalDamage>attacker.power;
                if (front!=blocker2.lethalDamage>attacker.power) {
                    return front?-1:1;
                }
                if (front) {
                    ldif=-ldif;
                }
            }
            if (ldif!=0) {
                return ldif;
            }
            final int pdif=blocker1.power-blocker2.power;
            if (pdif!=0) {
                return pdif;
            }
            final int sdif=blocker1.score-blocker2.score;
            if (sdif!=0) {
                return sdif;
            }
            return blocker1.permanent.compareTo(blocker2.permanent);
        }
    }
}
