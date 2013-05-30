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
            if (permanent == blocker) {
                return new MagicEvent(
                    permanent,
                    this,
                    "SN gets +X/+X until end of turn, where X is the number of attacking creatures."
                );
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getOpponent().getNrOfAttackers();
            game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),amount,amount));
        }
    },
    new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocked) {
            if (permanent == blocked) {
                return new MagicEvent(
                    permanent,
                    this,
                    "SN gets +X/+X until end of turn, where X is the number of attacking creatures."
                );
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getNrOfAttackers();
            game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),amount,amount));
        }
    }
]
