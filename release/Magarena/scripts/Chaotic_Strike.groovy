def winAct = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new ChangeTurnPTAction(event.getRefPermanent(), 1, 1));
}

def loseAct = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new ChangeTurnPTAction(event.getRefPermanent(), 0, 0));
}
def effect = MagicRuleEventAction.create("Draw a card.")


[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                this,
                "Flip a coin. If PN wins the flip, target creature\$ gets +1/+1 until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.addEvent(new MagicCoinFlipEvent(
                    event,
                    it,
                    winAct,
                    loseAct
                ));
            });
                game.addEvent(effect.getEvent(event));
        }
    }
]
