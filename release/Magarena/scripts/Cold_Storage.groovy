[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Exile"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{3}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_CREATURE_YOU_CONTROL,
                this,
                "Exile target creature\$ you control."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicExileLinkAction(
                    event.getPermanent(),
                    it
                )); 
            });
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Return"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Return each creature card exiled with SN to the battlefield under your control."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicReturnLinkedExileAction(
                event.getPermanent(),
                MagicLocationType.Play,
                event.getPlayer()
            ));
        }
    }
]
