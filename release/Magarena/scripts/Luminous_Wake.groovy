[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPermanent enchantedPermanent = permanent.getEnchantedPermanent();
            return creature == enchantedPermanent ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN gains 4 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(),4));
        }
    },
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPermanent enchantedPermanent = permanent.getEnchantedPermanent();
            return creature == enchantedPermanent ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN gains 4 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(),4));
        }
    }
]
