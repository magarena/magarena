[
    new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (permanent.isController(damage.getTarget())) {
                final int amount = damage.replace();
                game.doAction(new ChangeCountersAction(permanent,MagicCounterType.Depletion,amount));
            }
            return MagicEvent.NONE;
        }
    },
    
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            return new MagicEvent(
                permanent,
                permanent.getController(),
                this,
                "PN removes all Depletion counters from SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            game.doAction(new ChangeCountersAction(permanent,MagicCounterType.Depletion,-permanent.getCounters(MagicCounterType.Depletion)));
        }
    }
]
