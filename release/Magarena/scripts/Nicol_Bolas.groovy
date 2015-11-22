[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent && permanent.isOpponent(damage.getTarget())) ?
                new MagicEvent(
                    permanent,
                    permanent.getOpponent(),
                    this,
                    "PN discards his or her hand."
                ):
                MagicEvent.NONE
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicDiscardEvent(
                event.getPermanent(),
                event.getPlayer(),
                event.getPlayer().getHandSize()
            ));
        }
    }
]
