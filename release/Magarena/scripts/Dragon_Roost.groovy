[
    new MagicPermanentActivation(
        [MagicConditionFactory.ManaCost("{5}{R}{R}")],
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{5}{R}{R}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put a 5/5 red Dragon creature token with flying onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(event.getPlayer(),TokenCardDefinitions.get("Dragon5")));
        }
    }
]
