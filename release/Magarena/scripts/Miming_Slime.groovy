[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Put an X/X green Ooze creature token onto the battlefield, where X is the greatest power among creatures you control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(
                event.getPlayer(),
                MagicTargetFilterFactory.CREATURE_YOU_CONTROL
            );
            int x = 0;
            for (final MagicPermanent creature : targets) {
                x = Math.max(x,creature.getPower());
            }
            final MagicStatic PT = new MagicStatic(MagicLayer.SetPT) {
                @Override
                public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
                    pt.set(x,x);
                }
            };

            game.doAction(new MagicPlayTokenAction(
                event.getPlayer(),
                TokenCardDefinitions.get("green Ooze creature token"),
                {
                    final MagicPermanent perm ->
                    game.doAction(new MagicAddStaticAction(perm,PT));
                }
            ));
        }
    }
]
