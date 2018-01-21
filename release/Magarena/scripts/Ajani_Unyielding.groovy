[
    new MagicPlaneswalkerActivation(2) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN reveals the top three cards of PN's library. " +
                "PN puts all nonland permanent cards revealed this way into PN's hand and the rest on the bottom of PN's library in any order."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardList cards = player.getLibrary().getCardsFromTop(3);
            game.doAction(new RevealAction(cards));
            for (final MagicCard card : cards) {
                if (!card.hasType(MagicType.Land) && !card.hasType(MagicType.Sorcery) && !card.hasType(MagicType.Instant))  {
                    game.doAction(new ShiftCardAction(card, MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
                }
                else {
                    game.doAction(new ShiftCardAction(card, MagicLocationType.OwnersLibrary, MagicLocationType.BottomOfOwnersLibrary));
                }
            }
        }
    }
    ,
    new MagicPlaneswalkerActivation(-2) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "Exile target creature\$. Its controller gains life equal to its power."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new RemoveFromPlayAction(it,MagicLocationType.Exile));
                game.doAction(new ChangeLifeAction(
                    it.getController(),
                    it.getPower()
                ));
            });
        }
    }
]

