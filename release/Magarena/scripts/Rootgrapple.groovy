[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_NONCREATURE,
                new MagicDestroyTargetPicker(false),
                this,
                "Destroy target noncreature permanent\$. " +
				"If you control a Treefolk, draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent targetPermanent) {
                    game.doAction(new MagicDestroyAction(targetPermanent));
					if(event.getPlayer().getNrOfPermanents(MagicSubType.Treefolk) > 0){
						game.doAction(new MagicDrawAction(event.getPlayer()));
					}
                }
            });
        }
    }
]
