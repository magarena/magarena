[
    new MagicWhenSelfTurnedFaceUpTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent faceUp) {
            return new MagicEvent(
                permanent,
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
