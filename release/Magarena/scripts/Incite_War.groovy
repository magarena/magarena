[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                payedCost.isKicked() ? 
                    NEG_TARGET_PLAYER :
                    new MagicOrChoice(
                        NEG_TARGET_PLAYER,
                        MagicChoice.NONE
                    ),
                this,
                payedCost.isKicked() ?
                    "Creatures target player\$ controls attack this turn if able. "+ 
                    "Creatures you control gain first strike until end of turn." :
                    "Choose one\$ — • Creatures target player\$ controls attack this turn if able. "+
                    "• Creatures you control gain first strike until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isKicked() || event.isMode(1)) {
                event.processTargetPlayer(game, {
                    CREATURE_YOU_CONTROL.filter(it) each {
                        game.doAction(new GainAbilityAction(it,MagicAbility.AttacksEachTurnIfAble));
                    }
                });
            }
            if (event.isKicked() || event.isMode(2)) {
                CREATURE_YOU_CONTROL.filter(event) each {
                    game.doAction(new GainAbilityAction(it,MagicAbility.FirstStrike));
                }
            }
        }
    }
]
