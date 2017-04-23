[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            MagicPermanent enchantedCreature = permanent.getEnchantedPermanent();
            return (damage.getSource() == enchantedCreature && damage.isCombat() && damage.isTargetPlayer()) ?
                new MagicEvent(
                    permanent,
                    enchantedCreature.getController(),
                    this,
                    "PN doubles his or her life total."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new ChangeLifeAction(player, player.getLife()));
        }
    }
]
