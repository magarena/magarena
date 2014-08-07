[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Control"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "Gain control of target creature\$ for as long as PN controls SN. " + 
                "When Merieke Ri Berit leaves the battlefield or becomes untapped, destroy that creature. It can't be regenerated."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicAddStaticAction(
                    event.getPermanent(), 
                    MagicStatic.ControlAsLongAsYouControlSource(
                        event.getPlayer(),
                        it
                    )
                ));
                //TODO: When Merieke Ri Berit leaves the battlefield or becomes untapped, destroy that creature. It can't be regenerated.
            });
        }
    }
]
