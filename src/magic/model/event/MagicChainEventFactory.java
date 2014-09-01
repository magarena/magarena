package magic.model.event;

import magic.model.MagicSource;

interface MagicChainEventFactory {

    public MagicEvent getEvent(final MagicEvent event);

    public static final MagicChainEventFactory Tap = new MagicChainEventFactory() {
        public MagicEvent getEvent(final MagicEvent event) {
            return new MagicTapPermanentEvent(event.getSource(), event.getTargetChoice());
        }
    };
    
    public static final MagicChainEventFactory Untap = new MagicChainEventFactory() {
        public MagicEvent getEvent(final MagicEvent event) {
            return new MagicUntapPermanentEvent(event.getSource(), event.getTargetChoice());
        }
    };
    
    public static final MagicChainEventFactory Sac = new MagicChainEventFactory() {
        public MagicEvent getEvent(final MagicEvent event) {
            return new MagicSacrificePermanentEvent(event.getSource(), event.getTargetChoice());
        }
    };
}
