[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player=permanent.getController();
            final MagicTarget target=damage.getTarget();
            return (!damage.isCombat()&&target.isPlayer()&&target!=player) ?
                new MagicEvent(
                    permanent,
                    player,
                    this,
                    "SN gets +3/+0 until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),3,0));
        }
    }
]
