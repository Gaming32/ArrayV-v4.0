package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2019 w0rthy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */
 // bubble sort but bad :D. basically an inverse peelsort but uses an auxillary array for skims. totally not because using a variable didnt work for some weason

public final class SkimSort extends Sort {
    public SkimSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Skim");
        this.setRunAllSortsName("Skim Sort");
        this.setRunSortName("Skimsort");
        this.setCategory("Selection Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int min = Reads.analyzeMin(array, length, 0.075, true);
        int max = Reads.analyzeMax(array, length, 0.075, true);
        
        int[] skims = Writes.createExternalArray(length);
        int h = max;
		for(int g = length; g > 0; g--) {
			Writes.write(skims, h, g, 1, true, true);
			h--;
		}
        for(int i = length - 1; i > 0; i--) {
            boolean sorted = true;
            for(int j = 0; j < i; j++) {
                if(Reads.compareValues(array[j], skims[i] - 1) == 1){
                    Writes.swap(array, j, j + 1, 0.075, true, false);
                }
                if(Reads.compareValues(array[j], array[j + 1]) == 1){
                    sorted = false;
                }
                Highlights.markArray(1, j);
		Highlights.markArray(2, j + 1);
		Highlights.markArray(3, i);
                Delays.sleep(0.025);
            }
            if(sorted) break;
        }
		Writes.deleteExternalArray(skims);
    }
}
