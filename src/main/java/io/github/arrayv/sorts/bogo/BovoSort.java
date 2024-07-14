package io.github.arrayv.sorts.bogo;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.BogoSorting;

// Bovosort (Head pull bogo sort) randomly pulls elements to index 0 until the list is sorted.

// Bovo Sort is by PCBoy games (Sorting Algorithm Madhouse CEO) credit to him

public final class BovoSort extends BogoSorting {
    public BovoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Bovo");
        this.setRunAllSortsName("Bovo Sort (Head pull bogo sort)");
        this.setRunSortName("Bovosort");
        this.setCategory("Bogo Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(10);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while(!this.isArraySorted(array, length))
            Writes.multiSwap(array, BogoSorting.randInt(0, length - 1), 0, this.delay, true, false);
    }
}
