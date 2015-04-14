[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Reveal the top card of PN's library and put that card into PN's hand. PN loses life equal to its converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicCard card : event.getPlayer().getLibrary().getCardsFromTop(1)) {
                game.doAction(new MagicRevealAction(card));
                game.doAction(new MagicRemoveCardAction(
                    card,
                    MagicLocationType.OwnersLibrary
                ));
                game.doAction(new MoveCardAction(
                    card,
                    MagicLocationType.OwnersLibrary,
                    MagicLocationType.OwnersHand
                ));
                game.doAction(new ChangeLifeAction(
                    event.getPlayer(), 
                    -card.getConvertedCost()
                ));
            }
        }
    }
]
