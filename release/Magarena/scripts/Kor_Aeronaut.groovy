[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPlayer player) {   
            return (permanent.isKicked()) ?
                new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    this,
                    "Target creature\$ gains flying until end of turn."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Flying));
                }
            });
        }
    }
]
