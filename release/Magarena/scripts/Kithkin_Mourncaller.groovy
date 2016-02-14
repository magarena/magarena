[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isAttacking() &&
                    (otherPermanent.hasSubType(MagicSubType.Elf) || otherPermanent.hasSubType(MagicSubType.Kithkin)) &&
                    otherPermanent.isOwner(permanent.getController())) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.DRAW_CARDS,
                        1,
                        MagicSimpleMayChoice.DEFAULT_NONE
                    ),
                    this,
                    "PN may\$ draw a card."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes() && event.getRefCard().isInGraveyard()) {
                game.doAction(new DrawAction(event.getPlayer()));
            }
        }
    }
]
