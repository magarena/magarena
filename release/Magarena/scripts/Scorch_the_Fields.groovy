[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_LAND,
                new MagicDestroyTargetPicker(false),
                this,
                "Destroy target land\$. " +
                "SN deals 1 damage to each Human creature."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicDestroyAction(permanent));
                    final Collection<MagicPermanent> targets = game.filterPermanents(
                            event.getPlayer(),
                            MagicTargetFilter.TARGET_HUMAN);
                    for (final MagicPermanent target : targets) {
                        final MagicDamage damage = new MagicDamage(
                            event.getSource(),
                            target,
                            1
                        );
                        game.doAction(new MagicDealDamageAction(damage));
                    }
                }
            });
        }
    }
]
