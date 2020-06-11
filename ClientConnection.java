package ClientSide;

import Commands.*;
import Vehicle.*;


import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;

public class ClientConnection {
    private InetAddress inetAddress;
    private Scanner fromclient = new Scanner(System.in);
    private Boolean isConnected;
    private byte[] bytes = new byte[163840];
    Boolean isworking = true;
    private HistoryList historyList;
    private static ArrayList<String> files = new ArrayList<String>();
    SocketAddress socketAddress = new InetSocketAddress(inetAddress, 7415);
    SocketChannel outcoming;

    public ClientConnection() throws UnknownHostException {
        inetAddress = InetAddress.getLocalHost();
        isConnected = false;
        fromclient = new Scanner(System.in);
        historyList = new HistoryList(6);
    }

    public void connect() throws IOException {
        while (true) {
            try {
                outcoming = SocketChannel.open(socketAddress);
                outcoming.configureBlocking(false);
                System.out.println("Connection established");
                break;
            } catch (IOException e) {
                System.out.println("Could not connect. Something went wrong.");
                System.err.println("No connection to the server.  End connection attempts (enter {yes})?");
                String answer;
                if (fromclient.nextLine().equals("yes")) {
                    System.out.println("Are you sure?");
                    if (fromclient.nextLine().equals("yes")) {
                        System.exit(0);
                    }
                } else {
                    System.out.println("Connection....");
                }
                continue;
            }

        }
    }
    public void start() throws IOException {

        while (isworking) {
            connect();
            //beginning(fromclient,outcoming);
            work(outcoming,fromclient);
            //outcoming.close();

        }
    }


    public void work(SocketChannel outcoming, Scanner scanner) throws IOException {
        while (isworking && scanner.hasNext() && outcoming.isConnected()) {
            String clientcom = scanner.nextLine();
            String[] input_ar = clientcom.split(" ");
            switch (input_ar[0]) {

                case ("help"):
                    help(historyList);
                    break;

                case ("info"):
                    Command command2 = new Command("info");
                    this.sendMessage(command2, outcoming);
                    historyList.insert("info");
                    wait_answer(outcoming);
                    break;

                case ("show"):
                    Command command3 = new Command("show");
                    this.sendMessage(command3, outcoming);
                    historyList.insert("show");
                    wait_answer(outcoming);
                    break;

                case ("insert"):
                    try {
                    Integer Key = Key_check(input_ar[1], fromclient);
                    Vehicle vehicle = new Vehicle(fromclient);
                    Command command4 = new Command("insert");
                    command4.setKey(Key);
                    command4.setVehicle(vehicle);
                    this.sendMessage(command4, outcoming);
                    historyList.insert("insert");
                    wait_answer(outcoming);
                    break;}catch (ArrayIndexOutOfBoundsException e){
                        System.out.println("The arguments to the command are incorrect. Use the help command");
                        break;
                    }

                case ("update"):
                   try {
                    Long ID = Long_check(input_ar[1], fromclient);
                    Vehicle vehicle1 = new Vehicle(fromclient);
                    Command command5 = new Command("update");
                    command5.setID(ID);
                    command5.setVehicle(vehicle1);
                    this.sendMessage(command5, outcoming);
                    historyList.insert("update");
                    wait_answer(outcoming);
                    break;}catch (ArrayIndexOutOfBoundsException e){
                       System.out.println("The arguments to the command are incorrect. Use the help command");
                       break;
                   }

                case ("remove_key"):
                    try{
                    Integer key = Key_check(input_ar[1], fromclient);
                    Command command6 = new Command("remove_key");
                    command6.setKey(key);
                    this.sendMessage(command6, outcoming);
                    historyList.insert("remove_key");
                    wait_answer(outcoming);
                    break;}catch (ArrayIndexOutOfBoundsException e){
                        System.out.println("The arguments to the command are incorrect. Use the help command");
                        break;
                    }

                case ("clear"):
                    Command command7 = new Command("clear");
                    this.sendMessage(command7, outcoming);
                    historyList.insert("clear");
                    wait_answer(outcoming);
                    break;

                case ("execute_script"):
                   try{
                    read_script(input_ar[1],historyList,outcoming);
                    historyList.insert("execute_script");
                    break;}catch (ArrayIndexOutOfBoundsException e){
                       System.out.println("The arguments to the command are incorrect. Use the help command");
                       break;
                   }

                case ("exit"):
                    this.isworking = false;
                    Command command16 = new Command("exit");
                    this.sendMessage(command16, outcoming);

                    break;

                case ("remove_greater"):
                    Vehicle vehicle2 = new Vehicle(fromclient);
                    Command command10 = new Command("remove_greater");
                    command10.setVehicle(vehicle2);
                    this.sendMessage(command10, outcoming);
                    historyList.insert("remove_greater");
                    wait_answer(outcoming);
                    break;

                case ("history"):
                    historyList.show();
                    historyList.insert("history");
                    break;

                case ("replace_if_greater"):
                   try{
                    Integer Key1 = Key_check(input_ar[1], fromclient);
                    Vehicle vehicle3 = new Vehicle(fromclient);
                    Command command12 = new Command("replace_if_greater");
                    command12.setKey(Key1);
                    command12.setVehicle(vehicle3);
                    this.sendMessage(command12, outcoming);
                    historyList.insert("replace_if_greater");
                    wait_answer(outcoming);
                    break;}catch (ArrayIndexOutOfBoundsException e){
                       System.out.println("The arguments to the command are incorrect. Use the help command");
                       break;
                   }

                case ("remove_any_by_number_of_wheels"):
                    try{
                    Long number = Long_check(input_ar[1], fromclient);
                    Command command13 = new Command("remove_any_by_number_of_wheels");
                    command13.setNumber(number);
                    this.sendMessage(command13, outcoming);
                    historyList.insert("remove_any_by_number_of_wheels");
                    wait_answer(outcoming);
                    break;}catch (ArrayIndexOutOfBoundsException e){
                        System.out.println("The arguments to the command are incorrect. Use the help command");
                        break;
                    }

                case ("count_less_than_engine_power"):
                    try{
                    Float power = Float_check(input_ar[1], fromclient);
                    Command command14 = new Command("count_less_than_engine_power");
                    command14.setPower(power);
                    this.sendMessage(command14, outcoming);
                    historyList.insert("count_less_than_engine_power");
                    wait_answer(outcoming);
                    break;}catch (ArrayIndexOutOfBoundsException e){
                        System.out.println("The arguments to the command are incorrect. Use the help command");
                        break;
                    }
                case ("print_field_ascending_type"):
                    List<VehicleType> VehT = Arrays.asList(VehicleType.values());
                    Collections.sort(VehT);
                    System.out.println(VehT);
                    historyList.insert(input_ar[0]);
                    break;

                default:
                    System.out.println(input_ar[0]);
                    System.out.println("Incorrect command. Try again");
                    help(historyList);

            }
            continue;

        }
    }

