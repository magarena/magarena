[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isNonToken() &&
                    otherPermanent.isCreature() && 
                    otherPermanent.getCard().isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a 1/1 white Spirit creature token with flying onto the battlefield."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokenAction(
                event.getPlayer(), 
                TokenCardDefinitions.get("1/1 white Spirit creature token with flying")
            ));
        }
    }
]
