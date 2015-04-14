[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return new MagicEvent(
                permanent,
                permanent.getController(),
                cardOnStack.getOpponent(),
                this,
                "PN sacrifices SN. If you do, RN draws three cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSacrificeAction sac = new MagicSacrificeAction(event.getPermanent());
            game.doAction(sac);
            if (sac.isValid()) {
                game.doAction(new DrawAction(event.getRefPlayer(),3));
            }
        }
    }
]
