[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                NEG_TARGET_PLAYER,
                this,
                "SN deals damage to target player\$ equal to "+
                "the number of nonbasic lands that player controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amount = it.getNrOfPermanents(NONBASIC_LAND_YOU_CONTROL)
                game.doAction(new DealDamageAction(event.getSource(),it,amount));
                game.logAppendValue(event.getPlayer(),amount);
            });
        }
    }
]
