[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final MagicPermanent enchantedPermanent = permanent.getEnchantedPermanent();
            return (otherPermanent.isCreature() &&
                    enchantedPermanent != otherPermanent &&
                    enchantedPermanent.isValid()) ?
                new MagicEvent(
                    permanent,
                    enchantedPermanent,
                    this,
                    "Destroy RN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DestroyAction(event.getRefPermanent()));
        }
    }
]
