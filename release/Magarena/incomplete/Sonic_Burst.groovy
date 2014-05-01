[
    new MagicAdditionalCost() {
        @Override
        public MagicEvent getEvent(final MagicSource source) {
            return MagicDiscardEvent.Random(source, 1);
        }
    }
]
