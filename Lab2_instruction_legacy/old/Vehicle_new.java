/*
* Name:  Jillian Irvin
* Class: CIT-244
* Date:  9.18.17
* Description: This class is an abstract class that implements ModelFormat interface.
* It's used to instantiate common characteristics of a vehicle using an inputted PID#.
* The PID# is formatted using the ModelFormat interface for easier data extraction from the vehicle make configuration file.
*/

import java.io.*;
import java.util.ArrayList;

public abstract class Vehicle implements ModelFormat{
	private String make;
	private String model;
	private String[] options = new String[8];
	protected String[] parts = new String[10];
	private double price;
	private FileReader config;
	private String[] anyCarConfig;

	//used to store config file so not have to read-in multipe times
	protected ArrayList<String> configFileList = new ArrayList<String>();

	//overload construtor method - mine
	public Vehicle(String makeInput, FileReader configInput, String pidNumber){

		//call setMake to set private make variable to input
		setMake(makeInput);

		//set the configuration file of the object so can use config file now
		this.config = configInput;

		//create ArrayList to read in the config file
		buildArrayList(this.config);

		//call setModel with pidNumber
		setModel(pidNumber);

		//call setOptions with pidNumber
		setOptions(pidNumber);

		//call setOptions

		//after this constructor runs...these private variables can be
		//accessed by the subclasses using the getMethods here
	}


	//**double check this constructor below
	//overload construtor method
	public Vehicle(String makeInput, String modelInput, FileReader configInput){
		//set the FileRead object to input parameter
		config = configInput;

		//call setMake to update String Make
		setMake(makeInput);

		//don't use setModel because it requires PID number
		model = modelInput;
	} //end Vehicle constructor

	public void setMake(String m){
		make = m;
	}

	public String getMake(){
		return make;
	}

	/*
	* Input: String - pidNumber
	* Return: Void
	* Description: setModel using the inputted pidNumber to set the private model variable.
	*/
	public void setModel(String pidNumber){

		String[] pidFormatted = new String[8];  //formatted PID number
		boolean continueRead = true;  //used for while loop when reading config file
		String line = null;  //store string when found in config file
		String[] lineArray = null;  //break line into string array w/ space delimiter
		int counter = 0;  //count lines in configFileList
		String id;

		//set string array to a formatted pidNumber using pidFormat method
		pidFormatted = pidFormat(pidNumber);
		string modelID;

		id = pidFormat[0];

		for(int i = 0; i < configFileList.size(); i++){
			String[] index = configFileList[i].split(" ");

			if(index[0].equalsIgnoreCase(id)) {
				modelID = index[i];
			}
		}  //end for

		try{

			while(continueRead){

				//read in line from config file starting at counter = 0
				line = configFileList.get(counter);

				//check if string contains the model number in pidNumber (1st element of pidFormatted); if so stop reading
				if (line.contains(pidFormatted[0])){
					continueRead = false;
				}

				//increment counter to read next line of list
				counter++;
			} //end while

		} //end try
		catch(Exception e){

			//didn't match PID# with available model
			System.out.println("Model name not found...");
			System.exit(0);
		}

		//break stored line string into array
		//reference:  https://stackoverflow.com/questions/6086381/split-string-into-an-array-of-string
		lineArray = line.split("\\s+");

		//second element of line array is the model name
		this.model = lineArray[1];
	} //end setModel


	public String getModel(){
		return model;
	}


	/*
	* Input: String - pidNumber
	* Return: Void
	* Description: setOptions using the inputted pidNumber to set the options array variable
	*/
	public void setOptions(String pidNumber){

		//string array to append with userOptions string array
		String[] optionNames = {"Exterior Color", "Interior Color", "Powertrain", "Seat", "Radio",
		"Tire", "Rim", "Miscellaneous"};

		String[] pidFormatted = new String[8];  //formatted PID number
		boolean continueRead;  //used for while loop when reading config file
		String line = null;  //store string when found in config file
		String[] lineArray = null;  //break line into string array w/ space delimiter

		int counter;  //store index of optionName when found

		//set string array to a formatted pidNumber using pidFormat method
		pidFormatted = pidFormat(pidNumber);

		//loop through optionNames to find match for each option
		for(int i = 0; i < optionNames.length; i++){

			//initiate for while-loop
			continueRead = true;
			counter = 0;

			try{

				//find match for optionName
				while (continueRead){

					//set line to line of configFileList
					line = configFileList.get(counter);

					//test if the line is for the current option search
					if(line.contains(optionNames[i])){

						//if matches optionName searching for then get out of loop
						continueRead = false;
					}//end if

					//increment counter to read next line in list
					counter++;
				} //end while
			} //end try
			catch(ArrayIndexOutOfBoundsException e){
				System.out.println("Error finding option Name");
				System.exit(0);
			}

			try{
				//initialize loop
				continueRead = true;

				//now find option in Pid# against optionName
				while (continueRead){

					//if PIDformatted = 0; then option is none
					if(pidFormatted[i+1].equals("0")) {
						//update options Array
						this.options[i] = optionNames[i] + ": none";
						continueRead = false;
					}

					//now read in line starting directly after found Option
					line = configFileList.get(counter);

					//break stored line string into array
					lineArray = line.split("\\s+");

					//if first element of lineArray matches the next optionName then no option found
					//or if lineArray matches a blank space
					// if (lineArray[0].equals(optionNames[i+1]) || lineArray[0].equals(" ")) {
					// 	System.out.println("Invalid option");
					// 	this.options[i] = optionNames[i] + "null";
					// 	continueRead = false;
					// }

					//check if first element matches the pidNumber option element - 1
					if (lineArray[0].equals(pidFormatted[i+1])) {

						//update options Array
						this.options[i] = optionNames[i] + " - " + lineArray[1];

						//end read loop to match pidOption entered
						continueRead = false;
					}

					//increment position in configFile list
					counter++;
				} //end while
			}  //end try
			catch (ArrayIndexOutOfBoundsException e){
				System.out.println("Array out of bounds");
				System.exit(0);
			}
		}//end for
	} //end setOptions


