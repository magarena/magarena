def AB = new MagicStatic(MagicLayer.Ability, MagicStatic.UntilEOT) {
    @Override
    public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
        flags.remove(MagicAbility.FirstStrike);
    }
};

[
    new MagicWhenBlocksOrBecomesBlockedByTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            return new MagicEvent(
                permanent,
                permanent == blocker ? blocker.getBlockedCreature() : blocker,
                this,
                "RN loses first strike until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processRefPermanent(game, {
                game.doAction(new AddStaticAction(it, AB));
            });
        }
    }
]
