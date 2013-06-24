[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Destroy all enchantments. " +
                "PN gains 1 life for each enchantment destroyed this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final Collection<MagicPermanent> targets =
                game.filterPermanents(player,MagicTargetFilter.TARGET_ENCHANTMENT);
            game.doAction(new MagicDestroyAction(targets));
            if (targets.size() > 0) {
                game.doAction(new MagicChangeLifeAction(player,targets.size()));
            }
        }
    }
]
