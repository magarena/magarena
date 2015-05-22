def CREATURE_YOU_CONTROL_WITH_PLUSONE_COUNTER = MagicTargetFilterFactory.creature(MagicCounterType.PlusOne, Control.You);

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN draws a card for each creature he or she controls with a +1/+1 counter on it. "+
                "Those creatures gain indestructible until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final List<MagicPermanent> creatures = CREATURE_YOU_CONTROL_WITH_PLUSONE_COUNTER.filter(player)
            final int amount = creatures.size();
            game.logAppendMessage(player, "("+amount+")");
            game.doAction(new DrawAction(player, amount));
            for (final MagicPermanent creature : creatures) {
                game.doAction(new GainAbilityAction(creature, MagicAbility.Indestructible));
            }
        }
    }
]
