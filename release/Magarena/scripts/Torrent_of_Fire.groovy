[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_OR_PLAYER,
                this,
                "SN deals damage equal to the highest converted mana cost among permanents PN controls to target creature or player.\$"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicPlayer player = event.getPlayer()
                int amount = 0;
                PERMANENT_YOU_CONTROL.filter(player) each {
                    amount = Math.max(amount,it.getConvertedCost());
                }
                game.logAppendValue(player,amount);
                game.doAction(new DealDamageAction(event.getSource(), it, amount));
            });
        }
    }
]
