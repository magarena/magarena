[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return permanent.hasState(MagicPermanentState.CastFromHand) ? 
                new MagicEvent(
                    permanent,
                    this,
                    "Tap all nonblue creatures. Those creatures don't untap during their controllers' next untap steps."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            NONBLUE_CREATURE.filter(game) each {
                game.doAction(new MagicTapAction(it));
                game.doAction(MagicChangeStateAction.Set(
                    it,
                    MagicPermanentState.DoesNotUntapDuringNext
                ));     
            }
        }
    }
]
