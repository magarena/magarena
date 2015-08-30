[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return (cardOnStack.getCardDefinition().hasSubType(MagicSubType.Spirit) || cardOnStack.getCardDefinition().hasSubType(MagicSubType.Arcane)) &&
                    cardOnStack.isEnemy(permanent) ?
                new MagicEvent(
                    permanent,
                    cardOnStack.getController(),
                    this,
                    "SN deals 2 damage to PN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),2));
        }
    }
]
