[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                    permanent.isOpponent(damage.getTarget())) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN draws a card, then discards a card."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicDrawAction(player));
            game.addEvent(new MagicDiscardEvent(event.getPermanent(),player));
        }
    }
]
