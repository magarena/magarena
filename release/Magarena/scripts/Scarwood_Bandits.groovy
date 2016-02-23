def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isNo()) {
        game.doAction(new AddStaticAction(
            event.getPermanent(),
            MagicStatic.ControlAsLongAsSourceIsOnBattlefield(
                event.getPermanent().getController(),
                event.getRefPermanent()
            )
        ));
    }
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Control"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source, "{2}{G}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_ARTIFACT,
                MagicExileTargetPicker.create(),
                this,
                "Unless an opponent pays {2}, PN gains control of target artifact\$ for as long as SN remains on the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it.getController(),
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{2}"))
                    ),
                    it,
                    action,
                    "PN may\$ pay {2}."
                ));
            });
        }
    }
]
