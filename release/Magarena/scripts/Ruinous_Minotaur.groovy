[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (permanent == damage.getSource() &&
                    permanent.isOpponent(damage.getTarget())) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN sacrifices a land."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSacrificePermanentEvent(
                event.getPermanent(),
                event.getPlayer(),
                SACRIFICE_LAND
            ));
        }
    }
]
