[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "Creatures target player\$ controls get -1/-1 until end of turn. " +
                "If SN was kicked, those creatures get -2/-2 until end of turn instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amount = event.isKicked() ? -2 : -1;
                CREATURE_YOU_CONTROL.filter(it) each {
                    game.doAction(new ChangeTurnPTAction(it,amount,amount));
                }
            });
        }
    }
]
