[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Put the top two cards of your library into your graveyard?"),
                this,
                "PN may\$ put the top two cards of his or her library into his or her graveyard. If PN doesn't, sacrifice SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes() && event.getPlayer().getLibrary().size() >=2) {
                game.doAction(new MagicMillLibraryAction(event.getPlayer(),2));
            } else {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
            }
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.PumpFlash),
        "+Abilities"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{U}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN gains shroud until end of turn and doesn't untap during your next untap step. Tap SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicGainAbilityAction(event.getPermanent(),MagicAbility.Shroud));
            game.doAction(new MagicTapAction(event.getPermanent()));
		game.doAction(MagicChangeStateAction.Set(
                event.getPermanent(),
                MagicPermanentState.DoesNotUntapDuringNext
                ));
        }
    }
]
