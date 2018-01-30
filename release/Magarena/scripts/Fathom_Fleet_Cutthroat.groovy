[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicTargetChoice(
                    new MagicPermanentFilterImpl() {
                        @Override
                        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
                            return target.hasType(MagicType.Creature) && target.isEnemy(player) && target.hasState(MagicPermanentState.WasDealtDamage);
                        }
                    },
                    "target creature an opponent controls that was dealt damage this turn"
                ),
                this,
                "Destroy target creature PN's opponent controls that was dealt damage this turn\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
            });
        }
    }
]

