package magic.data;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicManaType;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicSacrificeTapManaActivation;
import magic.model.event.MagicTapManaActivation;
import magic.model.event.MagicVividManaActivation;

public class ManaActivationDefinitions {
	
	private final static void addManaActivation(final String name,final MagicManaActivation manaActivation) {
		final MagicCardDefinition cardDefinition=CardDefinitions.getInstance().getCard(name);
		cardDefinition.addManaActivation(manaActivation);
	}
	
	private final static void addCreatureActivations(final String name,final List<MagicManaType> manaTypes) {
		final MagicCardDefinition cardDefinition=CardDefinitions.getInstance().getCard(name);
		cardDefinition.addManaActivation(new MagicTapManaActivation(manaTypes,1));
		cardDefinition.setExcludeManaOrCombat();
	}
		
	private final static void addVividActivations(final String name,final MagicManaType manaType) {
		final List<MagicManaType> manaTypes=new ArrayList<MagicManaType>(MagicColor.NR_COLORS-1);
		for (final MagicColor color : MagicColor.values()) {
			final MagicManaType colorManaType=color.getManaType();
			if (colorManaType != manaType) {
				manaTypes.add(colorManaType);
			}
		}
		
		final MagicCardDefinition cardDefinition=CardDefinitions.getInstance().getCard(name);
		cardDefinition.addManaActivation(new MagicTapManaActivation(Arrays.asList(MagicManaType.Colorless,manaType),0));
		cardDefinition.addManaActivation(new MagicVividManaActivation(manaTypes));
	}
	
    public final static List<MagicManaType> getVividManaTypes(final MagicManaType manaType) {
		final List<MagicManaType> manaTypes=new ArrayList<MagicManaType>(MagicColor.NR_COLORS-1);
		for (final MagicColor color : MagicColor.values()) {
			final MagicManaType colorManaType=color.getManaType();
			if (colorManaType != manaType) {
				manaTypes.add(colorManaType);
			}
		}
        return manaTypes;
	}
	
	public static void addManaActivations() {
		// Vivid lands.
		//addVividActivations("Vivid Crag",MagicManaType.Red);
		//addVividActivations("Vivid Creek",MagicManaType.Blue);
		//addVividActivations("Vivid Grove",MagicManaType.Green);
		//addVividActivations("Vivid Marsh",MagicManaType.Black);
		//addVividActivations("Vivid Meadow",MagicManaType.White);
		
		//addManaActivation("Mind Stone",new MagicTapManaActivation(Arrays.asList(MagicManaType.Colorless),0));

        // Land generating colorless mana
        //addManaActivation("Tectonic Edge",new MagicTapManaActivation(Arrays.asList(MagicManaType.Colorless),0));
		
		// Man lands.
		//addCreatureActivations("Celestial Colonnade",Arrays.asList(MagicManaType.Blue,MagicManaType.White));
		//addCreatureActivations("Creeping Tar Pit",Arrays.asList(MagicManaType.Blue,MagicManaType.Black));
		//addCreatureActivations("Raging Ravine",Arrays.asList(MagicManaType.Red,MagicManaType.Green));
		//addCreatureActivations("Stirring Wildwood",Arrays.asList(MagicManaType.Green,MagicManaType.White));		
		//addCreatureActivations("Inkmoth Nexus",Arrays.asList(MagicManaType.Colorless));		
		
        // Artifacts.
		addManaActivation("Lotus Petal",new MagicSacrificeTapManaActivation(MagicManaType.ALL_TYPES));

		// Creatures.
		addCreatureActivations("Alloy Myr",MagicManaType.ALL_TYPES);
		addCreatureActivations("Birds of Paradise",MagicManaType.ALL_TYPES);
		addCreatureActivations("Llanowar Elves",Arrays.asList(MagicManaType.Colorless,MagicManaType.Green));
		addCreatureActivations("Noble Hierarch",Arrays.asList(MagicManaType.Colorless,MagicManaType.Blue,MagicManaType.Green,MagicManaType.White));
		addCreatureActivations("Steward of Valeron",Arrays.asList(MagicManaType.Colorless,MagicManaType.Green));
		addCreatureActivations("Vine Trellis",Arrays.asList(MagicManaType.Colorless,MagicManaType.Green));
		addCreatureActivations("Plague Myr",Arrays.asList(MagicManaType.Colorless));
		
        System.err.println("Added 8 mana activations");
	}
}
