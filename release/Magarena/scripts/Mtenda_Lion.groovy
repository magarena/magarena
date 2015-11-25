[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                game.getDefendingPlayer(),
                new MagicMayChoice(
                    "Pay {U}?",
                    new MagicPayManaCostChoice(MagicManaCost.create("{U}"))
                ),
                this,
                "PN may\$ pay {U}\$. If PN does, prevent all combat damage that would be dealt by SN this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(ChangeStateAction.Set(
                    event.getPermanent(),
                    MagicPermanentState.NoCombatDamage
                ));
            }
        }
    }
]
