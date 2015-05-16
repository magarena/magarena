[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                NEG_TARGET_CREATURE_OR_PLAYER,
                this,
                "SN deals damage to target creature or player\$ equal to the number of Mountains PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicPlayer player = event.getPlayer();
                final int amount = MOUNTAIN_YOU_CONTROL.filter(player).size();
                game.logAppendMessage(player,"("+amount+")");
                game.doAction(new DealDamageAction(event.getPermanent(), it, amount));
            });
        }
    }
]
