def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.doAction(new MagicChangeLifeAction(event.getPlayer(),5));
    }
} as MagicEventAction

def event = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        new MagicSimpleMayChoice(
            MagicSimpleMayChoice.GAIN_LIFE,
            5,
            MagicSimpleMayChoice.DEFAULT_YES
        ),
        action,
        "PN may\$ gain 5 life."
    );
}

[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return event(permanent);
        }
    },
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent!=permanent &&
                    otherPermanent.isFriend(permanent) &&
                    otherPermanent.isCreature() &&
                    otherPermanent.getPower()>=5) ?
                event(permanent) : MagicEvent.NONE;
        }
    }
]
