package src.elford.james.please;

public class Range {
	int min;
	int max;
	int size;
	
	public Range(int i) {
		this.min = 0;
		this.max = i-1;
		this.size = i;
	}
	
	public Range(int low, int high) {
		this.min = low;
		this.max = high-1;
		this.size = high-low;
	}

	public int size() {
		return size;
	}
}
