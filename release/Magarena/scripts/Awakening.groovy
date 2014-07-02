[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Untap all creatures and lands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final Collection<MagicPermanent> permanents = game.filterPermanents(MagicTargetFilterFactory.CREATURE_OR_LAND);
            for (final MagicPermanent permanent : permanents) {
                game.doAction(new MagicUntapAction(permanent));
            }
        }
    }
]
