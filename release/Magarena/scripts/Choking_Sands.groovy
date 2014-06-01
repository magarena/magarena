[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.Negative("Target non-Swamp land"),
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target non-Swamp land\$. If that land was nonbasic, SN deals 2 damage to that land's controller."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(new MagicDestroyAction(permanent));
                if (!permanent.hasType(MagicType.Basic)) {
                    final MagicDamage damage=new MagicDamage(event.getSource(),permanent.getController(),2);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    }
]
