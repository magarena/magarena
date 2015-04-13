[
    new MagicWhenBlocksOrBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent target = permanent == blocker ? blocker.getBlockedCreature() : blocker;
            return new MagicEvent(
                permanent,
                target,
                this,
                "SN has base power and toughness equal to RN's power and toughness until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature = event.getRefPermanent();
            final int power = creature.getPower();
            final int toughness = creature.getToughness();
            game.doAction(new AddStaticAction(
                event.getPermanent(), 
                new MagicStatic(
                    MagicLayer.SetPT,
                    MagicStatic.UntilEOT
                ) {
                    @Override
                    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
                        pt.set(power, toughness);
                    }
                }
            ));
        }
    }
]
