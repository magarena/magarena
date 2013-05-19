[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_NONBLACK_CREATURE,
                new MagicDestroyTargetPicker(false),
                this,
                "Destroy target nonblack creature\$. Its controller loses 2 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final MagicPlayer controller=creature.getController();
                    game.doAction(new MagicDestroyAction(creature));
                    game.doAction(new MagicChangeLifeAction(controller,-2));
                }
            });
        }
    }
]