	//getter for object's options array
	public String[] getOptions(){
		return options;
	}


	//to be overridden
	public abstract void setParts(String[] p);


	//getter for parts array
	public String[] getParts(){
		return parts;
	}


	//setter for price
	public void setPrice(double p){
		//ues model string, options/parts array,
		//reader, to filter out the prices?
		//use reader to match parts and then price after

		price = p;
	}


	//getter for price
	public double getPrice(){
		return price;
	}


	//public String toString(){

	//}


	/*
	* Input: String - pidNumber
	* Return: String array with formatted pidNumber
	* Description: pidFormat breaks down pidNumber into indexes depending on config file
	*/
	public String[] pidFormat(String pidNumber){
		//use to return pidNumber formatted with 8 elements
		String[] pidFormatter = new String[9];

		//determine config file identiefer to format pid number
		if (pidNumber.charAt(0) == '1'){

			//loop through first 4 digits and put in index 0 the first four digits of pidNumber
			pidFormatter[0] = pidNumber.substring(0,4);

			//loop through remaining indexes and set to chars of pidNumber
			for (int i = 1; i < pidFormatter.length; i++){
				pidFormatter[i] = Character.toString(pidNumber.charAt(i+3));
			}
		}  //end if

		//check for ThatAuto.config  - 15 digits
		else if(pidNumber.charAt(0) == '2'){

			//loop through first 6 digits and put in index 0 the first six digits of pidNumber
			pidFormatter[0] = pidNumber.substring(0,6);

			//loop through all but last index and set to chars of pidNumber
			for (int i = 1; i < pidFormatter.length - 1; i++){
				pidFormatter[i] = Character.toString(pidNumber.charAt(i+5));
			}

			//set last two digits of pidNumber to last index of pidFormatter
			pidFormatter[8] = pidNumber.substring(13);
		}  //end else if

		//check for OtherAuto.config
		else if(pidNumber.charAt(0) == '3'){
			//loop through first 4 digits and put in index 0 the first four digits of pidNumber
			pidFormatter[0] = pidNumber.substring(0,4);

			//loop through all but last index and set to chars of pidNumber
			for (int i = 1; i < pidFormatter.length - 1; i++){

				pidFormatter[i] = Character.toString(pidNumber.charAt(i+3));
			}

			//set last four digits to last index of pidFormatter
			pidFormatter[8] = pidNumber.substring(11);
		}  //end else if

		else{
			System.out.println("Invalid option. Exiting program...");
			System.exit(0);
		}

		return pidFormatter;
	} //end pidFormat method

	/*
	* Input: FileReader - object's pass in file reader
	* Return: Void
	* Description: builds private arrayList for object so it can be ready by other methods
	*/
	private void buildArrayList(FileReader configFile){
		String line;  //variable to hold each line of config file
		System.out.println("Entered Build Array List");

		public static String[] anyCarConfig = new String[52];
		try{
			//instantiate new bufferedreader object from configFile
			//reference:  https://stackoverflow.com/questions/15577688/search-a-file-for-a-string-and-return-that-string-if-found
			BufferedReader configReader = new BufferedReader(configFile);

			//set line to first line of file
			line = configReader.readLine();

			//loop until null
			while (line != null){
				//add line to ArrayList
				this.configFileList.add(line);

				//move to next line
				line = configReader.readLine();
			}

			configReader.close();
		}
		catch(Exception e){
			System.out.println("Error: e");
		}
	} //end buildArrayList method
}
