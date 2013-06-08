def WhenDies = new MagicWhenDiesTrigger() {
    @Override
    public MagicEvent getEvent(final MagicPermanent permanent) {
        return new MagicEvent(
            permanent,
            this,
            "PN puts a green and white Elemental creature token onto the battlefield with " +
            "\"This creature's power and toughness are each equal to the number of creatures you control.\""
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicPlayTokenAction(event.getPlayer(), TokenCardDefinitions.get("GW Elemental")));
    }
}

[
    WhenDies,
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return permanent.isEnemy(cardOnStack) && permanent.isController(game.getTurnPlayer()) ?
                WhenDies.getEvent(permanent) : MagicEvent.NONE;
        }
    }
]
