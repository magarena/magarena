[
    new MagicAdditionalCost() {
        @Override
        public MagicEvent getEvent(final MagicSource source) {
            return new MagicPayLifeEvent(source, 3);
        }
    }
]
