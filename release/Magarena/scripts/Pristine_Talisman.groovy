[
    new MagicManaActivation(MagicManaType.getList("{1}"), 0) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent perm) {
            return [
                new MagicTapEvent(perm),
                new MagicGainLifeEvent(perm, 1)
            ];
        }
    }
]
