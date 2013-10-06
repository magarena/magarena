[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Flash),
        "Token"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostSacrificeEvent(source, "{2}{U}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put a 3/3 green Centaur creature token onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(event.getPlayer(), TokenCardDefinitions.get("Centaur3")));
        }
    }
]
