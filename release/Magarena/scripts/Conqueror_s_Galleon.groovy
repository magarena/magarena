def endOfCombatTrigger = new AtEndOfCombatTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer endCombatPlayer) {
        return new MagicEvent(
            permanent,
            permanent,
            this,
            "Exile SN, then return it to the battlefield under PN's control."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicPermanent sourcePermanent = event.getPermanent();
        game.doAction(new RemoveFromPlayAction(sourcePermanent, MagicLocationType.Exile));
        game.doAction(new ReturnCardAction(MagicLocationType.Exile, sourcePermanent.getCard(), event.getPlayer(), MagicPlayMod.TRANSFORMED));
    }
}

[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                this,
                "Exile SN at end of combat, then return it to the battlefield transformed under PN's control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddTriggerAction(event.getRefPermanent(), endOfCombatTrigger));
        }
    }
]

