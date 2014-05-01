[
    new MagicAdditionalCost() {
        @Override
        public MagicEvent getEvent(final MagicSource source) {
            return new MagicDiscardEvent(source, 1);
        }
    }
]
