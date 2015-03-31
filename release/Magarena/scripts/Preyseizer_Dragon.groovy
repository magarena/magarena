[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final int amount = permanent.getCounters(MagicCounterType.PlusOne)
            return new MagicEvent(
                permanent,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(amount),
                this,
                "SN deals damage to target creature or player\$ equal to the number of +1/+1 counters on SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicPermanent permanent = event.getPermanent()
                final int amount = permanent.getCounters(MagicCounterType.PlusOne);
                game.doAction(new MagicDealDamageAction(permanent,it,amount));
                game.logAppendMessage(event.getPlayer()," ("+amount+")");
            });
        }
    }
]
