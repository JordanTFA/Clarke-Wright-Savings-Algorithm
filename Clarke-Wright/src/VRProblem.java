import java.util.*;
import java.io.*;
public class VRProblem {
	public String id;
	public Customer depot;
	ArrayList<Customer> customers;
	public VRProblem(String filename) throws Exception{
		this.id = filename;
		BufferedReader br = new BufferedReader(new FileReader(filename));
		//Details of the depot and the truck capacity are stored in the first line
		String s = br.readLine();
		String dpt [] = s.split(",");
		depot = new Customer(
				Integer.parseInt(dpt[0]),
				Integer.parseInt(dpt[1]),
				Integer.parseInt(dpt[2]));
		customers = new ArrayList<Customer>();
		//Every customer is stored on a comma separated line
		while ((s=br.readLine())!=null){
			String wrd [] = s.split(",");
			customers.add(new Customer(
					Integer.parseInt(wrd[0]),
					Integer.parseInt(wrd[1]),
					Integer.parseInt(wrd[2])));
		}
		br.close();
	}
	public int size(){
		return this.customers.size();				
	}
}
