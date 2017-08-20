[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return new MagicEvent(
                permanent,
                permanent.getController(),
                cardOnStack.getOpponent(),
                this,
                "PN sacrifices SN. If PN does, RN draws three cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent cost = new MagicSacrificeEvent(event.getPermanent());
            if (cost.isSatisfied()) {
                game.addEvent(cost);
                game.addEvent(new MagicDrawEvent(event.getSource(), event.getRefPlayer(), 3));
            }
        }
    }
]
