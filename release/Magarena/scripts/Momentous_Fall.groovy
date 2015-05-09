[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                payedCost.getTarget(),
                this,
                "PN draws cards equal to RN's power, then gains life equal to its toughness."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int power=event.getRefPermanent().getPower();
            final int toughness=event.getRefPermanent().getToughness();
            game.logAppendMessage(player,"("+power+"/"+toughness+")");
            game.doAction(new DrawAction(player, power));
            game.doAction(new ChangeLifeAction(player, toughness));
        }
    }
]
