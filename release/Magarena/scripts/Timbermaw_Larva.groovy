[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final int amount = permanent.getCounters(MagicCounterType.PlusOne)
            return new MagicEvent(
                permanent,
                this,
                "SN gets +1/+1 until end of turn for each Forest PN controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getNrOfPermanents(
                FOREST_YOU_CONTROL
            );
            game.doAction(new ChangeTurnPTAction(event.getPermanent(),amount,amount));
        }
    }
]
