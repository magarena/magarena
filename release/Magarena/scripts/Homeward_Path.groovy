[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Control"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
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
            for (final MagicPlayer player : game.getAPNAP()) {
                final List<MagicPermanent> permanents = game.filterPermanents(player, CREATURE_YOU_OWN);
                for (final MagicPermanent permanent : permanents) {
                    game.doAction(new MagicGainControlAction(player, permanent));
                }
            }
        }
    }
]
