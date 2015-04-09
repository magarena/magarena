[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Gain Control"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicRemoveCounterEvent(source,MagicCounterType.Ki,1)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "PN gains control of target creature\$ until end of turn"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicGainControlAction(event.getPlayer(),it,MagicStatic.UntilEOT));
            });
        }
    }
]
