[
    new MagicWhenBlocksOrBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent target = permanent == blocker ? blocker.getBlockedCreature() : blocker;
            return (target.hasColor(MagicColor.White)) ?
                new MagicEvent(
                    permanent,
                    target,
                    this,
                    "SN gains first strike until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicGainAbilityAction(
                    event.getPermanent(),
                    MagicAbility.FirstStrike
                ));
        }
    }
]
