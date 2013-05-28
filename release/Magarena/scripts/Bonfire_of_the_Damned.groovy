[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                new MagicDamageTargetPicker(amount),
                this,
                "SN deals " + amount +
                " damage to target player\$ and each creature he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    final int amount = event.getCardOnStack().getX();
                    MagicDamage damage = new MagicDamage(event.getSource(), player, amount);
                    game.doAction(new MagicDealDamageAction(damage));
                    final Collection<MagicPermanent> targets = game.filterPermanents(
                            player,
                            MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
                    for (final MagicPermanent target : targets) {
                        damage = new MagicDamage(event.getSource(), target, amount);
                        game.doAction(new MagicDealDamageAction(damage));
                    }
                }
            });
        }
    }
]
