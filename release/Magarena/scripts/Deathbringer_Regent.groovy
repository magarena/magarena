[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return (permanent.hasState(MagicPermanentState.CastFromHand) &&
                   game.getNrOfPermanents(CREATURE.except(permanent)) >=5) ? 
                new MagicEvent(
                    permanent,
                    this,
                    "Destroy all other creatures."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = CREATURE.except(event.getPermanent()).filter(event);
            if (targets.size() >= 5) {
                game.doAction(new DestroyAction(targets));
            }
        }
    }
]
