def DiscardDraw = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        final MagicCard card ->
        game.doAction(new MagicDiscardCardAction(event.getRefPlayer(),card));
    });
    game.doAction(new MagicDrawAction(event.getPlayer(),1));
};


[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_OPPONENT,
                this,
                "Target opponent\$ reveals his or her hand. PN chooses a card from it. That player discards that card. "+
                "PN draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player ->
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicFromCardListChoice(player.getHand(),1),
                    player,
                    DiscardDraw,
                    ""
                ));
            });
        }
    }
]
