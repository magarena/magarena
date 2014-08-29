[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
			MagicTargetChoice.NEG_TARGET_CREATURE,
                this,
                "Target creature\$ gets -X/-X until end of turn, where X is the number of Zombies on the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
		event.processTargetPermanent(game, {
		final int amount =game.getNrOfPermanents(MagicSubType.Zombie);
            game.doAction(new MagicChangeTurnPTAction(it,-amount,-amount));
	});
        }
    }
]
