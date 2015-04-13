[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            return permanent.isController(eotPlayer) ?
                new MagicEvent(
                    permanent,
                    TARGET_CREATURE_YOU_CONTROL,
                    MagicPowerTargetPicker.create(),
                    this,
                    "PN gains life equal to the power of target creature\$ he or she controls."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ChangeLifeAction(event.getPlayer(),it.getPower()));
            });
        }
    }
]
