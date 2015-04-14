[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isNonToken() &&
                    otherPermanent.isCreature() && 
                    otherPermanent.getCard().isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    otherPermanent.getCard(),
                    this,
                    "PN may\$ sacrifice SN. If you do, return RN to the battlefield."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
                game.doAction(new ReanimateAction(event.getRefCard(), event.getPlayer()));
            }
        }
    }
]
