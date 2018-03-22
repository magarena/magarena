[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "Creatures target opponent\$ controls attack this turn if able. " +
                "During that player's next untap step, creatures he or she controls don't untap."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player ->
                CREATURE_YOU_CONTROL.filter(player) each {
                    game.doAction(new GainAbilityAction(it, MagicAbility.AttacksEachTurnIfAble));
                    game.doAction(ChangeStateAction.DoesNotUntapDuringNext(it, player));
                }
            });
        }
    }
]

