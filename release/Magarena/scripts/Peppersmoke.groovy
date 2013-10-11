[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE,
                new MagicWeakenTargetPicker(1, 1),
                this,
                "Target creature\$ gets -1/-1 until end of turn. " +
				"If you control a Faerie, draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature, -1, -1));
					if(event.getPlayer().getNrOfPermanents(MagicSubType.Faerie) > 0){
						game.doAction(new MagicDrawAction(event.getPlayer()));
					}
                }
            });
        }
    }
]
