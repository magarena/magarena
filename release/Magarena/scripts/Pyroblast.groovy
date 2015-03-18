[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.TARGET_SPELL,
                    MagicTargetChoice.TARGET_PERMANENT
                ),
                this,
                "Choose one\$ - counter target spell if it's blue; " +
                "or destroy target permanent if it's blue.\$" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                event.processTargetCardOnStack(game, {
                    if (it.hasColor(MagicColor.Blue)) {
                        game.doAction(new MagicCounterItemOnStackAction(it));
                    }
                });
            } else if (event.isMode(2)) {
                event.processTargetPermanent(game, {
                    if (it.hasColor(MagicColor.Blue)) {
                       game.doAction(new MagicDestroyAction(it));
                    }
                });
            }
        }
    }
]
