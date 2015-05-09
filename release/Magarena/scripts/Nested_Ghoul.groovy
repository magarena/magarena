[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return damage.getTarget() == permanent ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a 2/2 black Zombie creature token onto the battlefield."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken("2/2 black Zombie creature token")
            ));
        }
    }
]
