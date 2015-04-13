def choice = Negative("target creature with Flying");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                choice,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target creature with flying\$. Its controller gets a poison counter."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDestroyAction(it));
                game.doAction(new ChangePoisonAction(it.getController(),1));
            });
        }
    }
]
