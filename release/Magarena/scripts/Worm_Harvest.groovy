[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN puts a 1/1 black and green Worm creature token onto the battlefield for each land card in his or her graveyard."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = game.filterCards(
                event.getPlayer(),
                LAND_CARD_FROM_YOUR_GRAVEYARD
            ).size();
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                TokenCardDefinitions.get("1/1 black and green Worm creature token"),
                amount
            ));
        }
    }
]
