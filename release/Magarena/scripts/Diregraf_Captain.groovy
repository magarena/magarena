[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent otherPermanent) {
            return (permanent != otherPermanent &&
                    otherPermanent.getController() == permanent.getController() &&
                    otherPermanent.isCreature() &&
                    otherPermanent.hasSubType(MagicSubType.Zombie)) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.TARGET_OPPONENT,
                    this,
                    "Target opponent\$ loses 1 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    game.doAction(new MagicChangeLifeAction(player,-1));
                }
            });
        }
    }
]
