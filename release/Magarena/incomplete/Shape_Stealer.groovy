[
    new MagicWhenBlocksOrBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent target = permanent == blocker ? blocker.getBlockedCreature() : blocker;
            return new MagicEvent(
                    permanent,
                    target,
                    this,
                    "Change SN's power and toughness to RN's power and toughness until end of turn."
                );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicStatic(
                MagicLayer.SetPT,
                MagicStatic.UntilEOT) {
                @Override
                public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
                    pt.set(event.getRefPermanent().getPower(),event.getRefPermanent().getToughness());
                }
            };);
        }
    }
]
