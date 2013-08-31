[
    new MagicManaActivation(MagicManaType.getList("{1}")) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent perm) {
            return [
                new MagicTapEvent(perm),
                new MagicGainLifeEvent(perm, 1)
            ];
        }
    }
]
