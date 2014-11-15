def EFFECT1 = MagicRuleEventAction.create("SN deals 3 damage to target player.");

def EFFECT2 = MagicRuleEventAction.create("SN deals 4 damage to target creature with flying.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    MagicTargetChoice.NEG_TARGET_CREATURE_WITH_FLYING
                ),
                this,
                "Choose one\$ - SN deals 3 damage to target player; " +
                "or SN deals 4 damage to target creature with flying."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                game.addEvent(EFFECT1.getEvent(event.getSource()));

            } else if (event.isMode(2)) {
                game.addEvent(EFFECT2.getEvent(event.getSource()));
            }
        }
    }
]
