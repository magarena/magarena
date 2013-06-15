[
    new MagicWeakenCreatureActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "-1/-1"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicSacrificeEvent(source)];
        }
    }
]
