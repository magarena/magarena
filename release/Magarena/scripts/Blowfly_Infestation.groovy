[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return otherPermanent.isCreature() && otherPermanent.hasCounters(MagicCounterType.MinusOne) ?
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
                game.doAction(new ChangeCountersAction(event.getPlayer(),it,MagicCounterType.MinusOne,1));
            });
        }
    }
]
