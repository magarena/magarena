[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Tokens"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{3}"),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN puts a 1/1 colorless Myr artifact creature token onto the battlefield for each charge counter on SN. "+ "("+source.getCounters(MagicCounterType.Charge)+")"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount=event.getPermanent().getCounters(MagicCounterType.Charge);
            game.doAction(new PlayTokensAction(
                    event.getPlayer(),
                    TokenCardDefinitions.get("1/1 colorless Myr artifact creature token"),
                    amount
                ));
        }
    }
]
