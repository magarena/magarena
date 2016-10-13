def winAct = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new ChangeCardDestinationAction(event.getCardOnStack(), MagicLocationType.OwnersHand))
    game.doAction(new MoveCardAction(event.getCardOnStack()));
}

def loseAct = {
    final MagicGame game, final MagicEvent event ->
    final MagicLocationType oldLocation = MagicLocationType.values()[event.getRefInt()];
    game.doAction(new ChangeCardDestinationAction(event.getCardOnStack(), oldLocation))
    game.doAction(new MoveCardAction(event.getCardOnStack()));
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN creates two 1/1 red Elemental creature tokens. "+
                "Then PN flips a coin. If PN wins the flip, return SN to its owner's hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("1/1 red Elemental creature token"),
                2
            ));
            final MagicCardOnStack spell = event.getCardOnStack();
            final MagicLocationType oldLocation = spell.getMoveLocation();

            //prevent auto move to moveLocation after executeEvent
            spell.setMoveLocation(MagicLocationType.Battlefield);

            game.addEvent(new MagicCoinFlipEvent(
                event,
                oldLocation.ordinal(),
                winAct,
                loseAct
            ));
        }
    }
]
