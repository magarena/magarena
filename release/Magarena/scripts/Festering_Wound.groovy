[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return upkeepPlayer == permanent.getEnchantedPermanent().getController() ? new MagicEvent(
                permanent,
                upkeepPlayer,
                this,
                "SN deals X damage to PN, where X is equal to the number of infection counters on SN."
            ):
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final int amount = event.getPermanent().getCounters(MagicCounterType.Infection);
            game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),amount));
            game.logAppendValue(event.getPlayer(),amount);
        }
    }
]
