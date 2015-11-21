[
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            return (permanent == blocker) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Sacrifice SN at end of combat."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddTriggerAction(event.getPermanent(), AtEndOfCombatTrigger.Sacrifice));
        }
    }
]
