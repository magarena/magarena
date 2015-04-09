[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_LAND,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target land\$. " +
                "If that land was nonbasic, SN deals 2 damage to the land's controller."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDestroyAction(it));
                if (!it.isBasic()) {
                    game.doAction(new MagicDealDamageAction(event.getSource(),it.getController(),2));
                }
            });
        }
    }
]
