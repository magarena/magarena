[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isNonToken() &&
                    otherPermanent.isCreature() &&
                    otherPermanent.isOwner(permanent.getController())) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    otherPermanent.getCard(),
                    this,
                    "PN may\$ sacrifice SN. If PN does, return RN to the battlefield."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new SacrificeAction(event.getPermanent()));
                game.doAction(new ReanimateAction(event.getRefCard(), event.getPlayer()));
            }
        }
    }
]
