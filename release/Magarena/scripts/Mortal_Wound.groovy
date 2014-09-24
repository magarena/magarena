[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicDamage damage) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return damage.getTarget() == enchanted ?
                new MagicEvent(
                    permanent,
                    this,
                    "Destroy the creature enchanted by SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDestroyAction(event.getPermanent().getEnchantedPermanent()));
        }
    }
]
