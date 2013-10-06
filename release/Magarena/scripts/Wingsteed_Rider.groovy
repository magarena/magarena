[
    new MagicHeroicTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicItemOnStack target) {
            return new MagicEvent(
                permanent,
                this,
                "Put a +1/+1 counter on SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(event.getPermanent(), MagicCounterType.PlusOne, 1, true))
        }
    }
]
