[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Reveal"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source, "{2}{R/G}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN reveals the top card of his or her library. If it isn't a creature card, "+
                "PN puts it into his or her graveyard. Otherwise, PN puts it onto the battlefield. "+
                "That creature gains haste. Sacrifice it at the beginning of the next end step."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardList cards = player.getLibrary().getCardsFromTop(1);
            for (final MagicCard card : cards) {
                game.doAction(new RevealAction(card));
                if (card.hasType(MagicType.Creature) == false) {
                    game.doAction(new ShiftCardAction(card, MagicLocationType.OwnersLibrary, MagicLocationType.Graveyard));
                    game.logAppendMessage(player, "${player.getName()} puts ${card.getName()} into the graveyard.");
                } else {
                    game.doAction(new ReturnCardAction(
                        MagicLocationType.OwnersLibrary,
                        card,
                        player,
                        [MagicPlayMod.HASTE, MagicPlayMod.SACRIFICE_AT_END_OF_TURN]
                    ));
                }
            }
        }
    }
]
