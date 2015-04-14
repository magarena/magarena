[
    new MagicWhenBecomesTappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent tapped) {
            final MagicPermanent enchantedCreature = permanent.getEnchantedPermanent();
            return (enchantedCreature.isCreature() && enchantedCreature==tapped) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN draws a card."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer()));
        }
    }
]
