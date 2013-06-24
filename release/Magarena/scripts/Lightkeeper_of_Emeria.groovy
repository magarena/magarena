[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPayedCost payedCost) {
            if (payedCost.isKicked()) {
                game.doAction(new MagicChangeLifeAction(
                    permanent.getController(),
                    2 * payedCost.getKicker()
                ));
            }
            return MagicEvent.NONE;
        }
    }
]
