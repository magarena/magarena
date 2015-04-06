[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Tap all other creatures."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicTargetFilter<MagicPermanent> filter = new MagicOtherPermanentTargetFilter(
                MagicTargetFilterFactory.CREATURE,
                event.getPermanent()
            );
            final Collection<MagicPermanent> targets = game.filterPermanents(filter);
            for (final MagicPermanent creature : targets) {
                game.doAction(new MagicTapAction(creature));
            }
        }
    }
]
