[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Exile"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicRemoveCounterEvent(source,MagicCounterType.Charge,1)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_LAND,
                MagicExileTargetPicker.create(),
                this,
                "Exile target land\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicExileUntilThisLeavesPlayAction(
                        event.getPermanent(),
                        permanent
                    ));
                }
            });
        }
    }
]
