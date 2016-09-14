def NEG_TARGET_CREATURE_WITH_PLUSONE_COUNTER = MagicTargetChoice.Negative("target creature with a +1/+1 counter on it");


[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Control"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source, "{U}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE_WITH_PLUSONE_COUNTER,
                MagicExileTargetPicker.create(),
                this,
                "PN gains control of target creature with a +1/+1 counter on it\$ for as long as SN remains on the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new AddStaticAction(
                    event.getPermanent(),
                    MagicStatic.ControlAsLongAsSourceIsOnBattlefield(
                        event.getPlayer(),
                        it
                    )
                ));
            });
        }
    }
]
