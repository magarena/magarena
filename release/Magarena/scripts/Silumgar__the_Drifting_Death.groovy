[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent.isFriend(creature) &&
                    permanent.hasSubType(MagicSubType.Dragon)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Creatures defending player controls get -1/-1 until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> creatures = game.getDefendingPlayer().filterPermanents(MagicTargetFilterFactory.CREATURE_YOU_CONTROL);
            for (final MagicPermanent creature : creatures) {
                game.doAction(new MagicChangeTurnPTAction(creature, -1, -1));
            }
        }
    }
]
