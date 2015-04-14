[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicDamage damage) {
            return (permanent.isController(damage.getTarget()) &&
                    damage.getSource().isPermanent() &&
                    damage.getSource().hasSubType(MagicSubType.Human)) ?
                new MagicEvent(
                    permanent,
                    damage.getSource(),
                    this,
                    "Destroy RN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DestroyAction(event.getRefPermanent()));
        }
    }
]
