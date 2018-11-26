def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetPermanent(game, {
        final MagicPermanent source = event.getPermanent();
        game.doAction(new TapAction(it));
        game.doAction(new AddStaticAction(
            source,
            MagicStatic.AsLongAsCond(
                it,
                MagicAbility.DoesNotUntap,
                MagicConditionFactory.PlayerControlsSource(event.getPlayer()
            ))
        ));
    });
}

def event = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
        action,
        "Tap target creature an opponent controls\$. " +
        "It doesn't untap during its controller's untap step for as long as you control SN."
    );
}

[
    new SagaChapterTrigger(1) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
            return event(permanent);
        }
    },

    new SagaChapterTrigger(2) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
            return event(permanent);
        }
    }
]
