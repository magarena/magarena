[
    new ThisBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            return new MagicEvent(
                permanent,
                this,
                "Return SN to its owner's hand at end of combat."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddTriggerAction(
                event.getPermanent(),
                AtEndOfCombatTrigger.Return
            ))
        }
    }
]
