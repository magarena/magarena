def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        event.payManaCost(game);
        game.doAction(new MagicCopyCardOnStackAction(event.getPlayer(),event.getCardOnStack()));
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
                game.doAction(new MagicDealDamageAction(event.getSource(),it,3));
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it.getController(),
                    new MagicMayChoice(
                        "Pay {R}{R}?",
                        new MagicPayManaCostChoice(MagicManaCost.create("{R}{R}"))
                    ),
                    action,
                    "PN may\$ pay {R}{R}\$. If that player does, he or she may copy this spell and may choose a new target for that copy."
                ));
            });
        }
    }
]
