[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (!damage.isCombat() &&
                    permanent.isOpponent(damage.getTarget())) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN gets +3/+0 until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeTurnPTAction(event.getPermanent(),3,0));
        }
    }
]
