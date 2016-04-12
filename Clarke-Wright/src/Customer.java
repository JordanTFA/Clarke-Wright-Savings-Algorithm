import java.awt.geom.Point2D;


public class Customer extends Point2D.Double{

	// Requirements of the customer (number to be delivered)
	public int c;
	public Customer(int x, int y, int requirement){
		this.x = x;
		this.y = y;
		this.c = requirement;
	}
}
