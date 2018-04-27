[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts all land cards from PN's graveyard onto the battlefield tapped."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            card(MagicType.Land).from(MagicTargetType.Graveyard).filter(event.getPlayer()) each {
                game.doAction(new ReturnCardAction(
                    MagicLocationType.Graveyard,
                    it,
                    event.getPlayer(),
                    MagicPlayMod.TAPPED
                ));
            }
        }
    }
]

