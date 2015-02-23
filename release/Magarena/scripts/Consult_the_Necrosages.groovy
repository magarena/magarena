def DRAW = MagicRuleEventAction.create("Target player draws two cards.");

def DISCARD = MagicRuleEventAction.create("Target player discards two cards.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.TARGET_PLAYER,
                    MagicTargetChoice.TARGET_PLAYER
                ),
                this,
                "Choose one\$ - Target player draws two cards; or target player discards two cards.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, DRAW, DISCARD);
        }
    }
]
