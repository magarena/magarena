[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            return (permanent.isFriend(spell) &&
                    (spell.getCardDefinition().hasSubType(MagicSubType.Spirit) || 
                     spell.getCardDefinition().hasSubType(MagicSubType.Arcane))) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.TARGET_OPPONENT,
                    this,
                    "Target opponent\$ loses 1 life and PN gains 1 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    game.doAction(new MagicChangeLifeAction(player,-1));
                    game.doAction(new MagicChangeLifeAction(event.getPlayer(),1));
                }
            });
        }        
    }
]
