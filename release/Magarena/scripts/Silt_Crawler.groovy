[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN taps all lands he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> allLand = 
                game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.LAND_YOU_CONTROL);
            for (final MagicPermanent land : allLand) {
                game.doAction(new MagicTapAction(land,false));
            }
        }
    }
]
