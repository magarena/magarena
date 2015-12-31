[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN reveals the top card of his or her library and puts that card into his or her hand. "+
                "PN loses life equal to its converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicCard card : event.getPlayer().getLibrary().getCardsFromTop(1)) {
                game.doAction(new RevealAction(card));
                game.doAction(new ShiftCardAction(
                    card,
                    MagicLocationType.OwnersLibrary,
                    MagicLocationType.OwnersHand
                ));
                game.doAction(new ChangeLifeAction(
                    event.getPlayer(),
                    -card.getConvertedCost()
                ));
            }
        }
    }
]
