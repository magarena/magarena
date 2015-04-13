[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent creature) {
            return (permanent.isFriend(creature) && creature.hasSubType(MagicSubType.Sliver))?
                new MagicEvent(
                    permanent,
                    game.getDefendingPlayer(),
                    this,
                    "PN loses 1 life."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(), -1));
        }
    }
]
