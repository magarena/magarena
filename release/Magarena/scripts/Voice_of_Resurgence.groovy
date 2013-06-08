def action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new MagicPlayTokenAction(event.getPlayer(), TokenCardDefinitions.get("GW Elemental")));
} as MagicEventAction

def getEvent = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        action,
        "PN puts a green and white Elemental creature token onto the battlefield with " +
        "\"This creature's power and toughness are each equal to the number of creatures you control.\""
    );
}

[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return getEvent(permanent);
        }
    },
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return permanent.isEnemy(cardOnStack) && permanent.isController(game.getTurnPlayer()) ?
                getEvent(permanent) : MagicEvent.NONE;
        }
    }
]
