[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer controller = creature.getController();
            return (controller != permanent.getController()) ?
                new MagicEvent(
                    permanent,
                    controller,
                    this,
                    "PN puts the top two cards of his or her library into his or her graveyard."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicMillLibraryAction(event.getPlayer(),2));
        }
    }
]
