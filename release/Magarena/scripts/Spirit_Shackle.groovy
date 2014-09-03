[
    new MagicWhenBecomesTappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent tapped) {
            final MagicPermanent enchantedCreature = permanent.getEnchantedPermanent();
            return (enchantedCreature.isCreature() && enchantedCreature==tapped) ?
                new MagicEvent(
                    permanent,
                    enchantedCreature,
                    this,
                    "PN puts a -0/-2 counter on creature enchanted by SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicPermanent enchanted=permanent.getEnchantedPermanent();
            if (enchanted.isValid()) {
                game.doAction(new MagicChangeCountersAction(enchanted,MagicCounterType.MinusZeroMinusTwo,1));
		 }
        }
    }
]
