def AURA_YOU_CONTROL_ATTACHED_TO_CREATURE = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.getEnchantedPermanent().hasType(MagicType.Creature) && target.isController(player);
        }
    };
    
[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each enchantment deals 2 damage to its controller, then each Aura attached to a creature "+
                "deals 2 damage to the creature it's attached to."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer sourcePlayer = event.getPlayer();
            for (final MagicPlayer player : game.getAPNAP()) {
                for (final MagicPermanent enchantment : ENCHANTMENT_YOU_CONTROL.filter(player)) {
                    game.doAction(new DealDamageAction(enchantment, enchantment.getController(), 2));
                    game.logAppendMessage(sourcePlayer, enchantment.getName()+" deals 2 damage to (${enchantment.getController().getName()})")
                }
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                for (final MagicPermanent aura : AURA_YOU_CONTROL_ATTACHED_TO_CREATURE.filter(player)) {
                    final MagicPermanent enchanted = aura.getEnchantedPermanent();
                    game.doAction(new DealDamageAction(aura, enchanted, 2));
                    game.logAppendMessage(sourcePlayer, aura.getName()+" deals 2 damage to (${enchanted.getName()})")
                }
            }
        }
    }
]
