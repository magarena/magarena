def sacrificeAction = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        event.processTargetPermanent(game, {
            game.doAction(new SacrificeAction(it));
        });
    }
}

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final MagicEvent sacrificeEvent = new MagicEvent(
                permanent,
                new MagicMayChoice(ANOTHER_CREATURE_YOU_CONTROL),
                sacrificeAction,
                "PN may\$ sacrifice another creature PN controls\$."
            );
            game.addEvent(sacrificeEvent);
            
            if (sacrificeEvent.isYes()) {
                final int amount = ((MagicPermanent)(sacrificeEvent.getTarget())).getPower();
                return new MagicEvent(
                    permanent,
                    NEG_TARGET_CREATURE_OR_PLAYER,
                    amount,
                    this,
                    "SN deals ${amount} damage to target creature or player\$."
                );
            } else {
                return MagicEvent.NONE;
            }
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new DealDamageAction(event.getSource(), it, event.getRefInt()));
            });
        }
    }
]

