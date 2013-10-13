[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
            if (permanent.isController(player)) {
                final Collection<MagicPermanent> targets=
                    game.filterPermanents(permanent.getController(),MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
                for (final MagicPermanent creature : targets) {
                    if (creature.isTapped() &&
                        creature.hasSubType(MagicSubType.Merfolk)) {
                        game.doAction(new MagicUntapAction(creature));
                    }
                }
            }
            return MagicEvent.NONE;
        }
    }
]
