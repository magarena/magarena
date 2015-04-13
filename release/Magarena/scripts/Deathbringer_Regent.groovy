[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return (permanent.hasState(MagicPermanentState.CastFromHand) &&
                   game.getNrOfPermanents(MagicType.Creature) >=5) ? 
                new MagicEvent(
                    permanent,
                    this,
                    "Destroy all other creatures."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets= game.filterPermanents(
                event.getPermanent().getController(),
                CREATURE.except(event.getPermanent())
            );
            game.doAction(new MagicDestroyAction(targets));
        }
    }
]
