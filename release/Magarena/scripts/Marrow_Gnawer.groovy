def choice = new MagicTargetChoice("a Rat to sacrifice"); 

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicSacrificePermanentEvent(source, choice)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN puts X 1/1 black Rat creature tokens onto the battlefield, where X is the number of Rats PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getNrOfPermanents(MagicSubType.Rat);
            game.doAction(new PlayTokensAction(event.getPlayer(),TokenCardDefinitions.get("1/1 black Rat creature token"),amount));
        }
    }
]
