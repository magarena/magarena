[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicDamage damage) {
            final MagicTarget target = damage.getTarget();
            return (permanent == damage.getSource() &&
                    target.isPlayer() &&
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    damage.getTargetPlayer(),
                    this,
                    "PN puts the top two cards of " +
                    "his or her library into his or her graveyard."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicMillLibraryAction(event.getPlayer(),2));
        }
    }
]
