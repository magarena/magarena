[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a colorless Construct artifact creature token named Twin onto the battlefield attacking. "+
                "Its power is equal to SN's power and its toughness is equal to SN's toughness. "+
                "Sacrifice the token at end of combat."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int power = event.getPermanent().getPower();
            final int toughness = event.getPermanent().getToughness();
            game.doAction(new PlayTokenAction(
                event.getPlayer(), 
                MagicCardDefinition.create({
                    it.setName("Twin");
                    it.setDistinctName("colorless Construct artifact creature token named Twin");
                    it.setPowerToughness(power, toughness);
                    it.addSubType(MagicSubType.Construct);
                    it.addType(MagicType.Creature);
                    it.setToken();
                    it.setValue(power);
                    it.setColors("");
                }),
                [MagicPlayMod.ATTACKING, MagicPlayMod.SACRIFICE_AT_END_OF_COMBAT]
            ));
        }
    }
]
