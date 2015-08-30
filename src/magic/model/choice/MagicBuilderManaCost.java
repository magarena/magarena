package magic.model.choice;

import magic.model.MagicCostManaType;

import java.util.Arrays;
import java.util.List;

public class MagicBuilderManaCost {

    public static final MagicBuilderManaCost ZERO_COST=new MagicBuilderManaCost();

    private final int[] amounts;
    private MagicCostManaType[] compressedTypes;
    private int[] compressedAmounts;
    private int typeCount;
    private int minimumAmount;
    private int XCount;

    public MagicBuilderManaCost() {
        amounts=new int[MagicCostManaType.NR_OF_TYPES];
        compressedTypes=new MagicCostManaType[0];
        compressedAmounts=new int[0];
        typeCount=0;
        minimumAmount=0;
        XCount=0;
    }

    public MagicBuilderManaCost(final MagicBuilderManaCost cost) {
        amounts=Arrays.copyOf(cost.amounts,cost.amounts.length);
        compressedTypes=Arrays.copyOf(cost.compressedTypes,cost.compressedTypes.length);
        compressedAmounts=Arrays.copyOf(cost.compressedAmounts,cost.compressedAmounts.length);
        typeCount=cost.typeCount;
        minimumAmount=cost.minimumAmount;
        XCount=cost.XCount;
    }

    public void compress() {
        compressedTypes=new MagicCostManaType[typeCount];
        compressedAmounts=new int[typeCount];
        int compressedIndex=0;

        // Ordered from most restrictive to least restrictive.
        for (int index = MagicCostManaType.NR_OF_TYPES - 1; index >= 0; index--) {
            final int amount=amounts[index];
            if (amount>0) {
                compressedTypes[compressedIndex]=MagicCostManaType.values()[index];
                compressedAmounts[compressedIndex]=amount;
                compressedIndex++;
            }
        }

        assert compressedIndex == typeCount : "typeCount is inconsistent: typeCount = " + typeCount + ", compressedIndex = " + compressedIndex;
    }

    public MagicCostManaType[] getTypes() {
        return compressedTypes;
    }

    public int[] getAmounts() {
        return compressedAmounts;
    }

    public int getMinimumAmount() {
        return minimumAmount;
    }

    boolean hasX() {
        return XCount > 0;
    }

    public void setXCount(final int amount) {
        addType(MagicCostManaType.Colorless,amount);
        XCount = amount;
    }

    int getX(final int amount) {
        return hasX() ? (amount-minimumAmount)/XCount + 1 : 0;
    }

    public boolean isEmpty() {
        return typeCount==0;
    }

    public void addType(final MagicCostManaType type,final int amount) {
        assert amount >= 0 : "amount of mana to add is negative: amount = " + amount;
        final int index=type.ordinal();
        amounts[index]+=amount;
        minimumAmount+=amount;
        if (amount > 0 && amounts[index] == amount) {
            typeCount++;
        }
    }

    public void removeType(final MagicCostManaType type,final int amount) {
        assert amount >= 0 : "amount of mana to remove is negative: amount = " + amount;
        final int index=type.ordinal();
        amounts[index]-=amount;
        minimumAmount-=amount;
        assert amounts[index] >= 0 : "amounts[index] is negative: amounts[index] = " + amounts[index];
        if (amount > 0 && amounts[index] == 0) {
            typeCount--;
        }
    }

    void addTypes(final List<MagicCostManaType> types) {
        for (final MagicCostManaType type : types) {
            addType(type,1);
        }
        compress();
    }

    @Override
    public String toString() {
        final StringBuilder builder=new StringBuilder();
        for (int index=0;index<compressedTypes.length;index++) {
            builder.append(compressedTypes[index]).append('=').append(compressedAmounts[index]).append(' ');
        }
        builder.append("Total=").append(minimumAmount);
        return builder.toString();
    }
}
