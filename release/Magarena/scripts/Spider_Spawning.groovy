[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN puts a 1/2 green Spider creature token with reach onto the battlefield "+
                "for each creature card in PN's graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("1/2 green Spider creature token with reach"),
                CREATURE_CARD_FROM_GRAVEYARD.filter(event).size()
            ));
        }
    }
]
