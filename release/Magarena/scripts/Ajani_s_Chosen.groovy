[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return otherPermanent.isEnchantment() && otherPermanent.isFriend(permanent) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "PN puts a 2/2 white Cat creature token onto the battlefield." 
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayTokenAction act = new MagicPlayTokenAction(
                event.getPlayer(),
                TokenCardDefinitions.get("Cat2")
            );
            game.doAction(act);
            final MagicPermanent token = act.getPermanent();
            final MagicPermanent enchantment = event.getRefPermanent();
            if (enchantment.isAura()) {
                game.addEvent(new MagicEvent(
                    enchantment,
                    new MagicMayChoice("Attach ${enchantment} to ${token}?"),
                    token,
                    {
                        final MagicGame G, final MagicEvent E ->
                        G.doAction(new MagicAttachAction(
                            E.getPermanent(), 
                            E.getRefPermanent()
                        ));
                    } as MagicEventAction,
                    "You may\$ attach SN to RN."
                ));
            }
        }
    }
]
