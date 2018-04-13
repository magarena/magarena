[
    new OneOrMoreCountersArePutTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
            return super.accept(permanent, data) &&
                data.counterType == MagicCounterType.MinusOne;
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
            return new MagicEvent(
                permanent,
                data.amount,
                this,
                (data.amount == 1) ?
                    "PN creates a 1/1 black Insect creature token." :
                    "PN creates RN 1/1 black Insect creature tokens."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("1/1 black Insect creature token"),
                event.getRefInt()
            ));
        }
    }
]

