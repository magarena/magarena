[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPermanent otherPermanent) {
            return (otherPermanent.hasType(MagicType.Artifact)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    this,
                    "PN may\$ untap SN."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()){
            game.doAction(new MagicUntapAction(event.getPermanent()));
            }
        }
    }
]
