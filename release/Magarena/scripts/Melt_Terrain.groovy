[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_LAND,
                new MagicDestroyTargetPicker(false),
                this,
                "Destroy target land\$. " +
                "SN deals 2 damage to that land's controller."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicDestroyAction(permanent));
                    final MagicDamage damage = new MagicDamage(event.getSource(),permanent.getController(),2);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    }
]
