[
    new ThisAttacksTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPermanent attacker) {
            return super.accept(permanent, attacker) && attacker.getController().getNrOfAttackers() == 1;
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                this,
                "SN can't be blocked this combat."
            );
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent event) {
            outerGame.doAction(new AddStaticAction(event.getPermanent(), new MagicStatic(MagicLayer.Game) {
                @Override
                public void modGame(final MagicPermanent source, final MagicGame game) {
                    source.addAbility(MagicAbility.Unblockable)
                }
                @Override
                public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                    if (game.isCombatPhase()) {
                        return true;
                    } else {
                        //remove this static after the update
                        game.addDelayedAction(new RemoveStaticAction(source, this));
                        return false;
                    }
                }
            }));
        }
    }
]
