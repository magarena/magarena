def AB = new MagicStatic(MagicLayer.Ability) {
    @Override
    public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
        flags.remove(MagicAbility.Defender);
    }
}

[
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent blocker) {
            return (permanent == blocker) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN loses defender."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddStaticAction(event.getPermanent(), AB));
        }
    }
]
