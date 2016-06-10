def ABILITY2 = MagicRuleEventAction.create("Put two 3/1 red Elemental creature tokens with haste onto the battlefield. Exile them at the beginning of the next end step.");

[
    new MagicPlaneswalkerActivation(+1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return ABILITY2.getEvent(source);
        }
    },
    new MagicPlaneswalkerActivation(0) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN discards all the cards in his or her hand, then draws that many cards +1."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer()
            final int amount = event.getPlayer().getHandSize();
            game.logAppendValue(player, amount);
            game.addEvent(new MagicDiscardHandEvent(event.getSource()));
            game.addEvent(new MagicDrawEvent(event.getSource(), player, amount+1));
        }
    },
    new MagicPlaneswalkerActivation(-3) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN deals 3 damage to each creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE.filter(event) each {
                game.doAction(new DealDamageAction(event.getSource(), it ,3));
            }
        }
    }
]