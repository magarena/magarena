[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "Creatures and lands target opponent\$ controls don't untap during that opponent's next untap step."
            );
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent event) {
            event.processTargetPlayer(outerGame, {
                final MagicPlayer outerPlayer ->
                outerGame.doAction(new AddStaticAction(new MagicStatic(MagicLayer.Game) {
                    @Override
                    public void modGame(final MagicPermanent source, final MagicGame game) {
                        final MagicPlayer p = outerPlayer.map(game);
                        CREATURE_OR_LAND_YOU_CONTROL.filter(p) each {
                            it.addAbility(MagicAbility.DoesNotUntap);
                        }
                    }
                    @Override
                    public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                        final MagicPlayer p = outerPlayer.map(game);
                        if (game.getTurnPlayer() == p && game.isPhase(MagicPhaseType.Upkeep)) {
                            game.addDelayedAction(new RemoveStaticAction(this));
                            return false;
                        } else {
                            return true;
                        }
                    }
                }))
            });
        }
    }
]
