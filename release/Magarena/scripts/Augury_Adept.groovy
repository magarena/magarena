[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                this,
                "Reveal the top card of PN's library and put that card into PN's hand. PN gains life equal to its converted mana cost."
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
                game.doAction(new MagicMoveCardAction(
                    card,
                    MagicLocationType.OwnersLibrary,
                    MagicLocationType.OwnersHand
                ));
                game.doAction(new MagicChangeLifeAction(
                    event.getPlayer(), 
                    card.getConvertedCost()
                ));
            }
        }
    }
]
