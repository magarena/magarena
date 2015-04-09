def choice = Negative("target artifact or land");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                choice,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target artifact or land\$. Its controller loses 1 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPlayer controller=it.getController();
                game.doAction(new MagicDestroyAction(it));
                game.doAction(new MagicChangeLifeAction(controller,-1));
            });
        }
    }
]
