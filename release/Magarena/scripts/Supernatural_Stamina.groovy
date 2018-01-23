def trigger = new ThisDiesTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
        return new MagicEvent(
            permanent,
            permanent.getOwner(),
            permanent.getCard(),
            this,
            "Return SN to the battlefield tapped under PN's control."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicPermanent source = (MagicPermanent)event.getSource();
        game.doAction(new ReanimateAction(
            event.getRefCard(),
            event.getPlayer(),
            MagicPlayMod.TAPPED
        ));
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                this,
                "Until end of turn, target creature\$ gets +2/+0 " +
                "and gains \"When creature dies, return it to the battlefield tapped under its owner's control.\""
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ChangeTurnPTAction(it, 2, 0));
                game.doAction(new AddTurnTriggerAction(it, trigger));
            });
        }
    }
]

