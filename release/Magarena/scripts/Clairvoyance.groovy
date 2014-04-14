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
        public void executeEvent(final MagicGame outerGame, final MagicEvent outerEvent) {
            MagicPlayer outerPlayer = outerEvent.getPlayer();
            MagicSource outerSource = outerEvent.getSource();
            outerEvent.processTargetPlayer(outerGame, {
                MagicPlayer player ->
                outerGame.doAction(new MagicRevealAction(player.getHand()));
            });
            outerGame.doAction(new MagicAddTriggerAction(new MagicAtUpkeepTrigger() {
                @Override
                public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
                    new MagicEvent(
                        outerSource,
                        outerPlayer,
                        this,
                        "PN draws a card"
                    );
                }

                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicDrawAction(outerPlayer));
                    game.doAction(new MagicRemoveTriggerAction(this));
                }
            }));         
        }
    }
]
