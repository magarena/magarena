[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                this,
                "SN deals X damage to target creature\$,"+
                "where X is 2 plus the number of cards named Galvanic Bombardment in PN's graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = cardName("Galvanic Bombardment")
                .from(MagicTargetType.Graveyard)
                .filter(event)
                .size()+2;
            event.processTarget(game, {
                game.logAppendX(event.getPlayer(),amount);
                game.doAction(new DealDamageAction(event.getSource(),it,amount));
            });
        }
    }
]
