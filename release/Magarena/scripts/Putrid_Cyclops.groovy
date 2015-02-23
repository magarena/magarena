[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Scry 1, then reveal the top card of your library. " + 
                "SN gets -X/-X until end of turn, where X is that card's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicCard card1 : event.getPlayer().getLibrary().getCardsFromTop(1)) {
                game.doAction(new MagicLookAction(card1, "top card of your library"));
                game.addEvent(new MagicScryEvent(event));
            }
            for (final MagicCard card2 : event.getPlayer().getLibrary().getCardsFromTop(1)) {
                game.doAction(new MagicRevealAction(card2));
                final int X = card2.getConvertedCost();
                    game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),-X,-X));
            }
        }
    }
]
