[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return cardOnStack.isFriend(permanent) && cardOnStack.isInstantOrSorcerySpell() ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a 1/1 red Elemental creature token onto the battlefield."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(
                event.getPlayer(), 
                TokenCardDefinitions.get("1/1 red Elemental creature token")
            ));
        }
    }
]
