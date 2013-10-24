[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource source = damage.getSource();
            return (source == permanent &&
                    damage.isCombat() &&
                    damage.getTarget().isPlayer()) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    damage.getTarget(),
                    this,
                    "RN loses the game."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicLoseGameAction(event.getRefPlayer()));
        }
    }
]
