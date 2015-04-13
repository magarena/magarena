def action = {
    final MagicGame game, final MagicEvent event ->
    final int amount = event.getPlayer().getOpponent().getNrOfAttackers() + event.getPlayer().getNrOfAttackers();
    game.doAction(new ChangeTurnPTAction(event.getPermanent(),amount,amount));
}

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
        CREATURE_YOUR_OPPONENT_CONTROLS
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.AttacksEachTurnIfAble, flags);
        }
    },
    new MagicWhenSelfBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            return event(permanent);
        }
    },
    new MagicWhenSelfBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocked) {
            return event(permanent);
        }
    }
]
