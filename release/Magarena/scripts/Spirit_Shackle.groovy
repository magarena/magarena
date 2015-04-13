[
    new MagicWhenBecomesTappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent tapped) {
            final MagicPermanent enchantedCreature = permanent.getEnchantedPermanent();
            return enchantedCreature == tapped ?
                new MagicEvent(
                    permanent,
                    enchantedCreature,
                    this,
                    "PN puts a -0/-2 counter on RN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            event.processRefPermanent(game, {
                game.doAction(new ChangeCountersAction(it,MagicCounterType.MinusZeroMinusTwo,1));
            });
        }
    }
]
