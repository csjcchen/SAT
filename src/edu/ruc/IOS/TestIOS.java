package edu.ruc.IOS;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.opensat.data.ICNF;
import org.opensat.data.simple.CNFSimpleImplAltWL;
import org.opensat.parsers.Dimacs;



public class TestIOS {
	
	static final String CNF_FILE_NAME_OPTION = "f";
	static final String CNF_FILE_NAME_DESCRIPTION = "set the file name containing the CNF formula"; 
	
	void run(String[] args){
		Options options = createOptions();
		GnuParser cmdlineparser = new GnuParser();		
		CommandLine cmd;
		String fileName = "";
		
		ICNF formula = new CNFSimpleImplAltWL(new DynamicVocImp());
		
		try {
			// parse command line options
			cmd = cmdlineparser.parse(options, args);

			if (cmd.hasOption(CNF_FILE_NAME_OPTION)){
				fileName = cmd.getOptionValue(CNF_FILE_NAME_OPTION);
				Dimacs parser = new Dimacs();
				parser.parseInstance(fileName, formula);
			}
			else{
				System.out.println("ERR: you must specify a CNF file.");
				return;
			}
			 
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return;
		}		
		
		IOSController control = new IOSController(formula);

	}

	
	public Options createOptions() {
		Options options = new Options();

		Option CNFFile =
			new Option(
				CNF_FILE_NAME_OPTION,
				true,
				CNF_FILE_NAME_DESCRIPTION);
		CNFFile.setArgName("CNF_file_name");
		options.addOption(CNFFile);		 

		return options;
	}
	

	public static void main(String[] args){
		new TestIOS().run(args);		
	}
	
}
