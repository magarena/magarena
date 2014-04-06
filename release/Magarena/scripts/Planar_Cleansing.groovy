[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Destroy all nonland permanents."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets=game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.TARGET_NONLAND_PERMANENT);
            game.doAction(new MagicDestroyAction(targets));
        }
    }
]
