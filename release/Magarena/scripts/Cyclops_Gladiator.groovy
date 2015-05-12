def choice = new MagicTargetChoice("target creature defending player controls");

[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(choice),
                new MagicDamageTargetPicker(permanent.getPower()),
                this,
                "PN may\$ have SN deal damage equal to its power to target creature defending player controls.\$ "+
                "If PN does, that creature deals damage equal to its power to SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    final MagicPermanent attacker = event.getPermanent();
                    game.doAction(new DealDamageAction(attacker,it,attacker.getPower()));
                    game.doAction(new DealDamageAction(it,attacker,it.getPower()));
                });
            }
        }
    }
]
