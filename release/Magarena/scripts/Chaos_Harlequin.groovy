[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{R}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Exile the top card of PN's library. If that card is a land card, SN gets -4/-0 until end of turn. " +
                "Otherwise, SN gets +2/+0 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicCard card : event.getPlayer().getLibrary().getCardsFromTop(1)) {
                game.doAction(new ShiftCardAction(
                    card,
                    MagicLocationType.OwnersLibrary,
                    MagicLocationType.Exile
                ));
                game.doAction(new ChangeTurnPTAction(
                    event.getPermanent(),
                    card.hasType(MagicType.Land) ? -4 : +2,
                    0
                ));
            }
        }
    }
]
