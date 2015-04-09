[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            if (permanent.isOpponent(upkeepPlayer)) {
                final Collection<MagicPermanent> targets = game.filterPermanents(permanent.getController(),ARTIFACT_YOU_CONTROL);
                for (final MagicPermanent artifact : targets) {
                    game.doAction(new MagicUntapAction(artifact));
                }
            }
            return MagicEvent.NONE;
        }
    }
]
