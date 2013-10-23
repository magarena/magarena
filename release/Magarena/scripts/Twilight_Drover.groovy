[
    new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
            final MagicPermanent left = act.getPermanent();
            return (left.isCreature() && left.isToken()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a +1/+1 counter on SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.PlusOne,
                1,
                true
            ));
        }
    }
]
