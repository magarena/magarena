[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            return (damage.getSource().isCreaturePermanent() && damage.getSource().isFriend(permanent) && damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    damage.getDealtAmount(),
                    this,
                    "PN gains RN life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(), event.getRefInt()));
        }
    }
]
