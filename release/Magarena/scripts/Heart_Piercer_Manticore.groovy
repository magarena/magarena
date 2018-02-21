def damageAction = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetPermanent(game, {
        game.doAction(new DealDamageAction(event.getSource(), it, event.getRefInt()));
    });
}

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(new MagicTargetChoice("another creature to sacrifice")),
                this,
                "PN may\$ sacrifice another creature PN controls\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new SacrificeAction(it));
                    final int amount = it.getPower();
                    game.doAction(new EnqueueTriggerAction(new MagicEvent(
                        event.getSource(),
                        NEG_TARGET_CREATURE_OR_PLAYER,
                        amount,
                        damageAction,
                        "SN deals ${amount} damage to target creature or player\$."
                    )));
                });
            }
        }
    }
]

