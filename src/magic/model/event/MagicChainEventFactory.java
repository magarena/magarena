package magic.model.event;

import magic.model.MagicSource;
import magic.model.MagicCopyable;
import magic.model.choice.MagicTargetChoice;

public abstract class MagicChainEventFactory implements MagicCopyable {

    abstract public MagicEvent getEvent(final MagicSource source, final MagicTargetChoice tchoice);
    public MagicEvent getEvent(final MagicEvent event) {
        return getEvent(event.getSource(), event.getTargetChoice());
    }

    public static final MagicChainEventFactory Tap = new MagicChainEventFactory() {
        @Override
        public MagicEvent getEvent(final MagicSource source, final MagicTargetChoice tchoice) {
            return new MagicTapPermanentEvent(source, tchoice);
        }
    };

    public static final MagicChainEventFactory Untap = new MagicChainEventFactory() {
        @Override
        public MagicEvent getEvent(final MagicSource source, final MagicTargetChoice tchoice) {
            return new MagicUntapPermanentEvent(source, tchoice);
        }
    };

    public static final MagicChainEventFactory Sac = new MagicChainEventFactory() {
        @Override
        public MagicEvent getEvent(final MagicSource source, final MagicTargetChoice tchoice) {
            return new MagicSacrificePermanentEvent(source, tchoice);
        }
    };

    public static final MagicChainEventFactory Bounce = new MagicChainEventFactory() {
        @Override
        public MagicEvent getEvent(final MagicSource source, final MagicTargetChoice tchoice) {
            return new MagicBounceChosenPermanentEvent(source, tchoice);
        }
    };

    public static final MagicChainEventFactory ExileCard = new MagicChainEventFactory() {
        @Override
        public MagicEvent getEvent(final MagicSource source, final MagicTargetChoice tchoice) {
            return new MagicExileCardEvent(source, tchoice);
        }
    };

    public static final MagicChainEventFactory ExilePerm = new MagicChainEventFactory() {
        @Override
        public MagicEvent getEvent(final MagicSource source, final MagicTargetChoice tchoice) {
            return new MagicExileChosenPermanentEvent(source, tchoice);
        }
    };
}
