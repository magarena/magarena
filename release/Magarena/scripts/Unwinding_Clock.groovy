[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            if (permanent.isOpponent(upkeepPlayer)) {
                final Collection<MagicPermanent> targets = game.filterPermanents(permanent.getController(),MagicTargetFilterFactory.ARTIFACT_YOU_CONTROL);
                for (final MagicPermanent permanent : targets) {
                    game.doAction(new MagicUntapAction(permanent));
                }
            }
            return MagicEvent.NONE;
        }
    }
]
