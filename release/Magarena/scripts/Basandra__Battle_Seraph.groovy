[
    new MagicStatic(MagicLayer.Player) {
        @Override
        public void modPlayer(final MagicPermanent source, final MagicPlayer player) {
            player.setState(MagicPlayerState.CantCastSpells);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return game.isCombatPhase();
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.MustAttack),
        "Attacks"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{R}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE,
                MagicMustAttackTargetPicker.create(),
                this,
                "Target creature\$ attacks this turn if able."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicGainAbilityAction(
                    it,
                    MagicAbility.AttacksEachTurnIfAble
                ));
            });
        }
    }
]
