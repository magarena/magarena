def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicTuple tup = event.getRefTuple();
    if (event.isYes()) {
        game.doAction(new MoveCardAction(tup.getCard(0), tup.getLocationType(1), MagicLocationType.TopOfOwnersLibrary));
    } else {
        //needs to be delayed so it occurs after trigger is remove
        game.addDelayedAction(new MoveCardAction(tup.getCard(0), tup.getLocationType(1), MagicLocationType.Graveyard));
    }
}

def PulmonicTrigger = new WouldBeMovedTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MoveCardAction act) {
        if (permanent == act.permanent && act.to(MagicLocationType.Graveyard)) {
            //disable the current move action
            act.setToLocation(MagicLocationType.Battlefield);

            game.addEvent(new MagicEvent(
                permanent,
                new MagicMayChoice("Put ${permanent} on top of its owner's library?"),
                new MagicTuple(act.card, act.fromLocation),
                action,
                "PN may\$ put SN on top of its owner's library."
            ));
        }
        return MagicEvent.NONE;
    }
}

[
    new MagicStatic(MagicLayer.Ability, SLIVER) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(PulmonicTrigger);
        }
    }
]
