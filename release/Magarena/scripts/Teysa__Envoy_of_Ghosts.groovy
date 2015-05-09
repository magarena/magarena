[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (permanent.isController(damage.getTarget()) &&
                    damage.isCombat() &&
                    damage.getSource().isCreature()) ?
                new MagicEvent(
                    permanent,
                    damage.getSource(),
                    this,
                    "Destroy RN. " +
                    "PN puts a 1/1 white and black Spirit creature token with flying onto the battlefield."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DestroyAction(event.getRefPermanent()));
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken("1/1 white and black Spirit creature token with flying")
            ));
        }
    }   
]
