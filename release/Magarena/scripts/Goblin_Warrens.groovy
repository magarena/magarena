[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Tokens"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}{R}"),
                new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_GOBLIN),
		new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_GOBLIN)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put 3 1/1 red Goblin creature tokens onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicPlayTokensAction(
                event.getPlayer(),
                TokenCardDefinitions.get("1/1 red Goblin creature token"),
                3
            ));
        }
    }
]
