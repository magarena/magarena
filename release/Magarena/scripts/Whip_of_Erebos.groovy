def ExileAtLeaving = new MagicWhenLeavesPlayTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
            if (act.isPermanent(permanent) && act.getToLocation() != MagicLocationType.Exile) {
                act.setToLocation(MagicLocationType.Exile);
            }
            return MagicEvent.NONE;
        }
    };
[
    new MagicPermanentActivation([MagicCondition.SORCERY_CONDITION],new MagicActivationHints(MagicTiming.Animate),"Animate"){
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), 
                new MagicPayManaCostEvent(source,"{2}{B}{B}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "Return target creature card\$ from your graveyard to the battlefield. "+
                "It gains haste. Exile it at the beginning of the next end step. "+
                "If it would leave the battlefield, exile it instead of putting it anywhere else."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                final MagicCard targetCard ->
                game.doAction(new MagicRemoveCardAction(targetCard,MagicLocationType.Graveyard));
                final MagicPlayCardAction action = new MagicPlayCardAction(targetCard,event.getPlayer());
                game.doAction(action);
                final MagicPermanent permanent = action.getPermanent();
                game.doAction(new MagicGainAbilityAction(permanent,MagicAbility.Haste,MagicStatic.UntilEOT));
                game.doAction(new MagicGainAbilityAction(permanent,MagicAbility.ExileAtEnd,MagicStatic.UntilEOT));
                game.doAction(new MagicAddTriggerAction(permanent, ExileAtLeaving));
            });
        }
    }
]
