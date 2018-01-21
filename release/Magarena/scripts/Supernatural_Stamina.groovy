def trigger = new ThisDiesTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
        return new MagicEvent(
            permanent,
            this,
            "Return SN to the battlefield under ${permanent.getOwner()}'s control."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new PlayCardAction(
            event.getSource(),
            event.getSource().getOwner(),
            { TapAction.Enters(it) }
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
                "and gains \"When this creature dies, return it to the battlefield tapped under its owner's control."
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

