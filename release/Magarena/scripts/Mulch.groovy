[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN reveals the top four cards of his or her library. " + 
                "Put all land cards revealed this way into PN's hand and the rest into PN's graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList top4 = event.getPlayer().getLibrary().getCardsFromTop(4) ;
            game.doAction(new RevealAction(top4));
            for (final MagicCard top : top4) {
                game.doAction(new ShiftCardAction(
                    top,
                    MagicLocationType.OwnersLibrary,
                    top.hasType(MagicType.Land) ?
                        MagicLocationType.OwnersHand :
                        MagicLocationType.Graveyard
                ));
            }
        }
    }
]
