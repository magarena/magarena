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
            
            ENCHANTMENT_YOU_OWN_AND_CONTROL.filter(event) each {
                game.doAction(new RemoveFromPlayAction(it, MagicLocationType.OwnersHand));
            }

            AURA_YOU_OWN.filter(event) each {
                final MagicPermanent enchanted = it.getEnchantedPermanent();
                if (enchanted.isFriend(player) || (enchanted.isEnemy(player) && enchanted.isAttacking())) {
                    game.doAction(new RemoveFromPlayAction(it, MagicLocationType.OwnersHand));
                }
            }
            
            game.doAction(new DestroyAction(ENCHANTMENT_YOU_CONTROL.filter(event)));
            
            AURA.filter(event) each {
                final MagicPermanent enchanted = it.getEnchantedPermanent();
                if (enchanted.isFriend(player) || (enchanted.isEnemy(player) && enchanted.isAttacking())) {
                    game.doAction(new DestroyAction(it));
                }
            }
        }
    }
]
// All permanents returning to hand should return at the same time. All permanents being destroyed should be destroyed at the same time.
