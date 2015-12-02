package magic.model;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;

@SuppressWarnings("serial")
public class MagicCopyMap extends HashMap<MagicCopyable,MagicCopyable> {

    @SuppressWarnings("unchecked")
    public <E extends MagicCopyable> E copy(final E source) {
        if (source == null) {
            return source;
        }
        E target=(E)get(source);
        if (target==null) {
            target=(E)source.copy(this);
            put(source,target);
        }
        return target;
    }

    private Object copyObject(final Object source) {
        if (source != null && source instanceof MagicCopyable) {
            return copy((MagicCopyable)source);
        } else {
            assert source == null ||
                   source instanceof Enum ||
                   source instanceof Number ||
                   source instanceof String :
                   source.getClass().getName() + " not copied";
            return source;
        }
    }

    @SuppressWarnings("unchecked")
    public <E> E[] copyObjects(final E[] sources,final Class<E> clazz) {
        if (sources==null||sources.length==0) {
            return sources;
        }
        final E[] targets=(E[])Array.newInstance(clazz,sources.length);
        for (int index=0;index<targets.length;index++) {
            targets[index]=(E)copyObject(sources[index]);
        }
        return targets;
    }

    @SuppressWarnings("unchecked")
    public <E> void copyCollection(final Collection<E> sourceCollection,final Collection<E> targetCollection) {
        for (final E object : sourceCollection) {
            targetCollection.add((E)copyObject(object));
        }
    }
}
