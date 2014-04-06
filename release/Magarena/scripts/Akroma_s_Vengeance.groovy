[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Destroy all artifacts, creatures and enchantments."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets=game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.TARGET_ARTIFACT);
            targets.addAll(game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.TARGET_CREATURE));
            targets.addAll(game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.TARGET_ENCHANTMENT));
            game.doAction(new MagicDestroyAction(targets));
        }
    }
]
