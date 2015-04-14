[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource dmgSource = damage.getSource();
            return (permanent.isController(damage.getTarget()) &&
                    dmgSource.isCreature()) ?
                new MagicEvent(
                    permanent,
                    dmgSource,
                    this,
                    "Destroy "+dmgSource+"."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DestroyAction(event.getRefPermanent()));
        }
    }
]
