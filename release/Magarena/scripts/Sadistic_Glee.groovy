[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return otherPermanent.isCreature() ?
                new MagicEvent(
                    permanent,
                    permanent.getEnchantedPermanent(),
                    this,
                    "Put a +1/+1 counter on "+permanent.getEnchantedPermanent().toString()+"."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(event.getRefPermanent(),MagicCounterType.PlusOne,1));
        }
    }
]
