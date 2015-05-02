[
    new MagicPreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (permanent.isController(damage.getTarget()) &&
                permanent.isFriend(game.getTurnPlayer())
            ) {
                // Prevention effect, All damage.
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    }
]
