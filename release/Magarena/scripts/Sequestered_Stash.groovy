def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        event.processTargetCard(game, {
            game.doAction(new ShiftCardAction(it, MagicLocationType.Graveyard, MagicLocationType.TopOfOwnersLibrary));
        });
    }
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Flash),
        "Put on top"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "4"),
                new MagicTapEvent(source),
                new MagicSacrificeEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put the top five cards of PN's library into PN's graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MillLibraryAction(event.getPlayer(), 5));
            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicMayChoice(
                    card(MagicType.Artifact).from(MagicTargetType.Graveyard),
                    "an artifact card from your graveyard"
                ),
                action,
                "PN may\$ put an artifact card from PN's graveyard\$ on top of PN's library."
            ));
        }
    }
]

