def DiscardTwo = {
    final MagicGame game, final MagicEvent event ->
    if (event.getCardChoice().size() == 1) {
        game.doAction(new MagicDiscardCardAction(event.getRefPlayer(),event.getCardChoice().get(0)));
    } else if(event.getCardChoice().size() == 2) {
        game.doAction(new MagicDiscardCardAction(event.getRefPlayer(),event.getCardChoice().get(1)));
        game.doAction(new MagicDiscardCardAction(event.getRefPlayer(),event.getCardChoice().get(0)));
    }
    
};


[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "Target player\$ reveals his or her hand. PN chooses up to 2 cards from it. That player discards those card. "
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player ->
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicFromCardListChoice(player.getHand(),2),
                    player,
                    DiscardTwo,
                    "\$"
                ));
            });
        }
    }
]
