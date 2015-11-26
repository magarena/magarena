[
    new AttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return permanent.isEnemy(creature) ?
                new MagicEvent(
                    permanent,
                    creature.getController(),
                    this,
                    "PN puts the top two cards of his or her library into his or her graveyard."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MillLibraryAction(event.getPlayer(),2));
        }
    }
]
