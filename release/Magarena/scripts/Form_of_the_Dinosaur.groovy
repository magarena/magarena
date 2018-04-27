[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                this,
                "SN deals 15 damage to target creature an opponent controls\$ " +
                "and that creature deals damage equal to its power to PN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DealDamageAction(event.getSource(), it, 15));
                game.doAction(new DealDamageAction(it, event.getPlayer(), it.getPower()));
            });
        }
    }
]

