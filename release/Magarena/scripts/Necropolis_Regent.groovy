[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource source = damage.getSource()
            return (source.isCreaturePermanent() && source.isFriend(permanent) &&
                    damage.isCombat() && damage.isTargetPlayer()) ?
                new MagicEvent(
                    source,
                    damage.getAmount(),
                    this,
                    "Put RN +1/+1 counters on SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,event.getRefInt()));
        }
    }
]
