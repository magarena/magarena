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
            final MagicCardList nonTarget = (MagicCardList)library.takeWhile({ !predicate(it) });
            int amount = nonTarget.size();
            if (library.any(predicate)) {
                final MagicCard target = library.find(predicate);
                game.doAction(new RevealAction(nonTarget.plus(target)));
                game.doAction(new ReturnCardAction(MagicLocationType.OwnersLibrary, target, player));
                amount += 1;
            } else {
                game.doAction(new RevealAction(nonTarget));
            }
            nonTarget.shuffle();
            nonTarget.each {
                game.doAction(new ShiftCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.BottomOfOwnersLibrary))
            }
            game.doAction(new DealDamageAction(event.getSource(), player, amount));
        }
    }
]

