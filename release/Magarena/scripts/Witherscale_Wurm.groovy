[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            return (permanent == damage.getSource() &&
                    permanent.isOpponent(damage.getTarget()) &&
                    permanent.hasCounters(MagicCounterType.MinusOne)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Remove all -1/-1 counters from SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final int amount = permanent.getCounters(MagicCounterType.MinusOne);
            game.doAction(new ChangeCountersAction(permanent,MagicCounterType.MinusOne,-amount));
        }
    }
]
