[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Untap up to five lands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicRepeatedPermanentsEvent(
                event.getSource(),
                MagicTargetChoice.TARGET_LAND,
                5,
                MagicChainEventFactory.Untap
            ));
        }
    }
]
