[
    new LeavesBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final RemoveFromPlayAction act) {
            return act.getPermanent() == permanent.getEnchantedPermanent() &&
               (act.to(MagicLocationType.Graveyard) || act.to(MagicLocationType.OpponentsGraveyard)) ?
                 new MagicEvent(
                    permanent,
                    this,
                    "PN gains 6 life."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(), 6));
        }
    }
]
