[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Exile"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{1}{B}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_CARD_FROM_OPPONENTS_GRAVEYARD,
                MagicGraveyardTargetPicker.ExileOpp,
                this,
                "Exile target card\$ from an opponent's graveyard. " +
                "If no cards are in that graveyard, flip SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                game.doAction(new ShiftCardAction(
                    it,
                    MagicLocationType.Graveyard,
                    MagicLocationType.Exile
                ));
                if (it.getOwner().getGraveyard().isEmpty()) {
                    game.doAction(new FlipAction(event.getPermanent()));
                }
            });
        }
    }
]
