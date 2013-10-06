[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent.getEquippedCreature() &&
                    damage.getTarget().isCreature()) ?
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
            game.doAction(new MagicRemoveFromPlayAction(event.getRefPermanent(),MagicLocationType.Exile));
        }
    }
]
