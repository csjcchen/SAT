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
	static final String TESTING_MODE_OPTION = "m";
	static final String TESTING_MODE_DESCRIPTION = "set the testing mode";
	static final String NEW_CNF_FILE_OPTION = "n";
	static final String NEW_CNF_FILE_DESCRIPTION = "set the name of the file containing new knowledge";

	//constants of different testing modes
	static final int INITIALZE_TEST = 0;//default testing mode, initialize a KB
	static final int UPDATE_TEST = 1; // update an existing KB
	
	
	void run(String[] args){
		Options options = createOptions();
		GnuParser cmdlineparser = new GnuParser();		
		CommandLine cmd;
		String fileName = "";				
		
		try {
			// parse command line options
			cmd = cmdlineparser.parse(options, args);

			if (cmd.hasOption(CNF_FILE_NAME_OPTION)){
				fileName = cmd.getOptionValue(CNF_FILE_NAME_OPTION);				
			}
			else{
				System.out.println("ERR: you must specify a CNF file.");
				return;
			}
			
			if (cmd.hasOption(TESTING_MODE_OPTION)){
				if (Integer.parseInt(cmd.getOptionValue(TESTING_MODE_OPTION))==INITIALZE_TEST){
					testInitiailize(fileName);
				}
				else if  (Integer.parseInt(cmd.getOptionValue(TESTING_MODE_OPTION))==UPDATE_TEST){
					if (cmd.hasOption(NEW_CNF_FILE_OPTION)){
						String newKLGFile = cmd.getOptionValue(NEW_CNF_FILE_OPTION);
						testUpdate(fileName,newKLGFile);
					}
					else{
						System.out.println("ERR: your must specify a file containing new CNF-formed knowledge.");
						return;
					}
				}
			}
			else{
				//initialization is the default testing.  
				testInitiailize(fileName);
			}
			 
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return;
		}		
		
		

	}
	
	//try to initialize a KB
	void testInitiailize(String cnf_file){
		try {
			ICNF formula = new CNFSimpleImplAltWL(new DynamicVocImp());
			Dimacs parser = new Dimacs();
			parser.parseInstance(cnf_file, formula);
			IOSController control = new IOSController(formula);			
			control.kb_tree.showTree();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return;
		}	
	}
	
	//try to update a KB with new knowledge
	void testUpdate(String cnf_file, String new_cnf_file){
		try {
			ICNF KB = new CNFSimpleImplAltWL(new DynamicVocImp());
			Dimacs parser = new Dimacs();
			parser.parseInstance(cnf_file, KB);
			IOSController control = new IOSController(KB);
			control.kb_tree.showTree();
			
			ICNF new_klg = new CNFSimpleImplAltWL(new DynamicVocImp());
			parser = new Dimacs();
			parser.parseInstance(new_cnf_file, new_klg);
			
			control.update(new_klg);
			control.kb_tree.showTree();
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return;
		}	
		
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

		Option test_mode = 
				new Option(
						TESTING_MODE_OPTION,
						true,
						TESTING_MODE_DESCRIPTION
						);
		test_mode.setArgName("test_mode");
		options.addOption(test_mode);
		
		Option new_klg = 
				new Option(
						NEW_CNF_FILE_OPTION,
						true,
						NEW_CNF_FILE_DESCRIPTION
						);
		test_mode.setArgName("new_cnf_file");
		options.addOption(new_klg);
				
		return options;
	}
	

	public static void main(String[] args){
		new TestIOS().run(args);		
	}
	
}
