[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.Negative("target red spell"),
                    MagicTargetChoice.Negative("target red permanent"),
                ),
                0,
                this,
                "Choose one\$ - Counter target red spell; or destroy target red permanent."
            );
        }
        public MagicEvent counterRed(final MagicSource source) {
            return new MagicEvent(
                source,
                MagicTargetChoice.Negative("target red spell"),
                1,
                this,
                "Counter target red spell\$"
            );
        }
        public MagicEvent destroyRed(final MagicSource source) {
            return new MagicEvent(
                source,
                MagicTargetChoice.Negative("target red permanent"),
                MagicDestroyTargetPicker.Destroy,
                2,
                this,
                "Destroy target red permanent\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getRefInt() == 0 && event.isMode(1)) {
                game.addEvent(counterRed(event.getSource()));
            } else if (event.getRefInt() == 0 && event.isMode(2)) {
                game.addEvent(destroyRed(event.getSource()));
            } else if (event.getRefInt() == 1) {
                event.processTargetCardOnStack(game, {
                    final MagicCardOnStack spell ->
                    if (spell.hasColor(MagicColor.Red)) {
                        game.doAction(new MagicCounterItemOnStackAction(spell));
                    }
               });
            } else if (event.getRefInt() == 2) {
                event.processTargetPermanent(game, {
                    final MagicPermanent permanent ->
                    if (permanent.hasColor(MagicColor.Red)) {
                        game.doAction(new MagicDestroyAction(permanent));
                    }
                });
            }
        }
        @Override
        public boolean usesStack() {
            return false;
        }
    }
]
