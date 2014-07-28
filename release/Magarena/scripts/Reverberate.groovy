[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_INSTANT_OR_SORCERY_SPELL,
                this,
                "Copy target instant or sorcery spell\$. You may choose new targets for the copy."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.doAction(new MagicCopyCardOnStackAction(event.getPlayer(),it));
            });
        }
    }
]
