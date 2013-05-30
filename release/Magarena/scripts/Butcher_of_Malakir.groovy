def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicPlayer opponent=event.getPlayer().getOpponent();
    if (opponent.controlsPermanentWithType(MagicType.Creature)) {
        game.addEvent(new MagicSacrificePermanentEvent(
            event.getPermanent(),
            opponent,
            MagicTargetChoice.SACRIFICE_CREATURE
        ));
    }
} as MagicEventAction

def event = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        action,
        "PN's opponent sacrifices a creature."
    );
}

[
    new MagicWhenPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
            return (triggerData.fromLocation == MagicLocationType.Play) ?
                event(permanent) : MagicEvent.NONE;
        }
    },
    new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent != permanent && 
                    otherPermanent.isFriend(permanent) && 
                    otherPermanent.isCreature()) ?
                event(permanent) : MagicEvent.NONE;
        }
    }
]
