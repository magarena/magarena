[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_NONCREATURE,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target noncreature permanent\$. " +
                "If you control a Treefolk, draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
                final MagicPlayer you = event.getPlayer();
                if (you.controlsPermanent(MagicSubType.Treefolk)){
                    game.doAction(new DrawAction(you));
                }
            });
        }
    }
]
