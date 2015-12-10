def discardAction = {
    final MagicGame game, final MagicEvent event ->
    final MagicEvent discard = new MagicDiscardEvent(event.getSource(), event.getPlayer());
    if (event.isYes() && discard.isSatisfied()) {
        game.addEvent(discard);
        game.doAction(new CopyCardOnStackAction(event.getPlayer(),event.getCardOnStack()));
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_OR_PLAYER,
                this,
                "SN deals 3 damage to target creature or player\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new DealDamageAction(event.getSource(),it,3));
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it.getController(),
                    new MagicMayChoice("Discard a card?"),
                    discardAction,
                    "PN may discard a card\$. If PN does, he or she may copy this spell and may choose a new target for that copy."
                ));
            });
        }
    }
]
