[
    new MagicWhenBlocksOrBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    "Pay {1}{G}?",
                    new MagicPayManaCostChoice(MagicManaCost.create("{1}{G}"))
                ),
                this,
                "PN may\$ pay {1}{G}. If PN does, SN gains first strike until end of turn. Otherwise, "+
                "each creature blocking or blocked by SN gains first strike until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent()
            if (event.isYes()) {
                game.doAction(new GainAbilityAction(permanent, MagicAbility.FirstStrike));
            } else {
                if (permanent.isBlocking()) {
                    game.doAction(new GainAbilityAction(permanent.getBlockedCreature(), MagicAbility.FirstStrike));
                } else {
                    permanent.getBlockingCreatures() each {
                        game.doAction(new GainAbilityAction(it, MagicAbility.FirstStrike));
                    }
                }
            }
        }
    }
]
