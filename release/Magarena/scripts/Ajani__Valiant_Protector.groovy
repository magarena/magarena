[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN reveals cards from the top of PN's library until PN reveals a creature card. " +
                "Put that card into PN's hand and the rest on the bottom of PN's library in a random order."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList library = event.getPlayer().getLibrary();
            def predicate = { final MagicCard card -> card.hasType(MagicType.Creature) };
            final MagicCardList nonTarget = new MagicCardList(library.takeWhile({ !predicate(it) }));
            if (library.any(predicate)) {
                final MagicCard target = library.find(predicate);
                game.doAction(new RevealAction(nonTarget.plus(target)));
                game.doAction(new ShiftCardAction(target, MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
            } else {
                game.doAction(new RevealAction(nonTarget));
            }
            nonTarget.shuffle();
            nonTarget.each({ game.doAction(new ShiftCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.BottomOfOwnersLibrary)) });
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


