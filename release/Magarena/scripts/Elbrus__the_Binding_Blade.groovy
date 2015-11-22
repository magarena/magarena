[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.isSource(permanent.getEquippedCreature()) && damage.isCombat() && damage.isTargetPlayer()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Unattach SN, then transform it."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AttachAction(event.getPermanent(), MagicPermanent.NONE));
            game.doAction(new TransformAction(event.getPermanent()));
        }
    }
]
