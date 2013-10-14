[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
            return permanent.isController(player) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Untap all Merfolk PN controls."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = event.getPlayer().filterPermanents(
                MagicTargetFilter.TARGET_MERFOLK_YOU_CONTROL
            );
            for (final MagicPermanent creature : targets) {
                game.doAction(new MagicUntapAction(creature));
            }
        }
    }
]
