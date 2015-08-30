[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "Lands target player\$ controls don't untap during his or her next untap step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                LAND_YOU_CONTROL.filter(it) each {
                    game.doAction(ChangeStateAction.Set(
                        it,
                        MagicPermanentState.DoesNotUntapDuringNext
                    ));
                }
            });
        }
    }
]
