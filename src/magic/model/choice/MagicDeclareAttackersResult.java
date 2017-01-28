package magic.model.choice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import magic.model.MagicGame;
import magic.model.MagicMappable;
import magic.model.MagicPermanent;
import magic.model.MurmurHash3;

@SuppressWarnings("serial")
public class MagicDeclareAttackersResult extends ArrayList<MagicPermanent> implements MagicMappable<MagicDeclareAttackersResult> {

    MagicDeclareAttackersResult() {}

    MagicDeclareAttackersResult(final MagicPermanent[] attackers,final int length) {
        for (int index=0;index<length;index++) {
            add(attackers[index]);
        }
    }

    void addCreatures(final Collection<MagicCombatCreature> creatures) {
        for (final MagicCombatCreature creature : creatures) {
            add(creature.permanent);
        }
    }

    @Override
    public MagicDeclareAttackersResult map(final MagicGame game) {
        final MagicDeclareAttackersResult result=new MagicDeclareAttackersResult();
        for (final MagicPermanent permanent : this) {
            result.add(permanent.map(game));
        }
        return result;
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }

    @Override
    public long getId() {
        final long[] keys = new long[size()];
        int idx = 0;
        for (final MagicPermanent permanent : this) {
            keys[idx] = permanent.getStateId();
            idx++;
        }
        return MurmurHash3.hash(keys);
    }
}
