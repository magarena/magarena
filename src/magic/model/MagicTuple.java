package magic.model;

import magic.model.stack.MagicItemOnStack;
import magic.model.event.MagicChainEventFactory;

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

    @Override
    public MagicTuple copy(final MagicCopyMap copyMap) {
        final MagicCopyable[] args = new MagicCopyable[tuple.length];
        for (int i = 0; i < args.length; i++) {
            args[i] = copyMap.copy(tuple[i]);
        }
        return new MagicTuple(args);
    }

    @Override
    public long getStateId() {
        final long[] keys = new long[tuple.length];
        for (int i = 0; i < tuple.length; i++) {
            keys[i] = MagicObjectImpl.getStateId(tuple[i]);
        }
        return MurmurHash3.hash(keys);
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

    public final int getInt(int i) {
        return ((MagicInteger)tuple[i]).value;
    }
}
