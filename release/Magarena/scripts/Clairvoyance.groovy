def MagicAtUpkeepTrigger cantrip(final MagicEvent event) {
    return new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                event.getSource(),
                event.getPlayer(),
                {
                    final MagicGame game2, final MagicEvent event2 ->
                    game2.doAction(new MagicDrawAction(event2.getPlayer()));
                    game2.doAction(new MagicRemoveTriggerAction(this));
                },
                "PN draws a card"
            );
        }
    }
};         

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "PN looks at target player's\$ hand."+
                "PN draws a card at the beginning of the next turn's upkeep."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                MagicPlayer player ->
                game.doAction(new MagicRevealAction(player.getHand()));
            });
            game.doAction(new MagicAddTriggerAction(cantrip(event)));
        }
    }
]
