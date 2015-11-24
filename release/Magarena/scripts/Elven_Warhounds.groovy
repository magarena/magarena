[
    new ThisBecomesBlockedByTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            return new MagicEvent(
                permanent,
                blocker,
                this,
                "PN puts RN on top of its owner's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new RemoveFromPlayAction(event.getRefPermanent(), MagicLocationType.TopOfOwnersLibrary));
        }
    }
]
