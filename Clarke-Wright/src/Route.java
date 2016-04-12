import java.util.ArrayList;
import java.util.List;

public class Route {
	private Customer start;
	private Customer end;
	private List<Customer> route;
	private Customer depot;

	public Route(Customer a, Customer dep){
		this.start = a;
		this.end = a;
		this.route = new ArrayList<Customer>();
		this.route.add(a);
		this.depot = dep;
	}
	
	public Customer getDepot() {
		return depot;
	}

	public void setDepot(Customer depot) {
		this.depot = depot;
	}
	
	public Customer getStart() {
		return this.start;
	}

	public void setStart(Customer start) {
		this.start = start;
	}

	public Customer getEnd() {
		return this.end;
	}

	public void setEnd(Customer end) {
		this.end = end;
	}

	public List<Customer> getRoute() {
		return this.route;
	}

	public void setRoute(List<Customer> route) {
		this.route = route;
		this.start = route.get(0);
		this.end = route.get(route.size() - 1);
	}
	
	public void addRoute(Route toAdd){
		this.route.addAll(toAdd.getRoute());
		this.end = this.route.get(this.route.size() -1 );
	}
	
	public void addCustomer(Customer c){
		this.route.add(c);
		this.end = c;
	}
	
	public String toString(){
		return "Start: " + this.start.toString() + "\tEnd: " + this.end.toString() + "\n";		
	}
	
	//====================================================================
	// Calculate the cost of a route
	public double getCost(){
		// System.out.print(route.size() + ": ");
		Customer prev = this.depot;
		double cost = 0;
		for (Customer c:this.route){
			cost += prev.distance(c);
			prev = c;
		}
		//Add the cost of returning to the depot
		cost += prev.distance(this.depot);
		return cost;
	}
	
	// Check that a route does not exceed capacity
	public Boolean verify(){
		Boolean result = true;
		int total = 0;
		for(Customer c: this.route) {
			total += c.c;
		}
		if (total > this.depot.c){
			System.out.printf("********FAIL Route at %s is over capacity %d\n", route.get(0),total);
			result = false;
		}
		return result;
	}
}
