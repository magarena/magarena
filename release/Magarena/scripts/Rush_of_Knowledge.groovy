[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN draws cards equal to the highest converted mana cost among permanents he or she controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer()
            int amount = 0;
            PERMANENT_YOU_CONTROL.filter(player) each {
                amount = Math.max(amount,it.getConvertedCost());
            }
            game.logAppendMessage(player,"("+amount+")");
            game.doAction(new DrawAction(player, amount));
        }
    }
]
