[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource().hasType(MagicType.Creature) &&
                    damage.isCombat() &&
                    permanent.isController(damage.getTarget())) ?
                new MagicEvent(
                    permanent,
                    damage.getTargetPlayer().getOpponent(),
                    this,
                    "PN gains control of SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new GainControlAction(event.getPlayer(),event.getPermanent()));
        }
    }
]
