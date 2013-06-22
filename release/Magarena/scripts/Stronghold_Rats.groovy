[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                    damage.isCombat() &&
                    damage.getTarget().isPlayer()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Each player discards a card."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getPlayers()) {
                game.addEvent(new MagicDiscardEvent(event.getPermanent(),player));
            }
        }
    }
]
