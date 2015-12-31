[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Scry 1, then reveal the top card of PN's library. " +
                "SN gets -X/-X until end of turn, where X is that card's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicScryEvent(event));
            for (final MagicCard card : event.getPlayer().getLibrary().getCardsFromTop(1)) {
                game.doAction(new RevealAction(card));
                final int X = card.getConvertedCost();
                game.doAction(new ChangeTurnPTAction(event.getPermanent(),-X,-X));
            }
        }
    }
]
