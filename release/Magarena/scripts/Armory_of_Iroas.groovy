[
    new MagicWhenAttacksTrigger(1) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPermanent equippedCreature=permanent.getEquippedCreature();
            return (equippedCreature.isValid() && equippedCreature==creature) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a +1/+1 counter on creature equipped by SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicPermanent equipped=permanent.getEquippedCreature();
            game.doAction(new ChangeCountersAction(equipped,MagicCounterType.PlusOne,1));
        }
    }
]
