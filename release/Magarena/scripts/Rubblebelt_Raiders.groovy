[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent creature) {
            final int amount = permanent.getCounters(MagicCounterType.PlusOne)
            return new MagicEvent(
                permanent,
                this,
                "PN puts a +1/+1 counter on SN for each attacking creature PN controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer()
            final int attackers = player.getNrOfPermanents(
                ATTACKING_CREATURE_YOU_CONTROL
            );
            game.doAction(new ChangeCountersAction(event.getPlayer(),event.getPermanent(),MagicCounterType.PlusOne,attackers));
            game.logAppendValue(player,attackers);
        }
    }
]
