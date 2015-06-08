[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                game.getDefendingPlayer(),
                this,
                "PN discards a card."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicDiscardEvent(event.getSource(), event.getPlayer()));
        }
    }
]
