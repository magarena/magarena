def action = {
    final MagicGame game, final MagicEvent event ->
    final int amount = event.getPlayer().getOpponent().getNrOfAttackers() + event.getPlayer().getNrOfAttackers();
    game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),amount,amount));
} as MagicEventAction

def event = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        action,
        "SN gets +X/+X until end of turn, where X is the number of attacking creatures."
    );
}

[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            flags.add(MagicAbility.AttacksEachTurnIfAble);
        }
    },
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            return (permanent == blocker) ? event(permanent) : MagicEvent.NONE;
        }
    },
    new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocked) {
            return (permanent == blocked) ? event(permanent) : MagicEvent.NONE;
        }
    }
]
