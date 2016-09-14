[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Control"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Each player gains control of all creatures he or she owns."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.getAPNAP() each {
                final MagicPlayer player ->
                CREATURE_YOU_OWN.filter(player) each {
                    final MagicPermanent permanent ->
                    game.doAction(new GainControlAction(player, permanent));
                }
            }
        }
    }
]
