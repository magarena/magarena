[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature() &&
                    otherPermanent.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    permanent.getEnchantedCreature(),
                    this,
                    "Put a +1/+1 counter on "+permanent.getEnchantedCreature().toString()+"."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(
                event.getRefPermanent(),
                MagicCounterType.PlusOne,
                1,
                true
            ));
        }
    }
]
