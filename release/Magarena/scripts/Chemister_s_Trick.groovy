[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_YOU_DONT_CONTROL,
                MagicTapTargetPicker.Tap,
                this,
                "Target creature\$ PN doesn't control gets -2/-0 until end of turn and attacks this turn if able."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ChangeTurnPTAction(it, -2, 0));
                game.doAction(new GainAbilityAction(it, MagicAbility.AttacksEachTurnIfAble));
            });
        }
    },
    new MagicOverloadActivation(MagicTiming.MustAttack) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source,"{3}{U}{R}")
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
