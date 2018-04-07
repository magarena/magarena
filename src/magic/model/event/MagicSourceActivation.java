package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicMappable;
import magic.model.MagicPlayer;
import magic.model.MagicSource;

public class MagicSourceActivation<T extends MagicSource & MagicMappable<T> & Comparable<T>> implements Comparable<MagicSourceActivation<T>> {
    public final T source;
    public final MagicActivation<T> activation;

    private MagicSourceActivation(final T aSource, final MagicActivation<T> aActivation) {
        source = aSource;
        activation = aActivation;
    }

    public static <S extends MagicSource & MagicMappable<S> & Comparable<S>> MagicSourceActivation<S> create(final S aSource, final MagicActivation<S> aActivation) {
        return new MagicSourceActivation<>(aSource, aActivation);
    }

    public static <S extends MagicSource & MagicMappable<S> & Comparable<S>> MagicSourceActivation<S> create(final MagicGame game, final MagicSourceActivation<S> aSourceActivation) {
        return new MagicSourceActivation<>(aSourceActivation.source.map(game), aSourceActivation.activation);
    }

    public final boolean canPlay(final MagicGame game, final MagicPlayer player, final boolean useHints) {
        return activation.canPlay(game, player, source, useHints);
    }

    public Iterable<? extends MagicEvent> getCostEvent() {
        return activation.getCostEvent(source);
    }

    public MagicEvent getEvent() {
        return activation.getEvent(source);
    }

    public boolean usesStack() {
        return activation.usesStack();
    }

    public boolean isIndependent() {
        return activation.getActivationHints().isIndependent();
    }

    public void changeActivationPriority() {
        activation.changeActivationPriority(source.getController());
    }

    @Override
    public int compareTo(final MagicSourceActivation<T> other) {
        int c1 = activation.compareTo(other.activation);
        if (c1 != 0) {
            return c1;
        } else {
            return source.compareTo(other.source);
        }
    }
}
