[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return permanent.hasState(MagicPermanentState.CastFromHand) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Destroy all creatures your opponents control, then tap all other creatures you control."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(event.getPlayer().getOpponent(),CREATURE_YOU_CONTROL);
                game.doAction(new DestroyAction(targets));
            final Collection<MagicPermanent> targets2 = game.filterPermanents(
                event.getPlayer(),
                CREATURE_YOU_CONTROL.except(event.getPermanent())
                );
                for (final MagicPermanent creature : targets2) {                    game.doAction(new TapAction(creature));
            } 
        }
    }
]
