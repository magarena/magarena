[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature() &&
                    otherPermanent.hasSubType(MagicSubType.Rogue)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    otherPermanent,
                    this,
                    "PN may\$ attach SN to RN."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new AttachAction(
                    event.getPermanent(),
                    event.getRefPermanent()
                ));
            }
        }
    }
]
