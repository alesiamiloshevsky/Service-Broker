/**************************************************************************************
 * Service Broker
 *
 * Component: Orchestration
 ***************************************************************************************
 * Function:
 *   Calls upon the requested service
 *----------------------------------------------------------------------------------------------------------------------
 *    Input:
 *          Parameters - Service Code, Parameter list
 *    Output:
 *          Return – Data returned from service call
 *----------------------------------------------------------------------------------------------------------------------
 *    Alesia Miloshevsky
 *    Version 04/22/2022   CMCS 355
 **************************************************************************************/
import java.io.*;
import java.util.Scanner;

public class ServiceBroker {
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {

        /*---------------------------------------------------------------------------------
         *   Declare Variables:
         *      commandLineArguments - arguments from the command line
         *      splitCommandLine     - splits the arguments from the command line
         *      service              - first argument from the command line
         *      paramList            - second argument from the command line
         *      flag                 - if the desired service is found flag == true, else flag == false
         *      splitService         - Splits the arguments found in the Service.txt file
         *      serviceParam         - first element connected to the found service in the Service.txt file
         *      line                 - reads the lines in the text file
         *---------------------------------------------------------------------------------*/
        String commandLineArguments = args[0];
        String[] splitCommandLine = commandLineArguments.split(",", 2);
        String service = splitCommandLine[0];
        String paramList = splitCommandLine[1];
        boolean flag = false;
        String[] splitService;
        String serviceParam = null;
        String line;

        /*---------------------------------------------------------------------------------
         *   @code - Open Service.txt file
         *         - Read the text file until the desired service is found or the file ends
         *         - If desired service is found, set flag to true and parse out the element connected to the service
         *---------------------------------------------------------------------------------*/
        Scanner scanner = new Scanner(new File("src/Service.txt"));
        while(!flag && scanner.hasNextLine()) {
            line = scanner.nextLine();
            if(line.toLowerCase().contains(service.toLowerCase())) {
                flag = true;
                splitService = line.split(",");
                serviceParam = splitService[1];
            }
        }

        /*---------------------------------------------------------------------------------
         *   @code - If flag is false read the Service.txt file again until the service labeled
         *           Error is found, then split the first element in Error
         *           and make paramList equal to the error code: 703
         *---------------------------------------------------------------------------------*/
        if(flag == false) {
            while (scanner.hasNextLine()) {
                String line2 = scanner.nextLine();
                if (line2.toLowerCase().contains("Error".toLowerCase())) {
                    splitService = line2.split(",");
                    serviceParam = splitService[1];
                    paramList = "703";
                }
            }
        }

        /*---------------------------------------------------------------------------------
         *   Declare Variables:
         *      process         - create a process
         *      processCall     - create a string that will be passed into process
         *---------------------------------------------------------------------------------*/
        Process process;
        String processCall = serviceParam + " " + paramList;

        /*---------------------------------------------------------------------------------
         *   @code - Calls the specified service and passes on the parameters
         *         - Gets the input stream of the process
         *         - Wait for the process to finish
         *         - Gets the input stream and puts it into the buffered reader
         *         - Reads and prints out each line if it does not equal null
         *---------------------------------------------------------------------------------*/
        try {
            process = Runtime.getRuntime().exec(processCall);
            InputStream input = process.getInputStream();
            process.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String nextLine;
            while((nextLine = br.readLine()) != null) {
                System.out.println(nextLine);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}