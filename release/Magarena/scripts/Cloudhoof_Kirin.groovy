[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            return (permanent.isFriend(spell) &&
                    (spell.getCardDefinition().hasSubType(MagicSubType.Spirit) || 
                     spell.getCardDefinition().hasSubType(MagicSubType.Arcane))) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(MagicTargetChoice.NEG_TARGET_PLAYER),
                    spell.getConvertedCost(),
                    this,
                    "PN may\$ have target player\$ put the top RN cards of his or her library into his or her graveyard."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPlayer(game,new MagicPlayerAction() {
                    public void doAction(final MagicPlayer player) {
                        game.doAction(new MagicMillLibraryAction(player, event.getRefInt()));
                    }
                });
            }
        }        
    }
]
