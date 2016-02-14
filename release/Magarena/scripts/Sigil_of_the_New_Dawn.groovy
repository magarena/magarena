[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature() &&
                    otherPermanent.isOwner(permanent.getController())) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{1}{W}"))
                    ),
                    otherPermanent.getCard(),
                    this,
                    "PN may pay {1}{W}\$. If PN does, return RN to his or her hand."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes() && event.getRefCard().isInGraveyard()) {
                game.doAction(new ShiftCardAction(event.getRefCard(), MagicLocationType.Graveyard, MagicLocationType.OwnersHand));
            }
        }
    }
]
