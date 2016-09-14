[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Return"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}"),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN returns all cards exiled with SN to the battlefield under his or her control."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ReturnLinkedExileAction(
                event.getPermanent(),
                MagicLocationType.Battlefield,
                event.getPlayer()
            ));
        }
    }
]
