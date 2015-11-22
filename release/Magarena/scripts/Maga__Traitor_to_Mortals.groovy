[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                NEG_TARGET_PLAYER, 
                this,
                "Target player\$ loses life equal to the number of +1/+1 counters on SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amount = event.getPermanent().getCounters(MagicCounterType.PlusOne);
                game.logAppendValue(event.getPlayer(),amount);
                game.doAction(new ChangeLifeAction(it,-amount));
            });
        }
    }
]
