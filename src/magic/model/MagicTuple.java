package magic.model;

import magic.model.stack.MagicItemOnStack;

public class MagicTuple implements MagicCopyable {

    final MagicCopyable[] tuple;

    public MagicTuple(final int n, final MagicCopyable... args) {
        tuple = new MagicCopyable[args.length + 1];
        tuple[0] = new MagicInteger(n);
        for (int i = 1; i < tuple.length; i++) {
            tuple[i] = args[i - 1];
        }
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

    public final int getInt(int i) {
        return ((MagicInteger)tuple[i]).value;
    }
}
