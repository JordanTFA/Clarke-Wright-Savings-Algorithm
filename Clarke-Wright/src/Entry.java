import java.util.ArrayList;
import java.io.*;

public class Entry {

	public static void main(String[] args)throws Exception {
		String [] shouldPass = {
				"rand00010",
				"rand00020",
				"rand00030",
				"rand00040",
				"rand00050",
				"rand00060",
				"rand00070",
				"rand00080",
				"rand00090",
				"rand00100",
				"rand00200",
				"rand00300",
				"rand00400",
				"rand00500",
				"rand00600",
				"rand00700",
				"rand00800",
				"rand00900",
				"rand01000"
				};
		System.out.println("Problem     \tSoln\tSize\tCost\tValid");
		
		for (String base:shouldPass){
			VRProblem vrp = new VRProblem("Tests/"+base+"prob.csv");
			VRSolution vrs = new VRSolution(vrp);
			vrs.clarkWright();
			System.out.printf("%s\t%s\t%d\t%.0f\t%s\n",base,"MINE",vrp.size(),vrs.solutionCost(),vrs.verifySolution());
			System.out.println("=========================================================");
			
			vrs.writeSVG(base + ".svg", base + " Solution.svg");
		}
	}
}
