def NEG_TARGET_LEGENDARY_CREATURE = MagicTargetChoice.Negative("target legendary creature")

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Control"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_LEGENDARY_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "PN gains control of target legendary creature\$ for as long as PN controls SN and SN remains tapped."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new AddStaticAction(
                    event.getPermanent(),
                    MagicStatic.ControlAsLongAsYouControlSourceAndSourceIsTapped(
                        event.getPlayer(),
                        it
                    )
                ));
            });
        }
    }
]
