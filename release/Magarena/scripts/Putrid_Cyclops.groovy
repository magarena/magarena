def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicPlayer player = event.getPlayer();
    for (final MagicCard card : player.getLibrary().getCardsFromTop(1)) {
        final int X = card.getConvertedCost();
        game.logAppendX(player,X);
        game.doAction(new RevealAction(card));
        game.doAction(new ChangeTurnPTAction(event.getPermanent(),-X,-X));
    }
}

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
            game.addEvent(new MagicEvent(
                event.getSource(),
                event.getPlayer(),
                action,
                ""
            ));
        }
    }
]
