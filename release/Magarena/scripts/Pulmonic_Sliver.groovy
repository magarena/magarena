def PulmonicTrigger = new MagicWouldBeMovedTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MoveCardAction act) {
        if (permanent == act.permanent && act.getToLocation() == MagicLocationType.Graveyard) {
            //disable the current move action
            act.setToLocation(MagicLocationType.Play);

            game.addEvent(new MagicEvent(
                permanent,
                new MagicMayChoice("Put ${permanent} on top of its owner's library?"),
                act.card,
                {
                    final MagicGame G, final MagicEvent E ->
                    if (E.isYes()) {
                        G.doAction(new MoveCardAction(E.getRefCard(), act.fromLocation, MagicLocationType.TopOfOwnersLibrary));
                    } else {
                        //needs to be delayed so it occurs after trigger is remove
                        G.addDelayedAction(new MoveCardAction(E.getRefCard(), act.fromLocation, MagicLocationType.Graveyard));
                    }
                },
                "PN may\$ put SN on top of its owner's library."
            ));
        }
        return MagicEvent.NONE;
    }
};

[
    new MagicStatic(MagicLayer.Ability, SLIVER) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(PulmonicTrigger);
        }
    }
]
