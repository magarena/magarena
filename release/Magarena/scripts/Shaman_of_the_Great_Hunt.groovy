[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource source = damage.getSource();
            return (source.isCreaturePermanent() && source.isFriend(permanent) &&
                    damage.isCombat() && damage.isTargetPlayer()) ?
                new MagicEvent(
                    permanent,
                    source,
                    this,
                    "Put a +1/+1 counter on RN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(event.getPlayer(),event.getRefPermanent(),MagicCounterType.PlusOne,1));
        }
    }
]
