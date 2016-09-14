[
    new MagicManaActivation(MagicManaType.getList("{C}")) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent perm) {
            return [
                new MagicTapEvent(perm),
                new MagicGainLifeEvent(perm, 1)
            ];
        }
    }
]
