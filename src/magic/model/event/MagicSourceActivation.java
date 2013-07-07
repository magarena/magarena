package magic.model.event;

import magic.model.MagicSource;
import magic.model.MagicGame;
import magic.model.MagicPlayer;

public class MagicSourceActivation<T extends MagicSource> implements Comparable<MagicSourceActivation<T>> {
    public final T source;
    public final MagicActivation<T> activation;

    private MagicSourceActivation(final T aSource, final MagicActivation<T> aActivation) {
        source = aSource;
        activation = aActivation;
    }

    public static <S extends MagicSource> MagicSourceActivation<S> create(final S aSource, final MagicActivation<S> aActivation) {
        return new MagicSourceActivation<S>(aSource, aActivation);
    }
    
    @SuppressWarnings("unchecked")
    public static <S extends MagicSource> MagicSourceActivation<S> create(final MagicGame game, final MagicSourceActivation<S> aSourceActivation) {
        return new MagicSourceActivation<S>((S)aSourceActivation.source.map(game), aSourceActivation.activation);
    }
    
    public final boolean canPlay(final MagicGame game, final MagicPlayer player, final boolean useHints) {
        return activation.canPlay(game, player, source, useHints);
    }
    
    public MagicEvent[] getCostEvent() {
        return activation.getCostEvent(source);
    }
    
    public MagicEvent getEvent() {
        return activation.getEvent(source);
    }
    
    @Override
    public int compareTo(final MagicSourceActivation other) {
        return activation.compareTo(other.activation);
    }
}
