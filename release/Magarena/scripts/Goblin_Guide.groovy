[
     new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                game.getDefendingPlayer(),
                this,
                "PN reveals the top card of his or her library. If it's a land card, PN puts it into his or her hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicCard card : game.getDefendingPlayer().getLibrary().getCardsFromTop(1)) {
                game.doAction(new MagicRemoveCardAction(
                    card,
                    MagicLocationType.OwnersLibrary
                ));
            if (card.hasType(MagicType.Land)) {
            game.doAction(new MagicMoveCardAction(
                    card,
                    MagicLocationType.OwnersLibrary,
                    MagicLocationType.OwnersHand));
            } else {
            game.doAction(new MagicMoveCardAction(
                    card,
                    MagicLocationType.OwnersLibrary,
                    MagicLocationType.TopOfOwnersLibrary));
            }
            }
        }
    }
]
