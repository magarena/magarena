[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            if (permanent.isOpponent(upkeepPlayer)) {
                final Collection<MagicPermanent> targets = game.filterPermanents(permanent.getController(),CREATURE_YOU_CONTROL);
                for (final MagicPermanent creature : targets) {
                    if (creature.isTapped() && (creature.hasColor(MagicColor.Blue) || creature.hasColor(MagicColor.Green))) {
                        game.doAction(new UntapAction(creature));
                    }
                }
            }
            return MagicEvent.NONE;
        }
    }
]
