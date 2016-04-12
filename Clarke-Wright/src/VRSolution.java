import java.util.*;
import java.io.*;

public class VRSolution {
	public VRProblem problem;
	public List<Route> solution;
	public ArrayList<Saving> savings = new ArrayList<Saving>();
	public ArrayList<Route> joined = new ArrayList<Route>();
	
	public VRSolution(VRProblem problem){
		this.problem = problem;
	}
	
	public void clarkeWright() throws Exception{
		oneRoutePerCustomerSolution();
		findAllPairs();
		
		while(savings.size() > 0){
			for(int i = 0; i < savings.size();i++){
				Saving saving = savings.get(i);
				Route a = saving.getR1();
				Route b = saving.getR2();
				if(!joined.contains(a) && !joined.contains(b)){
					join(a, b);
					break;
				}
				else if(!joined.contains(a)){
					for(Route r: solution){
						if(r.getStart() == b.getStart()) join(a, r);
						break;
					}
				}
				else if(!joined.contains(b)){
					for(Route r: solution){
						if(r.getEnd() == a.getEnd()) join(r, b);
						break;
					}
				}
			}
			joined.clear();
			savings.clear();
			findAllPairs();
		}
		randomise();
	}
	
	public void randomise(){
		for(Route r: this.solution){
			List<Customer> best = r.getRoute();
			List<Customer> mix = new ArrayList<Customer>();
			mix.addAll(best);
			Route ok = new Route(mix.get(0), this.problem.depot);
			for(int i = 0; i < 10000; i++){
				mix.sort(new RandomSort());
				ok.setRoute(mix);
				if(ok.getCost() < r.getCost()){		
					List<Customer> t = new ArrayList<Customer>();
					for(Customer c: mix) t.add(c);
					r.setRoute(t);
				}
			}
		}
	}

	private void join(Route a, Route b){
		if(verifyJoin(a, b)){
			a.addRoute(b);
			joined.add(a);
			joined.add(b);
			solution.remove(b);
		}
	}
	
	private boolean verifyJoin(Route r1, Route r2) {
		Boolean result = true;
		int total = 0;
		for(Customer c: r1.getRoute()) total += c.c;
		for(Customer c: r2.getRoute()) total += c.c;
		if (total > problem.depot.c) {
			result = false;
		}
		return result;
	}
	
	public double calculatePairSaving(Route a, Route b){
		Customer cus1 = a.getEnd();
		Customer cus2 = b.getStart();
		double bridge = cus1.distance(cus2);
		double sav1 = cus1.distance(this.problem.depot);
		double sav2 = cus2.distance(this.problem.depot);
		return sav1 + sav2 - bridge;
	}

 	public void findAllPairs(){
 		for(int j = 0; j < this.solution.size(); j++){
 			for( int i = j + 1; i < this.solution.size(); i++ ){
 				Route a = this.solution.get(j);
 				Route b = this.solution.get(i);
 				double sav = calculatePairSaving(a, b);
 				double sav2 = calculatePairSaving(b, a);
 				if(sav > sav2){
 					if(sav > 1 && verifyJoin(a, b)) savings.add( new Saving(sav, a, b) );
 				}
 				else if(sav2 > 1 && verifyJoin(b, a)) savings.add( new Saving(sav, b, a) );
 			}
 		}
 		savings.sort(new SavingSort());
	}

	public void oneRoutePerCustomerSolution(){
		this.solution = new ArrayList<Route>();
		this.solution = new ArrayList<Route>();
		for(Customer c:problem.customers){
			Route route = new Route(c, problem.depot);
			this.solution.add(route);
		}
	}
 	
	public double solutionCost(){
		double cost = 0;
		for(Route route: this.solution){
			cost += route.getCost();
		}
		return cost;
	}

