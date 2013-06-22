[    
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent blocked = blocker.getBlockedCreature();
            return blocked == permanent ||
                   (permanent == blocker && blocked.isValid()) ?
                new MagicEvent(
                    permanent,
                    blocked == permanent ? blocker : blocked,
                    this,
                    "RN gets +1/+1 until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeTurnPTAction(event.getRefPermanent(),1,1));
        }
    }
]
