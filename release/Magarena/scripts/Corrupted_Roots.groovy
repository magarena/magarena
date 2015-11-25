[
    new BecomesTappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent tapped) {
            final MagicPermanent enchantedPermanent = permanent.getEnchantedPermanent();
            return (enchantedPermanent == tapped) ?
                new MagicEvent(
                    permanent,
                    enchantedPermanent.getController(),
                    this,
                    "PN loses 2 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(),-2));
        }
    }
]
