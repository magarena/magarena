[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.isSource(permanent) && damage.isTargetCreature()) ?
                new MagicEvent(
                    permanent,
                    damage.getTarget(),
                    this,
                    "Exile RN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processRefPermanent(game, {
                game.doAction(new RemoveFromPlayAction(it,MagicLocationType.Exile));
            });
        }
    }
]
