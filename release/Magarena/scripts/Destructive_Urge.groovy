[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.isCombat() && 
                damage.isSource(permanent.getEnchantedPermanent()) &&
                damage.isTargetPlayer()) ?
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
                MagicTargetChoice.SACRIFICE_LAND)
            );
        }
    }
]
