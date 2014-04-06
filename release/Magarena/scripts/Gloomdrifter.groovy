[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return (MagicCondition.THRESHOLD_CONDITION.accept(permanent)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Nonblack creatures get -2/-2 until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.TARGET_NONBLACK_CREATURE);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicChangeTurnPTAction(target,-2,-2));
            }
        }
    }
]
