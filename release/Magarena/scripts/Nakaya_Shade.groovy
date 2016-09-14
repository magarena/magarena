[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{B}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                source.getOpponent(),
                new MagicMayChoice(
                    "Pay {2}?",
                    new MagicPayManaCostChoice(MagicManaCost.create("{2}"))
                ),
                this,
                "PN may\$ pay {2}\$. If PN doesn't, SN gets +1/+1 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.doAction(new ChangeTurnPTAction(event.getPermanent(), 1, 1));
            }
        }
    }
]
