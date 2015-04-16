[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Flip"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}"), new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                new MagicCoinFlipChoice(),
                this,
                "PN flips a coin.\$ If PN wins the flip, PN puts a 5/5 colorless Djinn artifact creature token with flying onto the battlefield." +
                " If PN loses the flip, SN deals 5 damage to PN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final Boolean heads = event.isMode(1) 
            game.addEvent(new MagicCoinFlipEvent(
                event.getSource(),
                heads,
                player,
                new PlayTokenAction(player,TokenCardDefinitions.get("5/5 colorless Djinn artifact creature token with flying")),
                new DealDamageAction(event.getPermanent(),player,5)
            ));
        }
    }
]
