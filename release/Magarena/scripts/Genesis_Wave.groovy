def toBattlefield = {
    final MagicGame game, final MagicEvent event ->
        event.processChosenCards(game, {
            final MagicCard chosen ->
                final MagicPlayer player = event.getPlayer();
                for (final MagicCard card : player.getLibrary()) {
                    if (card == chosen) {
                        game.doAction(new ReturnCardAction(
                            MagicLocationType.OwnersLibrary,
                            card,
                            event.getPlayer()
                        ));
                    }
                }
        });
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                amount,
                this,
                "PN reveals the top ${amount} cards of his or her library. " +
                "PN may put any number of permanent cards with converted mana cost ${amount} or less from among them " +
                "onto the battlefield. Then puts all cards revealed this way that weren't put onto the battlefield " +
                "into his or her graveyard."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefInt();
            final MagicPlayer player = event.getPlayer();
            final MagicCardList topCards = player.getLibrary().getCardsFromTop(amount);
            game.doAction(new RevealAction(topCards));
            MagicCardList permanentCards = new MagicCardList();
            for (MagicCard card : topCards) {
                if (card.isPermanentCard() && card.getConvertedCost() <= amount) {
                    permanentCards.add(card);
                }
            }
            game.addFirstEvent(new MagicEvent(
                event.getSource(),
                player,
                new MagicFromCardListChoice(permanentCards, topCards, permanentCards.size(), true),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                toBattlefield,
                ""
            ));
            for (MagicCard card : topCards) {
                if (!permanentCards.contains(card)) {
                    game.doAction(new ShiftCardAction(
                        card,
                        MagicLocationType.OwnersLibrary,
                        MagicLocationType.Graveyard)
                    );
                    game.logAppendMessage(player, "${player.getName()} puts ${card.getName()} into his or her graveyard")
                }
            }
        }
    }
]
