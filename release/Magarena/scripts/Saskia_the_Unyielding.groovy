[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return damage.isTargetPlayer() && 
            damage.isCombat() &&
            damage.getSource().isFriend(permanent) &&
            damage.getSource().hasType(MagicType.Creature) ?
                new MagicEvent(
                    damage.getSource(),
                    permanent.getChosenPlayer(),
                    damage.getDealtAmount(),
                    this,
                    "SN deals RN damage to PN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DealDamageAction(event.getSource(), event.getPlayer(), event.getRefInt()));
        }
    }
]
