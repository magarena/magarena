[
    new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicRemoveFromPlayAction act) {
            return act.isPermanent(permanent) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN sacrifices a creature."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
           game.addEvent(new MagicSacrificePermanentEvent(
                    event.getSource(),
                    event.getPlayer(),
                    MagicTargetChoice.SACRIFICE_CREATURE
                ));
        }
    }
]
