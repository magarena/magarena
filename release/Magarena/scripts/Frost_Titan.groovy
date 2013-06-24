def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetPermanent(game,new MagicPermanentAction() {
        public void doAction(final MagicPermanent perm) {
            game.doAction(new MagicTapAction(perm,true));
            game.doAction(MagicChangeStateAction.Set(perm,MagicPermanentState.DoesNotUntapDuringNext));
        }
    });
} as MagicEventAction

def event = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        MagicTargetChoice.TARGET_PERMANENT,
        action,
        "Tap target permanent\$. It doesn't untap during its controller's next untap step."
    );
}

[
    //counter opponent spell or ability unless its controller pay {2}
    new MagicWhenTargetedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicItemOnStack target) {
            return (target.containsInChoiceResults(permanent) &&
                    target.getController() != permanent.getController()) ?
                new MagicEvent(
                    permanent,
                    target,
                    this,
                    "Counter spell or ability\$ unless its controller pays {2}."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent mevent) {
            final MagicSource source = mevent.getSource();
            final MagicItemOnStack target = mevent.getRefItemOnStack();
            game.addEvent(new MagicCounterUnlessEvent(source,target,MagicManaCost.create("{2}")));
        }
    },
    //tap target permanent. It doesn't untap during its controller's next untap step.
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return event(permanent);
        }
    },
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent == creature) ?
                event(permanent) : MagicEvent.NONE;
        }
    }
]
