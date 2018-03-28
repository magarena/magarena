[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN reveals cards from the top of PN's library until PN reveals an artifact card. " +
                "Put that card onto the battlefield and the rest on the bottom of PN's library in a random order. " +
                "SN deals damage to PN equal to the number of cards revealed this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer()
            final MagicCardList library = player.getLibrary();
            def predicate = { final MagicCard card -> card.hasType(MagicType.Artifact) };
            final MagicCardList revealed = new MagicCardList();
            MagicCard target = MagicCard.NONE;
            int amount = 0;
            while (library.size() > 0) {
                final MagicCard topCard = library.getCardAtTop();
                game.doAction(new RevealAction(topCard));
                amount++;
                if (predicate(topCard)) {
                    game.doAction(new ReturnCardAction(MagicLocationType.OwnersLibrary, topCard, player));
                    break;
                } else {
                    revealed.add(topCard);
                    game.doAction(new RemoveCardAction(topCard, MagicLocationType.OwnersLibrary));
                }
            }

            revealed.shuffle();
            revealed.each({ game.doAction(new MoveCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.BottomOfOwnersLibrary)) });

            game.doAction(new DealDamageAction(event.getSource(), player, amount));
        }
    }
]

