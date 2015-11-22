[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN reveals the top three cards of his or her library. "+
                "Puts all artifact cards revealed this way into his or her hand and the rest into his or her graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList top3 = event.getPlayer().getLibrary().getCardsFromTop(3);
            game.doAction(new RevealAction(top3));
            for (final MagicCard top : top3) {
                game.doAction(new ShiftCardAction(
                    top,
                    MagicLocationType.OwnersLibrary,
                    top.hasType(MagicType.Artifact) ?
                        MagicLocationType.OwnersHand :
                        MagicLocationType.Graveyard
                ));
            }
        }
    }
]
