[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN puts a 1/1 green Insect creature token onto the battlefield for each Forest PN controls. "+
                "Shuffle SN into its owner's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer()
            game.doAction(new PlayTokensAction(
                player,
                TokenCardDefinitions.get("1/1 green Insect creature token"),
                player.getNrOfPermanents(FOREST)
            ));
            game.doAction(new ChangeCardDestinationAction(event.getCardOnStack(),MagicLocationType.OwnersLibrary));
        }
    }
]
