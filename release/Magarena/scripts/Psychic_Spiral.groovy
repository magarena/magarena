[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "PN shuffles all cards from his or her graveyard into his or her library. "+
                "Target player\$ puts that many cards from the top of his or her library into his or her graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicCardList graveyard = new MagicCardList(event.getPlayer().getGraveyard());
                if (graveyard.size > 0) {
                    for (final MagicCard card : graveyard) {
                        game.doAction(new RemoveCardAction(card,MagicLocationType.Graveyard));
                        game.doAction(new MoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersLibrary));
                    }
                    game.doAction(new MillLibraryAction(it,graveyard.size()));
                }
            });
        }
    }
]
