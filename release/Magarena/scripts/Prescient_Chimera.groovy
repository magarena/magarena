[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return cardOnStack.isFriend(permanent) && cardOnStack.isInstantOrSorcerySpell() ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN Scry 1."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicScryEvent(event));
        }
    }
]
