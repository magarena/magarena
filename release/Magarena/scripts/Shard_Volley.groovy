[
    new MagicAdditionalCost() {
        @Override
        public MagicEvent getEvent(final MagicSource source) {
            return new MagicSacrificePermanentEvent(source, MagicTargetChoice.SACRIFICE_LAND);
        }
    }
]
