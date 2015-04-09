[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature() &&
                    otherPermanent.getCounters(MagicCounterType.MinusOne) > 0) ?
                new MagicEvent(
                    permanent,
                    TARGET_CREATURE,
                    new MagicWeakenTargetPicker(1,1),
                    this,
                    "Put a -1/-1 counter on target creature\$."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicChangeCountersAction(it,MagicCounterType.MinusOne,1));
            });
        }
    }
]
