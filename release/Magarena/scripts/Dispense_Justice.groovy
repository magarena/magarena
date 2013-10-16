[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "Target player\$ sacrifices a creature. " +
				"If you control three or more artifacts, he or she sacrifices two creatures instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    game.addEvent(new MagicSacrificePermanentEvent(
                        event.getSource(),
                        player,
                        MagicTargetChoice.POS_TARGET_ATTACKING_CREATURE
                    ));
                    if (MagicCondition.METALCRAFT_CONDITION.accept(event.getSource())) {
                        game.addEvent(new MagicSacrificePermanentEvent(
							event.getSource(),
							player,
							MagicTargetChoice.POS_TARGET_ATTACKING_CREATURE
						));
                    }
                }
            });
        }
    }
]
