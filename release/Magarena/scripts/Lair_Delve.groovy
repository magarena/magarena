[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN reveal the top two cards of his or her library. " + 
                "Put all creature and land cards revealed this way into PN's hand and the rest on the bottom of PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList top2 = event.getPlayer().getLibrary().getCardsFromTop(2) ;
            game.doAction(new RevealAction(top2));
            for (final MagicCard top : top2) {
                game.doAction(new RemoveCardAction(
                    top,
                    MagicLocationType.OwnersLibrary
                ));
                game.doAction(new MoveCardAction(
                    top,
                    MagicLocationType.OwnersLibrary,
                    top.hasType(MagicType.Creature) || top.hasType(MagicType.Land) ?
                        MagicLocationType.OwnersHand :
                        MagicLocationType.BottomOfOwnersLibrary
                ));
            }
        }
    }
]
