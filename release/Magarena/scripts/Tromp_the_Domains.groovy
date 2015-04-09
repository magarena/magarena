[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Until end of turn, creatures you control gain trample and get +1/+1 for each basic land type among lands you control."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int domain = player.getDomain();
            game.logAppendMessage(player," ("+domain+")");
            CREATURE_YOU_CONTROL.filter(player) each {
                game.doAction(new MagicChangeTurnPTAction(it,domain,domain));
                game.doAction(new MagicGainAbilityAction(it,MagicAbility.Trample));
            }
        }
    }
]
