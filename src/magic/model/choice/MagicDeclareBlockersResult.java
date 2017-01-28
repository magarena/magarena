package magic.model.choice;

import java.util.Arrays;
import java.util.LinkedList;

import magic.model.MagicGame;
import magic.model.MagicMappable;
import magic.model.MurmurHash3;
import magic.model.score.MagicScoreResult;

/** First creature in array is the attacker, the other creatures are blockers. */
@SuppressWarnings("serial")
public class MagicDeclareBlockersResult extends LinkedList<MagicCombatCreature[]> implements MagicMappable<MagicDeclareBlockersResult>, MagicScoreResult {

    private final int position;
    private final int score;

    MagicDeclareBlockersResult(final int position,final int score) {
        this.position=position;
        this.score=score;
    }

    MagicDeclareBlockersResult(final MagicDeclareBlockersResult result,final int position,final int score) {
        this(position,score);
        for (final MagicCombatCreature[] creatures : result) {
            add(Arrays.copyOf(creatures,creatures.length));
        }
    }

    @Override
    public MagicDeclareBlockersResult map(final MagicGame game) {
        final MagicDeclareBlockersResult result=new MagicDeclareBlockersResult(position,score);
        for (final MagicCombatCreature[] creatures : this) {
            final int size=creatures.length;
            final MagicCombatCreature[] mappedCreatures=new MagicCombatCreature[size];
            for (int index=size-1;index>=0;index--) {
                mappedCreatures[index]=new MagicCombatCreature(game,creatures[index]);
            }
            result.add(mappedCreatures);
        }
        return result;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        final StringBuilder builder=new StringBuilder();
        builder.append('S');
        builder.append(score);
        for (final MagicCombatCreature[] creatures : this) {
            if (creatures.length>1) {
                builder.append(' ');
                builder.append(creatures[0].getName());
                builder.append('=');
                builder.append(creatures[1].getName());
                builder.append(creatures.length > 2 ? "+" + (creatures.length-2) : "");
            }
        }
        return builder.toString();
    }

    @Override
    public long getId() {
        int size = 0;
        for (final MagicCombatCreature[] creatures : this) {
            size += creatures.length;
        }
        final long[] keys = new long[size];
        int idx = 0;
        for (final MagicCombatCreature[] creatures : this) {
            for (final MagicCombatCreature creature : creatures) {
                keys[idx] = creature.permanent.getStateId();
                idx++;
            }
        }
        return MurmurHash3.hash(keys);
    }
}
