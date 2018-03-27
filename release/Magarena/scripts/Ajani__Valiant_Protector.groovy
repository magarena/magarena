[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN reveals cards from the top of PN's library until PN reveals a creature card. " +
                "PN puts that card into PN's hand and the rest on the bottom of PN's library in a random order."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList library = event.getPlayer().getLibrary();
            def predicate = { final MagicCard card -> card.hasType(MagicType.Creature) };
            final MagicCardList revealed = new MagicCardList();
            MagicCard target = MagicCard.NONE;
            while (target == MagicCard.NONE && library.size() > 0) {
                final MagicCard topCard = library.getCardAtTop();
                game.doAction(new RevealAction(topCard));
                if (predicate(topCard)) {
                    target = topCard;
                    game.doAction(new ShiftCardAction(topCard, MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
                } else {
                    revealed.add(topCard);
                    game.doAction(new RemoveCardAction(topCard, MagicLocationType.OwnersLibrary));
                }
            }

            revealed.shuffle();
            revealed.each({ game.doAction(new MoveCardAction(it, MagicLocationType.BottomOfOwnersLibrary)) });
        }
    }
    ,
    new MagicPlaneswalkerActivation(-11) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                POS_TARGET_CREATURE,
                this,
                "Put X +1/+1 counters on target creature\$, where X is your life total. " +
                "That creature gains trample until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = event.getPlayer().getLife();
                game.doAction(new ChangeCountersAction(it, MagicCounterType.PlusOne, amount));
                game.doAction(new GainAbilityAction(it, MagicAbility.Trample, MagicStatic.UntilEOT));
            });
        }
    }
]


