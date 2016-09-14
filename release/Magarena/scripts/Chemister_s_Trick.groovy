[
    new MagicOverloadActivation(MagicTiming.MustAttack) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [
                MagicPayManaCostEvent.Cast(source,"{3}{U}{R}")
            ];
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each creature PN doesn't control gets -2/-0 until end of turn and attacks this turn if able."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE_YOUR_OPPONENT_CONTROLS.filter(event.getPlayer()) each {
                game.doAction(new ChangeTurnPTAction(it, -2, 0));
                game.doAction(new GainAbilityAction(it, MagicAbility.AttacksEachTurnIfAble));
            }
        }
    }
]
