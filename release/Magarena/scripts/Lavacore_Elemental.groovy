[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource source = damage.getSource();
            return (source.isCreature() &&
                    permanent.isFriend(source) &&
                    damage.isCombat() &&
                    damage.isTargetPlayer()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a time counter on SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(event.getPermanent(),MagicCounterType.Time,1));
        }
    }
]