    public void help(HistoryList historyList) {
        System.out.println("help: вывести справку по доступным командам");
        System.out.println("info: вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        System.out.println("show: вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        System.out.println("insert key {element}: добавить новый элемент с заданным ключом");
        System.out.println("update id {element}: обновить значение элемента коллекции, id которого равен заданному");
        System.out.println("remove_key key: удалить элемент из коллекции по его ключу");
        System.out.println("clear: очистить коллекцию");
        System.out.println("save: сохранить коллекцию в файл");
        System.out.println("execute_script file_name: считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
        System.out.println("exit: завершить программу (без сохранения в файл)");
        System.out.println("remove_greater {element}: удалить из коллекции все элементы, превышающие заданный");
        System.out.println("history: вывести последние 6 команд(без их аргументов)");
        System.out.println("replace_if_greater key {element}: заменить значение по ключу, если новое значение больше старого");
        System.out.println("remove_any_by_number_of_wheels numberOfWheels:удалить из коллекции один элемент, значение поля numberOfWheels которого эквивалентно заданному");
        System.out.println("count_less_than_engine_power: вывести количество элементов, значение поля enginePower которых меньше заданного");
        System.out.println("print_field_ascending_type: вывести значения поля type в порядке возрастания");
        historyList.insert("help");
    }

    public Command wait_answer(SocketChannel outcoming) {
        Command command00=new Command();
        System.out.println("Answer from server:");
        while(command00.getAnswer()==null&&isworking) {
            try {
                Thread.sleep(1000);
                command00.setAnswer(this.read_answer(outcoming).getAnswer());
                if(command00.getAnswer()==null){
                    break;
                }else{
                    System.out.println(command00.getAnswer());
                }
            } catch (IOException e) {
                //System.out.println("Server did not send an answer");
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                isworking=false;
                System.out.println("Server is closed");
            }
        }
        return command00;
    }

    public void sendMessage(Command command, SocketChannel socketChannel) throws IOException {
        ByteBuffer sendBuffer = ByteBuffer.allocate(16384);
        try (

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

            objectOutputStream.writeObject(command);

            sendBuffer.put(byteArrayOutputStream.toByteArray());

            objectOutputStream.flush();
            byteArrayOutputStream.flush();
            sendBuffer.flip();
            socketChannel.write(sendBuffer);

            System.out.println("----\nMessage sent.\n----");

            objectOutputStream.close();

            byteArrayOutputStream.close();
            sendBuffer.clear();


        } catch (IOException e) {

            //System.out.println("----\nAn error occurred:\n");

            // e.printStackTrace();
            start();

        }

    }

    public Command read_answer(SocketChannel socketChannel) throws IOException {
        Command command = new Command();
        ByteBuffer readBuffer = ByteBuffer.allocate(163840);
        try {
            socketChannel.read(readBuffer);
            readBuffer.flip();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(readBuffer.array());
            readBuffer.flip();
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Command command1=(Command)objectInputStream.readObject();
            command.setAnswer(command1.getAnswer());
            //System.out.println(command.getAnswer());
            objectInputStream.close();
            byteArrayInputStream.close();
            readBuffer.clear();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Some problem with server. Try again");
            //e.printStackTrace();
        }

        return command;
    }

    public Integer Key_check(String string, Scanner scanner) {
        Integer Key = null;
        try {
            Key = Integer.valueOf(string);
        } catch (NumberFormatException E) {
            System.out.println("Input arg of key is incorrect.It must be Integer value. Try again");
            while (Key == null) {
                System.out.println("Key:");
                try {
                    Key = Integer.valueOf(scanner.next());
                } catch (NumberFormatException e) {
                    System.out.println("Input arg of key is incorrect. Try again");
                }
            }
        }
        return Key;
    }

    public Long Long_check(String string, Scanner scanner) {
        Long aLong = null;
        try {
            aLong = Long.valueOf(string);
        } catch (NumberFormatException E) {
            System.out.println("Input arg of long value is incorrect. Try again");
            while (aLong == null) {
                System.out.println("Long value:");
                try {
                    aLong = Long.valueOf(scanner.next());
                } catch (NumberFormatException e) {
                    System.out.println("Input arg of long value is incorrect. Try again");
                }
            }
        }
        return aLong;
    }

    public Float Float_check(String string, Scanner scanner) {
        Float aFloat = null;
        try {
            aFloat = Float.valueOf(string);
        } catch (NumberFormatException E) {
            System.out.println("Input arg of float value is incorrect. Try again");
            while (aFloat == null) {
                System.out.println("Float value:");
                try {
                    aFloat = Float.valueOf(scanner.next());
                } catch (NumberFormatException e) {
                    System.out.println("Input arg of float value is incorrect. Try again");
                }
            }
        }
        return aFloat;
    }


    public void read_script(String string, HistoryList historyList, SocketChannel outcoming) throws IOException {
        if (files.contains(string) == false) {
            files.add(string);
            File file1 = new File(string);
            try {
                Scanner scan = new Scanner(file1);
                while (scan.hasNextLine()) {
                    work(outcoming,scan);
                }
                scan.close();
            } catch (FileNotFoundException|SecurityException e) {
                System.out.println("Some problems with file");
                System.out.println("Try again");

            }
            historyList.insert("Execute_script");
        } else {
            System.out.println("The file specified in the execute method is already used, you cannot use it again");
        }

        files.clear();

    }

    /**public void beginning(Scanner scanner, SocketChannel outcoming) throws IOException {
        Command command2=new Command();
        command2.setAnswer("0");
        while (!command2.getAnswer().equals("Collection was read from file")) {
            System.out.println("Enter the file that contains the collection in xml format or exit:");
            String filename = scanner.nextLine();
            if (filename.equals("exit")) {
                Command command = new Command("first_exit");
                this.sendMessage(command, outcoming);
                command2.setAnswer(wait_answer(outcoming).getAnswer());
                outcoming.close();
                System.exit(0);
            } else {
                Command command = new Command("file_with_collection");
                command.setFile_name(filename);
                this.sendMessage(command, outcoming);
                command2.setAnswer(wait_answer(outcoming).getAnswer());
            }
        }

  */
}




