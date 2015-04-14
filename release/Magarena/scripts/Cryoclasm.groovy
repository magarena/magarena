def choice = Negative("target Plains or Island");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                choice,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target Plains or Island\$. SN deals 3 damage to that land's controller."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDestroyAction(it));
                game.doAction(new DealDamageAction(event.getSource(),it.getController(),3));
            });
        }
    }
]
