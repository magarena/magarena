def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.logAppendMessage(event.getPlayer(),"Return this to your hand? (Yes)");
        game.doAction(new RemoveFromPlayAction(event.getPermanent(),MagicLocationType.OwnersHand));
    } else {
        game.logAppendMessage(event.getPlayer(), "Return this to your hand? (No)");
    }
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Bounce"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}{C}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ exiles the top card of his or her library. If it's a land card, PN may return SN to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                for (final MagicCard card : it.getLibrary().getCardsFromTop(1)) {
                    game.doAction(new ShiftCardAction(card, MagicLocationType.OwnersLibrary, MagicLocationType.Exile));
                    game.logAppendMessage(event.getPlayer(), MagicMessage.format("%s is exiled.", card));
                    if (card.hasType(MagicType.Land)) {
                        game.addEvent(new MagicEvent(
                            event.getSource(),
                            event.getPlayer(),
                            new MagicMayChoice("Return this to your hand?"),
                            action,
                            ""
                        ));
                    }
                }
            });
        }
    }
]
