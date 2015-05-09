[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource source = damage.getSource();
            return (damage.isCombat() &&
                    permanent.isOpponent(damage.getTarget()) &&
                    source.hasType(MagicType.Artifact)) ?
                new MagicEvent(
                    permanent,
                    source.getController(),
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.PLAY_TOKEN,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    this,
                    "PN may\$ put a 1/1 blue Thopter artifact creature token with flying onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    CardDefinitions.getToken("1/1 blue Thopter artifact creature token with flying")
                ));
            }
        }
    }
]
