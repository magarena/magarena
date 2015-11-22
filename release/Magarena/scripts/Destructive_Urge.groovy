[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.isSource(permanent.getEnchantedPermanent()) &&
                    damage.isCombat() && damage.isTargetPlayer()) ?
                new MagicEvent(
                    permanent,
                    damage.getTarget(),
                    this,
                    "RN sacrifices a land."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSacrificePermanentEvent(
                event.getPermanent(),
                event.getRefPlayer(), 
                MagicTargetChoice.SACRIFICE_LAND
            ));
        }
    }
]
