[
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent blocked = permanent.getBlockedCreature();
            return (permanent == blocker && blocked.isValid()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Return SN to its owner's hand at end of combat."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicAddTriggerAction(
                    event.getPermanent(),
                    MagicAtEndOfCombatTrigger.Return
                ))
        }
    }
]
