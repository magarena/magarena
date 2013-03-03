def T = new MagicWhenDamageIsDealtTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
        return (damage.getSource().isCreature() &&
                damage.isCombat() &&
                damage.getTarget() == permanent) ?
            new MagicEvent(
                permanent,
                damage.getSource(),
                this,
                "Destroy RN."
            ):
            MagicEvent.NONE;
    }
    
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] choiceResults) {
        game.doAction(new MagicDestroyAction(event.getRefPermanent()));
    }
}

[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Until PN's next turn, whenever a creature deals combat damage to Vraska the Unseen, destroy that creature."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame outerGame,
                final MagicEvent event,
                final Object[] choiceResults) {
            outerGame.doAction(new MagicAddTriggerAction(event.getPermanent(), T));
            // remove the trigger during player's next upkeep
            outerGame.doAction(new MagicAddTriggerAction(event.getPermanent(), new MagicAtUpkeepTrigger() {
                @Override
                public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
                    if (upkeepPlayer.getId() == event.getPlayer().getId()) {
                        game.addDelayedAction(new MagicRemoveTriggerAction(permanent, T));
                        game.addDelayedAction(new MagicRemoveTriggerAction(permanent, this));
                    }
                    return MagicEvent.NONE;
                }
            }));
        }
    },
    new MagicPlaneswalkerActivation(-3) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_NONLAND_PERMANENT,
                new MagicDestroyTargetPicker(false),
                this,
                "Destroy target nonland permanent."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicDestroyAction(creature));
                }
            });
        }
    },
    new MagicPlaneswalkerActivation(-7) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put three 1/1 black Assassin creature tokens onto the battlefield with " + 
                "\"Whenever this creature deals combat damage to a player, that player loses the game.\""
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicPlayTokensAction(event.getPlayer(), TokenCardDefinitions.get("Assassin"), 3));
        }
    }
]
