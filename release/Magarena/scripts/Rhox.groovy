[
    new SelfBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(),
                this,
                "PN may\$ have SN assign its combat damage as though it weren't blocked."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final MagicPermanent permanent = event.getPermanent();
                final MagicDamage damage = MagicDamage.Combat(
                    permanent,
                    game.getDefendingPlayer(),
                    permanent.getPower()
                );
                game.doAction(new DealDamageAction(damage));
                game.doAction(ChangeStateAction.Set(
                    permanent,
                    MagicPermanentState.NoCombatDamage
                ));
            }
        }
    }
]
