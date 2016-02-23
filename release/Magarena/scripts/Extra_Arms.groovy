[
    new AttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return creature == enchanted ?
                new MagicEvent(
                    permanent,
                    TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(2),
                    enchanted,
                    this,
                    "RN deals 2 damage to target creature or player.\$"
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new DealDamageAction(event.getRefPermanent(), it, 2));
            })
        }
    }
]
