[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump,true),
        "Pump"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{3}{G}")];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature gets +X/+X until end of turn, " +
                "where X is the number of creatures PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = event.getPlayer().getNrOfPermanents(MagicType.Creature);
                game.doAction(new ChangeTurnPTAction(it,amount,amount));
            });
        }
    }
]
