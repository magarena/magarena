[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN returns to his or her hand all enchantments he or she both owns and controls, "+
                "all Auras PN owns attached to permanents he or she controls, "+
                "and all Auras PN owns attached to attacking creatures PN's opponents control. "+
                "Then destroy all other enchantments PN controls, all other Auras attached to permanents PN controls, "+
                "and all other Auras attached to attacking creatures PN's opponents control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final List<MagicPermanent> enchantments = game.filterPermanents(player, ENCHANTMENT_YOU_OWN_AND_CONTROL);
            for (final MagicPermanent permanent : enchantments) {
                game.doAction(new RemoveFromPlayAction(permanent, MagicLocationType.OwnersHand));
            }
            final List<MagicPermanent> auras = game.filterPermanents(player, AURA_YOU_OWN);
            for (final MagicPermanent permanent : auras) {
                final MagicPermanent enchantedPermanent = permanent.getEnchantedPermanent();
                final MagicPlayer enchantedController = enchantedPermanent.getController();
                if (enchantedController == player || 
                        (enchantedController == player.getOpponent() &&
                            (enchantedPermanent.isCreature() && enchantedPermanent.isAttacking())
                        )
                    ) {
                    game.doAction(new RemoveFromPlayAction(permanent, MagicLocationType.OwnersHand));
                }
            }
            final List<MagicPermanent> destroyEnchantment = game.filterPermanents(player, ENCHANTMENT_YOU_CONTROL);
            game.doAction(new DestroyAction(destroyEnchantment));
            final List<MagicPermanent> destroyAura = game.filterPermanents(player, AURA);
            for (final MagicPermanent permanent : destroyAura) {
                final MagicPermanent enchantedPermanent = permanent.getEnchantedPermanent();
                final MagicPlayer enchantedController = enchantedPermanent.getController();
                if (enchantedController == player || 
                        (enchantedController == player.getOpponent() &&
                            (enchantedPermanent.isCreature() && enchantedPermanent.isAttacking())
                        )
                    ) {
                    game.doAction(new DestroyAction(permanent));
                }
            }
        }
    }
]
// All permanents returning to hand should return at the same time. All permanents being destroyed should be destroyed at the same time.
