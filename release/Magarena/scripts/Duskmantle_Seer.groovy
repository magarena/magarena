[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Each player reveals the top card of his or her library, "+
                "loses life equal to that card's converted mana cost, then puts it into his or her hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            //Each step should be performed simultaneously
            for (final MagicPlayer player : game.getAPNAP()) {
                for (final MagicCard card : player.getLibrary().getCardsFromTop(1)) {
                    game.doAction(new RevealAction(card));
                    game.doAction(new ChangeLifeAction(player, -card.getConvertedCost()));
                    game.doAction(new RemoveCardAction(card, MagicLocationType.OwnersLibrary));
                    game.doAction(new MoveCardAction(
                        card,
                        MagicLocationType.OwnersLibrary,
                        MagicLocationType.OwnersHand
                    ));
                }
            }
        }
    }
]
