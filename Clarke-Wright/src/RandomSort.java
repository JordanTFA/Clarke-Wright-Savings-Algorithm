import java.util.Comparator;
import java.util.Random;

public class RandomSort implements Comparator<Customer>{

	public int compare(Customer a, Customer b) {
		Random r = new Random();
		int ri = r.nextInt(10);
		if(ri < 5) return 1;
		if(ri >= 5) return -1;
		return 0;
	}
}
