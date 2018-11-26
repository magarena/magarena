[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN creates a 1/1 white Soldier creature token for each nontoken creature PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = (int)(player.getPermanents().count({ it.isCreature() && it.isNonToken() }));
            amount.times {
                game.doAction(new PlayTokenAction(
                    player,
                    CardDefinitions.getToken("1/1 white Soldier creature token"),
                ));
            }
        }
    },

    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "indestructible"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Creatures PN controls with power less than Lena's power gain indestructible until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int pow = event.getPermanent().getPower();
            event.getPlayer().getPermanents().findAll({
                it.isCreature() && it.getPower() < pow
            }).each {
                game.doAction(new GainAbilityAction(it, MagicAbility.Indestructible));
            }
        }
    }
]
