def Action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new PlayTokenAction(event.getPlayer(), TokenCardDefinitions.get("green and white Elemental creature token")));
}

def Event = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        Action,
        "PN puts a green and white Elemental creature token onto the battlefield with " +
        "\"This creature's power and toughness are each equal to the number of creatures you control.\""
    );
}

[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return Event(permanent);
        }
    },
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return permanent.isEnemy(cardOnStack) && permanent.isController(game.getTurnPlayer()) ?
                Event(permanent) : MagicEvent.NONE;
        }
    }
]
