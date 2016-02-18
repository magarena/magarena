[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource dmgSource = damage.getSource();
            return (damage.isCombat() &&
                    damage.isTargetPlayer() &&
                    permanent.isFriend(dmgSource) &&
                    dmgSource.isCreaturePermanent() &&
                    dmgSource.hasSubType(MagicSubType.Vampire)) ?
                new MagicEvent(
                    permanent,
                    dmgSource,
                    this,
                    "Put a +1/+1 counter on RN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(event.getRefPermanent(),MagicCounterType.PlusOne,1));
        }
    }
]
