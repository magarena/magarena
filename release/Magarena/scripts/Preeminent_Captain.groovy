def A_SOLDIER_CARD_FROM_HAND = new MagicTargetChoice("a Soldier creature card from your hand");

[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                this,
                "PN may put a Soldier creature card from his or her hand onto the battlefield tapped and attacking."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicPutOntoBattlefieldEvent(
                event,
                new MagicMayChoice(
                    "Put a Soldier creature card onto the battlefield?",
                    A_SOLDIER_CARD_FROM_HAND,
                ),
                [MagicPlayMod.TAPPED,MagicPlayMod.ATTACKING]
            ));
        }
    }
]
