[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return new MagicEvent(
                permanent,
                cardOnStack,
                this,
                "When a player casts a spell, sacrifice SN. If you do, each of that player's opponents draws three cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicEvent sac = new MagicSacrificeEvent(event.getPermanent());
        game.addEvent(sac);
        if (sac.isSatisfied()) {
                game.doAction(new MagicDrawAction(event.getRefCardOnStack().getOpponent(),3));
        }
        }
    }
]
