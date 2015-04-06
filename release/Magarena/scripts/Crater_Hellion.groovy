[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "SN deals 4 damage to each other creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final Collection<MagicPermanent> creatures = game.filterPermanents(
                MagicTargetFilterFactory.CREATURE.except(permanent)
            );
            for (final MagicPermanent creature : creatures) {
                game.doAction(new MagicDealDamageAction(permanent,creature,4));
            }
        }
    }
]