	public Boolean verifySolution(){
		Boolean okSoFar = true;
		for(Route route : solution){
			okSoFar = route.verify();
		}
		Map<String,Integer> reqd = new HashMap<String,Integer>();
		for(Customer c:this.problem.customers){
			String address = String.format("%fx%f", c.x,c.y);
			reqd.put(address, c.c);
		}
		for(Route route:this.solution){
			for(Customer c:route.getRoute()){
				String address = String.format("%fx%f", c.x,c.y);
				if (reqd.containsKey(address))
					reqd.put(address, reqd.get(address)-c.c);
				else
					System.out.printf("********FAIL no customer at %s\n",address);
			}
		}
		for(String address:reqd.keySet())
			if (reqd.get(address)!=0){
				System.out.printf("********FAIL Customer at %s has %d left over\n",address,reqd.get(address));
				okSoFar = false;
			}
		return okSoFar;
	}
	
	public void readIn(String filename) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String s;
		this.solution = new ArrayList<Route>();
		Route route = null;
		while((s=br.readLine())!=null){
			String [] xycTriple = s.split(",");
			for(int i=0;i<xycTriple.length;i+=3){
				Customer c = new Customer((int)Double.parseDouble(xycTriple[i]),(int)Double.parseDouble(xycTriple[i+1]),(int)Double.parseDouble(xycTriple[i+2]));
				if(route == null || i == 0 ) route = new Route(c, this.problem.depot);
				else route.addCustomer(c);
			}
			solution.add(route);
		}
		br.close();
	}
	
	public void writeSVG(String probFilename,String solnFilename) throws Exception{
		String[] colors = "chocolate cornflowerblue crimson cyan darkblue darkcyan darkgoldenrod".split(" ");
		int colIndex = 0;
		String hdr = 
				"<?xml version='1.0'?>\n"+
				"<!DOCTYPE svg PUBLIC '-//W3C//DTD SVG 1.1//EN' '../../svg11-flat.dtd'>\n"+
				"<svg width='8cm' height='8cm' viewBox='0 0 500 500' xmlns='http://www.w3.org/2000/svg' version='1.1'>\n";
		String ftr = "</svg>";
        StringBuffer psb = new StringBuffer();
        StringBuffer ssb = new StringBuffer();
        psb.append(hdr);
        ssb.append(hdr);
        for(Route route:this.solution){
        	ssb.append(String.format("<path d='M%s %s ",this.problem.depot.x,this.problem.depot.y));
        	for(Customer c:route.getRoute())
        		ssb.append(String.format("L%s %s",c.x,c.y));
        	ssb.append(String.format("z' stroke='%s' fill='none' stroke-width='2'/>\n",
        			colors[colIndex++ % colors.length]));
        }
        for(Customer c:this.problem.customers){
        	String disk = String.format(
        			"<g transform='translate(%.0f,%.0f)'>"+
        	    	"<circle cx='0' cy='0' r='%d' fill='pink' stroke='black' stroke-width='1'/>" +
        	    	"<text text-anchor='middle' y='5'>%d</text>"+
        	    	"</g>\n", 
        			c.x,c.y,10,c.c);
        	psb.append(disk);
        	ssb.append(disk);
        }
        String disk = String.format("<g transform='translate(%.0f,%.0f)'>"+
    			"<circle cx='0' cy='0' r='%d' fill='pink' stroke='black' stroke-width='1'/>" +
    			"<text text-anchor='middle' y='5'>%s</text>"+
    			"</g>\n", this.problem.depot.x,this.problem.depot.y,20,"D");
    	psb.append(disk);
    	ssb.append(disk);
        psb.append(ftr);
        ssb.append(ftr);
        PrintStream ppw = new PrintStream(new FileOutputStream(probFilename));
        PrintStream spw = new PrintStream(new FileOutputStream(solnFilename));
        ppw.append(psb);
        spw.append(ssb);
    	ppw.close();
    	spw.close();
	}
	
	public void writeOut(String filename) throws Exception{
		PrintStream ps = new PrintStream(filename);
		for(Route route:this.solution){
			boolean firstOne = true;
			for(Customer c:route.getRoute()){
				if (!firstOne)
					ps.print(",");
				firstOne = false;
				ps.printf("%f,%f,%d",c.x,c.y,c.c);
			}
			ps.println();
		}
		ps.close();
	}
}
