[
    new AttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPermanent enchantedPermanent = permanent.getEnchantedPermanent();
            return creature == enchantedPermanent ?
                new MagicEvent(
                    permanent,
                    enchantedPermanent.getController(),
                    this,
                    "PN loses 3 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(),-3));
        }
    },
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPermanent enchantedPermanent = permanent.getEnchantedPermanent();
            return creature == enchantedPermanent ?
                new MagicEvent(
                    permanent,
                    enchantedPermanent.getController(),
                    this,
                    "PN loses 3 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(),-3));
        }
    }
]
