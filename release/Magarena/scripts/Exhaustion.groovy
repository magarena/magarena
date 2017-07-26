[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "Creatures and lands target opponent\$ controls don't untap during that opponent's next untap step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player ->
                CREATURE_OR_LAND_YOU_CONTROL.filter(player) each {
                    game.doAction(ChangeStateAction.DoesNotUntapDuringNext(it, player));
                }
            });
        }
    }
]
