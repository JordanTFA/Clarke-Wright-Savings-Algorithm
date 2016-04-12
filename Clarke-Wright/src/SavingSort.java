import java.util.Comparator;

public class SavingSort implements Comparator<Saving> {

	@Override
	public int compare(Saving a, Saving b) {
		if(a.getSaving() < b.getSaving()) return 1;
		else if(a.getSaving() > b.getSaving()) return -1;
		return 0;
	}
}
