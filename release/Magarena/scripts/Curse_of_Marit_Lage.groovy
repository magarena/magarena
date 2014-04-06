[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Tap all Islands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(
                    event.getPlayer(),
                    MagicTargetFilterFactory.ISLAND);
            for (final MagicPermanent target : targets) {
                if (target != event.getPermanent()) {
                    game.doAction(new MagicTapAction(target,true));
                }
            }
        }
    }
]
