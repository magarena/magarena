[
    new AtEndOfTurnTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            return permanent.isController(eotPlayer) && eotPlayer.getCreaturesAttackedThisTurn() > 0;
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                this,
                "PN reveals the top card of PN's library and put that card into PN's hand. " +
                "PN loses life equal to the card's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.getPlayer().getLibrary().getCardsFromTop(1).each {
                game.doAction(new RevealAction(it));
                game.doAction(new ShiftCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
                game.doAction(new ChangeLifeAction(event.getPlayer(), -it.getConvertedCost()));
            }
        }
    }
]

