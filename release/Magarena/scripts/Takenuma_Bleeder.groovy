[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return permanent == attacker && permanent.getController().controlsPermanent(MagicSubType.Demon) == false ?
                new MagicEvent(
                    permanent,
                    this,
                    "If PN controls no Demons, lose 1 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().controlsPermanent(MagicSubType.Demon) == false) {
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),-1));
            }
        }
    },	
	new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
		final MagicPermanent blocked = permanent.getBlockedCreature();
            return (permanent == blocker && blocked.isValid()) && permanent.getController().controlsPermanent(MagicSubType.Demon) == false ?
                new MagicEvent(
                    permanent,
                    this,
                    "If PN controls no Demons, lose 1 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().controlsPermanent(MagicSubType.Demon) == false) {
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),-1));
            }
        }
    }
]
