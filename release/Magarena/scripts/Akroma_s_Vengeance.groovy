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
            final Collection<MagicPermanent> targets=game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.ARTIFACT);
            targets.addAll(game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.CREATURE));
            targets.addAll(game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.ENCHANTMENT));
            game.doAction(new MagicDestroyAction(targets));
        }
    }
]
