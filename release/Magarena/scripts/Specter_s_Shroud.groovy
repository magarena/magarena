[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPermanent equippedCreature=permanent.getEquippedCreature();
            return (damage.getSource() == equippedCreature &&
                    damage.getTarget().isPlayer() &&
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    damage.getTarget(),
                    this,
                    "RN discards a card."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicDiscardEvent(event.getSource(),event.getRefPlayer()));
        }
    }
]
