[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isNonToken() &&
                    otherPermanent.isCreature() &&
                    otherPermanent.isOwner(permanent.getController())) ?
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
                CardDefinitions.getToken("1/1 white Spirit creature token with flying")
            ));
        }
    }
]
