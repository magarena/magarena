package magic.model;

import magic.model.stack.MagicItemOnStack;
import magic.model.event.MagicChainEventFactory;
import magic.model.action.MagicPermanentAction;

import java.util.List;
import java.util.ArrayList;

public class MagicTuple implements MagicCopyable {

    final MagicCopyable[] tuple;

    public MagicTuple(final int x, final int y, final MagicCopyable... args) {
        tuple = new MagicCopyable[args.length + 2];
        tuple[0] = new MagicInteger(x);
        tuple[1] = new MagicInteger(y);
        System.arraycopy(args, 0, tuple, 2, args.length);
    }

    public MagicTuple(final int n, final MagicCopyable... args) {
        tuple = new MagicCopyable[args.length + 1];
        tuple[0] = new MagicInteger(n);
        System.arraycopy(args, 0, tuple, 1, args.length);
    }

    public MagicTuple(final MagicCopyable... args) {
        tuple = args;
    }

    public MagicTuple(final List<? extends MagicCopyable> args) {
        this(args.toArray(new MagicCopyable[0]));
    }

    @Override
    public MagicTuple copy(final MagicCopyMap copyMap) {
        final MagicCopyable[] args = new MagicCopyable[tuple.length];
        for (int i = 0; i < args.length; i++) {
            args[i] = copyMap.copy(tuple[i]);
        }
        return new MagicTuple(args);
    }

    @Override
    public int hashCode() {
        final long[] keys = new long[tuple.length];
        for (int i = 0; i < tuple.length; i++) {
            keys[i] = MagicObjectImpl.getStateId(tuple[i]);
        }
        return (int)(MurmurHash3.hash(keys) >>> 32);
    }

    public final MagicItemOnStack getItemOnStack(int i) {
        return (MagicItemOnStack)tuple[i];
    }

    public final MagicLocationType getLocationType(int i) {
        return (MagicLocationType)tuple[i];
    }

    public final MagicCounterType getCounterType(int i) {
        return (MagicCounterType)tuple[i];
    }

    public final MagicChainEventFactory getChainEventFactory(int i) {
        return (MagicChainEventFactory)tuple[i];
    }

    public final MagicPlayer getPlayer(int i) {
        return (MagicPlayer)tuple[i];
    }

    public final MagicPermanent getPermanent(int i) {
        return (MagicPermanent)tuple[i];
    }

    public final MagicCard getCard(int i) {
        return (MagicCard)tuple[i];
    }

    public final int getInt(int i) {
        return ((MagicInteger)tuple[i]).value;
    }

    public final MagicPermanentAction getMod(int i) {
        return (MagicPermanentAction)tuple[i];
    }

    public final List<MagicPermanentAction> getMods() {
        List<MagicPermanentAction> mods = new ArrayList<>(tuple.length);
        for (final MagicCopyable c : tuple) {
            mods.add((MagicPermanentAction)c);
        }
        return mods;
    }
}
