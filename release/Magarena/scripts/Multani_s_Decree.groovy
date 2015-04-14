[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Destroy all enchantments. " +
                "PN gains 2 life for each enchantment destroyed this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final Collection<MagicPermanent> targets =
                game.filterPermanents(player,ENCHANTMENT);
            final DestroyAction destroy = new DestroyAction(targets);
            game.doAction(destroy);
            game.doAction(new ChangeLifeAction(player,destroy.getNumDestroyed()*2));
        }
    }
]
