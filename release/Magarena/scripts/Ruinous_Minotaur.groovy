[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                    damage.getTarget().isPlayer()) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    this,
                    "PN sacrifices a land"
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            if (player.controlsPermanent(MagicType.Land)) {
                game.addEvent(new MagicSacrificePermanentEvent(
                    event.getPermanent(),
                    player,
                    MagicTargetChoice.SACRIFICE_LAND
                ));
            }
        }
    }
]
