[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}{W}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN gets +0/+X until end of turn, where X is the number of cards in PN's hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int X = event.getPlayer().getHandSize();
            game.doAction(new ChangeTurnPTAction(event.getPermanent(),0,X));
        }
    }
]
